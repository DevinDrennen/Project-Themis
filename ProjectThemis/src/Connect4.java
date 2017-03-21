
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import javax.swing.*;

public class Connect4 extends JFrame {


	static char[][] board;
	static JButton[][] display;
	JPanel boardpanel;
	JLabel playerTurn = new JLabel("It is Red's Turn!");
	//JButton NewGame = new JButton("New Game");
	int moveCount = 0;
	boolean gameOver = false;
	//static boolean redTurn = true;

	static Turn turn;

	public Connect4(int n) {
		int r,c;
		turn = new Turn();
		turn.red();
		setTitle("Connect Four");
		setSize(500,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		board = new char[n][n+1];
		display = new JButton[n][n+1];
		boardpanel = new JPanel();
		boardpanel.setLayout(new GridLayout(n,n+1));
		for (r=0;r<n;r++)
			for (c=0;c<n+1;c++) {
				display[r][c] = new JButton();
				display[r][c].setFont(new Font(null,1,40));
				display[r][c].setFocusPainted(false);
				display[r][c].addActionListener(new MoveListener(r,c));
				boardpanel.add(display[r][c]);
				board[r][c] = ' ';
			}
		add(boardpanel,BorderLayout.CENTER);
		//add(NewGame,BorderLayout.SOUTH);
		add(playerTurn,BorderLayout.NORTH);
		playerTurn.setHorizontalAlignment(JLabel.CENTER);
		playerTurn.setFont(new Font(null,1,24));
		//NewGame.setFont(new Font(null,1,20));
		//NewGame.addActionListener(new NewGameListener());
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
				if (makeMove(r,c)) {
					moveCount++;
					winCheck(turn.symbol,moveCount);
					turn.flip();  
					playerTurn.setText("It is " +turn.name+ "'s Turn!");
				}
			}
		}
	}

	private static boolean makeMove(int row, int col) {
		int i=5;
		while (i>=0){
			if (board[i][col]==' '){
				board[i][col]=turn.symbol;
				//display[i][col].setText(""+turn.symbol);
				display[i][col].setBackground(turn.color);
				return true;
			}
			i--;
		}
		return false;

	}

	//checks for win or tie

	public void winCheck(char h, int count) {
		int j,i;

		for (i = 0; i < board.length; i++) 
			if ((board[i][0] == h) && (board[i][1] == h) && (board[i][2] == h)) {
				playerTurn.setText(h+" Wins!");
				gameOver = true;
			}

		for (j = 0; j < board.length; j++)
			if ((board[0][j] ==  h) && (board[1][j] == h) && (board[2][j] == h)) { 
				playerTurn.setText(h+" Wins!");
				gameOver = true;
			}

		if ((board[0][0] == h) && (board[1][1] == h) && (board[2][2] == h)) {
			playerTurn.setText(h+" Wins!");
			gameOver = true;
		}

		if ((board[0][2] == h) && (board[1][1] == h) && (board[2][0] == h)) {
			playerTurn.setText(h+" Wins!"); 
			gameOver = true;
		}

		if (count > 8 && gameOver == false) 
			playerTurn.setText("Out of moves, no one wins!");
	}

	public static void main(String[] args) {
		new Connect4(6);

	}

}