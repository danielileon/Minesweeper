package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ThreadLocalRandom;

import utilities.GameOverException;
import utilities.GameStatus;
import utilities.ModelSpace;

/*
 * @authors Andrew Waugaman, Daniel Leon, Ben Wunderlich, Gavin Pogson
 * 
 * This class represents a model of Minesweeper and checks if the user wins or
 * loses, reveals spaces and updates the view, and is serializable.
 */
public class MinesweeperModel extends Observable implements Serializable {

	/*
	 * This is the serial version ID of MinesweeperModel.
	 */
	private static final long serialVersionUID = -6182871967766931429L;

	/*This stores the board of the minesweeper game in the form of a 2D array of spaces. Mines are represented by null.*/
	private ModelSpace[][] board;
	
	/*This stores the view or views observing the model.*/
	private transient ArrayList<Observer> observers = new ArrayList<Observer>();
	
	/*These store the dimensions of the game board.*/
	private int rows;
	private int cols;
	
	// Keeps track of the status of the game. 1 indicated the game is still going, 2 indicates the user won, 3 indicates the user lost.
	private int gameStatus = 1;
	
	/*
	 * Constructs a MinesweeperModel. rows and cols define the dimensions of the game
	 * and spaceProportion is the proportion of spaces on the game board that aren't mines.
	 * 
	 * @param rows is the number of rows in the board.
	 * @param cols is the number of cols in the board.
	 * @param spaceProportion is a decimal that if multiplies by 100 gives the percentage of spaces without mines.
	 */
	public MinesweeperModel(int rows, int cols, double spaceProportion) {
		this.rows = rows;
		this.cols = cols;
		board = new ModelSpace[rows][cols];
		for (int spaces = 0; spaces < (rows*cols*spaceProportion); spaces++) {
			int row = ThreadLocalRandom.current().nextInt(0, rows);
			int col = ThreadLocalRandom.current().nextInt(0, cols);
			if (board[row][col] == null) {
				board[row][col] = new ModelSpace();
			} else {
				placeSpaceRecur(row, col);
			}
		}
	}
	
	/*
	 * This recursively attempts to place a space on the board. It randomly generates a number from -1 to 1
	 * for both row and col and adds both to the current row and col before attempting to place the space again.
	 * If it attempts placing off the board or rowOffset and colOffset are both 0 it instead chooses a random place
	 * and if it attempts to place a space where one exists it recurses.
	 * 
	 * @param row is the original row of the location that will be offset to find a new location.
	 * @param col is the original column that location will be offset to find a new location.
	 */
	private void placeSpaceRecur(int row, int col) {
		int rowOffset =ThreadLocalRandom.current().nextInt(-1, 2);
		int colOffset =ThreadLocalRandom.current().nextInt(-1, 2);
		if ((row+rowOffset) >= 0 && (col+colOffset) >= 0 && (row+rowOffset) < rows && (col+colOffset) < cols) {
			if (board[row+rowOffset][col+colOffset] == null) {
				board[row+rowOffset][col+colOffset] = new ModelSpace();
			} else {
				placeSpaceRecur(row+rowOffset, col+colOffset);
			}
		} else {
			placeSpaceRecur(row, col);
		}
	}

	/*
	 * This checks a space on the board and tells it how many mines are adjacent to it.
	 * 
	 * @param row is the row of the space that is checking for adjacent mines.
	 * @param col is the column of the space that is checking for adjacent mines.
	 */
	private void setMines(int row, int col) {
		if (row > 0 && col > 0) {
			if (board[row-1][col-1] == null) {
				board[row][col].addMine();
			}
		}
		if (row > 0 && col < cols-1) {
			if (board[row-1][col+1] == null) {
				board[row][col].addMine();
			}
		}
		if (row < rows-1 && col > 0) {
			if (board[row+1][col-1] == null) {
				board[row][col].addMine();
			}
		}
		if (row < rows-1 && col < cols-1) {
			if (board[row+1][col+1] == null) {
				board[row][col].addMine();
			}
		}
		if (row > 0) {
			if (board[row-1][col] == null) {
				board[row][col].addMine();
			}
		}
		if (col > 0) {
			if (board[row][col-1] == null) {
				board[row][col].addMine();
			}
		}
		if (row < rows-1) {
			if (board[row+1][col] == null) {
				board[row][col].addMine();
			}
		}
		if (col < cols-1) {
			if (board[row][col+1] == null) {
				board[row][col].addMine();
			}
		}
	}
	
