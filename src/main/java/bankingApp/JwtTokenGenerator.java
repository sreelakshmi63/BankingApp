package bankingApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JwtTokenGenerator extends HttpServlet{
	String apiKey = "";
	String custid = "";
	String issuer = "";
	String subject = "";
	long exptime;
	
	public void init(){
		 issuer = "SreelakshmiAjith";
		 subject = "jwt token";
		 exptime = 432000000;
		 apiKey = "jwtkey";
	}

	public void doGet(HttpServletRequest prequest, HttpServletResponse presponse) throws IOException{
		String queryparams = prequest.getParameter("custid");
		String lresponse = "";
		if(!queryparams.isEmpty()){
			lresponse = createJWT(queryparams, issuer, subject, exptime);
			JSONObject ljsonobj = new JSONObject();
			ljsonobj.append("token", lresponse);
			presponse.setContentType("application/json");
			PrintWriter out = presponse.getWriter();
			out.println(ljsonobj);
		}else{
			presponse.sendError(500);
		}
	}
	
	private String createJWT(String id, String issuer, String subject, long exptime) {
		 
	    //The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	 
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
	 
	    //We will sign our JWT with our ApiKey secret
	    
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	 
	    //Let's set the JWT Claims
	    JwtBuilder builder = Jwts.builder().setId(id)
	                                .setIssuedAt(now)
	                                .setSubject(subject)
	                                .setIssuer(issuer)
	                                .signWith(signatureAlgorithm, signingKey);
	 
	    //if it has been specified, let's add the expiration
	    if (exptime >= 0) {
	    	long expMillis = nowMillis + exptime;
	        Date exp = new Date(expMillis);
	        builder.setExpiration(exp);
	    }
	 
	    //Builds the JWT and serializes it to a compact, URL-safe string
	    return builder.compact();
	}
}
