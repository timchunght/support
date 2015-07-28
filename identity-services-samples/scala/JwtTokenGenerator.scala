package util.messaging

import java.io.{ FileInputStream, DataInputStream, File }
import java.security.spec.{ PKCS8EncodedKeySpec, EncodedKeySpec }
import java.security.{ PrivateKey, KeyFactory, KeyPairGenerator }
import java.security.interfaces.RSAPrivateKey
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jwt.{ SignedJWT, JWTClaimsSet }
import org.joda.time.DateTime
import play.api.libs.json.{ Json, JsObject }

object JwtTokenGenerator {

  /*
   * You will have to convert your private key to PKCS8 Format so that Java
   * can read it:
   * openssl pkcs8 -topk8 -nocrypt -outform DER -in layer.pem -out layer.pk8
   */

  private val PROVIDER_ID: String = "Layer Provider ID URI"
  private val LAYER_KEY_ID: String = "Layer Key ID URI"
  private val RSA_KEY_PATH: String = "layer.pk8"

  def getToken(nonce: String, userId: String): String = {

    val header: JsObject = Json.obj("typ" -> "JWT",
      "alg" -> "RS256",
      "cty" -> "layer-eit;v=1",
      "kid" -> LAYER_KEY_ID)

    val privateKey = getPrivateKey

    // Create RSA-signer with the private key
    val signer = new RSASSASigner(privateKey)

    // Prepare JWT with claims set
    val claimsSet = new JWTClaimsSet()
    claimsSet.setClaim("prn", userId)
    claimsSet.setClaim("nce", nonce)
    claimsSet.setClaim("iss", PROVIDER_ID)
    claimsSet.setIssueTime(DateTime.now().toDate)

    // Chnage this to desired expiration date
    claimsSet.setExpirationTime(DateTime.now().plusDays(30).toDate)

    val headerSet: JWSHeader = JWSHeader.parse(header.toString)
    val signedJWT = new SignedJWT(headerSet, claimsSet)

    // Compute the RSA signature
    signedJWT.sign(signer)

    signedJWT.serialize
  }

  private def getPrivateKey: RSAPrivateKey = {
    val keyFile = new File(RSA_KEY_PATH)
    val dis: DataInputStream = new DataInputStream(new FileInputStream(keyFile))
    val privateBytes: Array[Byte] = new Array[Byte](keyFile.length.toInt)

    dis.readFully(privateBytes)
    dis.close()

    val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
    val privateKeySpec: EncodedKeySpec = new PKCS8EncodedKeySpec(privateBytes)
    val privateKey: PrivateKey = keyFactory.generatePrivate(privateKeySpec)

    privateKey.asInstanceOf[RSAPrivateKey]
  }
}
