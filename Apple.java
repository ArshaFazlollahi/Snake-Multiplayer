import java.awt.Color;
import java.util.ArrayList;


public class Apple extends GameObject{
	
	public Apple(int x, int y){ //Apple's constructor.
		
		setX(x);
		setY(y);
		setSize(30, 30);
		//The color of the apple object is the same as the background color because this object is a hit box. The apple that will be displayed is a JLabel.
		setColor(Color.green.brighter()); 
		
	}

	public void act() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Adds a new apple in a new randomly generated(within the range of the game's arena) location.
	 * Takes a BodyPart ArrayList as a parameter.
	 * Runs a do-while loop that checks every BodyPart in that ArrayList to make sure the new apple is not spawning on top of a snake body part.
	 * The loop generates a new apple location for as long as the new apple overlaps with a snake body part.
	 */
	public void newApple(ArrayList<BodyPart> allParts){
		
		int x, y;
		boolean onBody;
		do{
			onBody = false;
			x = (int)(Math.random() * 600 + 100);
			y = (int)(Math.random() * 600 + 100);
			setX(x);
			setY(y);
			for(BodyPart body : allParts){
				if(this.collides(body)){
					onBody = true;
					break;
				}
			}
		}while(onBody);
		
	}

}
