import java.io.*;
import java.net.*;

public class ProjectThemisClient {

	public static void main(String[] args) throws IOException{
        String hostName = "localhost";
        int portNumber = 4445;

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
        	new PasswordAuthentication();         
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
}
