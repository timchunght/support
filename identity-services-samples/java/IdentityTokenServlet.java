package servlet;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// JSONObject can be found here:  https://github.com/netplex/json-smart-v2
import net.minidev.json.JSONObject;

// JsonObject can be found here: https://code.google.com/p/google-gson/
import com.google.gson.JsonObject;

// JSONObjectUtils can be found here: https://bitbucket.org/connect2id/nimbus-jose-jwt/wiki/Home
import com.nimbusds.jose.util.JSONObjectUtils;

import util.IdentityTokenGenerator;

@WebServlet(
        name = "Identity Token Servlet", 
        urlPatterns = {"/get-identity-token"}
    )
public class IdentityTokenServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
    	try {
    		JSONObject jo = JSONObjectUtils.parseJSONObject(req.getReader().readLine());
    		String nonce = (String) jo.get("nonce");;
        	String userId = (String) jo.get("user_id");
        	
        	String token = IdentityTokenGenerator.getToken (nonce, userId);
        	
        	JsonObject outJson = new JsonObject();
        	outJson.addProperty("identity_token", token);
        	
			ServletOutputStream out = resp.getOutputStream();
	        out.write(outJson.toString().getBytes());
	        out.flush();
	        out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
}
