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
				display[i][col].setText(""+turn.symbol);
				currentRow=i;
				return true;
			}
			i--;
		}
		return false;

	}

	//checks for win or tie ***NEEDS FINE TUNING OF DIAGONAL CHECKS***
	public void winCheck(char player, int moveCount, int row, int col) {
		int count = 0;

		if (moveCount >= 42 && !gameOver) {
			playerTurn.setText("Out of moves, no one wins!");
			gameOver = true;
			JOptionPane tieMessage = new JOptionPane();
			JOptionPane.showMessageDialog(tieMessage, "No winner, it's a tie!");
		}

		if (moveCount >= 7 && !gameOver) {
			//horizontal check
			for (int c=0;c<numCols;c++) {
				if (board[row][c] == player)
					count++;
				else 
					count = 0;
				if (count >= 4) {
					gameOver = true;
					winnerMessage();
				}
			}
			count = 0;

			//vertical check
			for (int r=0;r<numRows;r++) {
				if (board[r][col] == player)
					count++;
				else
					count = 0;
				if (count >= 4) {
					gameOver = true;
					winnerMessage();
				}
			}
			count = 0;

			//down diagonal check of rows
			for (int rowStart = 0; rowStart < numRows - 3; rowStart++) {
				//only goes to numRows-3 because there's not 4 in diagonal direction below that
				int r,c;
				for (r = rowStart, c = 0; r < numRows && c < numCols; r++, c++ ) {
					if (board[r][c] == player)
						count++;
					else
						count = 0;
					if (count >= 4) {
						gameOver = true;
						winnerMessage();
					}
				}
			}
			count = 0;
			
			//down diagonal check of columns
			for (int colStart = 1; colStart < numRows - 3; colStart++) {
				//starts at 1 because 0 was already checked in rows loop
				int r,c;
				for (r = 0, c = colStart; r < numRows && c < numCols; r++, c++ ) {
					if (board[r][c] == player)
						count++;
					else
						count = 0;
					if (count >= 4) {
						gameOver = true;
						winnerMessage();
					}
				}
			}
			count = 0;
			
			//up diagonal check of rows
			for (int rowStart = numRows-1; rowStart > numRows - 3; rowStart--) {
				int r,c;
				for (r = rowStart, c = 0; r > 0 && c < numCols; r--, c++) {
					if (board[r][c] == player)
						count++;
					else
						count = 0;
					if (count >= 4) {
						gameOver = true;
						winnerMessage();
					}
				}
			}
			count = 0;
			
			//up diagonal check of columns
			for (int colStart = 1; colStart < numCols - 3; colStart++) {
				int r,c;
				for (r = numRows - 1, c = colStart; r > 0 && c < numCols; r--, c++) {
					if (board[r][c] == player)
						count++;
					else
						count = 0;
					if (count >= 4) {
						gameOver = true;
						winnerMessage();
					}
				}
			}
			count = 0;
		}
	}

	public void winnerMessage() {
		JOptionPane window = new JOptionPane();
		JOptionPane.showMessageDialog(window, turn.name + "(" + turn.symbol + ")" + " wins!");
	}

	public static void main(String[] args) {
		new Connect4(6);
	}

}