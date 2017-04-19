
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
	char[][] board;
	JButton[][] display;
	JPanel boardpanel;
	JLabel playerTurn = new JLabel("It is Red's Turn!");
	int moveCount = 0;
	boolean gameOver = false;
	int numRows;
	int numCols;
	int currentRow;
	Turn turn;
	String opponentName;

    int gameID;
    int playerID; //The specific ID of the player.
    boolean playerIsRed; //Why both?
    boolean playerIsBlack; //In case we decide to add a spectating feature,or they manage to view someone else's game.
    
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
				System.out.println("Game isn't over");
				if(isPlayerTurn()){
					System.out.println("And it's your turn...");
					if (findLowestOpen(c) != -1) {
						System.out.println("You stupid fam");
						sendMove(makeMove(c),c);
						moveCount++;
						winCheck(turn.symbol, moveCount, findLowestOpen(c)+1, c);
						turn.flip();  
						playerTurn.setText("It is " +turn.name+ "'s Turn!");
						
						//note that we're sending playerNo to the output stream
						//even though our makeMove method doesn't take it as a parameter
					}
				}
			}
		}
	}

	/**
	 * Checks if it's currently the player's turn
	 * @return If it's the player's turn, return true.
	 */
	private boolean isPlayerTurn(){
		if(playerIsRed && turn.isRed)
			return true;
		if(playerIsBlack && !turn.isRed)
			return true;
		return false;
	}

	
	/**
	 * Animates the move/makes it appear on the display
	 * 
	 * @param col The column the piece is being dropped into.
	 * @return If it's a valid location to drop a piece
	 */
	private int makeMove(int col) {
		int finalRow = findLowestOpen(col);
		System.out.println("Final row is: " + finalRow);
		if(finalRow == -1)
			return -1;
		
		board[finalRow][col] = turn.symbol;
		display[finalRow][col].setIcon(turn.icon);
		
		/*
		for(int i = 5; i >= finalRow; i--){
			if (board[i][col]==' '){
				board[i][col]=turn.symbol;
				display[i][col].setIcon(turn.icon);
			}
		}
		*/
		return finalRow;
	}
	
	/**
	 * Sends a move to the ProjectThemisServer.
	 * 
	 * @param row The row to send to the server
	 * @param col The column to send to the server
	 * @return If it successfully was sent.
	 */
	private boolean sendMove(int row, int col) {
		os.println("CONNECT4 MOVE " + row + " " + col + " " + Turn.playerNo);
		os.flush();
		return true;
	}
	
	
	/**
	 * This method will return the lowest open slot in a given column.
	 * 
	 * @param col The column you want to find the lowest available slot.
	 * @return The row value. 5 means empty column, -1  means full column.
	 */
	private int findLowestOpen(int col) {
		for(int i = 0; i < 6; i++){
			if(board[i][col] != ' '){
				return --i; //A return value of 6 means the col is full.
			}
		}
		return 5;
	}
	
	/**
	 * When a move is received from the server, use this method to make sure the move is valid.
	 * 
	 * @param row The row of the piece we just got
	 * @param col The column of the piece we just got
	 * @return True if the move was new and valid.
	 */
	private boolean gotMove(int row, int col) {
		if(board[row][col] != ' ')
			return false;
		if(makeMove(col) != -1){
			turn.flip();
			playerTurn.setText("It is " +turn.name+ "'s Turn!");
			return true;
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
	
	private boolean setPlayer(int player){
		if(player == 1){
			playerIsRed = true;
			playerIsBlack = false;
			return true;
		}
		if(player == 2){
			playerIsRed = false;
			playerIsBlack = true;
		}
		
		playerIsRed = false;
		playerIsBlack = false;
		
		return false;
	}
	
	
	public void processInput(String[] inputs){
	    switch (inputs[1]){
	    case "MOVE":
	    	gotMove(Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]));
	    	//we do not have inputs[4] because we keep track of which player's turn it is within the game
	    	//if there are issues here, this is why.
	    	System.out.println("Move recieved!");
	    	break;
	    case "PLAYER":
	    	setPlayer(Integer.parseInt(inputs[2]));
	    	System.out.println("Player is " + inputs[2]);
	    	break;
	    }
	}
}