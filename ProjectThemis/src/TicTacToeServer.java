/* 
 * This class will be the connection from the server to 
 * each of the given clients. 
 * 
 * It will handle a number of basic commands for transmitting data
 * between the two.
 */
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;

public class TicTacToeServer {
	
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	final String DB_URL = "jdbc:mysql://localhost:3306/PT?useSSL=false";
	final String USER = "root";
	final String PASS = "123456";
	
	int playerID;
	
	public TicTacToeServer(BufferedReader is, PrintWriter os, int PID){
		
		playerID = PID;
		
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
			rs = stmt.executeQuery("SELECT EMP_ID, EMP_ADDR_CITY FROM EMPLOYEE WHERE EMP_ADDR_CITY = \"Elizabethtown\"");
			if (stmt.execute("SELECT EMP_ID, EMP_ADDR_CITY FROM EMPLOYEE WHERE EMP_ADDR_CITY = \"Elizabethtown\"")) {
				rs = stmt.getResultSet();
				}
			    
			    ResultSetMetaData rsmd = rs.getMetaData();
			    int columnsNumber = rsmd.getColumnCount();
			    while (rs.next()) {
			        for (int i = 1; i <= columnsNumber; i++) {
			            if (i > 1) System.out.print(",  ");
			            String columnValue = rs.getString(i);
			            System.out.print(columnValue + " " + rsmd.getColumnName(i));
			        }
			        System.out.println("");

			    }
			
		}
		catch (SQLException e){
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
	}
	
	void inputHandling(String[] inputs, PrintWriter os){
		switch (inputs[1]) {
			case "NEWGAME": //The Client is trying to start a new game.
				SQLNewGame();
				break;
		}
	}
	
	void SQLNewGame(){
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT PVP_ID, PVP_PLAYER_P2, PVP_GAME_ID WHERE PVP_GAME_ID EQUALS 1 AND PVP_PLAYER_P2 IS NULL"); //Get all PVP info, merged with game so we can look for Tic Tac Toe. For now, let's do this.
			if (stmt.execute("SELECT PVP_ID, PVP_PLAYER_P2, PVP_GAME_ID WHERE PVP_GAME_ID EQUALS 1 AND PVP_PLAYER_P2 IS NULL")) {
				rs = stmt.getResultSet();
				}
			    
			    ResultSetMetaData rsmd = rs.getMetaData();
			    int columnsNumber = rsmd.getColumnCount();
			    if (rs.next()) {
			        rs.getInt(1);
			    }    
		}
		catch (SQLException e){
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
	}
}
