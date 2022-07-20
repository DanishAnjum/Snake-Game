import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH=600;
	static final int SCREEN_HEIGHT=600;
	static final int UNIT_SIZE=25;     //object size
	static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;    //number of objects or game size
	static final int DELAY=75;
	
	/*creating two arrays to hold all the coordinates for all the
	body parts of our snake including the head*/
	final int x[]=new int[GAME_UNITS];    //x coordinates
	final int y[]=new int[GAME_UNITS];    //y coordinates
	
	//initial amount of body parts for the snake
	int bodyParts=6;
	int applesEaten;       //initially 0
	
	/*x coordinate of where the apple is located and it's going to appear
	randomly each time that the snake eats an apple*/
	int appleX;      
	int appleY;     //y coordinate of apple
	char direction='R';   //snake begin going right when we start the game
	boolean running=false;
	
	Timer timer;    //declaring a timer called timer
	Random random;  //declaring an instance of the random class called random
	
	
	GamePanel(){                     //constructor
		
		random = new Random();       //creating an instance of the random class
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));     //setting the preferred size for this game panel   
        this.setBackground(Color.black);	
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
	}
	
	public void startGame() {
		
		newApple();
		running=true;
		timer = new Timer(DELAY, this);       //passing 'this' because we're using the ActionListener interface
	    timer.start();
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if(running) {
			/*
			//turning the window into a matrix or grid to help visualize things
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {         //loop to draw lines across the game panel so it becomes a grid
				//drawing lines across the x-axis and y-axis by passing starting coordinates and ending coordinates
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);       //lines across y-axis
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);        //lines across x-axis
			}
		    */
			
			//draw the apple
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			//draw the snake
			//loop to iterate through all the body parts of the snake
				for(int i=0;i<bodyParts;i++) {
					if(i==0) {          //head of the snake
						g.setColor(Color.green);
						g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					}
					else {              //body of the snake
						//g.setColor(new Color(45,180,0));
						//changing the color of the snake randomly. RGB values lie on a scale of 0 to 255
						g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
						g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					}
				}
				
				//to draw the current score
				g.setColor(Color.red);
				g.setFont(new Font("Ink Free", Font.BOLD, 40));
				
				//font metrics used for aligning up text
				//creating an instance of the font metrics
				FontMetrics metrics = getFontMetrics(g.getFont());
				g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());     //display score at top of the screen
		}
		else {
			gameOver(g);
		}
	}
	
	public void newApple() { 
		
		//generates the coordinates of a new apple whenever this method is called
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;          //multiplying it with unit size so the apple gets placed evenly within one of the units
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;         
	}
	
	public void move() {
		
		//moving the snake
		//iterate through all the body parts of the snake
		for(int i=bodyParts;i>0;i--) {
			x[i] = x[i-1];     //shifting all the coordinates in this array over by 1 spot
			y[i] = y[i-1];
		}
		
		//switch that will change the direction of where the snake is headed
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;      //y coordinate of the head of snake
		    break;
	    case 'D':
		    y[0] = y[0] + UNIT_SIZE;      //y coordinate of the head of snake
	        break;
	    case 'L':
		    x[0] = x[0] - UNIT_SIZE;      //y coordinate of the head of snake
	        break;
	    case 'R':
		    x[0] = x[0] + UNIT_SIZE;      //y coordinate of the head of snake
	        break;
	   }
	}
	
	public void checkApple() {
		
		//grabbing the apple
		//examine the coordinates of snake and apple
		if((x[0] == appleX) && (y[0] == appleY)) {      //if the snake collides with the apple
			bodyParts++;                                //increase the body parts
		    applesEaten++;
		    newApple();                                 //generate a new apple
		}
	}
	
	public void checkCollisions() {
		
		//checks if head of snake collides with it's body
		//iterate through all body parts of the snake
		for(int i=bodyParts;i>0;i--) {
			if((x[0] == x[i]) && y[0] == y[i]) {       //if head collided with the body
				running = false;
			}
		}
		
		/*checks if head touches left border or right border
		or top border or bottom border*/
		if((x[0] < 0) || (x[0] > SCREEN_WIDTH) || (y[0] < 0) || (y[0] > SCREEN_HEIGHT)){
			running = false;
		}
		
		if(!running) {          //if running is false
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		
		//display score on the game over screen
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		
		//font metrics used for aligning up text
		//creating an instance of the font metrics
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());     //display score 
		
		//Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		
		//font metrics used for aligning up text in the center of the screen
		//creating an instance of the font metrics
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);     //string at the center of the screen
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {                  //if the game is running
			move();                    //move the snake
			checkApple();              //if the snake ran into the apple
			checkCollisions();
		}
		repaint();        //if the game is no longer running
		
	}
	
	public class MyKeyAdapter extends KeyAdapter{                 //innerclass

		@Override
		public void keyPressed(KeyEvent e) {
			
			//control the snake
			//within switch there are four cases one for each of the arrow keys
			switch(e.getKeyCode()) {
			
			/*the user should not turn 180deg in opp direction, they might get
			a game over because they are going directly into themselves so we want
			to limit the user to only 90deg turns*/
			case KeyEvent.VK_LEFT:       //left arrow key
			      if(direction != 'R') {
				      direction = 'L';
			      }
			      break;
			case KeyEvent.VK_RIGHT:       
			      if(direction != 'L') {
				      direction = 'R';
			      }
			      break;
			case KeyEvent.VK_UP:       
			      if(direction != 'D') {
				      direction = 'U';
			      }
			      break;
			case KeyEvent.VK_DOWN:       
			      if(direction != 'U') {
				      direction = 'D';
			      }
			      break;
			}
		}
		
}}
