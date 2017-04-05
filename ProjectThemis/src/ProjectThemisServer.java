
import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ProjectThemisServer {

	static String USER;
	static String PASS;
	
	public static void main(String[] args) {

		/*
		 * if (args.length != 1) { System.err.println(
		 * "Usage: java ProjectThemisServer <port number>"); System.exit(1); }
		 * 
		 * int portNumber = Integer.parseInt(args[0]);
		 */

		//Check if the correct number of args are being used. If not, abort it like I should've been.
		if(args.length < 2){
			System.out.println("Incorrect Args Format. Correct is ProjectThemisServer MySqlUSER MySqlPASS");
			System.exit(0);
		}
		
		//Use the args to fill out the USER and PASS for MySQL.
		USER = args[0];
		PASS = args[1];
		
		
		Socket s = null;
		ServerSocket ss2 = null;
		System.out.println("Server Listening......");
		try {
			ss2 = new ServerSocket(4445); // can also use static final PORT_NUM
											// , when defined

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Server error");

		}

		while (true) {
			try {
				s = ss2.accept();
				System.out.println("connection Established");
				ProjectThemisServerThread st = new ProjectThemisServerThread(s);
				st.start();

			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Connection Error");

			}
		}
	}
}

class ProjectThemisServerThread extends Thread {

	String line = null;
	BufferedReader is = null;
	PrintWriter os = null;
	Socket socket = null;
	int clientID;
	
	TicTacToeServer ttts;

	public ProjectThemisServerThread(Socket s) {
		this.socket = s;
	}

	public void run() {
		try {
			is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os = new PrintWriter(socket.getOutputStream());

		} catch (IOException e) {
			System.out.println("IO error in server thread");
		}

		try {
			line = is.readLine();
			while (line.compareTo("QUIT") != 0) {
				processInput(line);
				line=is.readLine();
			}
		} catch (IOException e) {

			line = this.getName(); // reused String line for getting thread
									// name
			System.out.println("IO Error/ Client " + line + " terminated abruptly");
		} catch (NullPointerException e) {
			line = this.getName(); // reused String line for getting thread
									// name
			System.out.println("Client " + line + " Closed");
		}

		finally {
			try {
				System.out.println("Connection Closing..");
				if (is != null) {
					is.close();
					System.out.println(" Socket Input Stream Closed");
				}

				if (os != null) {
					os.close();
					System.out.println("Socket Out Closed");
				}
				if (socket != null) {
					socket.close();
					System.out.println("Socket Closed");
				}

			} catch (IOException ie) {
				System.out.println("Socket Close Error");
			}
		} // end finally
	}
	
	private void processInput(String inputLine) {
		String[] inputs = inputLine.split(" ");

		System.out.println("Inputs: " + inputs[0] + " " + inputs[1]);
		
		switch (inputs[0]) {
		case "ECHO": //When the ECHO command is given, repeat the previously given command.
			os.println(inputs[1]);
			os.flush();
			System.out.println("Response to Client " + socket.getRemoteSocketAddress() + "  :  " + inputs[1]);
			break;
			
		case "GETID": //The GetID class will handle logins etc. The client should send this FIRST.
			int i = -1;
			i = LoginServer.tryLogin(inputs[0],  inputs[2]);
			if(i != -1){
				clientID = i;
				os.println("GETID " + clientID);
			}
			else {
				os.println("GETID -1");
			}
			
			break;
			
		case "NEWID":
			if(!LoginServer.checkPlayer(inputs[1]))
				LoginServer.addPlayer(inputs[1], inputs[2]);
			break;
			
		case "TICTACTOE": //The TicTacToe prompt means we'll be handling the TicTacToe game's commands. 
			if(inputs[1].equals("START")){
				ttts = new TicTacToeServer(os, clientID); //Create new TTT game if the second chunk of data is "NEW".
				System.out.println("Created new TicTacToeServer instance");
			}
			else if(ttts != null){
				if(inputs[1].equals("QUIT")) //Terminate the TTT game if the second chunk of data is "QUIT"
					ttts = null;
				else //In all other cases, let ttts handle the inputs as it's a game function. Yay!
					ttts.processInput(inputs);
			}
		}
	}
}
