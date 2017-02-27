import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Battleship extends Board {
	Boolean placingShips;
	Boolean firing;
	
	
	public Battleship(int d, String GameName) {
		super(d, "Battleship"+GameName);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Battleship opponentBoard = new Battleship(8, "- Opponent's Board");
		Battleship yourBoard = new Battleship(8, "- Your Board");
		yourBoard.playerTurn.setText("Place 1x6 ship");
		Battleship.placingShips();
		
	}
	private class MoveListener implements ActionListener {
		 int r,c;
	        public MoveListener(int row,int col) {
	            r = row;
	            c = col;
	        }
	        public void actionPerformed(ActionEvent e) {
	        		display[r][c].setBackground(Color.red);
	        }
	
	}
	public static void placingShips(){
		Board.updateColor = Color.cyan;
	}

}