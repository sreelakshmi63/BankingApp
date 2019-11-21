package jwtFilter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class MainFilter implements Filter{
	String apiKey = "";
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		apiKey = "jwtkey	";
	}
	
	public void doFilter(ServletRequest prequest, ServletResponse presponse, FilterChain chain)
			throws IOException, ServletException {
		String header = ((HttpServletRequest) prequest).getHeader("Authorization");
		HttpServletResponse phttpresponse = (HttpServletResponse)presponse;
        if (header == null || !header.startsWith("Bearer ")) {
        	phttpresponse.sendError(403, "Invalid Token");
        	return;
        }

        String authToken = header.substring(7);
        try{
        	Claims claims = Jwts.parser()         
 			       .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey))
 			       .parseClaimsJws(authToken).getBody();
        }catch(Exception e){
        	phttpresponse.sendError(403, "Invalid Token");
        	return;
        }
		
		chain.doFilter(prequest, presponse);
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}
