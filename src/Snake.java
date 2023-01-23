import java.awt.Color;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Snake extends GameObject{
	
	public ArrayList<BodyPart> body; //ArrayList of type BodyPart that will temporarily store the snake's body parts in this class.
	public ArrayList<ImageIcon> snakeImages; //ArrayList of type ImageIcon that will temporarily store the snake's images in this class.
	public JLabel snakeLabel; //JLabel that will temporarily store the snake's label in this class.
	public String currentDirection="right";
	
	public Snake(BodyPart bp, ArrayList<BodyPart> bodyList, JLabel label, ArrayList<ImageIcon> images){ //Snake constructor.
		bodyList.add(bp); // Starts off the snake with one body part (head only).
		this.body = bodyList; //Sets the passed in body's ArrayList to this class's body ArrayList.
		this.snakeLabel = label; //Sets the passed in JLabel to this class's snake JLabel.
		this.snakeImages = images; //Sets the passed in image ArrayList to this class's image ArrayList.
	}
	
	/**
	 * If the snake's current direction is not "down", the direction is set to "up".
	 * The snake image is updated to index 0 of the ImageIcon ArrayList that was passed into the class's constructor.
	 * Index 0 represents the image that shows the snake facing up.
	 */
	public void up(){
		if(!currentDirection.equalsIgnoreCase("down")){
			currentDirection = "up";
			snakeLabel.setIcon(snakeImages.get(0));
		}
	}
	
	/**
	 * If the snake's current direction is not "up", the direction is set to "down".
	 * The snake image is updated to index 1 of the ImageIcon ArrayList that was passed into the class's constructor.
	 * Index 1 represents the image that shows the snake facing down.
	 */
	public void down(){
		if(!currentDirection.equalsIgnoreCase("up")){
			currentDirection = "down";
			snakeLabel.setIcon(snakeImages.get(1));
		}
	}
	
	/**
	 * If the snake's current direction is not "right", the direction is set to "left".
	 * The snake image is updated to index 2 of the ImageIcon ArrayList that was passed into the class's constructor.
	 * Index 2 represents the image that shows the snake facing left.
	 */
	public void left(){
		if(!currentDirection.equalsIgnoreCase("right")){
			currentDirection = "left";
			snakeLabel.setIcon(snakeImages.get(2));
		}
	}
	
	/**
	 * If the snake's current direction is not "left", the direction is set to "right".
	 * The snake image is updated to index 3 of the ImageIcon ArrayList that was passed into the class's constructor.
	 * Index 3 represents the image that shows the snake facing right.
	 */
	public void right(){
		if(!currentDirection.equalsIgnoreCase("left")){
			currentDirection = "right";
			snakeLabel.setIcon(snakeImages.get(3));
		}
	}

	public void act() {
		
		//Moves each body tile of the snake to the position of the tile before it.
		//Gives the illusion that the snake is one continuous piece moving in union.
		for (int i = body.size() - 1; i > 0; i--) {
			body.get(i).setX(body.get(i - 1).getX());
			body.get(i).setY(body.get(i - 1).getY());
		}	
		/*
		 * The if/else if statements below move the head in the direction it needs to go.
		 * The constant BLOCK_SIZE from BodyPart is used to move the snake's pieces one block at a time.
		 */
		if(currentDirection.equalsIgnoreCase("up")) {
			getHead().setY(body.get(0).getY() - BodyPart.BLOCK_SIZE);
			//The snake's head label follows the snake's head with a specific offset that prevents the label from appearing separate from the body.
			snakeLabel.setLocation(getHead().getX() - 28, getHead().getY() - 8);
		}
		else if (currentDirection.equalsIgnoreCase("down")) {
			getHead().setY(body.get(0).getY() + BodyPart.BLOCK_SIZE);
			snakeLabel.setLocation(getHead().getX() - 28, getHead().getY() - 16);
		}
		else if (currentDirection.equalsIgnoreCase("left")) {
			getHead().setX(body.get(0).getX() - BodyPart.BLOCK_SIZE);
			snakeLabel.setLocation(getHead().getX() - 26, getHead().getY() - 13);
		}
		else if (currentDirection.equalsIgnoreCase("right")) {
			getHead().setX(body.get(0).getX() + BodyPart.BLOCK_SIZE);
			snakeLabel.setLocation(getHead().getX() - 30, getHead().getY() - 13);
		}
	}
	/**
	 * Takes a BodyPart object as a parameter and adds it to the class's local body ArrayList.
	 */
	public void addBodyPart(BodyPart bp) {
		body.add(bp); // Adds a reference to this new part into the snake so it can be moved.
	}
	/**
	 * Returns the first index of a body ArrayList(the head).
	 */
	public BodyPart getHead() {
		return body.get(0);
	}

}
