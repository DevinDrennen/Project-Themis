import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import javax.swing.*;

public class loginClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		 int portNumber = 4445;
		 String hostName = "themis.servegame.com";
		 Socket socket = new Socket(hostName, portNumber); //The socket is the channel it uses to communicate with the server.
         PrintWriter os = new PrintWriter(socket.getOutputStream(), true); //Output stream - things are sent from here.

		
		
		Login in = new Login();
		String[] info = in.login(null);//gets login information from user input
		os.println("USERNAME" + info[1] + "ENCRYPTEDPASSWORD" + info[2]);//probably passes stuff to the server
	}
}
