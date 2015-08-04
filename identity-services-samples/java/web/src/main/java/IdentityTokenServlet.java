import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.layer.LayerConfig;
import com.layer.LayerIdentityTokenGenerator;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(
        urlPatterns = {"/identity-token"},
        name = "Layer Identity Token Example"
)
public class IdentityTokenServlet extends HttpServlet {

    private LayerConfig layerConfig;

    /**
     * Creates a servlet with your default {@link LayerConfig}
     */
    public IdentityTokenServlet() {
        this(new LayerConfig());
    }

    /**
     * Creates a servlet with a specified {@link LayerConfig}
     *
     * @param config the configuration for your Layer app
     */
    public IdentityTokenServlet(LayerConfig config) {
        super();
        layerConfig = config;
    }

    /**
     * Handles the POST for the identity token
     *
     * This method expects a POST'ed JSON payload of the format:
     *
     * <pre>
     *     {@code {"nonce": "...", "user_id": "..." } }
     * </pre>
     *
     * Upon success, it will return a JSON payload of the format:
     *
     * <pre>
     *     {@code {"identity_token": "..."}}
     * </pre>
     *
     * There are 3 errors which may occur: your LayerConfig may be invalid, you may have passed in a
     * non JSON payload, or your JSON did not contain the required parameters
     *
     * @param request  the inbound request
     * @param response the outbound response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtain the JSON object from the request
        try {
            final JsonObject jsonObject = readJsonFromRequest(request);
            final JsonElement nonceEl = jsonObject.get("nonce");
            final JsonElement userIdEl = jsonObject.get("user_id");

            if (nonceEl != null && userIdEl != null) {
                final String nonce = nonceEl.getAsString();
                final String userId = userIdEl.getAsString();
                final LayerIdentityTokenGenerator identityTokenGenerator =
                        new LayerIdentityTokenGenerator(layerConfig);

                try {
                    final String token = identityTokenGenerator.getToken(nonce, userId);
                    returnIdentityToken(response, token);
                } catch (Exception e) {
                    // There are a few exceptions that can be thrown when generating the token
                    // We'll capture them here and just report it as a server error.
                    returnError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Server configuration error");
                }
            } else {
                returnError(response, HttpServletResponse.SC_BAD_REQUEST,
                        "Required 'nonce' and 'user_id' parameters not found.");
            }
        } catch (IllegalStateException ise) {
            // this means that the inbound request was not JSON. return an error.
            returnError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "A JSON object is required.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "This endpoint only supports POST");
    }

    /**
     * Builds and returns the Identity Token response
     *
     * @param response the servlet's response object
     * @param token    the identity token to return
     * @throws IOException if we encounter a problem writing our response
     */
    private void returnIdentityToken(HttpServletResponse response, String token)
            throws IOException {

        final JsonObject outJson = new JsonObject();
        outJson.addProperty("identity_token", token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");
        final ServletOutputStream out = response.getOutputStream();
        out.write(outJson.toString().getBytes());
        out.flush();
        out.close();
    }

    /**
     * Convenience method for returning status error code and message to the client
     *
     * @param response      the servlet's response object
     * @param statusCode    the status code to return
     * @param statusMessage the message to return with the error
     * @throws IOException {@inheritDoc}
     */
    private void returnError(HttpServletResponse response, int statusCode, String statusMessage)
            throws IOException {

        response.sendError(statusCode, statusMessage);
    }


    /**
     * Converts the request content into a JSON object
     *
     * @param request the servlet's request
     * @return a {@link JsonObject} representation of the request
     * @throws IllegalStateException if unable to obtain a JSON object
     */
    private JsonObject readJsonFromRequest(HttpServletRequest request)
            throws IllegalStateException {

        final StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(stringBuilder.toString());
        return element.getAsJsonObject();
    }
}