package client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
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
	JPanel games;
	JPanel options;
	JLabel label;
	BoxLayout layout;
	Box box;Box gameBox;Box pwBox;
	JOptionPane message;
	JPasswordField passwordField;
	
	boolean authen = false;
	
	Color darkest = new Color(10, 120, 141);
	Color dark = new Color(37, 140, 159);
	Color medium = new Color(66, 164, 183);
	Color light = new Color(104, 191, 207);
	Color lightest = new Color(155, 218, 230);
	
	Color backgroundColor = darkest;	 //menu background
	Color buttonColor = light;			 //button background
	Color textColor = dark;				 //button text
	Color textHighlightColor = lightest; //button text when hovered
	Color titleTextColor = medium;		 //title text
	
	public Menu() {
		label = new JLabel("Project Themis:");
		label.setLayout(layout);
		label.setForeground(titleTextColor);
		label.setOpaque(true);
		label.setBackground(backgroundColor);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(new Font("Rockwell",1,48));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		layout = new BoxLayout(label, BoxLayout.Y_AXIS);
		
		gameBox = Box.createVerticalBox(); 
		gameBox.setOpaque(true);
		gameBox.setBackground(backgroundColor);
		gameBox.add(label);
		gameBox.add(Box.createVerticalStrut(25));
		gameBox.add(setUpButton("Tic Tac Toe", 35));
	    gameBox.add(Box.createVerticalStrut(15));
	    gameBox.add(setUpButton("Connect 4", 35));
	    gameBox.add(Box.createVerticalStrut(15));
	    
	    options = new JPanel();
	    options.setBackground(backgroundColor);
	    options.setLayout(new FlowLayout());
	    options.add(setUpButton("Login", 15));
	    options.add(setUpButton("Create an account", 15));
	    options.add(setUpButton("Help", 15));
	    
	    boardpanel = new JFrame();
	    boardpanel.add(gameBox, BorderLayout.CENTER);
	    boardpanel.setTitle("Menu");
	    boardpanel.add(options, BorderLayout.SOUTH);
	    boardpanel.setSize(400, 300);
	    boardpanel.setResizable(false);
	    boardpanel.add(label, BorderLayout.NORTH);
	    boardpanel.setVisible(true);
	    boardpanel.setLocationRelativeTo(null);
	    boardpanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private JButton setUpButton(String name, int fontSize) {
		JButton button = new JButton(name);
		button.setBackground(buttonColor);
		button.setFont((new Font("Franklin Gothic Demi", 1, fontSize)));
		button.setForeground(textColor);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setFocusPainted(false);
		
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
				case "Help" : message = new JOptionPane();
						JOptionPane.showMessageDialog(message, "Create an account and login to play a game!" + 
									     "\nTicTacToe: Your goal is to get three of your pieces in a row. Click to place a piece." +
									      "\nConnect Four: Your goal is to get 4 of your pieces in a row. Placing a piece by clicking will result in the placed piece falling to the lowest available position in the selected column.");
					
					break;
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				button.setForeground(textHighlightColor);
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
