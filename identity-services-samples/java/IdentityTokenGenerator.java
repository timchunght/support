package tokenizer;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Calendar;
import com.google.gson.JsonObject;

// JsonToken can be found here: https://code.google.com/p/jsontoken/
import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.crypto.RsaSHA256Signer;

public class IdentityTokenGenerator {
	private final static String PROVIDER_ID = "PROVIDER_ID";
	private final static String LAYER_KEY_ID = "KEY_ID";
    
    /*
     * You will have to convert your private key to PKCS8 Format so that Java
     * can read it:
     * openssl pkcs8 -topk8 -nocrypt -outform DER -in layer.pem -out layer.pk8
     */
    private final static String RSA_KEY_PATH = "/file_location/layer.pk8";

    
	public static String getToken(String nonce, String userId)
			throws InvalidKeyException, FileNotFoundException, IOException,
			ClassNotFoundException, NoSuchAlgorithmException,
			InvalidKeySpecException, SignatureException {
		// Current time and signing algorithm
		Calendar cal = Calendar.getInstance();
		RsaSHA256Signer signer = new RsaSHA256Signer(null, null,
				getPrivateKey());
		// Configure JSON token
		JsonToken token = new JsonToken(signer);
		JsonObject header = token.getHeader();
		
		header.addProperty("typ", "JWT");
		header.addProperty("alg", "RS256");
		header.addProperty("cty", "layer-eit;v=1");
		header.addProperty("kid", LAYER_KEY_ID);
		
		token.setParam("iss", PROVIDER_ID);
		token.setParam("prn", userId);
		token.setIssuedAt(new org.joda.time.Instant(cal.getTimeInMillis()));
		token.setExpiration(new org.joda.time.Instant(
				cal.getTimeInMillis() + 60000L));
		token.setParam("nce", nonce);

		return token.serializeAndSign();
	}


	public static RSAPrivateKey getPrivateKey() throws InvalidKeyException,
			FileNotFoundException, IOException, ClassNotFoundException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			SignatureException {
		File privKeyFile = new File(RSA_KEY_PATH);
		DataInputStream dis = new DataInputStream(new FileInputStream(
				privKeyFile));
		byte[] privateBytes = new byte[(int) privKeyFile.length()];
		dis.readFully(privateBytes);
		dis.close();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateBytes);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
		return (RSAPrivateKey) privateKey;
	}
}
