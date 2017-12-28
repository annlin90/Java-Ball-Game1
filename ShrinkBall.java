import java.applet.*;
import java.awt.*;
import java.util.*;
import java.net.*;
import java.lang.*;

public class ShrinkBall extends Ball
{
	public int numberofShrinks;
	public int firstRadius;
	public ShrinkBall (int radius, int initXpos, int initYpos, int speedX, int speedY, int maxBallSpeed, Color color, AudioClip outSound, Player player,  GameWindow gameW,int points)
	{
		super(radius, initXpos, initYpos, speedX, speedY, maxBallSpeed, color, outSound, player,   gameW,points);
	    firstRadius = radius;
		numberofShrinks = 0;
		
	}
	
	public boolean userHit(int maus_x, int maus_y)
	{
		double x = maus_x - pos_x;
		double y = maus_y - pos_y;

		double distance = Math.sqrt ((x*x) + (y*y));
		
		if (distance-this.radius < (int)(player.scoreConstant)) {
			if(numberofShrinks == 0){ //if hasn't shrinked yet
				player.addScore ((player.scoreConstant * Math.abs(x_speed) + player.scoreConstant)); //add score normally
			}
			else if(numberofShrinks >0)
			{
				//else double score for each shrink
				player.addScore (2*(player.scoreConstant * Math.abs(x_speed) + player.scoreConstant));
			}
			 
			shrinkBall();
			if(x_speed == 0){
				x_speed++; //make sure speed isn't 0,0
				}
				
			if(y_speed == 0){
				y_speed++;
				}
			return true;
		}
		else
		{
			return false;
		}
	}
	public void shrinkBall()
	{
		if(numberofShrinks>2)
		{
			radius =  firstRadius; //if less than 30% in size radius is resetted
			numberofShrinks = 0; //and so is number of shrinks
		}
		else
		{
			numberofShrinks++;
			radius = (int)(radius*(0.7));
		}	
	}
}