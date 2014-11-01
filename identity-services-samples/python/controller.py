def getIdentityToken (request):
    #Grab nonce from request
    nonce = request.POST['nonce']

    #Read RSA key
    root = os.path.dirname(__file__)
    with open(os.path.join(root, "layer.pem"), 'r') as rsa_priv_file:
        priv_rsakey = RSA.importKey(rsa_priv_file.read())

    #Create identity token
    #Make sure you have PyJWT and PyCrypto libraries installed and imported
    identityToken = jwt.encode(
        payload={
            "iss": "00c39b0c-2e21-11e4-9001-4d6602005389",  # The Layer Provider ID
            "prn": "948374839",                             # String - Provider's internal ID for the authenticating user
            "iat": datetime.now(),                          # Integer - Time of Token Issuance in RFC 3339 seconds
            "exp": datetime.utcnow() + timedelta(seconds=30),   # Integer - Arbitrary time of Token Expiration in RFC 3339 seconds
            "nce": nonce                                    # The nonce obtained via the Layer client SDK.
        },
        key=priv_rsakey,
        headers = {
            "typ": "JWS",               # String - Expresses a MIME Type of application/JWS
            "alg": "RS256",             # String - Expresses the type of algorithm used to sign the token, must be RS256
            "cty": "layer-eit;v=1",     # String - Express a Content Type of Layer External Identity Token, version 1
            "kid": "88a05500-2efe-11e4-8611-830000002b1e" # Sting - Layer Key ID used to sign the token
        },
        algorithm='RS256'
    )

    print identityToken

    return HttpResponse(identityToken)