
The code in this folder represents a simple backend to generate identity tokens using Ruby.

### Parameters:

* user_id:  The user ID of the person you want to authenticate
* nonce: The nonce you receive from Layer. See [docs](https://developer.layer.com/docs/guide#authentication) for more info

You will also need to define the following ENV variables:

* `layer_provider_id` - Provider ID found in the Layer Dashboard under "Authentication"
* `layer_key_id` - Public key generated and stored in the Layer Dashboard under "Authentication"
* `layer_private_key` - Private key associated with the public key


###Output:
A JSON object containing the identity_token.

Example Result:

```json
{
"identity_token": "eyJ0eXAiOiJKV1MiLCJhbGciOiJSUzI1NiIsImN0eSI6ImxheWVyLWVpdDt2PTEiLCJraWQiOiI2OWZkZDVhYS02NDc4LTExZTQtOTdmMS0xZGVkMDAwMDAwZTYifQ.eyJpc3MiOiI1YTczMWE0Yy02M2JlLTExZTQtOTEyNC1hYWE1MDIwMDc1ZjgiLCJpYXQiOjE0MTUxNTExMTcsImV4cCI6MTQxNTE2MTExNywibmNlIjoibkcrWHZFb0c3dDBXSEZrZjN0QmlWdEdjekFsTXArUmwydXVqWkN0TVJsSEcxb1FVU05BSXZnM0ZTbnFzTDhiNlFFK2pIZU8vZHJsZ2FJNXJnRXVIR2c9PSJ9.SPVPzzN7S09OafQZV7E_LvHF1mmvj5VU0Kn780ef4tegwLUS_pYj7ODfgSZPS2-MNRFhYb5ACZqtoxNkv32CJBzJzFuwBkZ3CsuX8xdpeXWEqvYtK2OV73x1TNA8RGmWyVjVKq7xjpGUORkLk7KQW3QRrQGSVx_jeOiUxb9HvZI"
}
```
