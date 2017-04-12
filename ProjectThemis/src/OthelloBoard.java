import java.awt.*;
import javax.swing.*;

public class OthelloBoard extends JPanel {

    private final int ROWS = 8, COLS = 8;

    int [] data = new int[64];

    // GUI elements
    JLabel darkScoreLbl, lightScoreLbl, currentPlayerLbl;
    JButton btnPass;
    JPanel boardPanel;

    // Game board
    Move[][] board = new Move[ROWS][COLS];

    // current player
    private Move move_piece;

    boolean validMoves; //keeps track of where the player can move

    int darkScore = 2, lightScore = 2, counter = 0; //set initial score

    public OthelloBoard() {
        super(new BorderLayout());
        setBorder(new TitledBorder("Game Area"));
        setOpaque(true);    // content pane must be opaque

        JPanel topPanel = new JPanel(new FlowLayout());

        JButton startNewGame = new JButton("Start New Game");
        startNewGame.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });

        currentPlayerLbl = new JLabel("Current Player : DARK", JLabel.RIGHT);
        currentPlayerLbl.setFont(new Font("Lucida Calligraphy", Font.BOLD, 18));

        topPanel.add(startNewGame);
        topPanel.add(currentPlayerLbl);

        add(topPanel, BorderLayout.NORTH);
        // Board Panel
        boardPanel = new JPanel(new GridLayout(8,8));
        for(int row = 0; row < 8; row++) {
            for(int col=0; col < 8; col++) {
                JPanel cell = new JPanel(new BorderLayout());
                cell.setSize(70, 70);
                cell.setBackground(new Color(188, 222, 255));
                cell.setBorder(BorderFactory.createLineBorder(Color.gray));
                boardPanel.add(cell);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // bottom score panel
        JPanel scorePanel = new JPanel(new FlowLayout());
        
        // Creating score Labels
        darkScoreLbl = new JLabel("Dark : " + darkScore);
        lightScoreLbl = new JLabel("Light: " + lightScore);
        
        // Pass Button
        btnPass = new JButton("Pass Turn");
        btnPass.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                swapTurns(move_piece);
            }    
        });
        
        scorePanel.add(darkScoreLbl);
        scorePanel.add(btnPass);
        scorePanel.add(lightScoreLbl);

        add(scorePanel, BorderLayout.SOUTH);

        newGame();
    }

    public void newGame() {
        for(int row = 0; row < ROWS; row++) { 
        	for(int col = 0; col < COLS; col++) { 
        		board[row][col] = Move.EMPTY;
        		}
        	}
        move_piece = Move.DARK; // starting from a black player        
        placePiece(3,3, Move.LIGHT); updateGUI();
        placePiece(3,4, Move.DARK); updateGUI();
        placePiece(4,4, Move.LIGHT); updateGUI();
        placePiece(4,3, Move.DARK); updateGUI();
           
        boardPanel.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                int row = e.getX() / 74;
                int col = e.getY() / 74;
                if(flipPiece(row, col, move_piece, false)) {
                    flipPiece(row, col, move_piece, true);
                    placePiece(row, col, move_piece);
                    updateGUI();
                    swapTurns(move_piece);
                }
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
    }//resets board to initial state

    public void placePiece(int row, int col, Move color) {]
        if(board[row][col] == Move.EMPTY)
            board[row][col] = color;
    }

    public void updateGUI() {
        darkScore = 0; lightScore = 0;
        for(int row = 0; row < 8; row ++) { 
        	for(int col = 0; col < 8; col++) { 
        		JPanel panel = (JPanel)boardPanel.getComponent(coordToindex(row, col));
				panel.removeAll();
			}
		}
        for(int row = 0; row < 8; row ++) {
            for(int col = 0; col < 8; col++) {//goes through the board and updates adds the correct picture based on the state of that board cell
                if(flipPiece(row, col, move_piece, false))
                    addRespectivePics(row, col, "transparent");
                
                if(board[row][col] == Move.DARK) {
                    addRespectivePics(row, col, Move.DARK.toString().toLowerCase());
                    darkScore++;
                    darkScoreLbl.setText("Dark : " + darkScore);
                }
                 if(board[row][col] == Move.LIGHT) {
                    addRespectivePics(row, col, Move.LIGHT.toString().toLowerCase());
                    lightScore++;
                    lightScoreLbl.setText("Light : " + lightScore);
                }          
                checkWin(darkScore, lightScore);
            }
        }
    }

    public boolean flipPiece(int row, int col, Move piece, boolean putDown) {
        boolean isValid = false;
        for(int checkX = -1; checkX < 2; checkX++) 
		{
            for(int checkY = -1; checkY < 2; checkY ++) {
                if(checkX == 0 && checkY == 0) { 
					continue; 
			} //skip checking self
             int checkRow = row + checkX;
             int checkCol = col + checkY;
                if(checkRow >= 0 && checkCol >= 0 && checkRow < 8 && checkCol < 8) {
                    if(board[checkRow][checkCol] == (piece == Move.DARK ? Move.LIGHT : Move.DARK)) { //checks if the current player matches the piece being looked at
                        for(int i = 0; i < 8; i++) // keep track of the length to be flipped
						{   
                            int checkRowSub = row + i * checkX;
                            int checkColSub = col + i * checkY;
                            if(checkRowSub < 0 || checkColSub < 0  || checkRowSub > 7 || checkColSub > 7) continue; //skips checking self or off the edge of the board
                            if(board[checkRowSub][checkColSub] == piece) {
                                if(putDown) {
                                    for(int j = 1; j < i; j ++) 
									{
                                        int flipRow = row+j*checkX;
                                        int flipCol = col+j*checkY;
                                        board[flipRow][flipCol] = piece;
                                    }
                                }
                                isValid = true; 
                                break;
                            }
                        }
                    }
                }
             }     
          }
      return isValid;
    }

    public void addRespectivePics(int row, int col, String colorName) 
	{
        ImageIcon picture = createImageIcon("images/" + colorName + ".png");
        JLabel picLbl = new JLabel(picture);
        JPanel panel = (JPanel)boardPanel.getComponent(coordToindex(row, col));
        panel.removeAll();
        panel.add(picLbl);
        boardPanel.updateUI();
    } 

    private void swapTurns(Move piece) 
	{
        move_piece = (move_piece == Move.DARK ? Move.LIGHT : Move.DARK);
        updateGUI();
        currentPlayerLbl.setText("Current Player :" + move_piece.toString());
    }

    private int coordToindex(int row, int col) {     // convert 2D array to 1D array index
        return (col * 8) + row;
    }

    private void checkWin(int darkScore, int lightScore) {     // counter the scores and return the winner
        if(darkScore + lightScore == 64 && darkScore > lightScore)
            GUIConsole.display("Black Player Wins!");
        if(darkScore + lightScore == 64 && lightScore > darkScore)
            GUIConsole.display("White Player Wins!");
        if(darkScore + lightScore == 64 && lightScore == darkScore)
            GUIConsole.display("It's a Tie!");
    }

    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if(imgURL != null) 
        	return new ImageIcon(imgURL);
        else {
            System.err.println("Couldn't find the file: " + path);
            return null;
        }
    }
}