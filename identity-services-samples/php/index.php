<?php

include 'jwt.php';

$PROVIDER_ID = "ENTER LAYER PROVIDER ID HERE";
$KEY_ID = "ENTER LAYER KEY ID HERE";
$PRIVATE_KEY_FILE = "ENTER LOCATION OF PRIVATE KEY FILE";  //for example : "file:///path/to/file.key"

$privateKey = openssl_pkey_get_private($PRIVATE_KEY_FILE);

$postData = json_decode($HTTP_RAW_POST_DATA);

$tokenClaims = array(
  "iss" => $PROVIDER_ID,
  "prn" => $postData->{'user_id'},
  "iat" => round(microtime(true) * 1000),
  "exp" => round(microtime(true) * 1000) + 60000,
  "nce" => $postData->{'nonce'}
);

$idToken = JWT::encode($tokenClaims, $privateKey, 'RS256', $KEY_ID);

print '{"identity_token":' . $idToken . '}';

openssl_free_key($privateKey);

?>
