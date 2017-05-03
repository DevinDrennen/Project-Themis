package client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
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
	JButton NewGame = new JButton("New Game");
	int moveCount = 0; //may not need to give this a value here
	boolean gameOver = false; //same deal
	int numRows;
	int numCols;
	int currentRow;
	Turn turn;
	String opponentName;

	int gameID;
	int playerID; //The specific ID of the player.
	boolean playerIsRed; //Why both?
	boolean playerIsBlack; //In case we decide to add a spectating feature,or they manage to view someone else's game.

	static ImageIcon whiteC = new ImageIcon("white_circle.png");

	static Image img = whiteC.getImage() ;  
	static Image newimg = img.getScaledInstance(65,65,Image.SCALE_SMOOTH);
	static ImageIcon whiteCircle = new ImageIcon( newimg );

	BufferedReader is;
	PrintWriter os;

	public Connect4Client(int n, BufferedReader inputStream, PrintWriter outputStream) {
		int r,c;

		is = inputStream;
		os = outputStream;
		playerID = ProjectThemisClient.playerID;

		numRows = n;
		numCols = n+1;
		initBoard();
	}

	private class MoveListener implements ActionListener {
		int r,c;
		public MoveListener(int row,int col) {
			r = row;
			c = col;
		}
		public void actionPerformed(ActionEvent e) {

			if (!gameOver) {
				if(isPlayerTurn()){
					if (findLowestOpen(c) != -1) {
						sendMove(makeMove(c),c);
						moveCount++;
						turn.flip();  
						playerTurn.setText("It is " +turn.name+ "'s Turn!");
						winCheck(turn.symbol);
						//note that we're sending playerNo to the output stream
						//even though our makeMove method doesn't take it as a parameter
					}
				}
			}
		}
	}

	private class NewGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			os.println("CONNECT4 ENDGAME");
			os.flush();
			os.println("CONNECT4 NEWGAME");
			os.flush();
			
			resetBoard();

			


			//started setting this up for opponent name display
			//before realizing I don't know what I'm doing.

			//opponen = null;
			//requestOpName();
			//os.println("CONNECT4 GETOPPONENT");
			//os.flush();
			//displayTurn();
		}
	}

	private void initBoard(){
		
		turn = new Turn();
		//turn.red();
		setTitle("Connect Four");
		setSize(500,500);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		board = new char[numRows][numCols];
		display = new JButton[numRows][numCols];
		boardpanel = new JPanel();
		boardpanel.setLayout(new GridLayout(numRows,numCols));
		int r, c;
		for (r=0;r<numRows;r++)
			for (c=0;c<numCols;c++) {
				display[r][c] = new JButton();
				display[r][c].setFont(new Font(null,1,40));
				display[r][c].addActionListener(new MoveListener(r,c));
				initButton(r,c);
				boardpanel.add(display[r][c]);
			}
		moveCount = 0;
		gameOver = false;
		turn.red();
		add(boardpanel,BorderLayout.CENTER);
		add(NewGame,BorderLayout.SOUTH);
		add(playerTurn,BorderLayout.NORTH);
		playerTurn.setHorizontalAlignment(JLabel.CENTER);
		playerTurn.setFont(new Font(null,1,24));
		NewGame.setFont(new Font(null,1,20));
		NewGame.addActionListener(new NewGameListener());
		setLocationRelativeTo(null);
		setVisible(true);		
	}
	
	private void resetBoard(){
		for (int r=0;r<numRows;r++)
			for (int c=0;c<numCols;c++) {
				initButton(r,c);
			}
		moveCount = 0;
		gameOver = false;
		turn.red();
	}
	
	private void initButton(int r, int c){
		display[r][c].setFocusPainted(false);
		display[r][c].setBorder(null);
		display[r][c].setBackground(Color.yellow);
		display[r][c].setIcon(whiteCircle);
		board[r][c] = ' ';
	}


	/**
	 * Checks if it's currently the player's turn
	 * @return If it's the player's turn, return true.
	 */
	private boolean isPlayerTurn(){
		if(playerIsRed && turn.isRed)
			return true;
		if(playerIsBlack && !(turn.isRed))
			return true;
		
		System.out.println("Player can't go!");
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
		if(!gameOver){
			if(board[row][col] != ' ')
				return false;
			if(makeMove(col) != -1){
				turn.flip();
				playerTurn.setText("It is " +turn.name+ "'s Turn!");
				moveCount++;
				winCheck(turn.symbol);
				return true;
			}
		}
		return false;
	}

	public boolean winCheck(char player){	

		char[][] winMarker = new char[numRows][numCols]; //Stores winning moves - we want to mark them later!

		//First, the moves exhausted case
		if (moveCount >= 42 && !gameOver) {
			gameOver = true;
			JOptionPane tieMessage = new JOptionPane();
			JOptionPane.showMessageDialog(tieMessage, "No winner, it's a tie!");
			return true;
		}

		//Now we look for potential wins. You can't win until 7 moves have been made.
		if (moveCount >= 7 && !gameOver) {

			//Start horizontal check
			for(int r = 0; r < numRows; r++){
				for(int c = 0; c < numCols; c++){
					if(checkU(player, r, c, 1) > 4){
						for(int i = r; i > r-4; --i){
							winMarker[i][c] = board[i][c];
						}
						winnerMessage(winMarker);
						gameOver = true;
						return true;
					}

					if(checkD(player, r, c, 1) > 4){
						for(int i = r; i < r+4; ++i){
							winMarker[i][c] = board[i][c];
						}
						winnerMessage(winMarker);
						gameOver = true;
						return true;
					}

					if(checkR(player, r, c, 1) > 4){
						for(int i = c; i < c+4; ++i){
							winMarker[r][i] = board[r][i];
						}
						winnerMessage(winMarker);
						gameOver = true;
						return true;
					}

					if(checkL(player, r, c, 1) > 4){
						for(int i = r; i > c-4; --i){
							winMarker[r][i] = board[r][i];
						}
						winnerMessage(winMarker);
						gameOver = true;
						return true;
					}

					if(checkUR(player, r, c, 1) > 4){
						for(int i = 0; i < 4; i++){
							winMarker[r-i][c+1] = board[r-1][c+1];
						}
						winnerMessage(winMarker);
						gameOver = true;
						return true;
					}

					if(checkDR(player, r, c, 1) > 4){
						for(int i = 0; i < 4; i++){
							winMarker[r+i][c+1] = board[r+1][c+1];
						}
						winnerMessage(winMarker);
						gameOver = true;
						return true;
					}

					if(checkUL(player, r, c, 1) > 4){
						for(int i = 0; i < 4; i++){
							winMarker[r-i][c-1] = board[r-1][c-1];
						}
						winnerMessage(winMarker);
						gameOver = true;
						return true;
					}

					if(checkDL(player, r, c, 1) > 4){
						for(int i = 0; i < 4; i++){
							winMarker[r-i][c-1] = board[r-1][c-1];
						}
						winnerMessage(winMarker);
						gameOver = true;
						return true;
					}
				}	
			}			
		}	
		return false;
	}

	//Checks if you won to the vertical up
	private int checkU(char player, int r, int c, int count){
		if(count == 0)
			return 0;
		if(player == board[r][c]){
			if(count > 4)
				return count;
			if(r-1 > 0)
				return checkU(player, r-1, c, ++count);
		}
		return 0;
	}

	//Checks if you won to the vertical down
	private int checkD(char player, int r, int c, int count){
		if(count == 0)
			return 0;
		if(player == board[r][c]){
			if(count > 4)
				return count;
			if(r+1 < numRows)
				return checkD(player, r+1, c, ++count);
		}
		return 0;
	}

	//Checks if you won to the right
	private int checkR(char player, int r, int c, int count){
		if(count == 0)
			return 0;
		if(player == board[r][c]){
			if(count > 4)
				return count;
			if(c+1 < numCols)
				return checkR(player, r, c+1, ++count);
		}
		return 0;
	}

	//Checks if you won to the left
	private int checkL(char player, int r, int c, int count){
		if(count == 0)
			return 0;
		if(player == board[r][c]){
			if(count > 4)
				return count;
			if(c-1 > 0)
				return checkL(player, r, c-1, ++count);
		}
		return 0;
	}

	//Checks if you won to the up and left
	private int checkUL(char player, int r, int c, int count){
		if(count == 0)
			return 0;
		if(player == board[r][c]){
			if(count > 4)
				return count;
			if(c-1 > 0 && r-1 > 0)
				return checkUL(player, r-1, c-1, ++count);
		}
		return 0;
	}

	//Checks if you won to the down and left
	private int checkDL(char player, int r, int c, int count){
		if(count == 0)
			return 0;
		if(player == board[r][c]){
			if(count > 4)
				return count;
			if(c-1 > 0 && r+1 < numRows)
				return checkDL(player, r+1, c-1, ++count);
		}
		return 0;
	}

	//Checks if you won to the down and right
	private int checkDR(char player, int r, int c, int count){
		if(count == 0)
			return 0;
		if(player == board[r][c]){
			if(count > 4)
				return count;
			if(c+1 < numCols && r+1 < numRows)
				return checkDR(player, r+1, c+1, ++count);
		}
		return 0;
	}

	//Checks if you won to the up and right
	private int checkUR(char player, int r, int c, int count){
		if(count == 0)
			return 0;
		if(player == board[r][c]){
			if(count > 4)
				return count;
			if(c+1 < numCols && r-1 > 0)
				return checkUR(player, r-1, c+1, ++count);
		}
		return 0;
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
					display[r][c].setBorderPainted(true);
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
			return true;
		}

		playerIsRed = false;
		playerIsBlack = false;

		return false;
	}


	public void processInput(String[] inputs){
		System.out.println(inputs[1]);
		switch (inputs[1]){
		case "ENDGAME":
			gameOver = true;
			break;
		case "NEWGAME":
			gameOver = false;
			break;
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
		case "GETOPPONENT":
			// does not do anything right now
			opponentName = inputs[2];
			//displayTurn();
			break;
		}
	}
}
