import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Login {

	public String[] login(JFrame frame) {//opens a JFrame which gets a username and password from the user
	    String[] logininformation = new String[2];

	    JPanel panel = new JPanel(new BorderLayout(5, 5));

	    JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
	    label.add(new JLabel("Username", SwingConstants.RIGHT));
	    label.add(new JLabel("Password", SwingConstants.RIGHT));
	    panel.add(label, BorderLayout.WEST);

	    JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
	    JTextField username = new JTextField();
	    controls.add(username);
	    JTextField password = new JTextField();
	    controls.add(password);
	    panel.add(controls, BorderLayout.CENTER);

	    JOptionPane.showMessageDialog(frame, panel, "login", JOptionPane.QUESTION_MESSAGE);
	    JOptionPane.showMessageDialog(frame, panel, "register", JOptionPane.QUESTION_MESSAGE);

	    PasswordAuthentication encrypt = new PasswordAuthentication();
	    
	    logininformation[0] = username.getText();
	    logininformation[1] = encrypt.hash(password.getText().toCharArray());//takes entered password and encrypts it
	    return logininformation;//return username and encrypted password to LoginClient
	}
}