	/*
	 * This reveals each space adjacent to the original space when called and recurses
	 * if any of the revealed spaces also have 0 mines.
	 * 
	 * @param row is the row of the original space.
	 * @param col is the column of the original space.
	 */
	private void revealRecur(int row, int col) {
		if (row > 0 && col > 0) {
			if (board[row-1][col-1] != null && board[row-1][col-1].isRevealed() == false) {
				board[row-1][col-1].reveal();
				if (board[row-1][col-1].getAdjacentMines() == 0) {
					revealRecur(row-1, col-1);
				}
			}
		}
		if (row > 0 && col < cols-1) {
			if (board[row-1][col+1] != null && board[row-1][col+1].isRevealed() == false) {
				board[row-1][col+1].reveal();
				if (board[row-1][col+1].getAdjacentMines() == 0) {
					revealRecur(row-1, col+1);
				}
			}
		}
		if (row < rows-1 && col > 0) {
			if (board[row+1][col-1] != null && board[row+1][col-1].isRevealed() == false) {
				board[row+1][col-1].reveal();
				if (board[row+1][col-1].getAdjacentMines() == 0) {
					revealRecur(row+1, col-1);
				}
			}
		}
		if (row < rows-1 && col < cols-1) {
			if (board[row+1][col+1] != null && board[row+1][col+1].isRevealed() == false) {
				board[row+1][col+1].reveal();
				if (board[row+1][col+1].getAdjacentMines() == 0) {
					revealRecur(row+1, col+1);
				}
			}
		}
		if (row > 0) {
			if (board[row-1][col] != null && board[row-1][col].isRevealed() == false) {
				board[row-1][col].reveal();
				if (board[row-1][col].getAdjacentMines() == 0) {
					revealRecur(row-1, col);
				}
			}
		}
		if (col > 0) {
			if (board[row][col-1] != null && board[row][col-1].isRevealed() == false) {
				board[row][col-1].reveal();
				if (board[row][col-1].getAdjacentMines() == 0) {
					revealRecur(row, col-1);
				}
			}
		}
		if (row < rows-1) {
			if (board[row+1][col] != null && board[row+1][col].isRevealed() == false) {
				board[row+1][col].reveal();
				if (board[row+1][col].getAdjacentMines() == 0) {
					revealRecur(row+1, col);
				}
			}
		}
		if (col < cols-1) {
			if (board[row][col+1] != null && board[row][col+1].isRevealed() == false) {
				board[row][col+1].reveal();
				if (board[row][col+1].getAdjacentMines() == 0) {
					revealRecur(row, col+1);
				}
			}
		}
	}
	
	/*
	 * This makes a move. If the game has already ended an exception gets thrown, otherwise
	 * it first checks if the user clicked a mine, then reveals the clicked space if it wasn't a mine
	 * and calls revealRecur if the space had no mines around it, then checks if the user has won
	 * the game by revealing every space, and finally updates observers.
	 * 
	 * @param row is the row that was checked.
	 * @param col is the column that was checked.
	 * @throws GameOverException if the game ended previously and the user somehow tried making another move.
	 */
	public void checkSpace(int row, int col) throws GameOverException {
		if (gameStatus != 1) {
			throw new GameOverException("Game has ended.");
		}
		if (board[row][col] == null) {
			gameStatus = 2;
		} else {
			board[row][col].reveal();
			if (board[row][col].getAdjacentMines() == 0) {
				revealRecur(row, col);
			}
		}
		boolean allSpacesRevealed = true;
		for (int rowIterator = 0; rowIterator < rows; rowIterator++) {
			for (int colIterator = 0; colIterator < cols; colIterator++) {
				if (board[rowIterator][colIterator] != null && !(board[rowIterator][colIterator].isRevealed())) {
					allSpacesRevealed = false;
					break;
				}
			}
		}
		if (allSpacesRevealed) {
			gameStatus = 3;
		}
		updateObservers();
	}
	
