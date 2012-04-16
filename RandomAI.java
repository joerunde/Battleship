import java.io.File;
import java.io.FileNotFoundException;


public class RandomAI extends CommonAI{
	
	public RandomAI(File boardFile) throws FileNotFoundException {
		super(boardFile);
		lastRowFired = (int)(Math.random()*10);
		lastColFired = (int)(Math.random()*10);
	} 
	
	public RandomAI(int cols, int rows) throws BattleshipException {
		super(cols, rows);
		lastRowFired = (int)(Math.random()*10);
		lastColFired = (int)(Math.random()*10);
	} 
	
	public boolean takeTurn(BattleshipBoard playerBoard)
	{
		if(sinkingShip)
			sinkShip(playerBoard);
		if(!sinkingShip)
			while(spacesHit[lastColFired][lastRowFired])
			{
				lastRowFired = (int)(Math.random()*10);
				lastColFired = (int)(Math.random()*10);
			}
		
		spacesHit[lastColFired][lastRowFired] = true;
		
		checkSinkShip(playerBoard);
		
		this.setTurn(false);
		return playerBoard.fireShot(lastColFired, lastRowFired);
	}

}
