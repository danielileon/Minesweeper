package utilities;

import java.io.Serializable;

public class ModelSpace implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8999593141854752695L;

	private int adjacentMines;
	
	private boolean revealed = false;
	
	// This is for when the model is making spaces without knowing how many mines are next to it.
	public ModelSpace() {}
	
	public ModelSpace(int mines) {
		adjacentMines = mines;
	}
	
	public void addMine () {
		adjacentMines++;
	}
	
	public void reveal () {
		revealed = true;
	}
	
	public boolean isRevealed() {
		return revealed;
	}
	
	public int getAdjacentMines() {
		return adjacentMines;
	}
	
}
