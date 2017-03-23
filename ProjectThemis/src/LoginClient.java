import java.awt.*;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

public class loginClient {

	public static void main(String[] args) {

		Login in = new Login();
		String[] info = in.login(null);//gets login information from user input

		try {
			ProjectThemisClient.main(info);//probably passes stuff to the server
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
