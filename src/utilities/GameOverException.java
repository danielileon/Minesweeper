package utilities;

public class GameOverException extends Exception {
	
	public GameOverException(String message) {
		super(message);
	}
	
	public String toString() {
		return getMessage();
	}
	
}
