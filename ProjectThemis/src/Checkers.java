import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Checkers extends JFrame {
	static int dimension = 8; // input board size here
	String name;
	char[][] board;
	int[][] possibleMoves;
	JButton[][] display;
	JPanel boardpanel;
	JLabel playerTurn = new JLabel("Player Turn Info Here");
	//JButton NewGame = new JButton("New Game"); no need for a new game button while in game
	int moveCount = 0; // probably don't need this for most games
	boolean redTurn = true;
	boolean gameOver = false;

	public Checkers(int d, String GameName) {
		int i,j;

		setTitle(GameName);
		setSize(750,750);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		board = new char[d][d];
		display = new JButton[d][d];
		boardpanel = new JPanel();
		boardpanel.setLayout(new GridLayout(d,d));
		for (i=0;i<d;i++)
			for (j=0;j<d;j++) {
				display[i][j] = new JButton();
				display[i][j].setFont(new Font(null,1,125));
				display[i][j].setFocusPainted(false);
				display[i][j].addActionListener(new MoveListener(i,j));
				//display[i][j].setBorderPainted(false);
				boardpanel.add(display[i][j]);
				board[i][j] = ' ';
			}
		for (i=0;i<d;i++){
			for (j=0;j<d;j++) {
				if ((i+j) % 2 != 0) {
					//display[i][j].setBackground(Color.red);
					if (i<3) display[i][j].setBackground(Color.black);
					display[i][j].setOpaque(true);
				}
				if ((i+j) % 2 != 0) {
					if (i>4)  display[i][j].setBackground(Color.red);
					display[i][j].setOpaque(true);
				}	
			}
		}
		add(boardpanel,BorderLayout.CENTER);
		add(playerTurn,BorderLayout.NORTH);
		playerTurn.setHorizontalAlignment(JLabel.CENTER);
		playerTurn.setFont(new Font(null,1,24));
		setLocationRelativeTo(null);
		setVisible(true);


	}
	private class MoveListener implements ActionListener {
		int r,c;
		public MoveListener(int row,int col) {
			r = row;
			c = col;
		}
		public void actionPerformed(ActionEvent e) {
			if (!gameOver) {

			}
		}
	}

	public static void main(String[] args) {
		new Checkers(dimension, "Checkers"); // change name for each different game here
	}

}