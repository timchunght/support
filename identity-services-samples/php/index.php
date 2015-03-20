<?php

include 'jwt.php';

$PROVIDER_ID = "PROVIDER_ID";
$KEY_ID = "KEY_ID";
$PRIVATE_KEY_FILE = "RSA_PRIVATE_KEY_FILE_PATH";  //for example : "file:///path/to/file.key"

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
