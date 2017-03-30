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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;

public class TicTacToeServer {
	
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	final String DB_URL = "jdbc:mysql://localhost:3306/project_themis_test?useSSL=false";
	String USER = "root";
	String PASS = "123456";
	
	int playerID;
	int pvpID;
	
	boolean running = false;
	
	BufferedReader is;
	PrintWriter os;
	
	public TicTacToeServer(BufferedReader inputStream, PrintWriter outputStream, int PID, String USER, String PASS){
		
		playerID = 1;
		running = true;
		
		is = inputStream;
		os = outputStream;
		
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
			
			/*
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
			    
			    */
			
		}
		catch (SQLException e){
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
		
		while(running){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			sendMoves(markMoves());
		}
	}
	
	void processInput(String[] inputs){
		switch (inputs[1]) {
			case "NEWGAME": //The Client is trying to start a new game.
				SQLNewGame();
				break;
			case "MOVE":
				markMove(inputs[2], inputs[3], inputs[4]);
		}
	}
	
	//Mark a new move. Return true if it succeeds.
	boolean markMove(String row, String col, String dat){
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			
			String query = " insert into MOVES (MOVE_PLAYER_ID, MOVE_PVP_ID, MOVE_ROW, MOVE_COL, MOVE_D1) " +
					"values(?, ?, ?, ?, ?)";
			
			//Create an insert PreparedStatement,and insert out values.
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, playerID);
			stmt.setInt(2,  pvpID);
			stmt.setInt(3, Integer.parseInt(row));
			stmt.setInt(4, Integer.parseInt(col));
			stmt.setInt(5, Integer.parseInt(dat));
			
			//Execute the preparedstatement
			stmt.execute();
			
			
			//rs = stmt.executeQuery("INSERT INTO MOVES VALUES(," + playerID + ", " + pvpID + ", " + row + ", " + col + ", " + dat + ", NULL)"); //Get all PVP info, merged with game so we can look for Tic Tac Toe. For now, let's do this.
			//if (stmt.execute("INSERT INTO MOVES VALUES(," + playerID + ", " + pvpID + ", " + row + ", "+ col + ", " + dat + ", NULL)"))
		}
		catch (SQLException e){
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    return false;
		}
		return true;
	}
	
	void SQLNewGame(){
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT PVP_ID, PVP_PLAYER_P2, PVP_GAME_ID FROM PVP WHERE PVP_GAME_ID EQUALS 1 AND PVP_PLAYER_P2 IS NULL"); //Get all PVP info, merged with game so we can look for Tic Tac Toe. For now, let's do this.
			if (stmt.execute("SELECT PVP_ID, PVP_PLAYER_P2, PVP_GAME_ID FROM PVP WHERE PVP_GAME_ID EQUALS 1 AND PVP_PLAYER_P2 IS NULL")) {
				rs = stmt.getResultSet();
				}
			    
			    ResultSetMetaData rsmd = rs.getMetaData();
			    int columnsNumber = rsmd.getColumnCount();
			    if (rs.next()) {
			    	pvpID = rs.getInt(0);
			        stmt.execute("UPDATE PVP SET PVP_PLAYER_P2 = " + playerID + " WHERE PVP_ID = " + pvpID);
			    }    
		}
		catch (SQLException e){
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
	}
	
	int[][] markMoves(){
		int[][] moves = new int[9][3]; //Maximum needed for TTT
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT ROW, COL, D1 FROM MOVES WHERE PVP_ID EQUALS " + pvpID); //Get all PVP info, merged with game so we can look for Tic Tac Toe. For now, let's do this.
			if (stmt.execute("SELECT ROW, COL, D1 FROM MOVES WHERE PVP_ID EQUALS " + pvpID)) {
				rs = stmt.getResultSet();
				}
			    
			    ResultSetMetaData rsmd = rs.getMetaData();
			    int columnsNumber = rsmd.getColumnCount();
			    int i = 0;
			    while (rs.next()) {
			    	moves[i][0] = rs.getInt(0);
			    	moves[i][1] = rs.getInt(1);
			    	moves[i][2] = rs.getInt(2);
			    	i++;
			    }    
			    
			    return moves;
		}
		catch (SQLException e){
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
		
		return null;
	}
	
	void sendMoves(int[][] moves){
		for(int i = 0; i < moves.length; i++){
			os.println("TICTACTOE MOVE + " + moves[i][0] + " " + moves[i][1] + " " + moves[i][2]);
		}
	}
}
