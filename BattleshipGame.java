import java.io.File;
import java.io.FileNotFoundException;

public class BattleshipGame {

	private Player player;
	private CommonAI opponent;
	private BattleshipGui gui;
	private int rowFired, colFired;
	private int playerShipCounter, oppShipCounter;
	
	public BattleshipGame(String[] args){
		
		if(args[3].startsWith("2"))
			gui = new BattleshipGui2D();
		else
			gui = new BattleshipGui3D();
		
		gui.setVisible(true);
		new SimpleAudioPlayer("air-raid.wav");
		player = null;
		opponent = null;
		
		try{
			player = new Player(new File(args[0]));	
		}catch(FileNotFoundException e){
			try {
				player = new Player(10,10);
			} catch (BattleshipException e1) {
				System.out.println("Well this sucks...");
				System.exit(0);
			}
			playerSetUp();
		}
		
		try{
			if(args[2].startsWith("s"))
				opponent = new SystematicAI(new File(args[1]));
			else
				opponent = new RandomAI(new File(args[1]));	
			
		}catch(FileNotFoundException e){
			try{
				if(args[2].startsWith("s"))
					opponent = new SystematicAI(10,10);
				else
					opponent = new RandomAI(10,10);
			}catch(BattleshipException e1){
				System.out.println("Well this sucks...");
				System.exit(0);
			}
			gui.setMessage("Setting up opponent board.....");
			opponentSetUp();
			gui.setMessage("Finished placing opponent's ships.");
		}
			
		gui.setupBoard(player.getBoard());
		
		
	}
	
	public void playersTurn(){
		oppShipCounter = opponent.getBoard().getNumBattleshipsLeft();
		player.setTurn(true);
		while(player.getTurn()){
			
			gui.setButtonFired(-1);
			System.out.println("");
		
			while(player.getTurn()){
				System.out.print("");
				if(gui.getButtonFired() != -1){
					colFired = gui.getButtonFired() - (gui.getButtonFired()/10)*10;
					rowFired = gui.getButtonFired()/10;
					
					if(player.checkPlayerHitSquareTwice(colFired, rowFired)){
						break;
					}
					int rand = (int)(Math.random()*2);
					if(player.takeTurn(opponent.getBoard(), colFired, rowFired))
					{
						gui.updateButton(colFired, rowFired, 1, 1);
						if(oppShipCounter != opponent.getBoard().getNumBattleshipsLeft())
							new SimpleAudioPlayer("shipsunk.wav");
						else{
							if(rand == 0)
								new SimpleAudioPlayer("explosion-01.wav");
							else
								new SimpleAudioPlayer("explosion-02.wav");
						}
						
					}
					else{
						gui.updateButton(colFired, rowFired, 0, 1);
						if(rand == 0)
							new SimpleAudioPlayer("splash-01.wav");
						if(rand == 1)
							new SimpleAudioPlayer("splash-02.wav");					
					}
				}
			}
		}
		
	}
	
	public void opponentsTurn(){
		playerShipCounter = player.getBoard().getNumBattleshipsLeft();
		opponent.setTurn(true);
		if(opponent.getTurn())
		{
			if(opponent.takeTurn(player.getBoard()))
				gui.updateButton(opponent.getLastCol(), opponent.getLastRow(), 1, 0);
			else
				gui.updateButton(opponent.getLastCol(), opponent.getLastRow(), 0, 0);
		}
		if(playerShipCounter != player.getBoard().getNumBattleshipsLeft())
			new SimpleAudioPlayer("air-raid.wav");
	}
	
	public void setWinnerMessage(){
		if(player.checkLost()){
			gui.setMessage("The computer has won, sour.");
			new SimpleAudioPlayer("fail.wav");
		}
		else{
			gui.setMessage("The player has won!");
			new SimpleAudioPlayer("GG.wav");
		}
	}
	
	public boolean isGameOver()
	{
		if(player.checkLost() == false && opponent.checkLost() == false)
			return false;
		else
			return true;
	}
	
	private void playerSetUp(){
		gui.setButtonsEnabled(true, 0);
		gui.setButtonsEnabled(false, 1);
		boolean success, pushed;
		int col[] = new int[2];
		int row[] = new int[2];
		int shipMag = 0;
		for(int c = 0; c < 5; c++){
			success = false;
			while(!success){			
				for(int i = 0; i < 2; i++){
					gui.setButtonFired(-1);
					pushed = false;
					gui.setMessage(chooseMessage(c,i));
					while(!pushed){
						if(gui.getButtonFired() != -1){
							System.out.println("");
							col[i] = gui.getButtonFired() - (gui.getButtonFired()/10)*10;
							row[i] = gui.getButtonFired()/10;
							gui.updateButton(col[i], row[i], 1, 0);
							pushed = true;
						}
					}
				}
				gui.updateButton(col[0], row[0], 3, 0);
				gui.updateButton(col[1], row[1], 3, 0);
				if(col[1] == col[0])
					shipMag = Math.abs(row[1] - row[0]) + 1;
				if(row[1] == row[0])
					shipMag = Math.abs(col[1] - col[0]) + 1;
				if( (c == 0 && shipMag == 5) || (c == 1 && shipMag == 4) || 
						(c == 2 && shipMag == 3) ||(c == 3 && shipMag == 3) || 
						(c == 4 && shipMag == 2) ){				
					try{
						player.getBoard().placeShip(col[0], row[0], col[1], row[1]);
						success = true;
						if(col[0] == col[1])
							gui.placeShip(col[0], row[0], true, c);
						if(row[0] == row[1])
							gui.placeShip(col[0], row[0], false, c);
					}catch(BattleshipException e){
						success = false;
					}
				}
			}
			gui.setupBoard(player.getBoard());
		}
		gui.setButtonsEnabled(true, 1);
		gui.setButtonsEnabled(false, 0);
	}
	
	private void opponentSetUp(){
		boolean success;
		int col[] = new int[2];
		int row[] = new int[2];
		int shipMag = 0;
		for(int c = 0; c < 5; c++){
			success = false;
			while(!success){			
				for(int i = 0; i < 2; i++){	
					col[i] = (int)(Math.random()*10);
					row[i] = (int)(Math.random()*10);
				}
				if(col[1] == col[0])
					shipMag = Math.abs(row[1] - row[0]) + 1;
				if(row[1] == row[0])
					shipMag = Math.abs(col[1] - col[0]) + 1;
				if( (c == 0 && shipMag == 5) || (c == 1 && shipMag == 4) || 
						(c == 2 && shipMag == 3) ||(c == 3 && shipMag == 3) || 
						(c == 4 && shipMag == 2) ){				
					try{
						opponent.getBoard().placeShip(col[0], row[0], col[1], row[1]);
						success = true;
					}catch(BattleshipException e){
						success = false;
					}
				}
			}
		}
	}
	
	private String chooseMessage(int shipID, int startOrEnd){
		String message = "Select ";
		if(shipID == 0)
			message += "Aircraft Carrier(5) ";
		if(shipID == 1)
			message += "Battleship(4) ";
		if(shipID == 2)
			message += "Destroyer(3) ";
		if(shipID == 3)
			message += "Submarine(3) ";
		if(shipID == 4)
			message += "Frigate(2) ";
		
		if(startOrEnd == 0)
			message += "start position.";
		if(startOrEnd == 1)
			message += "end position.";
		
		return message;			
	}
	
}
