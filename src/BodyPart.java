import java.awt.Color;

public class BodyPart extends GameObject{
	public static final int BLOCK_SIZE = 20; //Constant that stores 20 as the size of each snake body part.
	public BodyPart(int x, int y, Color color) { //BodyPart constructor. Sets the size of the new object to the BLOCK_SIZE constant.
		setX(x);
		setY(y);
		setSize(BLOCK_SIZE, BLOCK_SIZE);
		setColor(color);
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

}