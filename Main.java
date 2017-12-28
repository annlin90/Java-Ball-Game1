import java.awt.*;
import java.util.*;
import java.applet.*;
import java.net.*;
import java.security.AccessController;
import java.awt.event.MouseEvent;
import javax.swing.event.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FilePermission;
/*<applet code="Main" height=400 width=400></applet>*/


public class Main extends Applet implements Runnable
{

/* Configuration arguments. These should be initialized with the values read from the config.xml file*/					
    private int numBalls;
/*end of config arguments*/

    private int refreshrate = 15;	           //Refresh rate for the applet screen. Do not change this value. 
	private boolean isStoped = true;		     
    Font f = new Font ("Arial", Font.BOLD, 18);
	
	private Player player;			           //Player instance.
	private Ball redball;                      //Ball instance. You need to replace this with an array of balls. 
	public ArrayList <Ball> ball_array;
	
	Thread th;						           //The applet thread. 
	
	
	public int clickAmount=0;
	public int success=0;
	
	public int basics = 0;
	public int shrinks = 0;
	public int bounces = 0;

	AudioClip shotnoise;	
	AudioClip hitnoise;		
	AudioClip outnoise;		
	  
    Cursor c;				
    private GameWindow gwindow;                 // Defines the borders of the applet screen. A ball is considered "out" when it moves out of these borders.
	private Image dbImage;
	private Graphics dbg;

	
	class HandleMouse extends MouseInputAdapter 
	{
    	public HandleMouse() 
    	{
            addMouseListener(this);
        }
		
    	public void mouseClicked(MouseEvent e) 
    	{
				clickAmount++;
        	if (!isStoped) {
        		 for(int i = 0; i<ball_array.size();i++){ //go through ball array and initiate wasHit whenever a specific ball is hit
					if(ball_array.get(i).userHit (e.getX(), e.getY())){
						hitnoise.play();
						ball_array.get(i).ballWasHit();
					    success++; //count successful hits 
						if (i==0) { //getting the count of hits for each ball type
							basics++; //basic ball
						}
						if (i==2) {
							shrinks++; //shrink ball
						}
						if (i==1) {
							bounces++; //bounce ball
						}
	        	}
				 
        		else {
					shotnoise.play();
				}
				 }
				
			}
			else if (isStoped && e.getClickCount() == 2) {
				isStoped = false;
				init ();
			}
    	}

    	public void mouseReleased(MouseEvent e) 
    	{
           
    	}
        
    	public void RegisterHandler() 
    	{

    	}
    }
	
	
	//xml reader here
	//used https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/ for reference
	public void xmlReader()
	{
		int life_score = 200; //not
		int numLives = 10; //not
		
     	try
		{	
			File fXmlFile = new File(".\\config.xml");
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
			//init GameWindow
			
			
			
			NodeList nList = doc.getElementsByTagName("GameWindow");
			Node nNode = nList.item(0);
			
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) 
			{
				Element eElement = (Element) nNode;
				
				gwindow =new GameWindow(
						Integer.parseInt( eElement.getElementsByTagName("x_leftout").item(0).getTextContent()),
						Integer.parseInt( eElement.getElementsByTagName("x_rightout").item(0).getTextContent()),
						Integer.parseInt( eElement.getElementsByTagName("y_upout").item(0).getTextContent()),
						Integer.parseInt( eElement.getElementsByTagName("y_downout").item(0).getTextContent()), 
						numLives
						//Integer.parseInt( eElement.getElementsByTagName("life_score").item(0).getTextContent()) //ss
						); 
			}
			//Init Player
			
			nList = doc.getElementsByTagName("Player");
			nNode = nList.item(0);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) 
			{
				Element eElement = (Element) nNode;
				int numLives1 = Integer.parseInt(eElement.getElementsByTagName("numLives").item(0).getTextContent());
				player = new Player (numLives1);
			    life_score = Integer.parseInt( eElement.getElementsByTagName("score2EarnLife").item(0).getTextContent());
				 
			}
			
			//Init NumOfBalls
			nList = doc.getElementsByTagName("numBalls"); //number of balls on field
			nNode = nList.item(0);
			this.numBalls = Integer.parseInt(nNode.getTextContent());
			
			ball_array = new ArrayList<Ball>(numBalls); //allocate space

