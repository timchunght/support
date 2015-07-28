The code in this folder represent a simple backend to generate identity tokens using Scala. It relies on Nimbus JOSE+JWT library (http://connect2id.com/products/nimbus-jose-jwt) that's available from Maven Central (http://mvnrepository.com/artifact/com.nimbusds/nimbus-jose-jwt).

Example Usage:

Parameters:

userId: The user ID of the person you want to authenticate
nonce: The nonce you receive from Layer. See docs for more info
The example refers to "PROVIDER_ID", "KEY_ID", and "RSA_KEY_PATH". These values are provided by the Layer Dashboard. "RSA_KEY_PATH" is the file path to the file containing the Private Key file you got from the Authentication section in the Dashboard.

Output:

A String containing the identity token.

Example Result:

eyJ0eXAiOiJKV1MiLCJhbGciOiJSUzI1NiIsImN0eSI6ImxheWVyLWVpdDt2PTEiLCJraWQiOiI2OWZkZDVhYS02NDc4LTExZTQtOTdmMS0xZGVkMDAwMDAwZTYifQ.eyJpc3MiOiI1YTczMWE0Yy02M2JlLTExZTQtOTEyNC1hYWE1MDIwMDc1ZjgiLCJpYXQiOjE0MTUxNTExMTcsImV4cCI6MTQxNTE2MTExNywibmNlIjoibkcrWHZFb0c3dDBXSEZrZjN0QmlWdEdjekFsTXArUmwydXVqWkN0TVJsSEcxb1FVU05BSXZnM0ZTbnFzTDhiNlFFK2pIZU8vZHJsZ2FJNXJnRXVIR2c9PSJ9.SPVPzzN7S09OafQZV7E_LvHF1mmvj5VU0Kn780ef4tegwLUS_pYj7ODfgSZPS2-MNRFhYb5ACZqtoxNkv32CJBzJzFuwBkZ3CsuX8xdpeXWEqvYtK2OV73x1TNA8RGmWyVjVKq7xjpGUORkLk7KQW3QRrQGSVx_jeOiUxb9HvZI
