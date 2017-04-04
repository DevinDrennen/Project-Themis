import java.io.*;
import java.net.*;

//Main class for client side things.
//Will create client classes for each game.
public class ProjectThemisClient {

<<<<<<< HEAD
	static String user = null;
	static String pass = null;
	static int id;

	public static void main(String[] args) throws IOException{
        String hostName = "localhost";
        int portNumber = 4445;

        if(args.length >= 2){
        	user = args[0];
        	pass = args[1];
        }
        else{ //Remove this before deployment.
=======
	//User and pass will store a plaintext version of the user's username and password. 
	//Both are only stored while the client is running, and the password will not leave the client's machine.
	static String user = null;
	static String pass = null;
	
	//The player ID - this is stored on the MySQL Database.
	static int playerID;
	
	static String hostName;
	static int portNumber;
	
	//The client classes for games/utils. 
	static TicTacToeClient tttc;
	
	//Start with: ProjectThemisClient [HOSTNAME] [USER] [PASS]
	public static void main(String[] args) throws IOException{
		
		//A simple args parser if the user loves command line.
        if(args.length == 1){
        	hostName = args[0];
        	user = "user";
        	pass = "pass";
        }
        else if(args.length == 2){
        	user = args[0];
        	pass = args[1];
        	hostName = "themis.servegame.com";
        }
        else if(args.length == 3){
        	hostName = args[0];
        	user = args[1];
        	pass = args[2];
        }
        else{ //Remove this before deployment.
        	hostName = "themis.servegame.com";
>>>>>>> TicTacToe
        	user = "user";
        	pass = "pass";
        }
        
<<<<<<< HEAD
=======
        
        playerID = 1;
        int portNumber = 4445; //Don't forget to set this - this might join the command line parser in the future.
        //Todo: Find smart way to let the user decide the order of commands.
        
        //Sets up communications with server.
>>>>>>> TicTacToe
        try (
            Socket socket = new Socket(hostName, portNumber); //The socket is the channel it uses to communicate with the server.
            PrintWriter os = new PrintWriter(socket.getOutputStream(), true); //Output stream - things are sent from here.
            BufferedReader is = new BufferedReader( new InputStreamReader(socket.getInputStream())); //Input stream - things come in here.
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)) //TBH idk what this is for.
        ) {
        	
<<<<<<< HEAD
        	String line;
        	os.println("GETID " + user + " " + pass);
//        	new TicTacToeClient(3, is, os);         
        	line=is.readLine();
=======
        	String line; //Line is where incoming messages are temporarily stored.
        	//os.println("GETID " + user + " " + pass); //GETID will instead send our username and HASHED/SALTED password to the server.
        	//line=is.readLine(); //Afterwards, it'll get back the player's ID.
        	
        	os.println("GETID " + playerID); //This is just for testing,
        	os.println("TICTACTOE NEW"); //Right now we just have TicTacToe and no menu, so it'll always create TicTacToe.
        	os.println("TICTACTOE NEWGAME");
        	
        	
        	tttc = new TicTacToeClient(3, is, os); //Creates the clientside game.      
        	line=is.readLine();
        	while(line.compareTo("QUIT") != 0){ //Until it is told to QUIT by the sertver, keep reading new lines and processing them.
        		processInput(line);
        		line = is.readLine();
        	}
>>>>>>> TicTacToe
        	
          
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
	}
<<<<<<< HEAD
	private void processInput(String inputLine) {
		String[] inputs = inputLine.split(" ");

		switch (inputs[0]) {

		}
=======
	
	//Processes our inputs and ensures the right classes get their messages.
	//String inputLine: A string made up of multiple words/characters seperated by spaces.
	private static void processInput(String inputLine) {
		String[] inputs = inputLine.split(" "); //Make our String into an array of Strings whenever we see a space.
		
		System.out.println("Processing Inputs (Client)!"); //DEBUGGING
		
		//Switch to send messages to the correct location. It checks the first string in the series to determine it's use.
		switch (inputs[0]) { 
		case "TICTACTOE": //If the first String is TICTACTOE, send it to the TicTacToe game
			if(tttc != null) //Only send it if TTTC has been created.
				tttc.processInput(inputs);
		}
		
		System.out.println("Processed Inputs (Client)!"); //DEBUGGING
>>>>>>> TicTacToe
	}
}
