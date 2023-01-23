import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/*
 * Arsha Fazlollahi's Snake Multiplayer
 * Class: Grade 12 Computer Science
 * Teacher: Mr. Benum
 * Resources:
 * https://www.javatpoint.com/java-joptionpane
 * https://www.geeksforgeeks.org/jlabel-java-swing/
 * https://stackoverflow.com/questions/2715118/how-to-change-the-size-of-the-font-of-a-jlabel-to-take-the-maximum-size
 * https://linuxhint.com/end-java-program/#:~:text=You%20can%20end%20a%20Java,terminates%20the%20currently%20running%20JVM.&text=Here%2C%20the%20System.,will%20terminate%20without%20any%20error.
 * https://docs.google.com/document/d/1WsYasD1hLBEpjJ8XCaTNBczKDMI5q57-fjW7J2V9gOc/edit
 */

public class SnakeGame extends Game {
	
	private Apple apple; //Object of type Apple that will be used as the apples' hit box.
	private JLabel appleLabel; //JLabel used to display the apples' ImageIcons as objects.
	private UI ui[]; //Array that stores the UI objects(Walls). UI objects will be constructed in addUI().
	public ArrayList<ImageIcon> snakeOneImages = new ArrayList<ImageIcon>(); //ArrayList that stores the images for the first snake.
	private JLabel snake1Label; //JLabel used to display the first snake's ImageIcons as objects.
	public ArrayList<ImageIcon> snakeTwoImages = new ArrayList<ImageIcon>(); //ArrayList that stores the images for the second snake.
	private JLabel snake2Label; //JLabel used to display the second snake's ImageIcons as objects.
	private JLabel p1ScoreLabel; //JLabel used to display the first snake's score and icon as objects.
	private JLabel p2ScoreLabel; //JLabel used to display the second snake's score and icon as objects.
	private int p1Score = 0;
	private int p2Score = 0;
	private Snake snakeOne, snakeTwo; //Snake one and snake two objects of type Snake, constructed in setup()
	BodyPart snakeOneHead; //Snake head objects of type BodyPart, constructed in setup().
	BodyPart snakeTwoHead;
	private ArrayList<BodyPart> bodyOne = new ArrayList<BodyPart>(); //ArrayLists that store the body parts for the snakes so that they can be passed to the Snake class as parameters when constructing a snake.
	private ArrayList<BodyPart> bodyTwo = new ArrayList<BodyPart>();
	private final String[] options = {"Continue", "New Game", "Exit"}; //String array of the different options that can be chosen in JOptionPane after a round is over.
	private boolean newGame = true; //Boolean used to keep track of when a game is new and when it's not.

	/*
	 * The main method in SnakeGame constructs a new Game of type SnakeGame.
	 * It then sets the window's dimensions and sets resizable to false.
	 * Makes the window visible and calls initComponents() to set the game up.
	 */
	public static void main(String[] args) throws IOException {

		SnakeGame sg = new SnakeGame();
		sg.setSize(840, 840); 
		sg.setResizable(false);
		sg.setVisible(true);
		sg.initComponents();
	}
		
