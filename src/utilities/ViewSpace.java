package utilities;

import javafx.scene.layout.StackPane;

public class ViewSpace extends StackPane {
	
	private int row;
	private int col;
	
	public ViewSpace(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
}