			//Parse the XML file and extract the config items 
			nList = doc.getElementsByTagName("Ball");
			for (int temp = 0; temp < nList.getLength(); temp++) 
			{
				nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{
					
					Element eElement = (Element) nNode;
					Color color = (Color) Color.class.getField(eElement.getElementsByTagName("color").item(0).getTextContent()).get(null);
					
					if(eElement.getElementsByTagName("type").item(0).getTextContent().toString().equals("basicball"))
					{
						Ball reds= new Ball( //create new basicball with the xml basicball values
								Integer.parseInt( eElement.getElementsByTagName("radius").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("initXpos").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("initYpos").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("speedX").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("speedY").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("maxBallSpeed").item(0).getTextContent()),
								color,
								outnoise, player, gwindow, life_score
								//Integer.parseInt( eElement.getElementsByTagName("score2EarnLife").item(0).getTextContent())
								);	
								ball_array.add(reds); //add basicball to the ball array
					}
					// TODO- add the conditions for bounceball and shrink ball
					if(eElement.getElementsByTagName("type").item(0).getTextContent().toString().equals("shrinkball"))
					{
						ShrinkBall ss = new ShrinkBall( //same as basic ball, create new shrinkball with values from xml shrinkball values
								Integer.parseInt( eElement.getElementsByTagName("radius").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("initXpos").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("initYpos").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("speedX").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("speedY").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("maxBallSpeed").item(0).getTextContent()),
								color,
								outnoise, player, gwindow, life_score
								//Integer.parseInt( eElement.getElementsByTagName("score2EarnLife").item(0).getTextContent())
								);	
						ball_array.add(ss); //also add to ball array
					}
				
					if(eElement.getElementsByTagName("type").item(0).getTextContent().toString().equals("bounceball"))
					{
						BounceBall vv = new BounceBall( //same as the previous two balls
								Integer.parseInt( eElement.getElementsByTagName("radius").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("initXpos").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("initYpos").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("speedX").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("speedY").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("maxBallSpeed").item(0).getTextContent()),
								color,
								outnoise, player, gwindow, 
								Integer.parseInt( eElement.getElementsByTagName("bounceCount").item(0).getTextContent()),
								life_score
								);	
						ball_array.add(vv); //add to ball array
					}
					
				}
			}
				
		}
		catch (Exception e)
		{
		
			e.printStackTrace();
		}
	}
	
	HandleMouse hm = new HandleMouse();	
	
    /*initialize the game*/
	public void init ()
	{	
		//reads info from XML doc
		this.xmlReader();
		c = new Cursor (Cursor.CROSSHAIR_CURSOR);
		this.setCursor (c);
					 	
        Color superblue = new Color (0, 0, 255);  
		setBackground (Color.black);
		setFont (f);

		if (getParameter ("refreshrate") != null) {
			refreshrate = Integer.parseInt(getParameter("refreshrate"));
		}
		else refreshrate = 15;

		
		hitnoise = getAudioClip (getCodeBase() , "gun.au");
		hitnoise.play();
		hitnoise.stop();
		shotnoise = getAudioClip (getCodeBase() , "miss.au");
		shotnoise.play();
		shotnoise.stop();
		outnoise = getAudioClip (getCodeBase() , "error.au");
		outnoise.play();
		outnoise.stop();

		this.setSize(gwindow.x_rightout, gwindow.y_downout); //set the size of the applet window.
	}
	
	/*start the applet thread and start animating*/
	public void start ()
	{		
		if (th==null){
			th = new Thread (this);
		}
		th.start ();
	}
	
	/*stop the thread*/
	public void stop ()
	{
		th=null;
	}

    
	public void run ()
	{	
		/*Lower this thread's priority so it won't infere with other processing going on*/
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        /*This is the animation loop. It continues until the user stops or closes the applet*/
		while (true) {
			if (!isStoped) {
				 for(int j = 0; j< ball_array.size(); j++){
					ball_array.get(j).move(); //animation loops for all the balls in ball array
				 }	 
			}
            /*Display it*/
			repaint();
            
			try {
				
				Thread.sleep (refreshrate);
			}
			catch (InterruptedException ex) {
				
			}			
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}

	
	public void paint (Graphics g)
	{
		/*if the game is still active draw the ball and display the player's score. If the game is active but stopped, ask player to double click to start the game*/ 
		if (!player.isGameOver()) {
			g.setColor (Color.yellow);
			
			g.drawString ("Score: " + player.getScore(), 10, 40);
			g.drawString ("Lives: " + player.getLives(), 10, 70);
			g.drawString ("# of Clicks: " + clickAmount, 200, 40);
			g.drawString ("# of Balls Hit: " + success, 200, 70);
			
			if(basics > bounces && basics > shrinks) { //prints out the ball type that has the most hits
				g.drawString("Ball with Most Hits: Basic Ball", 400, 70);
			}
				else if(shrinks > bounces && shrinks > basics) {
				g.drawString("Ball with Most Hits: Shrink Ball", 400, 70);
			}
			else if(bounces > basics && bounces > shrinks) {
				g.drawString("Ball with Most Hits: Bounce Ball", 400, 70);
			}
			else {
				g.drawString("There's a tie!!!", 400, 70);
			}
						
			 
		for(int j = 0; j< ball_array.size(); j++){
			ball_array.get(j).DrawBall(g); //draw all the balls in ball array
			}
			 
			
			if (isStoped) {
				g.setColor (Color.yellow);
				g.drawString ("Doubleclick on Applet to start Game!", 40, 200);
			}
		}
		/*if the game is over (i.e., the ball is out) display player's score*/
		else {
			 basics = 0;
			bounces = 0; //reset ball type hit scores
			shrinks = 0;
			g.setColor (Color.yellow);

			g.drawString ("Game over!", 130, 100);	
			
			if (player.getScore() < 300) g.drawString ("Well, it could be better!", 100, 190);
			else if (player.getScore() < 600 && player.getScore() >= 300) g.drawString ("That was not so bad", 100, 190);
			else if (player.getScore() < 900 && player.getScore() >= 600) g.drawString ("That was really good", 100, 190);
			else if (player.getScore() < 1200 && player.getScore() >= 900) g.drawString ("You seem to be very good!", 90, 190);
			else if (player.getScore() < 1500 && player.getScore() >= 1200) g.drawString ("That was nearly perfect!", 90, 190);
			else if (player.getScore() >= 1500) g.drawString ("You are the Champion!",100, 300);

			g.drawString ("Doubleclick on the Applet, to play again!", 20, 220);
			
			isStoped = true;	
		}
		    
	}
	
	public void update (Graphics g)
	{
		
		if (dbImage == null)
		{
			dbImage = createImage (this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics ();
		}
		
		dbg.setColor (getBackground ());
		dbg.fillRect (0, 0, this.getSize().width, this.getSize().height);

		
		dbg.setColor (getForeground());
		paint (dbg);

		
		g.drawImage (dbImage, 0, 0, this);
	}
}


