package tokenizer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.*;

@WebServlet(
        name = "Identity Token Servlet", 
        urlPatterns = {"/get-identity-token"}
    )

public class IdentityTokenServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
    	try {
    		String nonce = req.getParameter("nonce");
    		String userId = req.getParameter("user_id");
        	
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