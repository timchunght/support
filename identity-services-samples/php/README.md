
The code in this folder represent a simple backend to generate identity tokens using PHP. This has been tested against PHP 5. 

## Example Usage:

###Parameters:

* user_id:  The user ID of the person you want to authenticate
* nonce: The nonce you receive from Layer. See [docs](https://developer.layer.com/docs/guide#authentication) for more info

The example refers to  "PROVIDER_ID", "KEY_ID", and "RSA_PRIVATE_KEY_FILE_PATH".  These values are provided by  the Layer Dashboard. RSA_PRIVATE_KEY_FILE_PATH is the file path to the file containing the Private Key file you got from the Authentication section in the Dashboard.

###Output:
A JSON object containing the identity_token.

Example Result:

```json
{
    "identity_token": "eyJ0eXAiOiJKV1MiLCJhbGciOiJSUzI1NiIsImN0eSI6ImxheWVyLWVpdDt2PTEiLCJraWQiOiI2OWZkZDVhYS02NDc4LTExZTQtOTdmMS0xZGVkMDAwMDAwZTYifQ.eyJpc3MiOiI1YTczMWE0Yy02M2JlLTExZTQtOTEyNC1hYWE1MDIwMDc1ZjgiLCJpYXQiOjE0MTUxNTExMTcsImV4cCI6MTQxNTE2MTExNywibmNlIjoibkcrWHZFb0c3dDBXSEZrZjN0QmlWdEdjekFsTXArUmwydXVqWkN0TVJsSEcxb1FVU05BSXZnM0ZTbnFzTDhiNlFFK2pIZU8vZHJsZ2FJNXJnRXVIR2c9PSJ9.SPVPzzN7S09OafQZV7E_LvHF1mmvj5VU0Kn780ef4tegwLUS_pYj7ODfgSZPS2-MNRFhYb5ACZqtoxNkv32CJBzJzFuwBkZ3CsuX8xdpeXWEqvYtK2OV73x1TNA8RGmWyVjVKq7xjpGUORkLk7KQW3QRrQGSVx_jeOiUxb9HvZI"
}
```
