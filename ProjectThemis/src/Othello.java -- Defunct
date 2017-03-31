package ProjectThemisClient;
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
		display[3][3].setBackground(Color.BLACK);
		display[4][4].setBackground(Color.BLACK);
		display[4][3].setBackground(Color.WHITE);
		display[3][4].setBackground(Color.WHITE);
		board[3][3] = 'B';
		board[4][4] = 'B';
		board[4][3] = 'W';
		board[3][4] = 'W';
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
		boolean[][] array = new boolean[8][8];
		public MoveListener(int row,int col) {
			r = row;
			c = col;
		}
		public void actionPerformed(ActionEvent e) {
			if (!gameOver) {
				if (bTurn && board[r][c] != 'B' && board[r][c] != 'W' && checkArea(r,c,'B')) {
					board[r][c] = 'B';
					display[r][c].setBackground(Color.BLACK);
					flipTilesB();
					moveCount++;
					playerTurn.setText("It is W's Turn!");
					bTurn = !bTurn;
					wTurn = !wTurn;
				}
				else if (wTurn && board[r][c] != 'B' && board[r][c] != 'W' && checkArea(r,c,'W')) {
					board[r][c] = 'W';
					display[r][c].setBackground(Color.WHITE);
					flipTilesW();
					moveCount++;
					playerTurn.setText("It is B's Turn!");
					bTurn = !bTurn;
					wTurn = !wTurn;
				}
				
				for(int i = 0; i < 8; ++i)
					for(int j = 0; j < 8; ++j)
						array[i][j] = false;

				if(moveCount == 60)
					winCheck(whiteScore, blackScore);
			}
		}

		private void flipTilesB() {
			for(int i = 0; i < 8; ++i)
				for(int j = 0; j < 8; ++j)
					if(array[i][j])
					{
						board[i][j] = 'B';
						display[i][j].setBackground(Color.BLACK);
					}
		}
		
		private void flipTilesW() {
			for(int i = 0; i < 8; ++i)
				for(int j = 0; j < 8; ++j)
					if(array[i][j])
					{
						board[i][j] = 'W';
						display[i][j].setBackground(Color.WHITE);
					}
		}
		
		public boolean checkArea(int r, int c, char player)
		{
			boolean validMove = false;
			if(player == 'B')
				for(int i = r-1; i <= r+1; i++)
					for(int j = c-1; j <= c+1; j++)
						if(board[i][j] == 'W')
						{
							validMove = checkLineB(r,c,i,j,player);
						}
			if(player == 'W')
				for(int i = r-1; i <= r+1; i++)
					for(int j = c-1; j <= c+1; j++)
						if(board[i][j] == 'B')
						{
							validMove = checkLineW(r,c,i,j,player);
						}
			return validMove;
		}

		public boolean checkLineW(int realR, int realC, int r, int c, char player)
		{
			if(realR == r - 1 && realC == c - 1)//check diagonally down right
			{
				if(board[r+1][c+1] == 'B')
				{
					checkLineW(realR + 1, realC + 1, r + 1, c + 1, player);
					array[r+1][c+1] = true;
				}
				else if(board[r+1][c+1] == player)
					return true;
			}
			else if(realR == r - 1 && realC == c)//check directly down
			{
				if(board[r+1][c] == 'B')
				{
					checkLineW(realR + 1, realC, r + 1, c, player);
					array[r-1][c] = true;
				}
				else if(board[r+1][c] == player)
					return true;
			}
			else if(realR == r + 1 && realC == c + 1)//check diagonally down left
			{
				if(board[r-1][c-1] == 'B')
				{
					checkLineW(realR - 1, realC - 1, r - 1, c - 1, player);
					array[r-1][c-1] = true;
				}
				else if(board[r-1][c-1] == player)
					return true;
			}
			else if(realR == r && realC == c + 1)//check left
			{
				if(board[r][c-1] == 'B')
				{
					checkLineW(realR, realC - 1, r, c - 1, player);
					array[r][c-1] = true;
				}
				else if(board[r][c-1] == player)
					return true;
			}
			else if(realR == r + 1 && realC == c + 1)//check diagonally up left
			{
				if(board[r-1][c-1] == 'B')
				{
					checkLineW(realR - 1, realC - 1, r - 1, c - 1, player);
					array[r-1][c-1] = true;
				}
				else if(board[r-1][c-1] == player)
					return true;
			}
			else if(realR == r + 1 && realC == c)//check directly up
			{
				if(board[r-1][c] == 'B')
				{
					checkLineW(realR - 1, realC, r - 1, c, player);
					array[r-1][c] = true;
				}
				else if(board[r-1][c] == player)
					return true;
			}
			else if(realR == r + 1 && realC == c - 1)//check diagonally up right
			{
				if(board[r-1][c+1] == 'B')
				{
					checkLineW(realR - 1, realC + 1, r - 1, c + 1, player);
					array[r-1][c+1] = true;
				}
				else if(board[r-1][c+1] == player)
					return true;
			}
			else if(realR == r && realC == c - 1)//check directly right
			{
				if(board[r][c+1] == 'B')
				{
					checkLineW(realR, realC + 1, r, c + 1, player);
					array[r][c+1] = true;
				}
				else if(board[r][c+1] == player)
					return true;
			}
			for(int i = 0; i < 8; ++i)
				for(int j = 0; j < 8; ++j)
					array[i][j] = false;
			return false;
		}

		public boolean checkLineB(int realR, int realC, int r, int c, char player)
		{
			if(realR == r - 1 && realC == c - 1)//check diagonally down right
			{
				if(board[r+1][c+1] == 'W')
				{
					checkLineB(realR + 1, realC + 1, r + 1, c + 1, player);
					array[r+1][c+1] = true;
				}
				else if(board[r+1][c+1] == player)
					return true;
			}
			else if(realR == r + 1 && realC == c)//check directly down
			{
				if(board[r-1][c] == 'W')
				{
					checkLineB(realR - 1, realC, r - 1, c, player);
					array[r-1][c] = true;
				}
				else if(board[r-1][c] == player)
					return true;
			}
			else if(realR == r + 1 && realC == c + 1)//check diagonally down left
			{
				if(board[r-1][c-1] == 'W')
				{
					checkLineB(realR - 1, realC - 1, r - 1, c - 1, player);
					array[r-1][c-1] = true;
				}
				else if(board[r-1][c-1] == player)
					return true;
			}
			else if(realR == r && realC == c + 1)//check left
			{
				if(board[r][c-1] == 'W')
				{
					checkLineB(realR, realC - 1, r, c - 1, player);
					array[r][c-1] = true;
				}
				else if(board[r][c-1] == player)
					return true;
			}
			else if(realR == r + 1 && realC == c + 1)//check diagonally up left
			{
				if(board[r-1][c-1] == 'W')
				{
					checkLineB(realR - 1, realC - 1, r - 1, c - 1, player);
					array[r-1][c-1] = true;
				}
				else if(board[r-1][c-1] == player)
					return true;
			}
			else if(realR == r + 1 && realC == c)//check directly up
			{
				if(board[r-1][c] == 'W')
				{
					checkLineB(realR - 1, realC, r - 1, c, player);
					array[r-1][c] = true;
				}
				else if(board[r-1][c] == player)
					return true;
			}
			else if(realR == r + 1 && realC == c - 1)//check diagonally up right
			{
				if(board[r-1][c+1] == 'W')
				{
					checkLineB(realR - 1, realC + 1, r - 1, c + 1, player);
					array[r-1][c+1] = true;
				}
				else if(board[r-1][c+1] == player)
					return true;
			}
			else if(realR == r && realC == c - 1)//check directly right
			{
				if(board[r][c+1] == 'W')
				{
					checkLineB(realR, realC + 1, r, c + 1, player);
					array[r][c+1] = true;
				}
				else if(board[r][c+1] == player)
					return true;
			}
			return false;
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
