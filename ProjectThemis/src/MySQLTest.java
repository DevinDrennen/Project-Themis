import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLTest {

	
	
	public static void main(String[] args) {
			
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		final String DB_URL = "jdbc:mysql://localhost:3306/project_themis_test?useSSL=false";
		final String USER = args[1];
		final String PASS = args[2];
		
		int playerID;
		int pvpID;
		
		boolean running = false;
		
		BufferedReader is;
		PrintWriter os;
	
		final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		try{
			Class.forName(JDBC_DRIVER);

		}catch(ClassNotFoundException e){
			System.out.println("Cannot find the driver class!");
			e.printStackTrace();
		}

		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			
			System.out.println("About to execute statement!");
			
			String query = " insert into PLAYER VALUES(4, \"TEST4\")";
			PreparedStatement ppStmt = conn.prepareStatement(query);
			ppStmt.execute();
			
			System.out.println("Statement executed!");
			
		}
		catch (SQLException e){
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}

	}

}
