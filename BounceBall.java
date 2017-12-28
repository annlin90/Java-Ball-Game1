import java.applet.*;
import java.awt.*;
import java.util.*;
import java.net.*;
import java.lang.*;

import java.applet.*;
import java.awt.*;
import java.util.*;
import java.net.*;
import java.lang.*;

public class BounceBall extends Ball
{
	public int amountofBounces;
	public int stand = 0;
	public BounceBall(int radius, int initXpos, int initYpos, int speedX, int speedY, int maxBallSpeed, Color color, AudioClip outSound, Player player,  GameWindow gameW, int bounces, int points)
	{
		super(radius, initXpos, initYpos, speedX, speedY, maxBallSpeed, color, outSound, player, gameW, points);
		amountofBounces = bounces;
		
	}
	public boolean isOut ()
	{
		 if(stand == amountofBounces)
		{// if the max amount of bounces is reached 
		int max = 10;
		int min = -4; //basically reset position with speed changed
		Random rand = new Random();
		int number = rand.nextInt(max + 1 -min) + min;
		if (x_speed ==0) {
			 x_speed++; //make sure speed isn't 0,0
		}
		if (y_speed ==0) {
			y_speed++;
		}
		x_speed = number;
		y_speed = number;
			resetBallPosition();
			stand = 0; //reset stand
			player.lose(); //minus a life
			outSound.play();
			return true;
		}
		else if ((pos_x > gameW.x_rightout)|| pos_y > gameW.y_downout || pos_x < gameW.x_leftout || (pos_y < gameW.y_upout))
		{
			if(pos_x > gameW.x_rightout&&pos_y> gameW.y_downout)
			{
				x_speed = -x_speed;
				y_speed = -y_speed; //goes to the opposite direction if it hits the border of the game window
			}						//this and belows is all the cases of the possible out bounds the ball can reach
									//if ball reaches border or beyond then go opposite speed in order to "bounce"
			else if(pos_y -5< gameW.y_upout&&pos_x<gameW.x_leftout)
			{
				x_speed = -x_speed;	
				y_speed = -y_speed;
				 
			}
			
			else if (pos_x < gameW.x_leftout&&pos_y >gameW.y_downout) 
			{
				x_speed = -x_speed;
				y_speed = -y_speed;
			}
			 
			else if (pos_y > gameW.y_downout&&pos_x>gameW.x_rightout) 
			{ 
		     	x_speed = -x_speed;
				y_speed = -y_speed;
			}
			
			else if(pos_x>gameW.x_rightout)
			{
				x_speed = -x_speed;
			}
			else if(pos_x<gameW.x_leftout)
			{
				x_speed = -x_speed;
			}
			
			else if(pos_y > gameW.y_downout)
			{	
				y_speed = -y_speed;
			}
			else if(pos_y <gameW.y_upout)
			{
				y_speed = -y_speed;
			}
			 
			stand ++; //count for stand in order to check for maximum bounce limit
			return false;
		}
		else return false;	 	
		 
	}
}