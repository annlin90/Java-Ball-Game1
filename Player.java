public class Player
{
	private int score;			   //player score
	private boolean gameover=false;	
	public int scoreConstant = 15; //This constant value is used in score calculation. You don't need to change this. 
	public int lives;
	public int targetScore;
	
	public Player(int living)
	{
		lives = living;
		score = 0; //initialize the score to 0

	}
	/* get player score*/
	public int getScore ()
	{
		return score;
	}
	
	/*check if the game is over*/
	public boolean isGameOver ()
	{
		return this.gameover;
	}

	/*update player score*/
	public void addScore (int plus)
	{
		score += plus;
	}

	/*update "game over" status*/
	public void gameIsOver ()
	{
			gameover = true;
	}
	
	public void lose() 
	{
		lives--;
	}
	
	public void win() 
	{
		lives++;
	}
	
	public int getLives()
	{
		return lives;
	}
}