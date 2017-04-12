import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Turn {
	String name;
	boolean isRed;
	char symbol;
	Color color;
	ImageIcon icon;
	static int playerNo;
	
	boolean redTurn=true;
	boolean blackTurn=false;
	
	ImageIcon redC = new ImageIcon("red_circle.png");
	ImageIcon blackC = new ImageIcon("black_circle.png");
	

	Image img = redC.getImage() ;  
	Image newimg = img.getScaledInstance(80,80,Image.SCALE_SMOOTH);
	ImageIcon redCircle = new ImageIcon( newimg );
	
	Image img2 = blackC.getImage() ;  
	Image newimg2 = img2.getScaledInstance(80,80,Image.SCALE_SMOOTH);
	ImageIcon blackCircle = new ImageIcon( newimg2 );
	
	void red() {
		name = "Red";
		isRed = true;
		symbol = 'X';
		color = Color.red;
		icon = redCircle;
		playerNo = 1;
	}
	
	void black() {
		name = "Black";
		isRed = false;
		symbol = 'O';
		color = Color.black;
		icon = blackCircle;
		playerNo = 2;
	}
	
	void flip() {
		if (isRed)
			black();
		else
			red();
	}
}