import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Othello extends JFrame {
	int dimension;
	String name;
    char[][] board;
    JButton[][] display;
    JPanel boardpanel;
    JLabel playerTurn = new JLabel(" ");
    //JButton NewGame = new JButton("New Game");
    int moveCount = 0;
    boolean gameOver = false;
    
	public Othello(int d, String GameName) {
		  int i,j;
		  
	        setTitle(GameName);
	        setSize(500,500);
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
	                boardpanel.add(display[i][j]);
	                board[i][j] = ' ';
	            }
          display[4][4].setText("B");
          display[5][5].setText("B");
          display[5][4].setText("W");
          display[4][5].setText("W");
	        add(boardpanel,BorderLayout.CENTER);
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
            /*if (!gameOver) {
                if (xTurn && board[r][c] == ' ') {
                    board[r][c] = 'X';
                    display[r][c].setText("X");
                    moveCount++;
                    playerTurn.setText("It is O's Turn!");
                }
                else if (oTurn && board[r][c] == ' ') {
                    board[r][c] = 'O';
                    display[r][c].setText("O");
                    moveCount++;
                    playerTurn.setText("It is X's Turn!");
                }
 
                xTurn = !xTurn;
                winCheck('X',moveCount);
                oTurn = !oTurn;
                winCheck('O',moveCount);    
            }*/
        }
			
		}
    public static void main(String[] args) {
    	new Othello(8, "Othello");
    }

}
 

