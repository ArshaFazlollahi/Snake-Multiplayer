import java.awt.Color;


public class UI extends GameObject{
	
	public UI(int x, int y, int widthSize, int heightSize, Color color){ //The UI constructor. Takes and sets x,y locations, sizes, and colors of the new UI objects.
		
		setX(x);
		setY(y);
		setSize(widthSize, heightSize);
		setColor(color);
		
	}

	public void act() {
		// TODO Auto-generated method stub
		
	}

}
