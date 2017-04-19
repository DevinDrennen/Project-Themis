import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;

import java.awt.Container;
import java.awt.event.MouseListener;

public class Menu extends JFrame {
	JFrame boardpanel;
	JLabel label;
	BoxLayout layout;
	Box box;
	Box pwBox;
	JOptionPane message;
	JPasswordField passwordField;
	
	boolean authen = false;
	
	Color darkestRed = new Color(71, 0, 15);
	Color darkRed = new Color(110, 5, 28);
	Color mediumRed = new Color(146, 28, 54);
	Color lightRed = new Color(174, 56, 82);
	Color lightestRed = new Color(210, 100, 124);
	
	Color backgroundColor = darkestRed;
	Color buttonColor = lightRed;
	Color textColor = darkRed;
	int fontSize = 30;
	
	public Menu() {
		setTitle("Game Menu");
		setSize(400,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		label = new JLabel("Select an option:");
		label.setLayout(layout);
		label.setForeground(mediumRed);
		label.setOpaque(true);
		label.setBackground(backgroundColor);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(new Font("Courier",1,50));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		layout = new BoxLayout(label, BoxLayout.Y_AXIS);
		
		box = Box.createVerticalBox(); 
		box.setOpaque(true);
		box.setBackground(backgroundColor);
		box.add(label);
	    box.add(setUpButton("Login"));
	    box.add(Box.createVerticalStrut(10));
	    box.add(setUpButton("Create an account"));
	    box.add(Box.createVerticalStrut(10));
	    box.add(setUpButton("Tic Tac Toe"));
	    box.add(Box.createVerticalStrut(10));
	    box.add(setUpButton("Connect 4"));
	    box.add(Box.createVerticalStrut(10));
	    box.add(setUpButton("Othello"));
	    box.add(Box.createVerticalStrut(10));
	    box.add(setUpButton("Help"));
	    
	    boardpanel = new JFrame();
	    boardpanel.add(box, BorderLayout.CENTER);
	    boardpanel.setBackground(backgroundColor);
	    boardpanel.setSize(600, 500);
	    boardpanel.add(label, BorderLayout.NORTH);
	    boardpanel.setBackground(backgroundColor);
	    boardpanel.setVisible(true);		
	}
	
	private JButton setUpButton(String name) {
		JButton button = new JButton(name);
		button.setBackground(buttonColor);
		button.setFont((new Font("Courier", 1, fontSize)));
		button.setForeground(textColor);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		button.addMouseListener(new MouseListener(){
			@Override
			public void mouseExited(MouseEvent e) {
				button.setForeground(textColor);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				switch (name) {
				case "Login": message= new JOptionPane();
						JOptionPane.showMessageDialog(message, "Enter your username and password");
						String username = null;
						while(username == null || username.length() == 0)
							username = JOptionPane.showInputDialog("Enter your username");
						pwBox = Box.createHorizontalBox();
						pwBox.add(new JLabel("Password: "));
						passwordField = new JPasswordField(24);
						pwBox.add(passwordField);
						message.add(pwBox);
						passwordField.requestFocusInWindow(); //this line does not have any effect. >:(
						message.showConfirmDialog(null, pwBox, "Enter your password", JOptionPane.OK_CANCEL_OPTION);
						String password = new String(passwordField.getPassword());


						System.out.println(password);
						
						ProjectThemisClient.setUser(username);
						ProjectThemisClient.setPass(password);

						authen = false;
						ProjectThemisClient.login();
						
						
						break;
						
				case "Create an account": 
						message = new JOptionPane();
						JOptionPane.showMessageDialog(message, "Create an account");
						String newusername = null;
						while(newusername == null|| newusername.length() == 0)
							newusername = JOptionPane.showInputDialog("Enter a username");
						//String newpassword = JOptionPane.showInputDialog("Enter a password");
						pwBox = Box.createHorizontalBox();
						pwBox.add(new JLabel("Password: "));
						passwordField = new JPasswordField(24);
						pwBox.add(passwordField);
						message.add(pwBox);
						message.showConfirmDialog(null, pwBox, "Enter a password", JOptionPane.OK_CANCEL_OPTION);
						String newpassword = new String(passwordField.getPassword());
						
						ProjectThemisClient.newAccount(newusername, newpassword);
					break;
				case "Connect 4" : //new Connect4(6);
					if(authen)
						ProjectThemisClient.launchConnect4();
					break;
				case "Tic Tac Toe": //new TicTacToeClient(3);
					if(authen)
						ProjectThemisClient.launchTicTacToe();
					break;
				case "Othello" : 
					if(authen)
						System.out.println("Othello Client");
					break;
				case "Help" : message = new JOptionPane();
						JOptionPane.showMessageDialog(message, "Make a selection to play a game");
					break;
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				button.setForeground(lightestRed);
			}
				
			@Override
			public void mousePressed(MouseEvent e) { }

			@Override
			public void mouseReleased(MouseEvent e) { } 
			
		});
		return button;
	}
	
	public void login(boolean auth){
		if(auth){
			JOptionPane.showMessageDialog(message, "Login succeded!");
			authen = true;
		}
		else{
			JOptionPane.showMessageDialog(message, "Login failed!");
			authen = false;
		}
	}

}
