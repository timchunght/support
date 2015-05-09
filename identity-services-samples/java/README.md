
The code in this folder represents a simple backend to generate identity tokens using a Java Servlet.

### Setup:

Import the jsontoken-1.1.jar library into the WEB-INF/lib folder (make sure you only import v1.1 or higher, as older versions of the library will not generate the Identity Token header correctly).

You will then need to define the following variables in the `IdentityTokenGenerator` class:

* `PROVIDER_ID` - Provider ID found in the Layer Dashboard under "Authentication"
* `KEY_ID` - Public key generated and stored in the Layer Dashboard under "Authentication"
* `RSA_KEY_LOCATION` - Path to the PK8 file generated from the RSA key

### Parameters:

* user_id:  The user ID of the person you want to authenticate
* nonce: The nonce you receive from Layer. See [docs](https://developer.layer.com/docs/guide#authentication) for more info

### Output:
A JSON object containing the identity_token.

Example Result:

```json
{
"identity_token": "eyJ0eXAiOiJKV1MiLCJhbGciOiJSUzI1NiIsImN0eSI6ImxheWVyLWVpdDt2PTEiLCJraWQiOiI2OWZkZDVhYS02NDc4LTExZTQtOTdmMS0xZGVkMDAwMDAwZTYifQ.eyJpc3MiOiI1YTczMWE0Yy02M2JlLTExZTQtOTEyNC1hYWE1MDIwMDc1ZjgiLCJpYXQiOjE0MTUxNTExMTcsImV4cCI6MTQxNTE2MTExNywibmNlIjoibkcrWHZFb0c3dDBXSEZrZjN0QmlWdEdjekFsTXArUmwydXVqWkN0TVJsSEcxb1FVU05BSXZnM0ZTbnFzTDhiNlFFK2pIZU8vZHJsZ2FJNXJnRXVIR2c9PSJ9.SPVPzzN7S09OafQZV7E_LvHF1mmvj5VU0Kn780ef4tegwLUS_pYj7ODfgSZPS2-MNRFhYb5ACZqtoxNkv32CJBzJzFuwBkZ3CsuX8xdpeXWEqvYtK2OV73x1TNA8RGmWyVjVKq7xjpGUORkLk7KQW3QRrQGSVx_jeOiUxb9HvZI"
}
```