	public void setup() throws IOException {
		
		setDelay(70); //Sets the act method delay to 70 milliseconds.
		int appleX = (int)(Math.random() * 600 + 100); //Integers that store the apple object's x and y value and are set to a random number within the game window's range by default.
		int appleY = (int)(Math.random() * 600 + 100);
		
		/*
		 *The if statement below checks if this is a newly opened game.
		 *If yes, JOptionPane will display a message dialogue with the game's instructions.
		 *It will also call the addScoreBoard() method because the score board must only be added once in this game. 
		 */
		if(newGame){
			JOptionPane.showMessageDialog(null, "Welcome to Snake Multiplayer by Arsha Fazlollahi!\n"
					+ "The BLUE SNAKE can be controlled using WASD, and the RED SNAKE can be controlled using the arrow keys."
					+ "\nThe goal is to eliminate the other player by making them hit your tail after eating apples and growing."
					+ "\nThe scoreboard at the top tracks the number of rounds won by each snake color."
					+ "\nAfter the end of each round you can choose to continue playing, reset the scores by choosing *New Game*, or exit the game completely."
					+ "\nBe careful, if you hit your head on the walls or your own tail the other player wins."
					+ "\nHave fun!");
			addScoreBoard();
		}
		
		/*
		 * Adds the image of the apple stored in the game's files to the screen.
		 * Sets its location to the randomly generated appleX and appleY.
		 */
		appleLabel = new JLabel(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("apple.png"))));
		appleLabel.setSize(30, 30);
		appleLabel.setLocation(appleX, appleY);
		add(appleLabel);
		
		addSnakeImages(); //Calls the method that adds the images of the snakes' heads.

		/*
		 * Constructs the hit boxes of the snakes' heads of type BodyPart and adds them to the screen.
		 * Their color is the same as the background color because the images of the snakes' heads will be displayed instead.
		 * Constructs the snakes by passing their head objects, body arrays, and images/labels as parameters to the Snake class.
		 * Adds the snakes to the screen.
		 */
		snakeOneHead = new BodyPart(200,500, Color.green.brighter());
		snakeTwoHead = new BodyPart(200,300, Color.green.brighter());
		add(snakeOneHead);
		add(snakeTwoHead);
		snakeOne = new Snake(snakeOneHead, bodyOne, snake1Label, snakeOneImages);
		snakeTwo = new Snake(snakeTwoHead, bodyTwo, snake2Label, snakeTwoImages);
		add(snakeOne);
		add(snakeTwo);
		
		apple = new Apple(appleX, appleY); //Constructs the apple's hit box and sets it to the same location that the apple's image is set to.
		add(apple);
		
		addUI(); //Calls the method that adds the walls (and the score board background).
	}
	
	public void act() throws IOException {
		
		snakeControl(); //Calls the method that handles the key presses that controls each snake.
		
		/*
		 * The two if statements below handle the event where each snake collides with an apple.
		 * The newApple() method (from the Apple class) is called for the apple object which sets a new location for the apple's hit box.
		 * The body arrays are passed into the method as parameters so that the newApple() method can avoid placing a new apple on the snakes' bodies.
		 * The image for the apple will get set to the new location as well. The new locations are retrieved by calling apple.getX() and apple.getY().
		 * A new body part is constructed and added with a default 400,400 location.
		 * By adding the new body part to the corresponding snake object, the Snake class will handle adding that body part to the snake and making it follow the snake around.
		 */
		if (snakeOne.getHead().collides(apple)){
			apple.newApple(bodyOne);
			apple.newApple(bodyTwo);
			appleLabel.setLocation(apple.getX(), apple.getY());
			BodyPart bodyPart = new BodyPart(400,400, new Color(78,124,246));
			add(bodyPart);
			snakeOne.addBodyPart(bodyPart);
		}
		if (snakeTwo.getHead().collides(apple)){
			apple.newApple(bodyTwo);
			apple.newApple(bodyOne);
			appleLabel.setLocation(apple.getX(), apple.getY());
			BodyPart bodyPart = new BodyPart(400,400, new Color(236,28,36));
			add(bodyPart);
			snakeTwo.addBodyPart(bodyPart);
		}

		/*
		 * The for loop below goes through every object of the UI array and checks if the snakes' heads have hit the walls.
		 * If they have the score values and labels will be updated accordingly and the gameOver() method is called.
		 * The correct announcement at the end of the game is passed to the gameOver() method as a String parameter.
		 */
		for(int i = 0; i<ui.length; i++){
			if(snakeOne.getHead().collides(ui[i])){
				p2Score++;
				p2ScoreLabel.setText(Integer.toString(p2Score));
				gameOver("RED SNAKE Wins!");
			}
			if(snakeTwo.getHead().collides(ui[i])){
				p1Score++;
				p1ScoreLabel.setText(Integer.toString(p1Score));
				gameOver("BLUE SNAKE Wins!");
			}
		}	
		/*
		 * The for loops below go through every object of the body Array Lists and check if the snakes' heads have hit their own or each others bodies.
		 * If they have the score values and labels will be updated accordingly and the gameOver() method is called.
		 * The correct announcement at the end of the game is passed to the gameOver() method as a String parameter.
		 */
		for(int i = 1; i<bodyOne.size() - 1; i++){
			if(snakeOne.getHead().collides(bodyOne.get(i))){
				p2Score++;
				p2ScoreLabel.setText(Integer.toString(p2Score));
				gameOver("RED SNAKE Wins!");
			}
			if(snakeTwo.getHead().collides(bodyOne.get(i))){
				p1Score++;
				p1ScoreLabel.setText(Integer.toString(p1Score));
				gameOver("BLUE SNAKE Wins!");
			}
		}
		for(int i = 1; i<bodyTwo.size() - 1; i++){
			if(snakeTwo.getHead().collides(bodyTwo.get(i))){
				p1Score++;
				p1ScoreLabel.setText(Integer.toString(p1Score));
				gameOver("BLUE SNAKE Wins!");
			}
			if(snakeOne.getHead().collides(bodyTwo.get(i))){
				p2Score++;
				p2ScoreLabel.setText(Integer.toString(p2Score));
				gameOver("RED SNAKE Wins!");
			}
		}
	}
	
	/**
	 * Adds the walls as well as the background for the score board to the screen and the UI array.
	 */
	public void addUI(){
		
		ui = new UI[5];
		ui[0] = new UI(0, 80, getFieldWidth(), 10, Color.green.darker()); //top wall
		ui[1] = new UI(0, 0, 10, getFieldHeight(), Color.green.darker()); // left wall
		ui[2] = new UI(getFieldWidth() - 10, 0, 10, getFieldHeight(), Color.green.darker()); //right wall
		ui[3] = new UI(0, getFieldHeight() - 10, getFieldWidth(), 10, Color.green.darker()); //bottom wall
		ui[4] = new UI(0, 0, getFieldWidth(), 80, Color.green.darker().darker()); //Score board background.
		add(ui[4]);
		add(ui[0]);
		add(ui[1]);
		add(ui[2]);
		add(ui[3]);
		
	}
	
	/**
	 * Handles key presses that control each snake by calling the Snake class's up/down/left/right methods.
	 */
	public void snakeControl(){
		
		if (WKeyPressed()){
			/*
			 * Setting every other movement state to false in the Game class prevent simultaneous input.
			 * Fixes a bug where a snake could turn on its own body and lose by pressing up/down and left/right at the same time.
			 */
			Game.p1Left = false;
			Game.p1Right = false;
			Game.p1Down = false;
			snakeOne.up();
		}
		
		if (SKeyPressed()){
			Game.p1Left = false;
			Game.p1Right = false;
			Game.p1Up = false;
			snakeOne.down();
		}
		
		if (DKeyPressed()){
			Game.p1Left = false;
			Game.p1Up = false;
			Game.p1Down = false;
			snakeOne.right();
		}
		
		if (AKeyPressed()){
			Game.p1Right = false;
			Game.p1Up = false;
			Game.p1Down = false;
			snakeOne.left();
		}
		if (UpKeyPressed()){
			Game.p2Left = false;
			Game.p2Right = false;
			Game.p2Down = false;
			snakeTwo.up();
		}
		
		if (DownKeyPressed()){
			Game.p2Left = false;
			Game.p2Right = false;
			Game.p2Up = false;
			snakeTwo.down();
		}
		
		if (RightKeyPressed()){
			Game.p2Left = false;
			Game.p2Up = false;
			Game.p2Down = false;
			snakeTwo.right();
		}
		
		if (LeftKeyPressed()){
			Game.p2Right = false;
			Game.p2Up = false;
			Game.p2Down = false;
			snakeTwo.left();
		}
	}
	
	/**
	 * Constructs the JLabels for each snake, adds the correct images to the ImageIcon Array Lists, and displays the images of the snakes' heads.
	 * Images are set to 185,138 and facing right by default. The labels and ImageIcon array lists must be passed to the Snake constructor in order for the images to follow the snakes.
	 */
	public void addSnakeImages() throws IOException{
		
		snake1Label = new JLabel(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("snakeHeadRight.png"))));
		snake2Label = new JLabel(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("snake2HeadRight.png"))));
		snake1Label.setSize(77, 46);
		snake1Label.setLocation(185, 138);
		add(snake1Label);
		snake2Label.setSize(77, 46);
		snake2Label.setLocation(185, 138);
		add(snake2Label);
		
		/*
		 * Images of new snakes must be added in this order (Up, Down, Left, Right).
		 * These indexes are important because when the ArrayList is used in the Snake class, every index corresponds to a specific direction.
		 * For example, in Snake class, when current direction is set to up the class sets the snake's image to the one at index 0 from the ArrayList that was passed to its constructor.
		 */
		
		snakeOneImages.add(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("snakeHeadUp.png"))));
		snakeOneImages.add(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("snakeHeadDown.png"))));
		snakeOneImages.add(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("snakeHeadLeft.png"))));
		snakeOneImages.add(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("snakeHeadRight.png"))));
		
		snakeTwoImages.add(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("snake2HeadUp.png"))));
		snakeTwoImages.add(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("snake2HeadDown.png"))));
		snakeTwoImages.add(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("snake2HeadLeft.png"))));
		snakeTwoImages.add(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("snake2HeadRight.png"))));
		
	}
	
	/**
	 * Stops the game using Game class's stopGame() and displays the JOPtionPane options (Continue, New Game, Exit).
	 * Takes a String parameter for the announcement in JOptionPane.
	 */
	public void gameOver(String announcement) throws IOException{

		/*
		 *The code block below sets every direction state to false in the Game class.
		 *Fixes a bug where holding down/pressing a key at the end of the round causes that key to never call its keyReleased() method.
		 */
		Game.p1Left = false;
		Game.p1Right = false;
		Game.p1Up = false;
		Game.p1Down = false;
		Game.p2Left = false;
		Game.p2Right = false;
		Game.p2Up = false;
		Game.p2Down = false;
		
		stopGame();
		
		/*
		 * The code block below displays the JOptionPane options using the options[] array field and handles each choice.
		 * If the choice is *Continue*, the game continues by calling resetGame() and startGame() which starts a new round.
		 * If the choice is *New Game*, the score values and labels are reset and then resetGame() and startGame() are called.
		 * If the choice is *Exit*, the game is closed using System.exit(0).
		 * The newGame boolean is set to false in both *Continue* and *New Game* because the instructions do not need to be shown again.
		 */
		int selection = JOptionPane.showOptionDialog(null, announcement, "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		if (selection == 0){
			newGame = false;
			resetGame();
			startGame();
		}
		if (selection == 1){
			newGame = false;
			p1Score = 0;
			p2Score = 0;
			p1ScoreLabel.setText("0");
			p2ScoreLabel.setText("0");
			resetGame();
			startGame();
		}
		if (selection == 2){
			System.exit(0);
		}
		
	}
	
	/**
	 * Resets the snake positions and lengths, removes all objects within the arena, and calls setup() and repaint().
	 */
	public void resetGame() throws IOException{
		
		remove(snakeOneHead);
		remove(snakeTwoHead);
		remove(snakeOne);
		remove(snakeTwo);
		
		/*
		 * The two for loops below go through every body part in their array lists and removes them from the screen.
		 */
		for (int j = 0; j<bodyOne.size(); j++){
			
			remove(bodyOne.get(j));
			
		}
		for (int j = 0; j<bodyTwo.size(); j++){
			
			remove(bodyTwo.get(j));
			
		}
		
		bodyOne.clear(); //Removes all body part objects from the bodyOne/bodyTwo ArrayList.
		bodyTwo.clear();
		
		remove(appleLabel);
		remove(snake1Label);
		remove(snake2Label);
		remove(apple);
		
		setup();
		repaint();
	}
	
	/**
	 * Constructs new JLabels that contain both the score texts and the snakes' icons, and adds them to the screen.
	 * Sets the JLabels to the correct sizes, fonts, and locations.
	 */
	public void addScoreBoard() throws IOException{
		
		p1ScoreLabel = new JLabel("0", new ImageIcon(ImageIO.read(getClass().getResourceAsStream("BlueSnake.png"))), 2);
		p1ScoreLabel.setSize(70, 70);
		p1ScoreLabel.setLocation(40, 5);
		p1ScoreLabel.setForeground(Color.white);
		p1ScoreLabel.setFont(new Font("Serif", Font.PLAIN, 25));
		p2ScoreLabel = new JLabel("0", new ImageIcon(ImageIO.read(getClass().getResourceAsStream("RedSnake.png"))), 2);
		p2ScoreLabel.setSize(70, 70);
		p2ScoreLabel.setLocation(170, 5);
		p2ScoreLabel.setForeground(Color.white);
		p2ScoreLabel.setFont(new Font("Serif", Font.PLAIN, 25));
		add(p1ScoreLabel);
		add(p2ScoreLabel);
		
	}
	
}
