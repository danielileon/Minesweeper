/**
 * @author benwunderlich, gavinpogson, andrewwaugaman, danielleon
 *         this program creates the modal window for the new game containing a
 *         message that says if you won or lost, and a button the creates a new
 *         game.
 */

package view;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class newGameView {
	
	
	/**
	 * displays a modal letting the player create a new game.
	 * 
	 * displays a modal letting the player create a new game. the modal
	 * contains a label telling the player how the game ended and a button
	 * that will create a new game.
	 * 
	 * @param view
	 *  				MinesweeperView object
	 * @param status
	 *  				int representing the status of the game
	 */
	public static void display(MinesweeperView view, int status) {
		//creates window
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Game Over");
		
		//create label
		Label label = new Label("Game Over, You Lose!");
		if (status == 3) {
			label.setText("Congratulations, You Won!");
		}
		
		//creates button
		Button button = new Button("Play Again");
		
		//adds objects to layout
		VBox vBox = new VBox(15);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(label, button);
		
		//handles when button is pressed
		button.setOnAction(new EventHandler() {
		     public void handle(Event t) {
		    	 view.newGame();
		    	 window.close();
		     }
		 });
		
		//adds scene to the window
		Scene scene = new Scene(vBox, 200, 200);
		window.setScene(scene);
		window.showAndWait();
	}
}
