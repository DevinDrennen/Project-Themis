package server;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import shared.PasswordAuthentication;

public class LoginServer {
	static Connection conn = null; //A connection to the MySQL Database.
	static Statement stmt = null; //A statement to be executed on the database.
	static ResultSet rs = null; //The results from a query.
	static final String DB_URL = "jdbc:mysql://localhost:3306/project_themis_test?useSSL=false"; //Database location and name.
	
	static String USER = ProjectThemisServer.USER; //Grab the username and password from the ProjectThemisServer..
	static String PASS = ProjectThemisServer.PASS;
	
	PrintWriter os;
	
	public LoginServer(PrintWriter outputStream){
		os = outputStream;
	}
	
	//Username is the Player_Name to check for, Hashword is the Player_Password to check for. Hashword because it's hashed, get it?
	public static int tryLogin(String username, String password){ //Laugh.
		
		int pid = -1; //Player ID
		String token = null; //The authentication token we store
		boolean good = false; //Is the password good? That is, does it match?
		
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			
			if(stmt.execute("SELECT PLAYER_ID, PLAYER_PASSWORD FROM PLAYER WHERE PLAYER_NAME = '" + username + "';"))
				rs = stmt.getResultSet();
			
			if(rs.next()){
				pid = rs.getInt(1);
				token = rs.getString(2);
			}
			
			PasswordAuthentication authen = new PasswordAuthentication();
			
			if(authen.authenticate(password.toCharArray(), token))
				good = true;
			
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
		if(good)
			return pid;
		else
			return -1;
	}
	
	public static boolean checkPlayer(String username){
		
		boolean usernameExists = false;
		
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			
			if(stmt.execute("SELECT PLAYER_ID FROM PLAYER WHERE PLAYER_NAME = '" + username + "';"))
				rs = stmt.getResultSet();
			
			if(rs.next()){
				usernameExists = true;
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
		return usernameExists;
	}
	
	public static boolean addPlayer(String username, String hashword){
		
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			
			stmt.execute("INSERT INTO PLAYER (PLAYER_NAME, PLAYER_PASSWORD) VALUES('" + username + "', '" + hashword + "');");
			
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
		return true;
	}
	

}
