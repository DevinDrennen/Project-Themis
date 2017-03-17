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
	int blackScore = 2;
	int whiteScore = 2;

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
		display[3][3].setText("B");
		display[4][4].setText("B");
		display[4][3].setText("W");
		display[3][4].setText("W");
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
		boolean bTurn = false;
		boolean wTurn = true;
		public MoveListener(int row,int col) {
			r = row;
			c = col;
		}
		public void actionPerformed(ActionEvent e) {
			if (!gameOver) {
				if (bTurn && board[r][c] == ' ' && checkArea(r,c,'B')) {
					board[r][c] = 'B';
					display[r][c].setText("B");
					flipTilesB(r,c);
					moveCount++;
					playerTurn.setText("It is W's Turn!");
				}
				else if (wTurn && board[r][c] == ' ') {
					board[r][c] = 'W';
					display[r][c].setText("W");
					flipTilesW(r,c);
					moveCount++;
					playerTurn.setText("It is B's Turn!");
				}

				bTurn = !bTurn;
				wTurn = !wTurn;
				if(moveCount == 60)
					winCheck(whiteScore, blackScore);
			}
		}
		
		public boolean checkArea(int r, int c, char player)
		{
			boolean validMove = false;
			for(int i = r-1; i < r+1; i++)
				for(int j = c-1; j < c+1; j++)
					if(board[i][j] == 'W' || board[i][j] == 'B' && board[i][j] != player)
						validMove = true;
			return validMove;
			
		}

		public void flipTilesW(int r, int c)
		{
			int i, j;

			i = r+1;
			j = c+1;
			for(; j < 8 && board[r][j] != 'W'; j++)
			{
				if(board[r][j] == 'B')
					blackScore--;
				board[r][j] = 'W';
				whiteScore++;
			}

			i = r+1;
			j = c+1;
			for(; i < 8 && board[i][c] != 'W'; i++)
			{
				if(board[i][c] == 'B')
					blackScore--;
				board[i][c] = 'W';
				whiteScore++;
			}

			i = r+1;
			j = c+1;
			for(; i < 8 && board[i][j] != 'W'; i++, j++)
			{
				if(board[i][j] == 'B')
					blackScore--;
				board[i][j] = 'W';
				whiteScore++;
			}

		}

		public void flipTilesB(int r, int c)
		{
			int i, j;

			i = r+1;
			j = c+1;
			for(; j < 8 && board[r][j] != 'B'; j++)
			{
				if(board[r][j] == 'w')
					whiteScore--;
				board[r][j] = 'B';
				blackScore++;
			}

			i = r+1;
			j = c+1;
			for(; i < 8 && board[i][c] != 'B'; i++)
			{
				if(board[i][c] == 'w')
					whiteScore--;
				board[i][c] = 'B';
				blackScore++;
			}

			i = r+1;
			j = c+1;
			for(; i < 8 && board[i][j] != 'B'; i++, j++)
			{
				if(board[i][j] == 'w')
					whiteScore--;
				board[i][j] = 'B';
				blackScore++;
			}

		}

		public void winCheck(int whiteScore, int blackScore)
		{
			gameOver = true;
			if(whiteScore > blackScore)
				System.out.println("White wins!");
			else if(blackScore > whiteScore)
				System.out.println("Black wins!");
			else
				System.out.println("Tie!");
		}

	}
	public static void main(String[] args) {
		new Othello(8, "Othello");
	}

}


