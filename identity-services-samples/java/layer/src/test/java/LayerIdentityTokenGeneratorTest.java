import com.google.gson.JsonObject;
import com.layer.LayerConfig;
import com.layer.LayerIdentityTokenGenerator;
import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.JsonTokenParser;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class LayerIdentityTokenGeneratorTest {

    private static final String TEST_LAYER_KEY = "layer:///key/boom";
    private static final String TEST_LAYER_PROVIDERS = "layer:///providers/pdiddy";
    private static final String TEST_NONCE = "nonce_nonce_nonce";
    private static final String TEST_USER_ID = "clark_kent";
    private final String TEST_RSA_KEY_PATH = this.getClass().getResource("layer.pk8").getPath();

    private LayerIdentityTokenGenerator generator;

    private class TestLayerConfig extends LayerConfig {
        @Override
        public String getProviderId() {
            return TEST_LAYER_PROVIDERS;
        }

        @Override
        public String getLayerKeyId() {
            return TEST_LAYER_KEY;
        }

        @Override
        public String getRsaKeyPath() {
            return TEST_RSA_KEY_PATH;
        }
    }


    @Before
    public void setUp() {
        generator = new LayerIdentityTokenGenerator(new TestLayerConfig());
    }

    @Test
    public void testGetToken() throws Exception {
        final String responseToken = generator.getToken(TEST_NONCE, TEST_USER_ID);
        JsonTokenParser parser = new JsonTokenParser(null, null);
        JsonToken token = parser.deserialize(responseToken);
        JsonObject header = token.getHeader();

        //cty should always be layer EIT v1
        assertThat(header.get("cty").getAsString(), is(equalTo("layer-eit;v=1")));

        //typ should always be JWT
        assertThat(header.get("typ").getAsString(), is(equalTo("JWT")));

        //algo should always be RS256
        assertThat(header.get("alg").getAsString(), is(equalTo("RS256")));

        //kid should match given key
        assertThat(header.get("kid").getAsString(), is(TEST_LAYER_KEY));

        //pid should match given provider id
        assertThat(token.getIssuer(), is(TEST_LAYER_PROVIDERS));

        //the PRN field should reflect the user id that was sent in
        assertThat(token.getParamAsPrimitive("prn").getAsString(), is(TEST_USER_ID));

        //the nonce field should reflect the nonce that was passed in
        assertThat(token.getParamAsPrimitive("nce").getAsString(), is(TEST_NONCE));
    }

}
