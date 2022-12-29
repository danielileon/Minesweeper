/**
 * @author benwunderlich, gavinpogson, andrewwaugaman, danielleon
 *         this program creates the modal window for the customize color containing
 *         color pickers for each customizeable part like board and mine. these can 
 *         then be saved via a save button.
 */

package view;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomizeColorsView {
	private static Color boardColor;
	private static Color strokeColor;
	private static Color hoverColor;
	private static Color[] returnedArr = new Color[7];
	
	/**
	 * displays a modal letting the player customize colors.
	 * 
	 * displays a modal letting the player customize colors. the modal
	 * contains color pickers for each option and lets the user save the
	 * picks. these color picks are then sent back to MinesweeperView and
	 * used to update the gui.
	 * 
	 * @param currentBoardColor
	 *  				Color object representing the board
	 * @param currentStrokeColor
	 *  				Color object representing the stroke
	 * @param currentHoverColor
	 *  				Color object representing the hover
	 * @param currentBackgroundColor
	 *  				Color object representing the background
	 * @param currentFlagColor
	 *  				Color object representing the flag
	 * @param currentClickedColor
	 *  				Color object representing the current clicked
	 * @param currentScreenColor
	 *  				Color object representing the screen
	 * @return Color[]
	 *  				array of Colors
	 */
	public static Color[] display(Color currentBoardColor, Color currentStrokeColor, Color currentHoverColor, Color currentBackgroundColor, Color currentFlagColor, Color currentClickedColor, Color currentScreenColor) {
		
		//initializes default colors
		returnedArr[0] = currentBoardColor;
		returnedArr[1] = currentStrokeColor;
		returnedArr[2] = currentHoverColor;
		returnedArr[3] = currentBackgroundColor;
		returnedArr[4] = currentFlagColor;
		returnedArr[5] = currentClickedColor;
		returnedArr[6] = currentScreenColor;
		
		//creates window
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Customize Colors");
		
		//initializes board section
		HBox boardHBox = new HBox(10);
		boardHBox.setAlignment(Pos.CENTER);
		Label boardColorLabel = new Label("Board Color:          ");
		ColorPicker boardColorPicker = new ColorPicker(currentBoardColor);
		boardHBox.getChildren().addAll(boardColorLabel,boardColorPicker);
		
		//initializes stroke section
		HBox strokeHBox = new HBox(10);
		strokeHBox.setAlignment(Pos.CENTER);
		Label strokeColorLabel = new Label("Stroke Color:          ");
		ColorPicker strokeColorPicker = new ColorPicker(currentStrokeColor);
		strokeHBox.getChildren().addAll(strokeColorLabel,strokeColorPicker);
		
		//initializes hover section
		HBox hoverHBox = new HBox(10);
		hoverHBox.setAlignment(Pos.CENTER);
		Label hoverColorLabel = new Label("Hover Color:            ");
		ColorPicker hoverColorPicker = new ColorPicker(currentHoverColor);
		hoverHBox.getChildren().addAll(hoverColorLabel,hoverColorPicker);
		
		//initializes background section
		HBox backgroundHBox = new HBox(10);
		backgroundHBox.setAlignment(Pos.CENTER);
		Label backgroundColorLabel = new Label("Background Color:");
		ColorPicker backgroundColorPicker = new ColorPicker(currentBackgroundColor);
		backgroundHBox.getChildren().addAll(backgroundColorLabel, backgroundColorPicker);
		
		//initializes flag section
		HBox flagHBox = new HBox(10);
		flagHBox.setAlignment(Pos.CENTER);
		Label flagColorLabel = new Label("Flag Color:                ");
		ColorPicker flagColorPicker = new ColorPicker(currentFlagColor);
		flagHBox.getChildren().addAll(flagColorLabel, flagColorPicker);
		
		//initializes flag section
		HBox clickedHBox = new HBox(10);
		clickedHBox.setAlignment(Pos.CENTER);
		Label clickedColorLabel = new Label("Clicked Cell Color: ");
		ColorPicker clickedColorPicker = new ColorPicker(currentClickedColor);
		clickedHBox.getChildren().addAll(clickedColorLabel, clickedColorPicker);
		
		//initializes flag section
		HBox screenHBox = new HBox(10);
		screenHBox.setAlignment(Pos.CENTER);
		Label screenColorLabel = new Label("Screen Color:           ");
		ColorPicker screenColorPicker = new ColorPicker(currentScreenColor);
		screenHBox.getChildren().addAll(screenColorLabel, screenColorPicker);

		//handles when save button is clicked
		Button saveButton = new Button("Save Settings");
		saveButton.setOnAction(e -> {
			returnedArr[0] = boardColorPicker.getValue();
			returnedArr[1] = strokeColorPicker.getValue();
			returnedArr[2] = hoverColorPicker.getValue();
			returnedArr[3] = backgroundColorPicker.getValue();
			returnedArr[4] = flagColorPicker.getValue();
			returnedArr[5] = clickedColorPicker.getValue();
			returnedArr[6] = screenColorPicker.getValue();
			
			window.close();
		});
		
		//adds components to layout
		VBox layout = new VBox(15);
		layout.getChildren().addAll(boardHBox, strokeHBox, hoverHBox, backgroundHBox, flagHBox, clickedHBox,  screenHBox, saveButton);
		layout.setAlignment(Pos.CENTER);
		
		//adds scene to the window
		Scene scene = new Scene(layout, 350, 350);
		window.setScene(scene);
		window.showAndWait();
		
		return returnedArr;
	}
}
