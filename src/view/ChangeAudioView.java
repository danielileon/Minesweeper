/**
 * @author benwunderlich, gavinpogson, andrewwaugaman, danielleon
 *         this program creates the modal window for the audio picker containing a
 *         message that says the current audio file name, and a button that saves a
 *         new mp3 file to be used as the audio in the game.
 */

package view;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ChangeAudioView {
	static File file;
	
	/**
	 * displays a modal letting the player upload an audio.
	 * 
	 * displays a modal letting the player upload an audio. the modal
	 * contains a label telling the player the current audio file and a 
	 * button that will save a new audio file.
	 * 
	 * @param currentFile
	 *  				mp3 file representing the current audio
	 * @return File
	 *  				mp3 file representing the user chosen audio
	 */
	public static File display(File currentFile) {
		
		//creates window
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Change Audio");
		
		//creates label
		Label label = new Label();
		
		//updates label depending on the file name
		if(currentFile == null) {
			label.setText("Current Audio File: None");
		} else {
			label.setText("Current Audio File: " + currentFile.getName());
		}
		
		//creates FileChooser
		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("MP3 files (*.mp3)", "*.mp3"));
		fileChooser.setTitle("Open File");
		
		//handles saveButton being clicked
		Button saveButton = new Button("Open File");
		saveButton.setOnAction(e -> {
			//gets file from fileChooser
			file = fileChooser.showOpenDialog(window);
			label.setText("Current Audio File: " + file.getName());
		});
		
		//adds component to layout
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, saveButton);
		layout.setAlignment(Pos.CENTER);
		
		//adds scene to the window
		Scene scene = new Scene(layout, 200, 200);
		window.setScene(scene);
		window.showAndWait();
		return file;
	}
}
