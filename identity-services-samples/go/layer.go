//Code created by http://github.com/maju6406 and http://github.com/emilsjolander
package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"time"

	jwt "github.com/dgrijalva/jwt-go"
)

const LayerProviderId = "<PROVIDER_ID>"
const LayerKeyId = "<KEY_ID>"
const LayerPrivateKey = "<RSA_PRIVATE_KEY>"

func init() {
	r := pat.New()
	r.Post("/authenticate", http.HandlerFunc(authenticate))
	http.Handle("/", r)
}

func authenticate(w http.ResponseWriter, r *http.Request) {
	params := struct {
		Username string
		Nonce    string
	}{}
	if err := json.NewDecoder(r.Body).Decode(&params); err != nil {
		writeError(w, err, http.StatusInternalServerError)
		return
	}

	token := jwt.New(jwt.GetSigningMethod("RS256"))

	// Set header values
	token.Header["typ"] = "JWT"
	token.Header["alg"] = "RS256"
	token.Header["cty"] = "layer-eit;v=1"
	token.Header["kid"] = LayerKeyId

	// Set claims
	token.Claims["iss"] = LayerProviderId
	token.Claims["prn"] = params.Username
	token.Claims["iat"] = time.Now().Unix()
	token.Claims["exp"] = time.Now().Add(time.Hour * 72).Unix()
	token.Claims["nce"] = params.Nonce

	// Sign and get the complete encoded token as a string
	tokenString, err := token.SignedString([]byte(LayerPrivateKey))
	if err != nil {
		writeError(w, err, http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json; charset=utf-8")
	json.NewEncoder(w).Encode(&struct {
		IdentityToken string `json:"identity_token"`
	}{tokenString})
}

func writeError(w http.ResponseWriter, e error, status int) {
	w.Header().Set("Content-Type", "application/json; charset=utf-8")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(&struct {
		Error  string `json:"error"`
		Status int    `json:"status"`
	}{e.Error(), status})
}
