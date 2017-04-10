
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.*;

public class Connect4Client extends JFrame {
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

    int gameID;
    int playerID;
    
    BufferedReader is;
    PrintWriter os;
	
	public Connect4Client(int n, BufferedReader inputStream, PrintWriter outputStream) {
		int r,c;
		
		is = inputStream;
        os = outputStream;
        playerID = ProjectThemisClient.playerID;
		
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
				display[r][c].setBorder(null);
				display[r][c].addActionListener(new MoveListener(r,c));
				boardpanel.add(display[r][c]);
				board[r][c] = ' ';
			}
		for (c = 0; c < numCols; c++)
			for (r = 0; r < numRows; r++) {
				if (c % 2 == 0)
					display[r][c].setBackground(Color.lightGray);
				else
					display[r][c].setBackground(Color.gray);
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
					os.println("CONNECT4 MOVE " + r + " " + c + " " + turn.playerNo);
					//note that we're sending playerNo to the output stream
					//even though our makeMove method doesn't take it as a parameter
				}
			}
		}
	}

	private static boolean makeMove(int row, int col) {
		int i=5;
		while (i>=0){
			if (board[i][col]==' '){
				board[i][col]=turn.symbol;
				display[i][col].setIcon(turn.icon);
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
		char[][] winMarker = new char[numRows][numCols];

		if (moveCount >= 42 && !gameOver) {
			gameOver = true;
			JOptionPane tieMessage = new JOptionPane();
			JOptionPane.showMessageDialog(tieMessage, "No winner, it's a tie!");
		}

		if (moveCount >= 7 && !gameOver) {
			//horizontal check
			for (int c=0;c<numCols;c++) {
				if (board[row][c] == player) {
					count++;
					winMarker[row][c] = turn.symbol;
				}
				else {
					count = 0;
					clearWinMarker(winMarker);
				}
				if (count >= 4) {
					gameOver = true;
					winnerMessage(winMarker);
				}
			}
			count = 0;

			//vertical check
			for (int r=0;r<numRows;r++) {
				if (board[r][col] == player) {
					count++;
					winMarker[r][col] = turn.symbol;
				}
				else {
					count = 0;
					clearWinMarker(winMarker);
				}
				if (count >= 4) {
					gameOver = true;
					winnerMessage(winMarker);
				}
			}
			count = 0;

			//down diagonal check of rows
			for (int rowStart = 0; rowStart < numRows - 3; rowStart++) {
				//only goes to numRows-3 because there's not 4 in diagonal direction below that
				int r,c;
				count = 0;
				for (r = rowStart, c = 0; r < numRows && c < numCols; r++, c++ ) {
					if (board[r][c] == player) {
						count++;
						winMarker[r][c] = turn.symbol;
					}
					else {
						count = 0;
						clearWinMarker(winMarker);
					}
					if (count >= 4) {
						gameOver = true;
						winnerMessage(winMarker);
					}
				}
			}
			count = 0;

			//down diagonal check of columns
			for (int colStart = 1; colStart < numRows - 3; colStart++) {
				//starts at 1 because 0 was already checked in rows loop
				int r,c;
				count = 0;
				for (r = 0, c = colStart; r < numRows && c < numCols; r++, c++ ) {
					if (board[r][c] == player) {
						count++;
						winMarker[r][c] = turn.symbol;
					}
					else {
						count = 0;
						clearWinMarker(winMarker);
					}
					if (count >= 4) {
						gameOver = true;
						winnerMessage(winMarker);
					}
				}
			}
			count = 0;

			//up diagonal check of rows
			for (int rowStart = numRows-1; rowStart > numRows - 3; rowStart--) {
				int r,c;
				count = 0;
				for (r = rowStart, c = 0; r > 0 && c < numCols; r--, c++) {
					if (board[r][c] == player) {
						count++;
						winMarker[r][c] = turn.symbol;
					}
					else {
						count = 0;
						clearWinMarker(winMarker);
					}
					if (count >= 4) {
						gameOver = true;
						winnerMessage(winMarker);
					}
				}
			}
			count = 0;

			//up diagonal check of columns
			for (int colStart = 1; colStart < numCols - 3; colStart++) {
				int r,c;
				count = 0;
				for (r = numRows - 1, c = colStart; r > 0 && c < numCols; r--, c++) {
					if (board[r][c] == player) {
						count++;
						winMarker[r][c] = turn.symbol;
					}
					else {
						count = 0;
						clearWinMarker(winMarker);
					}
					if (count >= 4) {
						gameOver = true;
						winnerMessage(winMarker);
					}
				}
			}
			count = 0;
		}
	}
	public void clearWinMarker(char[][] winMarker) {
		for (int r = 0; r < numRows; r++)
			for (int c = 0; c < numCols; c++)
				winMarker[r][c] = ' ';
	}

	public void winnerMessage(char[][] winningBoard) {
		for (int r = 0; r < numRows; r++)
			for (int c = 0; c < numCols; c++)
				if (winningBoard[r][c] == turn.symbol)
					display[r][c].setBackground(Color.YELLOW);
		JOptionPane window = new JOptionPane();
		JOptionPane.showMessageDialog(window, turn.name + " wins!");
	}
	
	
	public void processInput(String[] inputs){
	    System.out.println("Processing inputs!");
	    switch (inputs[1]){
	    case "MOVE":
	    	makeMove(Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]));
	    	//we do not have inputs[4] because we keep track of which player's turn it is within the game
	    	//if there are issues here, this is why.
	    	System.out.println("Move recieved!");
	    }
	}
}