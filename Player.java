import java.io.File;
import java.io.FileNotFoundException;

public class Player {
	private BattleshipBoard board;
	private boolean turn;
	protected boolean spacesHit[][];
	
	public Player(File boardFile) throws FileNotFoundException{
		board = new BattleshipBoard(boardFile);
		this.turn = false;
		spacesHit = new boolean[10][10];
		for(int c = 0; c < 10; c++)
			for(int i = 0; i < 10; i++)
				spacesHit[c][i] = false;
	}
	
	public Player(int cols, int rows) throws BattleshipException{
		board = new BattleshipBoard(cols, rows);
		this.turn = false;
		spacesHit = new boolean[10][10];
		for(int c = 0; c < 10; c++)
			for(int i = 0; i < 10; i++)
				spacesHit[c][i] = false;
	}
	
	public BattleshipBoard getBoard(){
		return this.board;
	}
	
	public boolean takeTurn(BattleshipBoard oppBoard, int col, int row)
	{
		spacesHit[col][row] = true;
		this.turn = false;
		return oppBoard.fireShot(col, row);
	}
	
	public void setTurn(boolean turn){
		this.turn = turn;
	}
	
	public boolean getTurn(){
		return this.turn;
	}
	
	public boolean checkLost(){
		return this.board.isGameOver();
	}
	
	public boolean checkPlayerHitSquareTwice(int col, int row){
		if(spacesHit[col][row])
			return true;
		else
			return false;
	}
}
