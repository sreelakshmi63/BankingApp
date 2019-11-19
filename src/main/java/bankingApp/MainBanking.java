package bankingApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainBanking extends HttpServlet{
	public String ifsccode;
	public String bankname;
	public String city;
	
	
	public void doGet(HttpServletRequest prequest, HttpServletResponse presponse) throws IOException{
		Map<String, String[]> queryparams = prequest.getParameterMap();
		JSONArray lresponse = null;
		String limit = "";
		String offset = "";
			try {
				if(queryparams.containsKey("ifsccode")){	
					lresponse = getBankDetails(queryparams.get("ifsccode"));
				}else if(queryparams.containsKey("bankname") && queryparams.containsKey("city")){
					if(queryparams.containsKey("limit") ){
						limit = queryparams.get("limit")[0];
					}
					if(queryparams.containsKey("offset")){
						offset = queryparams.get("offset")[0];
					}
					lresponse = getBankDetails(queryparams.get("bankname"), queryparams.get("city"), limit, offset);				
				}else{
					presponse.sendError(500);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		presponse.setContentType("application/json");
		PrintWriter out = presponse.getWriter();
		out.println(lresponse);
	}
	
	public static JSONArray getBankDetails(String[] pifsccode) throws SQLException{
		//String lquery = "SELECT * FROM bank_details WHERE bank_ifsc = '" + pifsccode + "';";
		String lquery = createQuery(pifsccode.length, -1);
		JSONArray ljsonarray = new JSONArray();
		JSONObject ljsonobj = new JSONObject();
		ResultSet rs = getDbData(lquery, pifsccode, null);
		ResultSetMetaData rsmd = rs.getMetaData();
		while(rs.next()) {
			ljsonobj = new JSONObject();
			for(int i = 1; i <= rsmd.getColumnCount(); i++){	
				ljsonobj.append(rsmd.getColumnName(i), rs.getString(i));
			}	
			ljsonarray.put(ljsonobj);
		}
		if(ljsonarray.length() == 0){
			ljsonobj.append("Response", "NO DATA FOUND");
			ljsonarray.put(ljsonobj);
		}
		
		return ljsonarray;
	}
	
	
	
	public static JSONArray getBankDetails(String[] pbankname, String[] pcity, String limit, String offset) throws SQLException{
		String lquery = createQuery(pbankname.length, pcity.length);
		if(!limit.isEmpty() && !offset.isEmpty()){
			lquery += " LIMIT " + limit + " OFFSET " + offset + ";";
		}else if(!limit.isEmpty()){
			lquery += " LIMIT " + limit + ";";
		}else if(!offset.isEmpty()){
			lquery += " OFFSET " + offset + ";";
		}
		
		JSONArray ljsonarray = new JSONArray();
		JSONObject ljsonobj = new JSONObject();
		ResultSet rs = getDbData(lquery, pbankname, pcity);
		ResultSetMetaData rsmd = rs.getMetaData();
		while(rs.next()){
			ljsonobj = new JSONObject();
			for(int i = 1; i <= rsmd.getColumnCount(); i++){
				ljsonobj.append(rsmd.getColumnName(i), rs.getString(i));
			}
			ljsonarray.put(ljsonobj);
		}
		if(ljsonarray.length() == 0){
			ljsonobj.append("Response", "NO DATA FOUND");
			ljsonarray.put(ljsonobj);
		}
		return ljsonarray;
	}
	
	public static String createQuery(int len1, int len2) { 
		String query = "";
		if(len2 == -1){
			query = "SELECT * FROM bank_details WHERE bank_ifsc in (";
		}else{
			query = "SELECT * FROM bank_details WHERE bank_name in (";
		}
		
		StringBuilder queryBuilder = new StringBuilder(query);
		for( int i = 0; i< len1; i++){
			queryBuilder.append(" ?");
			if(i != len1 -1) queryBuilder.append(",");
		}
		queryBuilder.append(")");
		if(len2 != -1){
			queryBuilder.append(" AND bank_city in (");
			for( int i = 0; i< len2; i++){
				queryBuilder.append(" ?");
				if(i != len2 -1) queryBuilder.append(",");
			}
			queryBuilder.append(")");
		}
		return queryBuilder.toString();
	}
	
	public static ResultSet getDbData(String pquery, String[] pparam1, String[] pparam2){ 
		ResultSet rs = null;
		try {
			String classname = "org.postgresql.Driver";
			String connurl = "jdbc:postgresql://ec2-54-217-228-25.eu-west-1.compute.amazonaws.com:5432/dfqq6q27s9a3h4" + "?sslmode=require";
			String username = "jzqculveibpsky";
			String password = "54845eb6297322892567b652eb110cfd18255557d6da4a4c9150ad186437d104";
			/*String classname = "com.mysql.jdbc.Driver";
			String connurl = "jdbc:mysql://localhost:3306/sampleBankingApp";
			String username = "root";
			String password = "root";*/
			Class.forName(classname);
			Connection conn = DriverManager.getConnection(connurl, username, password);
			//Statement st = conn.createStatement();
			PreparedStatement st = conn.prepareStatement(pquery);
			if(pparam2 == null){
				for(int i = 1; i <= pparam1.length; i++){
					st.setString(i, pparam1[i-1]);
				}
			}else{
				for(int i = 1; i <= pparam1.length; i++){
					st.setString(i, pparam1[i-1]);
				}
				for(int i = 1; i <= pparam2.length; i++){
					st.setString(i + pparam1.length, pparam2[i-1]);
				}
			}
			rs = st.executeQuery();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

}
