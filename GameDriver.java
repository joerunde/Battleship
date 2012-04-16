/*
 * Joe Runde
 * CS 162 Assn 3
 * 
 * Sounds implemented with a slight modification of Matthias Pfisterer's SimpleAusioPlayer class from jsresources.org
 */


public class GameDriver { 
	
	public static void main(String args[]){
		
		BattleshipGame game = null;
		if(args.length == 4){
			game = new BattleshipGame(args);
		}
		else
		{
			System.out.println("Error: expected four arguments, please specify how many dimensions you wish to play in");
		}
		 
		while(!game.isGameOver()){
			System.out.println("");
			game.playersTurn();
			if(!game.isGameOver())
				game.opponentsTurn();
		}
		game.setWinnerMessage();
	}

}
