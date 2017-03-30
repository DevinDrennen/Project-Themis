import java.io.*;
import java.net.*;

public class ProjectThemisClient {

	static String user = null;
	static String pass = null;
	static int id;
	
	
	//Here's some fun stuff:
	TicTacToeClient tttc;
	
	public static void main(String[] args) throws IOException{
        String hostName = "10.1.73.58";
        int portNumber = 4445;

        if(args.length >= 2){
        	user = args[0];
        	pass = args[1];
        }
        else{ //Remove this before deployment.
        	user = "user";
        	pass = "pass";
        }
        
        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter os =
                new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader is =
                new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))
        ) {
        	
        	String line;
        	os.println("GETID " + user + " " + pass);
        	os.println("TICTACTOE NEW");
        	
        	
        	new TicTacToeClient(3, is, os);         
        	line=is.readLine();
        	
          
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
	}
	private void processInput(String inputLine) {
		String[] inputs = inputLine.split(" ");

		switch (inputs[0]) {
		case "TICTACTOE":
			if(tttc != null)
				tttc.processInput(inputs);
		}
	}
}
