import java.io.File;
import java.io.FileNotFoundException;


public abstract class CommonAI extends Player{

	protected int lastRowFired, lastColFired, hitCheckRow, hitCheckCol;
	protected boolean directionsChecked[], sinkingShip, hitAMiss;
	
	public CommonAI(File boardFile) throws FileNotFoundException {
		super(boardFile);
		directionsChecked = new boolean[4];
		for(int c = 0; c < 4; c++)
			directionsChecked[c] = true;
		sinkingShip = false;
		hitAMiss = false;
	}
	
	public CommonAI(int cols, int rows) throws BattleshipException {
		super(cols, rows);
		directionsChecked = new boolean[4];
		for(int c = 0; c < 4; c++)
			directionsChecked[c] = true;
		sinkingShip = false;
		hitAMiss = false;
	}
	
	public abstract boolean takeTurn(BattleshipBoard playerBoard);
	
	public void sinkShip(BattleshipBoard playerBoard){
		boolean keepChecking = true;
		if(hitAMiss){
			lastRowFired = hitCheckRow;
			lastColFired = hitCheckCol;
		}
		hitAMiss = false;


		for(int c = 0; c < 4; c++){
			if(!directionsChecked[c] && keepChecking){
				keepChecking = false;
				if(c == 0 && lastColFired > 0)
					lastColFired -= 1;
				if(c == 1 && lastColFired < 9)
					lastColFired += 1;
				if(c == 2 && lastRowFired > 0)
					lastRowFired -= 1;
				if(c == 3 && lastRowFired < 9)
					lastRowFired += 1;
				if(spacesHit[lastColFired][lastRowFired]){
					directionsChecked[c] = true;
					lastRowFired = hitCheckRow;
					lastColFired = hitCheckCol;
					keepChecking = true;
				}
				if(!playerBoard.isOccupied(lastColFired, lastRowFired) && !keepChecking){
					directionsChecked[c] = true;
					hitAMiss = true;
				}
			}
		}
		
		if(keepChecking){
			System.out.println("notsinkinganymore");
			sinkingShip = false;
			lastRowFired = hitCheckRow;
			lastColFired = hitCheckCol;
		}
	}
	
	public void checkSinkShip(BattleshipBoard playerBoard){
		if(playerBoard.isOccupied(lastColFired, lastRowFired) && sinkingShip == false)
		{
			sinkingShip = true;
			hitCheckRow = lastRowFired;
			hitCheckCol = lastColFired;
			hitAMiss = false;
			for(int c = 0; c < 4; c++)
				directionsChecked[c] = false;
		}
	}
	
	public int getLastRow(){
		return lastRowFired;
	}
	
	public int getLastCol(){
		return lastColFired;
	}

}



