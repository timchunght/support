import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import com.layer.LayerConfig;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TestIdentityTokenServlet {

    Mockery mockContext;
    HttpServletRequest mockRequest;
    HttpServletResponse mockResponse;
    IdentityTokenServlet servletUnderTest;

    final String VALID_JSON = "{\"nonce\": \"sean\", \"user_id\": \"sean\"}";
    final String EMPTY_JSON = "{}";

    final class TestServletOutputStream extends ServletOutputStream {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        public void write(int i) throws IOException {
            byteArrayOutputStream.write(i);
        }
    }


    /**
     * Dummy values for a Layer app, including path to test private key.
     */
    private class TestConfig extends LayerConfig {
        @Override
        public String getProviderId() {
            return "test_provider_id";
        }

        @Override
        public String getLayerKeyId() {
            return "test_layer_key";
        }

        @Override
        public String getRsaKeyPath() {
            return getClass().getResource("layer.pk8").getPath();
        }
    }

    /**
     * If the server is incorrectly configured (e.g. Layer app config not set properly), the server
     * should response with a 500.
     */
    private class InvalidTestConfig extends TestConfig {

        @Override
        public String getRsaKeyPath() {
            return "/this/file/does/not/exist";
        }
    }


    @Before
    public void setup() {
        mockContext = new JUnit4Mockery() {{
            setThreadingPolicy(new Synchroniser());
        }};
        mockRequest = mockContext.mock(HttpServletRequest.class);
        mockResponse = mockContext.mock(HttpServletResponse.class);

        servletUnderTest = new IdentityTokenServlet(new TestConfig());
    }

    @Test
    public void testValidJsonInvalidParamsInput() throws Throwable {

        final StringReader reader = new StringReader(EMPTY_JSON);
        final BufferedReader bufferedReader = new BufferedReader(reader);

        mockContext.checking(new Expectations() {
            {

                allowing(mockRequest).getReader();
                will(returnValue(bufferedReader));

                allowing(mockResponse).sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Required 'nonce' and 'user_id' parameters not found.");
            }
        });

        servletUnderTest.doPost(mockRequest, mockResponse);
        mockContext.assertIsSatisfied();
    }

    @Test
    public void testValidJsonInput() throws Throwable {

        final TestServletOutputStream outputStream = new TestServletOutputStream();
        final StringReader reader = new StringReader(VALID_JSON);
        final BufferedReader bufferedReader = new BufferedReader(reader);

        mockContext.checking(new Expectations() {
            {
                allowing(mockRequest).getReader();
                will(returnValue(bufferedReader));

                allowing(mockResponse).setContentType("application/json");
                allowing(mockResponse).setCharacterEncoding("UTF8");

                allowing(mockResponse).getOutputStream();
                will(returnValue(outputStream));
            }
        });

        servletUnderTest.doPost(mockRequest, mockResponse);
        mockContext.assertIsSatisfied();

        JsonParser parser = new JsonParser();
        JsonElement el = parser.parse(outputStream.byteArrayOutputStream.toString());

        Assert.assertNotNull("we should receive an identity token in the response payload",
                el.getAsJsonObject().get("identity_token"));
    }

    @Test
    public void testInvalidJsonInput() throws Throwable {
        final StringReader sr = new StringReader("");
        final BufferedReader reader = new BufferedReader(sr);

        mockContext.checking(new Expectations() {{
            allowing(mockRequest).getReader();
            will(returnValue(reader));

            allowing(mockResponse).sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "A JSON object is required.");
        }});
        servletUnderTest.doPost(mockRequest, mockResponse);
        mockContext.assertIsSatisfied();
    }

    @Test
    public void testInvalidServerConfiguration() throws Throwable {
        servletUnderTest = new IdentityTokenServlet(new InvalidTestConfig());

        final StringReader sr = new StringReader(VALID_JSON);
        final BufferedReader reader = new BufferedReader(sr);

        mockContext.checking(new Expectations() {{
            allowing(mockRequest).getReader();
            will(returnValue(reader));

            allowing(mockResponse).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server configuration error");
        }});
        servletUnderTest.doPost(mockRequest, mockResponse);
        mockContext.assertIsSatisfied();
    }

    @Test
    public void testInvalidHttpMethodGET() throws Throwable {
        mockContext.checking(new Expectations() {{
            allowing(mockResponse).sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                    "This endpoint only supports POST");
        }});

        servletUnderTest.doGet(mockRequest, mockResponse);
        mockContext.assertIsSatisfied();
    }
}
