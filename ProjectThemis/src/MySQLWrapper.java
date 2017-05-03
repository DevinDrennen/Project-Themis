
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * @author Devin
 * A class because I'm lazy - also it'll help the other teams.
 * Will handle calling the MySQL Database.
 * Can only be used server-side.
 */
public class MySQLWrapper {
	String USER = ProjectThemisServer.USER; //Grab the username and passwordfrom the ProjectThemisServer..
	String PASS = ProjectThemisServer.PASS;
	final String DB_URL = ProjectThemisServer.DB_URL; //Database location and name.
	
	Connection conn = null; //A connection to the MySQL Database.
	Statement stmt = null; //A statement to be executed on the database.
	ResultSet rs = null; //The results from a query.
	
	MySQLWrapper(){
		
		final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; //Make sure we can locate the driver.
		try{
			Class.forName(JDBC_DRIVER);

		}
		catch(ClassNotFoundException e){
			System.out.println("Cannot find the driver class!");
			e.printStackTrace();
		}
			
	}
	

	/**
	 * @param query The query you want executed. Should be a select statement.
	 * @return The first string result of the query. Returns Integer.MIN_VALUE if rs is null.
	 */
	String queryString(String query){
		String result = null;
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			
			if(stmt.execute(query)){ //Try to execute the query. We don't want to get the result set if we don't have a result set to get
				rs = stmt.getResultSet(); //That causes a null pointer error and bad.
				if(rs.next()){ //Make sure our result set has contents first. Also helpful.
					result = rs.getString(1); //In this case, we're just returning that one string. Nice and ez.
				}
			}
		}
		catch (SQLException e){
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return result;
	}
	
	
	/**
	 * @param query The query you want executed. Should be a select statement.
	 * @return The first int of the query. Returns Integer.MIN_VALUE if rs is null.
	 */
	int queryInt(String query){
		int result = Integer.MIN_VALUE;
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			
			if(stmt.execute(query)){ //Try to execute the query. We don't want to get the result set if we don't have a result set to get
				rs = stmt.getResultSet(); //That causes a null pointer error and bad.
				if(rs.next()){ //Make sure our result set has contents first. Also helpful.
					result = rs.getInt(1); //In this case, we're just returning that one string. Nice and ez.
				}
			}
		}
		catch (SQLException e){
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return result;
	}
	
	

	/**
	 * @param query The query you want executed. Should not be a select statement.
	 */
	void query(String query){
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			
			stmt.execute(query);
		}
		catch (SQLException e){
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	
	/**
	 * @param query The query you want executed. Should be a query. Also probably won't work right.
	 * @return The ResultSet object of the result. Could be used for a single string or int as well. 
	 */
	ResultSet queryRS(String query){
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			rs = null;
			if(stmt.execute(query)){ //Try to execute the query. We don't want to get the result set if we don't have a result set to get
				rs = stmt.getResultSet(); //That causes a null pointer error and bad.
			}
		}
		catch (SQLException e){
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return rs;
	}
	
}
