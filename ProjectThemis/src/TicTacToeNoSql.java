//This is purely for reference
//Of no use - to be removed before final version.

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.*;
/* Author: Alex Good
 * Date: 3/20/15
 * HW: Tic Tac Toe
 */
 
 
public class TicTacToeNoSql extends JFrame {
 
 
    char[][] board;
    JButton[][] display;
    JPanel boardpanel;
    JLabel playerTurn = new JLabel("It is X's Turn!");
    JButton NewGame = new JButton("New Game");
    int moveCount = 0;
    boolean gameOver = false;
    boolean xTurn = true;
    boolean oTurn = false;
    boolean isX = false;
    boolean isY = false;
 
    public TicTacToeNoSql(int n) {
        int i,j;
 
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
                if (xTurn && board[r][c] == ' ' && isX) {
                    board[r][c] = 'X';
                    display[r][c].setText("X");
                    moveCount++;
                    playerTurn.setText("It is O's Turn!");
                }
                else if (oTurn && board[r][c] == ' ' && isY) {
                    board[r][c] = 'O';
                    display[r][c].setText("O");
                    moveCount++;
                    playerTurn.setText("It is X's Turn!");
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
        new TicTacToeNoSql(3);
 
    }

}