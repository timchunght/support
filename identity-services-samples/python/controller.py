# -*- coding: utf-8 -*-
import os
from datetime import timedelta, datetime

# Third party
import jwt
from Crypto.PublicKey import RSA
from django.http.response import HttpResponse

PROVIDER_ID = "PROVIDER_ID"
KEY_ID = "KEY_ID"
RSA_KEY_PATH = "file_location/layer.pem"

def get_identity_token(request):
    user_id = request.POST['user_id']
    nonce = request.POST['nonce']

    # Read RSA key
    root = os.path.dirname(__file__)
    with open(os.path.join(root, RSA_KEY_PATH), 'r') as rsa_priv_file:
        priv_rsakey = RSA.importKey(rsa_priv_file.read())

    # Create identity token
    # Make sure you have PyJWT and PyCrypto libraries installed and imported
    identityToken = jwt.encode(
        payload={
            "iss": PROVIDER_ID,                 # String - The Provider ID found in the Layer Dashboard
            "prn": user_id,                     # String - Provider's internal ID for the authenticating user
            "iat": datetime.now(),              # Integer - Time of Token Issuance in RFC 3339 seconds
            "exp": datetime.utcnow() + timedelta(seconds=30),   # Integer - Arbitrary Token Expiration in RFC 3339 seconds
            "nce": nonce                        # The nonce obtained via the Layer client SDK.
        },
        key=priv_rsakey.exportKey(),
        headers={
            "typ": "JWS",               # String - Expresses a MIME Type of application/JWS
            "alg": "RS256",             # String - Expresses the type of algorithm used to sign the token, must be RS256
            "cty": "layer-eit;v=1",     # String - Express a Content Type of Layer External Identity Token, version 1
            "kid": KEY_ID               # String - Private Key associated with "layer.pem", found in the Layer Dashboard
        },
        algorithm='RS256'
    )
    return HttpResponse(identityToken)
