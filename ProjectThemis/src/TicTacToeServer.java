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

//A helper class for ProjectThemisServerThread. 
public class TicTacToeServer {
	
	Connection conn = null; //A connection to the MySQL Database.
	Statement stmt = null; //A statement to be executed on the database.
	ResultSet rs = null; //The results from a query.
	final String DB_URL = "jdbc:mysql://localhost:3306/project_themis_test?useSSL=false"; //Database location and name.
	
	String USER = ProjectThemisServer.USER; //Grab the username and passwordfrom the ProjectThemisServer..
	String PASS = ProjectThemisServer.PASS;
	
	int playerID; //The player's ID. This should be passed in from the server when created.
	int pvpID;
	
	PrintWriter os;
	TicTacToeListener listener;
	
	public TicTacToeServer( PrintWriter outputStream, int PID){
		
		playerID = PID;
		pvpID = 2; //DEBUGGING
		
		os = outputStream;
		
		final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; //The driver it's used for JDBC.
		try{
			Class.forName(JDBC_DRIVER);

		}catch(ClassNotFoundException e){
			System.out.println("Cannot find the driver class!");
			e.printStackTrace();
		}

		try{ //DEBUGGING - Pretty sure this chunk can be deleted.
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
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	/*	
		while(running){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//sendMoves(markMoves());
		}
	*/
		
		listener = new TicTacToeListener(pvpID, os); //Starts the listener thread which will watch for moves being added to the database.
		listener.start(); //Start the listener.
	}
	
	//Processes the inputs received from ProjectThemisServerThread. 
	void processInput(String[] inputs){
		
		System.out.println("Inputs Being Processed...");
		
		switch (inputs[1]) {
			case "NEWGAME": //The Client is trying to start a new game.
				closeCurrentGame();
				SQLNewGame();
				break;
			case "MOVE": //The client has sent new moves that should be written to the MySQL Database.
				markMove(inputs[2], inputs[3], inputs[4]);
				break;
			case "REQUEST": //The client would like to know if there are any new moves from the database. If so, send them!
				//sendMoves(markMoves());
				
		}
		
		System.out.println("Inputs processed!");
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
			stmt.setInt(2, pvpID);
			stmt.setInt(3, Integer.parseInt(row));
			stmt.setInt(4, Integer.parseInt(col));
			stmt.setInt(5, Integer.parseInt(dat));
			
			//Execute the preparedstatement
			stmt.execute();
			
			System.out.println("Executed MySQL write");
			//rs = stmt.executeQuery("INSERT INTO MOVES VALUES(," + playerID + ", " + pvpID + ", " + row + ", " + col + ", " + dat + ", NULL)"); //Get all PVP info, merged with game so we can look for Tic Tac Toe. For now, let's do this.
			//if (stmt.execute("INSERT INTO MOVES VALUES(," + playerID + ", " + pvpID + ", " + row + ", "+ col + ", " + dat + ", NULL)"))
		}
		catch (SQLException e){
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    return false;
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
	
	//Set the current game to inactive.
	void closeCurrentGame(){
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			
			stmt.execute("UPDATE PVP SET PVP_ACTIVE = 0 WHERE PVP_ID = " + pvpID +";");
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
	
	//Make a new game - set up the DB for it and find the pvpID.
	void SQLNewGame(){
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			//rs = stmt.executeQuery("SELECT PVP_ID, PVP_PLAYER_P2, PVP_GAME_ID FROM PVP WHERE PVP_GAME_ID = 1 AND PVP_PLAYER_P2 IS NULL;"); //Get all PVP info, merged with game so we can look for Tic Tac Toe. For now, let's do this.
			if (stmt.execute("SELECT PVP_ID, PVP_PLAYER_P2, PVP_GAME_ID FROM PVP WHERE PVP_GAME_ID = 1 AND PVP_PLAYER_P2 IS NULL AND PVP_PLAYER_P1 != " + playerID + ";")) {
				rs = stmt.getResultSet();
				}
			    
			    ResultSetMetaData rsmd = rs.getMetaData();
			    int columnsNumber = rsmd.getColumnCount();
			    if (rs.next()) {
			    	pvpID = rs.getInt(1);
			        stmt.execute("UPDATE PVP SET PVP_PLAYER_P2 = " + playerID + " WHERE PVP_ID = " + pvpID + ";");
			        os.println("TICTACTOE PLAYER 2");
			        os.flush();
			    }
			    else{
			    	if(stmt.execute("SELECT PVP_ID FROM PVP WHERE PVP_GAME_ID = 1 AND PVP_PLAYER_P1 = " + pvpID + " AND PVP_ACTIVE = 1;")); //Check if there's an active game.
			    		rs = stmt.getResultSet();
			    	if(rs.next()){ //Make sure we can look at the next thinger first...
			    		pvpID = rs.getInt(1);
			    		os.println("TICTACTOE PLAYER 1");
			    		os.flush();
			    	}
			    	else{
			    		if(stmt.execute("SELECT PVP_ID FROM PVP WHERE PVP_GAME_ID = 1 AND PVP_PLAYER_P2 = " + pvpID + " AND PVP_ACTIVE = 1;")); //Check if there's an active game.
			    			rs = stmt.getResultSet();
				    	if(rs.next()){ //Make sure we can look at the next thinger first...
				    		pvpID = rs.getInt(1);
				    		os.println("TICTACTOE PLAYER 2");
				    		os.flush();
				    	}
				    	else{ //If we can't look at the next thing, there must not be an active game, so make a new one!
					    	stmt.execute("INSERT INTO PVP (PVP_PLAYER_P1, PVP_GAME_ID, PVP_ACTIVE) VALUES (" + playerID + ", 1, 1);");
					    	if(stmt.execute("SELECT PVP_ID FROM PVP WHERE PVP_GAME_ID = 1 AND PVP_PLAYER_P2 IS NULL AND PVP_PLAYER_P1 = " + playerID + ";")){
					    		rs = stmt.getResultSet();
					    		rs.next();
					    		pvpID = rs.getInt(1);
					    		os.println("TICTACTOE PLAYER 1");
					    		os.flush();
					    	}
				    	}
			    	}
			    }
			    listener.updatePVPID(pvpID);
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
	
	void sendMoves(int[][] moves){
		if(moves != null)
			for(int i = 0; i < moves.length; i++){
				os.flush();
				os.println("TICTACTOE MOVE " + moves[i][0] + " " + moves[i][1] + " " + moves[i][2]);
				os.flush();
		}
	}
}

class TicTacToeListener extends Thread {
	
	//See TicTacToeServer for  an explanation of all this.
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	final String DB_URL = "jdbc:mysql://localhost:3306/project_themis_test?useSSL=false";
	String USER = ProjectThemisServer.USER;
	String PASS = ProjectThemisServer.PASS;
	
	int pvpID;
	
	PrintWriter os;
	
	//Constructor method. 
	TicTacToeListener(int pvpID, PrintWriter os){
		this.pvpID = pvpID;
		this.os = os;
		System.out.println("TicTacToeListener initialized!");
	}
	
	//This is called when you start a thread. Vital.
	public void run(){
		while(true){
			try {
				Thread.sleep(500); //Wait a half second.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			sendMoves(findMoves()); //Check for moves, then send them.
		}
	}
	
	//Find any moves in the database for the game. Ideally, we should be storing old ones so we don't keep sending them.  #Goals
	int[][] findMoves(){
		
		int[][] moves = null; //The new moves will go here.
	
		//int[][] moves = new int[81][3]; //Maximum needed for TTT - lol jk
	
		try{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT MOVE_ROW, MOVE_COL, MOVE_D1 FROM MOVES WHERE MOVE_PVP_ID = " + pvpID + ";"); //Get all PVP info, merged with game so we can look for Tic Tac Toe. For now, let's do this.
			if (stmt.execute("SELECT MOVE_ROW, MOVE_COL, MOVE_D1 FROM MOVES WHERE MOVE_PVP_ID = " + pvpID + ";")) {
				rs = stmt.getResultSet();
				}
		    
			
			
			if(rs.last()) {
				moves = new int[rs.getRow()][3]; //To determine the size, jump to the end of the results, and get the row.
				rs.beforeFirst(); //Return the cursor to BEFORE the start so next goes to the first one.
				int i = 0; //An int, for counting
			    while (rs.next()) { //While there are more lines from our SELECT query above, keep parsing it into an array.
			    	moves[i][0] = rs.getInt(1);
			    	moves[i][1] = rs.getInt(2);
			    	moves[i][2] = rs.getInt(3);
			    	i++;
			    }    
			    //return moves;
			}	
		    //ResultSetMetaData rsmd = rs.getMetaData(); //Is this needed?
		    //int columnsNumber = rsmd.getColumnCount(); //Same with this.
		     
		}
		catch (SQLException e){
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
		finally{ //Make sure we close the connection.
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return moves;
	}
	
	void sendMoves(int[][] moves){
		if(moves != null)
			for(int i = 0; i < moves.length; i++){
				os.flush();
				os.println("TICTACTOE MOVE " + moves[i][0] + " " + moves[i][1] + " " + moves[i][2]);
				os.flush();
		}
	}
	
	void updatePVPID(int pvpID){
		this.pvpID = pvpID;
	}
}
