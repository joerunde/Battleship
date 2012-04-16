
import java.awt.Color;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 

import javax.swing.JButton; 
import javax.swing.JFrame; 
import javax.swing.JLabel;

public class BattleshipGui2D extends JFrame implements BattleshipGui{

	private static final int FRAME_WIDTH = 1055;
	private static final int FRAME_HEIGHT = 585;
	private FireButton fireButtons[][];
	private int buttonFired;
	private JLabel winnerLabel;
	
	public BattleshipGui2D(){
		int k, c, i;
		JButton background = new JButton();
		JLabel playerLabel, opponentLabel;
		playerLabel = new JLabel("Player's Board");
		opponentLabel = new JLabel("Opponent's Board");
		winnerLabel = new JLabel("");
		add(playerLabel);
		add(opponentLabel);
		add(winnerLabel);
		playerLabel.setBounds(0,0,200,20);
		opponentLabel.setBounds(540,0,200,20);
		winnerLabel.setBounds(450,520,500,20);
		
		FireButtonListener listener = new FireButtonListener();
		fireButtons = new FireButton[2][100];
		
		setSize(FRAME_WIDTH, FRAME_HEIGHT); 
		
		for(k = 0; k < 2; k++)
			for(i = 0; i < 10; i++)
				for(c = 0; c < 10; c++)
				{
					int buttonID = 10*c+i;
					fireButtons[k][buttonID] = new FireButton(c,i);
					fireButtons[k][buttonID].addActionListener(listener);
					fireButtons[k][buttonID].setActionCommand(""+buttonID);
					fireButtons[k][buttonID].setText("");
					add(fireButtons[k][buttonID]);
					fireButtons[k][buttonID].setBounds(i*50 + k*540, c*50+20, 50, 50);
					if(k == 0)
						fireButtons[k][buttonID].setEnabled(false);
				}

		add(background);
		background.setVisible(false);
		
		
	}
	
	public void setMessage(String message){
		winnerLabel.setText(message);
	}
	
	public void placeShip(int col, int row, boolean rotate, int ship){}
	
	public void setupBoard(BattleshipBoard board){
		int c, i;
		for(i = 0; i < 10; i++)
			for(c = 0; c < 10; c++)
				if(board.isOccupied(c, i))
					updateButton(c,i,2,0);
	}
	
	public void setButtonFired(int button){
		this.buttonFired = button;
	}
	
	public int getButtonFired(){
		return this.buttonFired;
	}
	
	public void setButtonsEnabled(boolean enabled, int side){
		int c;
		for(c = 0; c < 100; c++)
			fireButtons[side][c].setEnabled(enabled);
	}
	
	public void updateButton(int row, int col, int updateID, int boardID){
		int c;
		for(c = 0; c < 100; c++)
			if(fireButtons[boardID][c].getCell().getRow() == row && fireButtons[boardID][c].getCell().getColumn() == col)
			{
				if(updateID == 0){
					fireButtons[boardID][c].setText("M");
					fireButtons[boardID][c].setBackground(Color.blue);
				}
				if(updateID == 1){
					fireButtons[boardID][c].setText("H");
					fireButtons[boardID][c].setBackground(Color.yellow);
				}
				if(updateID == 2){
					fireButtons[boardID][c].setText("B");
					fireButtons[boardID][c].setBackground(Color.black);
				}
				if(updateID == 3){
					fireButtons[boardID][c].setText("");
					fireButtons[boardID][c].setBackground(Color.white);
				}
			}
	}
	
	private class FireButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			setButtonFired(Integer.parseInt(event.getActionCommand()));
			System.out.println("");
		}
		
	}
	
}
