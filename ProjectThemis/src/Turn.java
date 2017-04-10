import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Turn {
	String name;
	boolean isRed;
	char symbol;
	Color color;
	ImageIcon icon;
	
	static ImageIcon redC = new ImageIcon("red_circle.png");
	static ImageIcon blackC = new ImageIcon("black_circle.png");
	

	static Image img = redC.getImage() ;  
	static Image newimg = img.getScaledInstance(80,80,Image.SCALE_SMOOTH);
	static ImageIcon redCircle = new ImageIcon( newimg );
	
	static Image img2 = blackC.getImage() ;  
	static Image newimg2 = img2.getScaledInstance(80,80,Image.SCALE_SMOOTH);
	static ImageIcon blackCircle = new ImageIcon( newimg2 );
	
	void red() {
		name = "Red";
		isRed = true;
		symbol = 'X';
		color = Color.red;
		icon = redCircle;
	}
	
	void black() {
		name = "Black";
		isRed = false;
		symbol = 'O';
		color = Color.black;
		icon = blackCircle;
	}
	
	void flip() {
		if (isRed)
			black();
		else
			red();
	}
}
