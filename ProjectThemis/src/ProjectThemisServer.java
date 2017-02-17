
import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ProjectThemisServer {

	public static void main(String[] args) {

		/*
		 * if (args.length != 1) { System.err.println(
		 * "Usage: java ProjectThemisServer <port number>"); System.exit(1); }
		 * 
		 * int portNumber = Integer.parseInt(args[0]);
		 */

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
				ServerThread st = new ServerThread(s);
				st.start();

			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Connection Error");

			}
		}
	}
}

class ServerThread extends Thread {

	String line = null;
	BufferedReader is = null;
	PrintWriter os = null;
	Socket s = null;
	int clientID;

	public ServerThread(Socket s) {
		this.s = s;
	}

	public void run() {
		try {
			is = new BufferedReader(new InputStreamReader(s.getInputStream()));
			os = new PrintWriter(s.getOutputStream());

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
				if (s != null) {
					s.close();
					System.out.println("Socket Closed");
				}

			} catch (IOException ie) {
				System.out.println("Socket Close Error");
			}
		} // end finally
	}
	
	private void processInput(String inputLine) {
		String[] inputs = inputLine.split(" ");

		switch (inputs[0]) {
		case "ECHO": //When the ECHO command is given, repeat the previously given command.
			os.println(inputs[1]);
			os.flush();
			System.out.println("Response to Client " + s.getRemoteSocketAddress() + "  :  " + inputs[1]);
			break;
			
		case "GETID": //The GetID class will handle logins etc. The client should send this FIRST.
			
		case "TICTACTOE": //The TicTacToe prompt means we'll be handling the TicTacToe game's commands. 

		}
	}
}
