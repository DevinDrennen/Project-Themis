import java.awt.Color;

public class Turn {
	String name;
	boolean isRed;
	char symbol;
	Color color;
	
	void red() {
		name = "Red";
		isRed = true;
		symbol = 'X';
		color = Color.red;
	}
	
	void black() {
		name = "Black";
		isRed = false;
		symbol = 'O';
		color = Color.black;
	}
	
	void flip() {
		if (isRed)
			black();
		else
			red();
	}
}