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
	int moveCount = 0;
	boolean gameOver = false;
	int numRows;
	int numCols;

	static int currentRow;

	static Turn turn;

	public Connect4(int n) {
		int r,c;
		numRows = n;
		numCols = n+1;
		turn = new Turn();
		turn.red();
		setTitle("Connect Four");
		setSize(500,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		board = new char[numRows][numCols];
		display = new JButton[numRows][numCols];
		boardpanel = new JPanel();
		boardpanel.setLayout(new GridLayout(numRows,numCols));
		for (r=0;r<numRows;r++)
			for (c=0;c<numCols;c++) {
				display[r][c] = new JButton();
				display[r][c].setFont(new Font(null,1,40));
				display[r][c].setFocusPainted(false);
				display[r][c].addActionListener(new MoveListener(r,c));
				boardpanel.add(display[r][c]);
				board[r][c] = ' ';
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
				if (makeMove(r,c)) {
					moveCount++;
					winCheck(turn.symbol, moveCount, currentRow, c);
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
				display[i][col].setBackground(turn.color);
				currentRow=i;
				return true;
			}
			i--;
		}
		return false;

	}

	//checks for win or tie
	public void winCheck(char player, int moveCount, int row, int col) {
		int count = 0;

		if (moveCount >= 42 && !gameOver) {
			playerTurn.setText("Out of moves, no one wins!");
			gameOver = true;
		}

		if (moveCount >= 7 && !gameOver) {
			//horizontal check
			for (int i=0;i<numCols;i++) {
				if (board[row][i] == player)
					count++;
				else 
					count = 0;
				if (count >= 4)
					gameOver = true;
			}
			count = 0;

			//vertical check
			for (int i=0;i<numRows;i++) {
				if (board[i][col] == player)
					count++;
				else
					count = 0;
				if (count >= 4)
					gameOver = true;
			}
			count = 0;
		}
	}

	public static void main(String[] args) {
		new Connect4(6);

	}

}