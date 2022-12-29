/**
 * @author benwunderlich, gavinpogson, andrewwaugaman, danielleon
 *         this program creates the modal window for the change theme containing
 *         three themes representing the customizeable parts like board and mine.
 *         these themes are then saved via a save button.
 */

package view;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ChangeThemeView {
	private static Color[] returnedArr = new Color[4];
	private static Color tempBoardColor = null;
	private static Color tempStrokeColor = null;
	private static Color tempHoverColor = null;
	private static Color tempBackgroundColor = null;
	
	/**
	 * displays a modal letting the player choose a theme.
	 * 
	 * displays a modal letting the player choose a theme. the modal
	 * contains three buttons holding a different themes. these color 
	 * picks are then sent back to MinesweeperView and used to update the gui.
	 * 
	 * @param currentBoardColor
	 *  				Color object representing the board
	 * @param currentStrokeColor
	 *  				Color object representing the stroke
	 * @param currentHoverColor
	 *  				Color object representing the hover
	 * @param currentBackgroundColor
	 *  				Color object representing the background
	 * @return Color[]
	 *  				array of Colors
	 */
	@SuppressWarnings("unchecked")
	public static Color[] display(Color currentBoardColor, Color currentStrokeColor, Color currentHoverColor, Color currentBackgroundColor) {
		
		//initializes default colors
		tempBoardColor = currentBoardColor;
		tempStrokeColor = currentStrokeColor;
		tempHoverColor = currentHoverColor;
		tempBackgroundColor = currentBackgroundColor;
		returnedArr[0] = currentBoardColor;
		returnedArr[1] = currentStrokeColor;
		returnedArr[2] = currentHoverColor;
		returnedArr[3] = currentBackgroundColor;
		
		//creates window
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Change Theme");
		
		//theme one layout
		StackPane t1OutStack = new StackPane();
		Rectangle t1Rec1 = new Rectangle();
		t1Rec1.setWidth(265);
		t1Rec1.setHeight(60);
		t1Rec1.setArcWidth(30.0); 
		t1Rec1.setArcHeight(20.0);
		t1Rec1.setFill(Color.LIGHTGRAY);
		
		HBox t1HBox1 = new HBox(10);
		t1HBox1.setAlignment(Pos.CENTER);
		Label theme1 = new Label("Theme One");
		StackPane t1InStack = new StackPane();
		Rectangle t1Rec2 = new Rectangle();
		t1Rec2.setWidth(170);
		t1Rec2.setHeight(45);
		t1Rec2.setArcWidth(30.0); 
		t1Rec2.setArcHeight(20.0);
		t1Rec2.setFill(Color.DARKGRAY);
		
		HBox t1HBox2 = new HBox(10);
		t1HBox2.setAlignment(Pos.CENTER);
		
		Rectangle t1Rec3 = new Rectangle();
		t1Rec3.setWidth(30);
		t1Rec3.setHeight(30);
		t1Rec3.setFill(Color.web("0x7F58AF"));
		Rectangle t1Rec4 = new Rectangle();
		t1Rec4.setWidth(30);
		t1Rec4.setHeight(30);
		t1Rec4.setFill(Color.web("0x64C5EB"));
		Rectangle t1Rec5 = new Rectangle();
		t1Rec5.setWidth(30);
		t1Rec5.setHeight(30);
		t1Rec5.setFill(Color.web("0xE84D8A"));
		Rectangle t1Rec6 = new Rectangle();
		t1Rec6.setWidth(30);
		t1Rec6.setHeight(30);
		t1Rec6.setFill(Color.web("0xFEB326"));
		t1HBox2.getChildren().addAll(t1Rec3, t1Rec4, t1Rec5, t1Rec6);
		
		t1InStack.getChildren().addAll(t1Rec2, t1HBox2);
		t1HBox1.getChildren().addAll(theme1, t1InStack);
		
		t1OutStack.getChildren().addAll(t1Rec1, t1HBox1);

		//theme 2
		StackPane t2OutStack = new StackPane();
		Rectangle t2Rec1 = new Rectangle();
		t2Rec1.setWidth(265);
		t2Rec1.setHeight(60);
		t2Rec1.setArcWidth(30.0); 
		t2Rec1.setArcHeight(20.0);
		t2Rec1.setFill(Color.LIGHTGRAY);
				
		HBox t2HBox1 = new HBox(10);
		t2HBox1.setAlignment(Pos.CENTER);
		Label theme2 = new Label("Theme Two");
		StackPane t2InStack = new StackPane();
		Rectangle t2Rec2 = new Rectangle();
		t2Rec2.setWidth(170);
		t2Rec2.setHeight(45);
		t2Rec2.setArcWidth(30.0); 
		t2Rec2.setArcHeight(20.0);
		t2Rec2.setFill(Color.DARKGRAY);
				
		HBox t2HBox2 = new HBox(10);
		t2HBox2.setAlignment(Pos.CENTER);
		Rectangle t2Rec3 = new Rectangle();
		t2Rec3.setWidth(30);
		t2Rec3.setHeight(30);
		t2Rec3.setFill(Color.web("0x575A6C"));
		Rectangle t2Rec4 = new Rectangle();
		t2Rec4.setWidth(30);
		t2Rec4.setHeight(30);
		t2Rec4.setFill(Color.web("0xB4C540"));
		Rectangle t2Rec5 = new Rectangle();
		t2Rec5.setWidth(30);
		t2Rec5.setHeight(30);
		t2Rec5.setFill(Color.web("0xE0E2D2"));
		Rectangle t2Rec6 = new Rectangle();
		t2Rec6.setWidth(30);
		t2Rec6.setHeight(30);
		t2Rec6.setFill(Color.web("0x3686C9"));
		t2HBox2.getChildren().addAll(t2Rec3, t2Rec4, t2Rec5, t2Rec6);
				
		t2InStack.getChildren().addAll(t2Rec2, t2HBox2);
		t2HBox1.getChildren().addAll(theme2, t2InStack);
				
		t2OutStack.getChildren().addAll(t2Rec1, t2HBox1);
		
		
		//theme 3
		StackPane t3OutStack = new StackPane();
		Rectangle t3Rec1 = new Rectangle();
		t3Rec1.setWidth(265);
		t3Rec1.setHeight(60);
		t3Rec1.setArcWidth(30.0); 
		t3Rec1.setArcHeight(20.0);
		t3Rec1.setFill(Color.LIGHTGRAY);
				
		HBox t3HBox1 = new HBox(10);
		t3HBox1.setAlignment(Pos.CENTER);
		Label theme3 = new Label("Theme Three");
		StackPane t3InStack = new StackPane();
		Rectangle t3Rec2 = new Rectangle();
		t3Rec2.setWidth(170);
		t3Rec2.setHeight(45);
		t3Rec2.setArcWidth(30.0); 
		t3Rec2.setArcHeight(20.0);
		t3Rec2.setFill(Color.DARKGRAY);
		
		HBox t3HBox2 = new HBox(10);
		t3HBox2.setAlignment(Pos.CENTER);
		Rectangle t3Rec3 = new Rectangle();
		t3Rec3.setWidth(30);
		t3Rec3.setHeight(30);
		t3Rec3.setFill(Color.web("0x4535AA"));
		Rectangle t3Rec4 = new Rectangle();
		t3Rec4.setWidth(30);
		t3Rec4.setHeight(30);
		t3Rec4.setFill(Color.web("0xD6D1F5"));
		Rectangle t3Rec5 = new Rectangle();
		t3Rec5.setWidth(30);
		t3Rec5.setHeight(30);
		t3Rec5.setFill(Color.web("0xED639E"));
		Rectangle t3Rec6 = new Rectangle();
		t3Rec6.setWidth(30);
		t3Rec6.setHeight(30);
		t3Rec6.setFill(Color.web("0xB05CBA"));
		t3HBox2.getChildren().addAll(t3Rec3, t3Rec4, t3Rec5, t3Rec6);
				
		t3InStack.getChildren().addAll(t3Rec2, t3HBox2);
		t3HBox1.getChildren().addAll(theme3, t3InStack);
				
		t3OutStack.getChildren().addAll(t3Rec1, t3HBox1);
		
		//handle when mouse hovers theme one
		t1OutStack.setOnMouseEntered(new EventHandler() {
			public void handle(Event t) {
				if(t1Rec1.getStroke() == null) {
					t1Rec1.setFill(Color.WHITE);
				}
			}
		});
				
		//handle when mouse leaves theme one
		t1OutStack.setOnMouseExited(new EventHandler() {
			public void handle(Event t) {
				t1Rec1.setFill(Color.LIGHTGREY); 
			}
		});
				
		//handle when mouse clicks theme one
		t1OutStack.setOnMouseClicked(new EventHandler() {
			public void handle(Event t) {
				t1Rec1.setFill(Color.LIGHTGREY); 
				t1Rec1.setStroke(Color.BLACK);
				t2Rec1.setStroke(null);
				t3Rec1.setStroke(null);
				tempBoardColor = (Color) t1Rec3.getFill();
				tempStrokeColor = (Color) t1Rec4.getFill();
				tempHoverColor = (Color) t1Rec5.getFill();
				tempBackgroundColor = (Color) t1Rec6.getFill();
			}
		});
				
		//handle when mouse hovers theme two
		t2OutStack.setOnMouseEntered(new EventHandler() {
			public void handle(Event t) {
				if(t2Rec1.getStroke() == null) {
					t2Rec1.setFill(Color.WHITE); }
				}
		});
				
		//handle when mouse leaves theme two
		t2OutStack.setOnMouseExited(new EventHandler() {
			public void handle(Event t) {
				t2Rec1.setFill(Color.LIGHTGREY); 
			}
		});
				
		//handle when mouse clicks theme two
		t2OutStack.setOnMouseClicked(new EventHandler() {
			public void handle(Event t) {
				t2Rec1.setFill(Color.LIGHTGREY); 
				t1Rec1.setStroke(null);
				t2Rec1.setStroke(Color.BLACK);
				t3Rec1.setStroke(null);
				tempBoardColor = (Color) t2Rec3.getFill();
				tempStrokeColor = (Color) t2Rec4.getFill();
				tempHoverColor = (Color) t2Rec5.getFill();
				tempBackgroundColor = (Color) t2Rec6.getFill();
			}
		});
		
		//handle when mouse hovers theme three
		t3OutStack.setOnMouseEntered(new EventHandler() {
			public void handle(Event t) {
				if(t3Rec1.getStroke() == null) {
				    t3Rec1.setFill(Color.WHITE); }
				 }
			});
				
		//handle when mouse leaves theme three
		t3OutStack.setOnMouseExited(new EventHandler() {
			public void handle(Event t) {
				t3Rec1.setFill(Color.LIGHTGREY); 
			}
		});
				
		//handle when mouse clicks theme three
		t3OutStack.setOnMouseClicked(new EventHandler() {
			 public void handle(Event t) {
				  t3Rec1.setFill(Color.LIGHTGREY); 
				  t1Rec1.setStroke(null);
				  t2Rec1.setStroke(null);
				  t3Rec1.setStroke(Color.BLACK);
				  tempBoardColor = (Color) t3Rec3.getFill();
				  tempStrokeColor = (Color) t3Rec4.getFill();
				  tempHoverColor = (Color) t3Rec5.getFill();
				  tempBackgroundColor = (Color) t3Rec6.getFill();
			}
		});
				
		//handles when save button is clicked
		Button saveButton = new Button("Save Settings");
		saveButton.setOnAction(e -> {
			returnedArr[0] = tempBoardColor;
			returnedArr[1] = tempStrokeColor;
			returnedArr[2] = tempHoverColor;
			returnedArr[3] = tempBackgroundColor;
			window.close();
		});
		
		//adds component to layout
		VBox layout = new VBox(10);
		layout.getChildren().addAll(t1OutStack, t2OutStack, t3OutStack, saveButton);
		layout.setAlignment(Pos.CENTER);
		
		//adds theme to window
		Scene scene = new Scene(layout, 300, 300);
		window.setScene(scene);
		window.showAndWait();
		return returnedArr;
	}
}
