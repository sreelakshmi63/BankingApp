package bankingApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainBanking extends HttpServlet{
	public String ifsccode;
	public String bankname;
	public String city;
	
	public void init(){
		ifsccode = "";
		bankname = "";
		city = "";
	}
	
	public static void main(String[] args) throws SQLException{
		String lresponse = getBankDetails("ALLA0210156", "", "");
		System.out.println(lresponse);
	}
	
	public void doGet(HttpServletRequest prequest, HttpServletResponse presponse) throws IOException{
		Map<String, String[]> queryparams = prequest.getParameterMap();
		String lresponse = "";
		String limit = "";
		String offset = "";
			try {
				if(queryparams.containsKey("ifsccode")){	
					for(int i=0; i<queryparams.get("ifsccode").length; i++){
						if(queryparams.containsKey("limit") ){
							limit = queryparams.get("limit")[i];
						}
						if(queryparams.containsKey("offset")){
							offset = queryparams.get("offset")[i];
						}
						lresponse += getBankDetails(queryparams.get("ifsccode")[i], limit, offset);
					}	
				}else if(queryparams.containsKey("bankname") && queryparams.containsKey("city")){
					for(int i=0; i<queryparams.get("bankname").length; i++){
						if(queryparams.containsKey("limit") ){
							limit = queryparams.get("limit")[i];
						}
						if(queryparams.containsKey("offset")){
							offset = queryparams.get("offset")[i];
						}
						lresponse += getBankDetails(queryparams.get("bankname")[i], queryparams.get("city")[i], limit, offset);
					}				
				}else{
					presponse.sendError(500);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		presponse.setContentType("text/html");
		PrintWriter out = presponse.getWriter();
		out.println("<h1>" + lresponse + "</h1>");
	}
	
	/*public ArrayList<String> parseQueryParams(String pparams){
		ArrayList<String> queryparams = new ArrayList<String>();
		String param ="";
		for(int i = 0; i < pparams.split("&").length; i++){
			param = pparams.split("&")[i].split("=")[1];
			param = param.replace("%20"," ");
			queryparams.add(param);
		}
		return queryparams;
	}*/
	
	public static String getBankDetails(String pifsccode, String limit, String offset) throws SQLException{
		String lquery = "";
		if(!limit.isEmpty() && !offset.isEmpty()){
			System.out.println("0");
			lquery = "SELECT * FROM bank_details WHERE bank_ifsc = '" + pifsccode + "' LIMIT " + limit + " OFFSET " + offset + ";";
		}else if(!limit.isEmpty()){
			System.out.println("1");
			lquery = "SELECT * FROM bank_details WHERE bank_ifsc = '" + pifsccode + "' LIMIT " + limit + ";";
		}else if(!offset.isEmpty()){
			System.out.println("1");
			lquery = "SELECT * FROM bank_details WHERE bank_ifsc = '" + pifsccode + "' OFFSET " + offset + ";";
		}else{
			System.out.println("2");
			lquery = "SELECT * FROM bank_details WHERE bank_ifsc = '" + pifsccode + "';";
		}

		String lresponse = "";
		ResultSet rs = getDbData(lquery);
		ResultSetMetaData rsmd = rs.getMetaData();
		while(rs.next()) {
			for(int i = 1; i <= rsmd.getColumnCount(); i++){
				lresponse += rsmd.getColumnName(i) + ": " + rs.getString(i) + "<br>";
			}	
		}
		if(lresponse.isEmpty()){
			lresponse = "NO DATA FOUND";
		}else{
			lresponse += "------------------------------------------------------------<br>";
		}
		
		return lresponse;
	}
	
	public static String getBankDetails(String pbankname, String pcity, String limit, String offset) throws SQLException{
		String lquery = "";
		if(!limit.isEmpty() && !offset.isEmpty()){
			lquery = "SELECT * FROM bank_details WHERE bank_name= '" + pbankname + "' AND bank_city = '" + pcity  + "' LIMIT " + limit + " OFFSET " + offset + ";";
		}else if(!limit.isEmpty()){
			lquery = "SELECT * FROM bank_details WHERE bank_name= '" + pbankname + "' AND bank_city = '" + pcity + "' LIMIT " + limit + ";";
		}else if(!limit.isEmpty()){
			lquery = "SELECT * FROM bank_details WHERE bank_name= '" + pbankname + "' AND bank_city = '" + pcity + "' OFFSET " + offset + ";";
		}else{
			lquery = "SELECT * FROM bank_details WHERE bank_name= '" + pbankname + "' AND bank_city = '" + pcity + "';";
		}
		
		String lresponse = "";
		ResultSet rs = getDbData(lquery);
		ResultSetMetaData rsmd = rs.getMetaData();
		while(rs.next()){
			for(int i = 1; i <= rsmd.getColumnCount(); i++){
				lresponse += rsmd.getColumnName(i) + ": " + rs.getString(i) + "<br>";
			}
			lresponse += "------------------------------------------------------------<br>";
		}
		return lresponse;
	}
	
	public static ResultSet getDbData(String pquery){ 
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
			Statement st = conn.createStatement();
			System.out.println(pquery);
			rs = st.executeQuery(pquery);
			System.out.println(rs.getFetchSize());
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