	/*
	 * This is called when the player makes their first move. It replaces any mines that
	 * were on or directly adjacent to the space the user clicked first and makes sure
	 * they don't end up near the first space and then checks the space as normal.
	 * 
	 * @param row is the row that was checked.
	 * @param col is the column that was checked.
	 */
	public void checkFirstSpace(int row, int col) throws GameOverException{
		int minesToAdd = 0;
		if (board[row][col] == null) {
			minesToAdd++;
			board[row][col] = new ModelSpace();
		}
		if (row > 0 && col > 0) {
			if (board[row-1][col-1] == null) {
				board[row-1][col-1] = new ModelSpace();
				minesToAdd++;
			}
		}
		if (row > 0 && col < cols-1) {
			if (board[row-1][col+1] == null) {
				board[row-1][col+1] = new ModelSpace();
				minesToAdd++;
			}
		}
		if (row < rows-1 && col > 0) {
			if (board[row+1][col-1] == null) {
				board[row+1][col-1] = new ModelSpace();
				minesToAdd++;
			}
		}
		if (row < rows-1 && col < cols-1) {
			if (board[row+1][col+1] == null) {
				board[row+1][col+1] = new ModelSpace();
				minesToAdd++;
			}
		}
		if (row > 0) {
			if (board[row-1][col] == null) {
				board[row-1][col] = new ModelSpace();
				minesToAdd++;
			}
		}
		if (col > 0) {
			if (board[row][col-1] == null) {
				board[row][col-1] = new ModelSpace();
				minesToAdd++;
			}
		}
		if (row < rows-1) {
			if (board[row+1][col] == null) {
				board[row+1][col] = new ModelSpace();
				minesToAdd++;
			}
		}
		if (col < cols-1) {
			if (board[row][col+1] == null) {
				board[row][col+1] = new ModelSpace();
				minesToAdd++;
			}
		}
		addMines(minesToAdd, row, col);
		for (int rowIterator = 0; rowIterator < rows; rowIterator++) {
			for (int colIterator = 0; colIterator < cols; colIterator++) {
				if (board[rowIterator][colIterator] != null) {
					setMines(rowIterator, colIterator);
				}
			}
		}
		checkSpace(row, col);
	}

	/*
	 * This adds any mines that were removed by the player's first turn.
	 * @param firstRow is the row that the user clicked first.
	 * @param firstCol is the column that the user clicked first
	 */
	private void addMines(int minesToAdd, int firstRow, int firstCol) {
		int addedMines = 0;
		while (addedMines < minesToAdd) {
			int row = ThreadLocalRandom.current().nextInt(0,rows);
			int col = ThreadLocalRandom.current().nextInt(0, cols);
			if ((Math.abs(row-firstRow)+Math.abs(col-firstCol)) > 2 && board[row][col] != null) {
				board[row][col] = null;
				addedMines++;
			}
		}
	}

	/*
	 * This adds an observer to the list of observers.
	 * 
	 * @param observer is the observer to add.
	 */
	public void addObserver(Observer observer) {
		if (observers.contains(observer) == false)
			observers.add(observer);
	}
	
	/*
	 * This returns the board and it's used for loading games and updating the view.
	 * 
	 * @returns the board.
	 */
	public ModelSpace[][] getBoard(){
		return board;
	}
	
	/*
	 * This is used to change the board and dimensions in a model when a game is loaded.
	 * 
	 * @param board is the board to set.
	 * @param rows is the number of rows to set.
	 * @param cols is the number of columns to set.
	 */
	public void loadBoard(ModelSpace[][] board, int rows, int cols) {
		this.board = board;
		this.rows = rows;
		this.cols = cols;
		gameStatus = 1;
	}
	
	/*
	 * This updates any views observing the model and passes that status of the game.
	 */
	public void updateObservers() {
		GameStatus status = new GameStatus(board, gameStatus);
		for (Observer observer : observers) {
			observer.update(this, status);
		}
	}
	
}
