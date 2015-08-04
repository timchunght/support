# Overview

The code in this folder provides an example for creating a Layer Identity Token using an HttpServlet.

The example endpoint, `identity-token`, that requires the following parameters:
* `user_id`: The user ID of the user you want to authenticate.
* `nonce`: The nonce you receive from Layer. See [docs](https://developer.layer.com/docs/#authentication) for more info.

## Response

Upon success, the endpoint will return a JSON object that contains a single key, `identity_token`.

Example successful response:

```JSON
{
  'identity_token': 'eyJ0eXAiOiJKV1Mi...'
}
```

## Java setup

This code uses the gradle build tool via a gradle wrapper. To initialize your system with required dependencies, run the following command:

```console
./gradlew tasks
```

### Configure your Layer app

You will then need to define the following variables in the `LayerConfig` class:

* `LAYER_PROVIDER_ID` - Provider ID found in the Layer Dashboard under "Authentication"
* `LAYER_KEY_ID` - Public key generated and stored in the Layer Dashboard under "Authentication"
* `LAYER_RSA_KEY_PATH` - Path to the PK8 version of your RSA key (see below)

__Note:__ You must convert your private key to PKCS8 format so that Java can read it:

```console
openssl pkcs8 -topk8 -nocrypt -outform DER -in layer.pem -out layer.pk8```


## Running the servlet

To run the servlet locally, enter the following command:

```console
./gradlew appRun
```

The servlet will now be accessible at `http://localhost:8080/identity-token`.

## Testing the servlet

```console
curl              \
  -D -            \
  -X POST         \
  -d '{"nonce":"test_nonce", "user_id": "test_user_id"}' \
  http://localhost:8080/identity-token
```

You should see the following response:

```console
HTTP/1.1 200 OK
Server: Apache-Coyote/1.1
Content-Type: application/json;charset=UTF8
Transfer-Encoding: chunked
Date: Wed, 12 Aug 2015 03:47:29 GMT

{"identity_token":"...SNIP..."}
```

### Verify the generated token

You should verify the output of the signing request by visiting the **Tools** section of the [Layer dashboard](https://developer.layer.com/dashboard/). Paste the value of the `identity_token` key you received from the output above and click `validate`. You should see "Token valid."
