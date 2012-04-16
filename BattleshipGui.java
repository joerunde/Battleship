
public interface BattleshipGui {
	
	public void setMessage(String message);
	public void placeShip(int col, int row, boolean rotate, int ship);
	public void setupBoard(BattleshipBoard board);
	public void setButtonFired(int button);
	public int getButtonFired();
	public void setButtonsEnabled(boolean enabled, int side);
	public void updateButton(int row, int col, int updateID, int boardID);
	public void setVisible(boolean visible);
	
}
