import java.applet.*;
import java.awt.*;
import java.util.*;
import java.net.*;
import java.lang.*;
import java.util.Random;


public class Ball
{
    /*Properties of the basic ball. These are initialized in the constructor using the values read from the config.xml file*/
	protected  int pos_x;			
	protected int pos_y; 				
	protected int radius;
	protected int first_x;			
	protected int first_y;					
	protected int x_speed;			
	protected int y_speed;			
	protected int maxspeed;
	protected int points;
	Color color;
	AudioClip outSound;
	
    GameWindow gameW;
	Player player;
	
	/*constructor*/
	public Ball (int radius, int initXpos, int initYpos, int speedX, int speedY, int maxBallSpeed, Color color, AudioClip outSound, Player player,  GameWindow gameW, int points2)
	{	
		this.radius = radius;
		pos_x = initXpos;
		pos_y = initYpos;
		first_x = initXpos;
		first_y = initYpos;
		x_speed = speedX;
		y_speed = speedY;
		maxspeed = maxBallSpeed;
		this.color = color;
		this.outSound = outSound;
		this.player = player;
		this.gameW = gameW;
		points = points2;

	}

	/*update ball's location based on it's speed*/
	public void move ()
	{
		pos_x += x_speed;
		pos_y += y_speed;
		isOut();
	}

	/*when the ball is hit, reset the ball location to its initial starting location*/
	public void ballWasHit ()
	{	
		int max = 10;
		int min = -4;
		Random rand = new Random();
		int number = rand.nextInt(max + 1 -min) + min; //randomnize speed after ball is hit
		if (x_speed ==0) { //basically makes it so that the speed isn't randmozied to 0, so that it's
			 x_speed++; //always moving
		}
		if (y_speed ==0) {
			y_speed++;
		}
		x_speed = number;
		y_speed = number; //new random speed
		resetBallPosition(); //then reset position to initial
		 
	}
	

	/*check whether the player hit the ball. If so, update the player score based on the current ball speed. */	
	public boolean userHit (int maus_x, int maus_y)
	{
		double x = maus_x - pos_x;
		double y = maus_y - pos_y;

		double distance = Math.sqrt ((x*x) + (y*y));
		
		if (distance-this.radius < (int)(player.scoreConstant)) { //if user hit the ball
			player.addScore (player.scoreConstant * Math.abs(x_speed) + player.scoreConstant);
	        //if player gets more than or equal 100 points
			if((player.getScore()>=points)) 
			{
				player.win(); //they get an additional life
				player.targetScore+=points; //add to the target score afterwards
			}
			return true;
		}
		else return false;
	}

    /*reset the ball position to its initial starting location*/
	protected void resetBallPosition()
	{
		pos_x = first_x; //after ball is hit reset position to initial
		pos_y = first_y;
	}
	
	/*check if the ball is out of the game borders. if so, game is over!*/ 
	protected boolean isOut ()
	{
		
		if ((pos_x < gameW.x_leftout) || (pos_x > gameW.x_rightout) || (pos_y < gameW.y_upout) || (pos_y > gameW.y_downout)) {	
			outSound.play();
			player.lose();
			ballWasHit();
			if(player.getLives()==0) //if no more lives then game ends
			{
			player.gameIsOver();
			}
			return true;
		}	
		else return false;
	}

	/*draw ball*/
	public void DrawBall (Graphics g)
	{
		g.setColor (color);
		g.fillOval (pos_x - radius, pos_y - radius, 2 * radius, 2 * radius);
	}

}
