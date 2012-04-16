import java.io.File;
import java.io.FileNotFoundException;


public class SystematicAI extends CommonAI{
	
	public SystematicAI(File boardFile) throws FileNotFoundException {
		super(boardFile);
		lastRowFired = 0;
		lastColFired = 0;
	}
	
	public SystematicAI(int cols, int rows) throws BattleshipException {
		super(cols, rows);
		lastRowFired = 0;
		lastColFired = 0;
	}
	
	public boolean takeTurn(BattleshipBoard playerBoard)
	{
		if(sinkingShip)
			sinkShip(playerBoard);
		if(!sinkingShip)
			while(spacesHit[lastColFired][lastRowFired])
			{
				lastColFired += 2;
				if(lastColFired > 9){
					lastRowFired += 1;
					lastColFired -= 11;
					if(lastColFired < 0)
						lastColFired += 2;
				}
			}
		
		spacesHit[lastColFired][lastRowFired] = true;
		checkSinkShip(playerBoard);
		
		this.setTurn(false);
		return playerBoard.fireShot(lastColFired, lastRowFired);
	}
}
