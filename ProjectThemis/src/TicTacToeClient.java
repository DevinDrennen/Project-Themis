import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
 
import javax.swing.*;
/* Author: Alex Good
 * Date: 3/20/15
 * HW: Tic Tac Toe
 */
 
 
public class TicTacToeClient extends JFrame {
 
 
    char[][] board;
    JButton[][] display;
    JPanel boardpanel;
    JLabel playerTurn = new JLabel("It is X's Turn!");
    JButton NewGame = new JButton("New Game");
    int moveCount = 0;
    boolean gameOver = false;
    boolean xTurn = true;
    boolean oTurn = false;
    int gameID;
    
    BufferedReader is;
    PrintWriter os;
 
    public TicTacToeClient(int n, BufferedReader inputStream, PrintWriter outputStream) {
        int i,j;
 
        is = inputStream;
        os = outputStream;
        
        
        setTitle("Tic Tac Toe");
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        board = new char[n][n];
        display = new JButton[n][n];
        boardpanel = new JPanel();
        boardpanel.setLayout(new GridLayout(n,n));
        for (i=0;i<n;i++)
            for (j=0;j<n;j++) {
                display[i][j] = new JButton();
                display[i][j].setFont(new Font(null,1,125));
                display[i][j].setFocusPainted(false);
                display[i][j].addActionListener(new MoveListener(i,j));
                boardpanel.add(display[i][j]);
                board[i][j] = ' ';
            }
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
 
 
 
    private class MoveListener implements ActionListener {
        int r,c;
        public MoveListener(int row,int col) {
            r = row;
            c = col;
        }
        public void actionPerformed(ActionEvent e) {
            if (!gameOver) {
                if (xTurn && board[r][c] == ' ' && (moveCount % 2 == 0)) {
                    board[r][c] = 'X';
                    display[r][c].setText("X");
                    moveCount++;
                    playerTurn.setText("It is O's Turn!");
                    os.println("TICTACTOE MOVE " + r + " " + c + " " + 1);
                }
                else if (oTurn && board[r][c] == ' ' && (moveCount % 2 == 1)) {
                    board[r][c] = 'O';
                    display[r][c].setText("O");
                    moveCount++;
                    playerTurn.setText("It is X's Turn!");
                    os.println("TICTACTOE MOVE " + r + " " + c + " " + 2);
                }
 
                xTurn = !xTurn;
                winCheck('X',moveCount);
                oTurn = !oTurn;
                winCheck('O',moveCount);    
            }
        }
    }
 
    //starts a new game
     
    private class NewGameListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int r,c;
            for (r=0;r<board.length;r++)
                for (c=0;c<board.length;c++) {
                    board[r][c] = ' ';
                    display[r][c].setText(" ");
                    playerTurn.setText("It is X's Turn!");
                    moveCount = 0;
                    xTurn = true;
                    oTurn = false;
                    gameOver = false;
                }
        }
    }
 
    //checks for win or tie
     
    private void winCheck(char h, int count) {
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
    
    private void markX(int r, int c){
    	if(board[r][c] != 'X'){
    		board[r][c] = 'X';
        	display[r][c].setText("X");
        	moveCount++;
    	}
    }
    
    private void markY(int r, int c){
    	if(board[r][c] != 'Y'){
    		board[r][c] = 'Y';
    		display[r][c].setText("Y");
    		moveCount++;
    	}
    }
    
    private boolean markMove(int r, int c, int data){
    	if(data == 1)
    		markX(r, c);
    	else if (data == 2)
    		markY(r, c);
    	else 
    		return false; // For future error checking.
   
    	// After placing a move or breaking out if there were no valid moves, check how many moves have been made.
    	// An odd number of moves means it's O's turn, even number means it's X's.
    	if(moveCount % 2 == 0)
    		playerTurn.setText("It is X's Turn!");
    	else
    		playerTurn.setText("It is O's Turn!");
    
    	// For future error checking, return true if this method ran successfully.
    	return true;
    	
    }
    
    public void processInput(String[] inputs){
    	System.out.println("Processing inputs!");
    	switch (inputs[1]){
    	case "MOVE":
    		markMove(Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]), Integer.parseInt(inputs[4]));
    		System.out.println("Move recieved!");
    	}
    }
    
}