/**
 * @author benwunderlich, gavinpogson, andrewwaugaman, danielleon*/
package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Observable;
import java.util.Observer;

import org.junit.jupiter.api.Test;

import utilities.GameOverException;
import utilities.GameStatus;
import utilities.ModelSpace;

class ModelTests {

	/*
	* Tests if the model was successfully created.
	*/
	@Test
	void testModel1() {
		int rows = 18;
		int cols = 14;
		double spaceRatio = 0.8;
		MinesweeperModel model = new MinesweeperModel(rows, cols, spaceRatio);
		assert(model != null);
	}

	/*
	* Tests if the model successfully loaded a board.
	*/
	@Test
	void testModelContructor1() {
		int rows = 18;
		int cols = 14;
		ModelSpace[][] board = new ModelSpace[rows][cols];
		MinesweeperModel model = new MinesweeperModel(rows, cols, 0.8);
		model.loadBoard(board, rows, cols);
		ModelSpace[][] board2 = model.getBoard();
		assert(model != null && board == board2);
	}
	
	/*
	* Tests if the model correctly does a first move in every situation.
	*/
	@Test
	void testModeFirstMove() throws GameOverException {
		int rows = 18;
		int cols = 14; 
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				MinesweeperModel model = new MinesweeperModel(rows, cols, 0.8);
				model.checkFirstSpace(0, 0);
			}
		}
	}
	
	private class TestObserver implements Observer {

		public boolean updated = false;
		public int status;
		
		@Override
		public void update(Observable o, Object arg) {
			updated = true;
			GameStatus gameStatus = (GameStatus) arg;
			status = gameStatus.getGameStatus();
		}
		
	}
	
	/*
	* Tests if the model successfully adds an observer and doesn't add duplicate observers.
	*/
	@Test
	void testUpdate() throws GameOverException {
		TestObserver observer = new TestObserver();
		MinesweeperModel model = new MinesweeperModel(18, 14, 0.8);
		model.addObserver(observer);
		model.addObserver(observer);
		model.checkFirstSpace(5, 5);
		assert(observer.updated);
	}
	
	/*
	* Tests if the model correctly ends under certain conditions.
	*/
	@Test
	void testGameOver() throws GameOverException {
		TestObserver observer = new TestObserver();
		MinesweeperModel model = new MinesweeperModel(18, 14, 0.8);
		model.addObserver(observer);
		model.checkFirstSpace(5, 5);
		assert(observer.status == 1);
		ModelSpace[][] board = new ModelSpace[2][1];
		board[0][0] = null;
		board[1][0] = new ModelSpace();
		model.loadBoard(board, 2, 1);
		model.checkSpace(0, 0);
		assert(observer.status == 2);
		board[0][0] = new ModelSpace();
		model.loadBoard(board, 2, 1);
		model.checkSpace(0, 0);
		assert(observer.status == 3);
		assertThrows(GameOverException.class, () -> model.checkFirstSpace(0, 0));
	}
	
	/*
	* Tests if the model correctly replaces mines when they're overrided by the first turn.
	*/
	@Test
	void testMineReplacement() throws GameOverException {
		ModelSpace[][] board = new ModelSpace[5][5];
		for (int i = 0; i < 5; i++) {
			board[0][i] = new ModelSpace();
			board[i][0] = new ModelSpace();
			board[4][i] = new ModelSpace();
			board[i][4] = new ModelSpace();
		}
		board[1][1] = null;
		board[1][2] = null;
		board[1][3] = null;
		board[2][1] = null;
		board[3][1] = null;
		board[2][3] = null;
		board[3][2] = null;
		board[3][3] = null;
		MinesweeperModel model = new MinesweeperModel(5, 5, 0.8);
		model.loadBoard(board, 5, 5);
		model.checkFirstSpace(2, 2);
		assert(board[1][1] != null);
		assert(board[1][2] != null);
		assert(board[1][3] != null);
		assert(board[2][1] != null);
		assert(board[3][1] != null);
		assert(board[2][3] != null);
		assert(board[3][2] != null);
		assert(board[3][3] != null);
	}
	
}
