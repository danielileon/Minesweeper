package controller;

import model.MinesweeperModel;
import utilities.GameOverException;
import utilities.ModelSpace;

/*
 * @authors Andrew Waugaman, Daniel Leon, Ben Wunderlich, Gavin Pogson
 * 
 * This class represents a controller for Minesweeper. It mainly just relays moves from the view to the model and it can switch which model it's targeting.
 * It also keeps track of whether or not it's the first turn.
 */
public class MinesweeperController {

	/*This is a reference to the model that the controller is targeting.*/
	private MinesweeperModel model;

	/*This keeps track of whether or not it's the first turn.*/
	private boolean firstTurn;
	
	/*These are the dimensions of the board and they're used to verify moves.*/
	private int rows;
	private int cols;
	
	/*
	 * This verifies that a move is valid and calls checkFirstSpace if it's the first turn or checkSpace otherwise.
	 * 
	 * @param row is the row that was checked.
	 * @param col is the column that was checked.
	 * @throws GameOverException if the game ended with a previous move.
	 */
	public void checkSpace(int row, int col) throws GameOverException {
		if (row >= rows || row < 0 || col > cols || col < 0) {} else {
			if (firstTurn) {
				model.checkFirstSpace(row, col);
				firstTurn = false;
			} else {
				model.checkSpace(row, col);
			}
		}
	}
	
	/*
	 * This sets the dimensions for the board along with the model that the controller is targeting and
	 * sets firstTurn to true because this is used for new games.
	 * 
	 * @param model is the model that the controller will target.
	 * @param rows is the number of rows in the board.
	 * @param cols is the number of columns in the board.
	 */
	public void setBoard(MinesweeperModel model, int rows, int cols) {
		this.model = model;
		this.rows = rows;
		this.cols = cols;
		firstTurn = true;
	}
	
	/*
	 * This loads an existing board into the current model, sets the numbers of rows and columns, and sets
	 * firstTurn to false because this is used for loading existing games.
	 * 
	 * @param board is the board to load.
	 * @param rows is the number of rows in the board.
	 * @param cols is the number of columns in the board.
	 */
	public void loadBoard(ModelSpace[][] board, int rows, int cols) {
		model.loadBoard(board, rows, cols);
		this.rows = rows;
		this.cols = cols;
		firstTurn = false;
	}
	
	/*
	 * This tells the model is update observers and is called when a game is loaded.
	 */
	public void update() {
		model.updateObservers();
	}
	
}
