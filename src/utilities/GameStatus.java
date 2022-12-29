package utilities;

public class GameStatus {

	private ModelSpace[][] board;
	
	private int gameStatus;
	
	public GameStatus(ModelSpace[][] board, int gameStatus) {
		this.board = board;
		this.gameStatus = gameStatus;
	}
	
	public int getGameStatus() {
		return gameStatus;
	}
	
	public ModelSpace[][] getBoard(){
		return board;
	}
	
}
