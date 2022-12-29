/**
 * @author benwunderlich, gavinpogson, andrewwaugaman, danielleon
 *         this program creates the main view for the minesweeper program and its three
 *         main subsections: the menu, the status bar, and the view. within each subsection,
 *         the code includes the event handlers required for each object. this view also serves
 *         as a observer to the model.
 */

package view;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import controller.MinesweeperController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.stage.FileChooser.ExtensionFilter;
import model.MinesweeperModel;
import utilities.GameOverException;
import utilities.GameStatus;
import utilities.ModelSpace;
import utilities.ViewSpace;

import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;

public class MinesweeperView extends Application implements Observer {
	//GUI VISUALS
	
	//layout
	private static VBox layout = new VBox(30);
	private static MenuBar menu = new MenuBar();
	private static StackPane pauseStack = new StackPane();
	private static StackPane boardStack = new StackPane();
	private static StackPane[][] arr;
	private Set<StackPane> mines = new HashSet<>();
	
	//colors
	private static Color boardColor = Color.GREY;
	private static Color clickedBoardColor = Color.LAVENDER;
	private static Color flagColor = Color.RED; 
	private static Color hoverColor = Color.LIGHTGREY;
	private static Color outlineColor = Color.LIGHTGREY;
	private static Color strokeColor = Color.BLACK;
	private static Color mineColor = Color.ORANGERED;
	private static Color screenColor = Color.WHITE;
	
	//backgrounds
	private static Background boardBackground = new Background(new BackgroundFill(boardColor, null, null));
	private static Background clickedBoardBackground = new Background(new BackgroundFill(clickedBoardColor, null, null));
	private static Background flagBackground = new Background(new BackgroundFill(flagColor, null, null));
	private static Background hoverBackground = new Background(new BackgroundFill(hoverColor, null, null));
	private static Background mineBackground = new Background(new BackgroundFill(mineColor, null, null));
	private static Background screenBackground = new Background(new BackgroundFill(screenColor, null, null));
	private static Border strokeBorder = new Border(new BorderStroke(strokeColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
	
	//objects
	public static Rectangle pauseLeft;
	public static Rectangle pauseRight;
	public static Rectangle outsideTimerRectangle;
	public static Rectangle outsideFlagRectangle;
	public static Rectangle boardOutline;
	private static int flagsLeft;
    private static Label flagCount = new Label();
    private static ArrayList<Point> flagSpots = new ArrayList<>();
    
    
    //SETTINGS
    //model, view, and controller settings
  	private static MinesweeperModel model;
  	private MinesweeperView view = this;
  	private static MinesweeperController controller = new MinesweeperController();
  	private static volatile boolean addObserver = false;
	
	//board settings
	private static int rows = 18;
	private static int cols = 14;
	private static double spaceDensity = 0.8;
	
	//audio settings
	private static boolean audioOn = true;
	private static File audioFile;
	private static MediaPlayer mediaPlayer;
	private static Slider audioSlider;
	private static double currentVolume = 1;
	
	//timer settings
	private static int counter;
	private static Timer timer;
	private static Label timerLabel = new Label(Integer.toString(counter));
	private static boolean isGamePaused = true;
	private static Thread timerThread;
	private static boolean doesTimerExist = false;

	//game over animation settings
	private SequentialTransition transition = new SequentialTransition();
	
	//file settings
	static File location;
	
	/**
	 * creates the model and controller as well as the gui layout.
	 *
	 * creates the model and controller as well as the gui layout by calling initMenuBar,
	 * initStatusBar, and initBoard to make the 3 main components of the GUI. The scene
	 * holding this layout is then shown via the stage.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		
		//sets up model and connects it to the view
		model = new MinesweeperModel(rows, cols, 0.8);
		flagsLeft = (int) (rows * cols * (1-spaceDensity));
        model.addObserver(this);
        controller.setBoard(model, rows, cols);
       
        //creates layout and adds it to the stage
        MenuBar menu = initMenuBar();
        HBox status_bar = initStatusBar();
        layout.getChildren().addAll(menu, status_bar, initBoard(18, 14));
        Scene scene = new Scene(layout, 1100, 700);
        stage.setTitle("Minesweeper");
        stage.setScene(scene);
        stage.show();
        
        //adds observer
        AddObserverThread addObserver = new AddObserverThread();
        addObserver.start();
    }
	
	/**
	 * launches the start method.
	 * 
	 * @param args
	 * 				the arguments required by Application.
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * creates a new game by creating a new board and model.
	 *
	 * creates a new game by creating a new board and model. this model
	 * is then connected to the controller. arbitrary things like flag count
	 * and counter is updated to reflect the new game.
	 */
	public static void newGame() {
		
		//updates layout
		StackPane newBoard = initBoard(18, 14);
		layout.getChildren().remove(2);
		layout.getChildren().add(new Rectangle());
		layout.getChildren().set(2, newBoard);
		
		//creates new model and updates controller
		model = new MinesweeperModel(rows, cols, 0.8);
        controller.setBoard(model, rows, cols);
        addObserver = true;
        
        //updates flag
        flagsLeft = (int) (rows * cols * (1-spaceDensity));
        flagCount.setText(String.valueOf(flagsLeft));
        flagSpots.clear();
        
        //resets timer
        counter = 0;
        isGamePaused = true;
	}
	
	/**
	 * creates the gui and handles events for the board.
	 *
	 * creates the gui and handles events for the board by creating
	 * a GridPane holding StackPanes representing customized cells. event
	 * handling for hover and clicking also are present. this view also holds
	 * the timer and timer thread.
	 * 
	 * @param rows
	 * 				int representing the amount of rows
	 * @param cols
	 * 				int representing the amount of cows
	 * @return StackPane
	 * 				StackPane object holding the board
	 */
	public static StackPane initBoard(int rows, int cols) {
		
		//initializes base array
		arr = new StackPane[rows][cols];
	   
		//creates board layout
		GridPane board = new GridPane();
		board.setAlignment(Pos.CENTER);
		
		//creates each cell
		for(int x = 0; x < rows; x++) {
			for(int y = 0; y < cols; y++) {

				//adds number label
				Label label = new Label("");
				label.setFont(Font.font("Arial", FontWeight.BOLD, 25.0));
				ViewSpace space = new ViewSpace(x, y);
				
				//sets designs
				arr[x][y] = space;
				arr[x][y].setPrefSize(35, 35);
				arr[x][y].setBackground(boardBackground);
				arr[x][y].setBorder(strokeBorder);
				
				arr[x][y].getChildren().add(label);
				
				//creates variables to use within event handlers
				int a = x;
				int b = y;
				
				//handles if mouse enters cell
				arr[x][y].setOnMouseEntered(new EventHandler<MouseEvent>() {
		            @Override
		            public void handle(MouseEvent t) {
		            	//checks that cell hasn't been revealed, doesn't contain a flag, and is not a revealed mine
		            	if(arr[a][b].getBackground() != flagBackground && arr[a][b].getBackground() != clickedBoardBackground
		            			&& arr[a][b].getBackground() != mineBackground) {
		            		arr[a][b].setBackground(hoverBackground);
		            	}
		            }
		        });
				
				//handles if mouse leaves a cell
				arr[x][y].setOnMouseExited(new EventHandler<MouseEvent>() {
		            @Override
		            public void handle(MouseEvent t) {
		            	//checks that cell hasn't been revealed, doesn't contain a flag, and is not a revealed mine
		            	if(arr[a][b].getBackground() != flagBackground && arr[a][b].getBackground() != clickedBoardBackground
		            			&& arr[a][b].getBackground() != mineBackground) {
		            		arr[a][b].setBackground(boardBackground);
		            	}
		            }
		        });
				
				//handles if mouse clicks on a cell
				arr[x][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
			            @Override
			            public void handle(MouseEvent event) {
			               
			            	//creates button mouse button event
			            	MouseButton mouseButton = event.getButton();
			                
			                //handles if mouse left clicked and flag is not in cell
			                if(mouseButton == MouseButton.PRIMARY && arr[a][b].getBackground() != flagBackground){
			                	
			                	//checks space within controller
			                	isGamePaused = false;
			                	try {
									controller.checkSpace(space.getRow(), space.getCol());
								} catch (GameOverException e1) {
									Alert exceptionAlert = new Alert(AlertType.ERROR);
									exceptionAlert.setHeaderText("Error");
									exceptionAlert.setContentText(e1.getMessage());
									exceptionAlert.showAndWait();
								}
								
								//plays audio if set to on and audio file is available
								if(audioOn && audioFile != null) {
									
									//attempts to play the audio
									try {
										Media sound = new Media(audioFile.toURI().toString());
									    mediaPlayer = new MediaPlayer(sound);
									    mediaPlayer.setVolume(currentVolume);
									    mediaPlayer.play();
									    } catch (Exception e) {
									    	System.err.println(e.getMessage());
									    }
								}	  
						
							//handles if mouse right clicked and cell is not already clicked
			                } else if(mouseButton==MouseButton.SECONDARY && arr[a][b].getBackground() != clickedBoardBackground){

			                	//checks that flag isn't already in cell and there are flags left
			                	if(arr[a][b].getBackground() != flagBackground && flagsLeft > 0) {
			                		//changes to flag color
			                		arr[a][b].setBackground(flagBackground);
			                		
			                		//updates flags count
			                		flagsLeft -=1;
			                		flagCount.setText(String.valueOf(flagsLeft));
			                		flagSpots.add(new Point(a, b));
			                	
			                	//checks that flag is already in cell
			                	} else if (arr[a][b].getBackground() == flagBackground) {
			                		//changes flag color
			                		arr[a][b].setBackground(boardBackground);
			                		
			                		//updates flags count
			                		flagsLeft +=1;
			                		flagCount.setText(String.valueOf(flagsLeft));
			                		flagSpots.remove(new Point(a, b));
			                	}
			                }
			           }
				 });
			}
		}
		
		//adds each cell to the GridPane
	    for(int x = 0; x < rows; x++) {
	      	for(int y = 0; y < cols; y++) {
	      		board.addRow(y, arr[x][y]);
	      	}
	    }
	   
	    //creates board outline
	    boardOutline = new Rectangle((35 * rows) + rows + 25, (35 * cols) + cols + 25, outlineColor);
	    boardOutline.setArcWidth(20.0); 
	    boardOutline.setArcHeight(20.0);

	    //adds board and board outline to the layout
	    boardStack.getChildren().addAll(boardOutline, board);

	    //creates timer task
	    TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
            	//checks that game is not paused
            	if (!isGamePaused){
            		counter++;
            	}
            }
        };

        //creates thread to update timer
        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                        String timerText ;
                       
                        //checks if time is less than 60 seconds
                        if (counter%60 <10) {
                        	 timerText = "0"+Integer.toString(counter%60);}
                        else {
                        	 timerText = Integer.toString(counter%60);}
                        
                        //runs thread
                        Platform.runLater( () -> {timerLabel.setText(Integer.toString(counter/60)+" : "+timerText);});
                       
                        //sleeps one second
                        try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
                }
            }
        });
        if (!doesTimerExist) {
	        doesTimerExist = true;
	        timer = new Timer("MyTimer");
	        timer.scheduleAtFixedRate(timerTask, 30, 1000);
	        timerThread.start();
        }

	    return boardStack;
	}
	
	/**
	 * creates the gui MenuBar.
	 *
	 * creates the gui MenuBar and adds the play, customize, and settings
	 * sub menus. this method also holds event handling for when the menu items
	 * are clicked and dragged.
	 * 
	 * @return MenuBar
	 *  				MenuBar holding the menu
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static MenuBar initMenuBar() {
		
		//initializes menu
		menu = new MenuBar();
		
		//initializes play section
		Menu play = new Menu("Play");
		MenuItem newGame = new MenuItem("New Game");
		MenuItem loadGame = new MenuItem("Load Game");
		MenuItem saveGame = new MenuItem("Save Game");
		play.getItems().addAll(newGame, loadGame, saveGame);
		
		//initializes customize section
		Menu customize = new Menu("Customize");
		MenuItem changeTheme = new MenuItem("Change Theme");
		MenuItem customizeColors = new MenuItem("Customize Colors");
		MenuItem changeAudio = new MenuItem("Change Audio");
        customize.getItems().addAll(changeTheme, customizeColors, changeAudio);
       
        //initializes settings section
        Menu settings = new Menu("Settings");
        Menu toggleAudio = new Menu("Toggle Audio");
        CheckMenuItem audioTrue = new CheckMenuItem("Audio On");
        audioTrue.setSelected(true);
        CheckMenuItem audioFalse = new CheckMenuItem("Audio Off");
        toggleAudio.getItems().addAll(audioTrue, audioFalse);
        Menu changeVolume = new Menu("Change Volume");
        audioSlider = new Slider(0, 100, 100);
        MenuItem volume = new MenuItem();
        volume.setGraphic(audioSlider);
        changeVolume.getItems().add(volume);
        settings.getItems().addAll(toggleAudio, changeVolume);

		menu.getMenus().addAll(play, customize, settings);
        
		
		//handles when newGame is clicked
	    newGame.setOnAction(new EventHandler() {
	    	public void handle(Event t) {
	    		newGame();
			}
		});
		
		//handles when audioTrue is clicked
	    audioTrue.setOnAction(new EventHandler() {
		     public void handle(Event t) {
		    	 //turns audio on
		    	 audioOn = true;
		    	 
		    	 //updates gui
		    	 audioTrue.setSelected(true);
		    	 audioFalse.setSelected(false);
		     }
		 });
       
	    //handles when audioFalse is clicked
	    audioFalse.setOnAction(new EventHandler() {
		     public void handle(Event t) {
		    	 //turns audio off
		    	 audioOn = false;
		    	 
		    	 //updates gui
		    	 audioTrue.setSelected(false);
		    	 audioFalse.setSelected(true);
		     }
		 });
       
	    //handles when saveGame is clicked
	    saveGame.setOnAction(new EventHandler() {
		     public void handle(Event t) {
		    	 Stage window = new Stage();
				window.initModality(Modality.APPLICATION_MODAL);
				window.setTitle("Save Game");
				final DirectoryChooser directoryChooser = new DirectoryChooser();
				TextField text = new TextField();
				text.setPromptText("Enter Filename");
				Button locationButton = new Button("Choose File Location");
				locationButton.setOnAction(e -> {
					location = directoryChooser.showDialog(window);
				});
				Button saveButton = new Button("Save");
				saveButton.setOnAction(e -> {
					if (location != null) {
						try {
							String filePath = location.getAbsolutePath();
							File savedGame = new File(filePath + "\\" + text.getText() + ".msf");
							FileOutputStream fileOutput = new FileOutputStream(savedGame);
							ObjectOutputStream objectOutput;
							try {
								objectOutput = new ObjectOutputStream(fileOutput);
								objectOutput.writeObject(model);
								objectOutput.writeObject(flagSpots);
								fileOutput.write(rows);
								fileOutput.write(cols);
								fileOutput.write(flagsLeft);
								fileOutput.write(counter);
								fileOutput.close();
								objectOutput.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					} else {
						Alert saveAlert = new Alert(AlertType.ERROR);
						saveAlert.setHeaderText("Error");
						if (location == null) {
							saveAlert.setContentText("Choose a file to save in");
						} else {
							saveAlert.setContentText("Choose a name for your file");
						}
						saveAlert.showAndWait();
					}
				});
				VBox layout = new VBox(10);
				layout.getChildren().addAll(text, locationButton, saveButton);
				layout.setAlignment(Pos.CENTER);
				Scene scene = new Scene(layout, 300, 300);
				window.setScene(scene);
				window.showAndWait();
		     }
		 });
       

     //handles when loadGame is clicked
       loadGame.setOnAction(new EventHandler() {
		     public void handle(Event t) {
				final FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Minesweeper Save", "*.msf"));
				fileChooser.setTitle("Open File");
				File file;
				Stage window = new Stage();
				window.initModality(Modality.APPLICATION_MODAL);
				file = fileChooser.showOpenDialog(window);
				try {
					FileInputStream fileStream = new FileInputStream(file);
					ObjectInputStream objects = new ObjectInputStream(fileStream);
					layout.getChildren().set(2, initBoard(18, 14));
					MinesweeperModel model = (MinesweeperModel) objects.readObject();
					flagSpots = (ArrayList<Point>) objects.readObject();
					for (Point flag : flagSpots) {
						int row = (int) flag.getX();
						int col = (int) flag.getY();
						arr[row][col].setBackground(flagBackground);
					}
					rows = (int) fileStream.read();
					cols = (int) fileStream.read();
					flagsLeft = (int) fileStream.read();
					counter = (int) fileStream.read();
					controller.loadBoard(model.getBoard(), rows, cols);
					controller.update();
					isGamePaused = false;
					objects.close();
					fileStream.close();
				} catch (Exception e) {
					e.printStackTrace();
					}
		     }
		 });

       
	    //handles when audioSlider is dragged
	    audioSlider.setOnMouseDragged(new EventHandler() {
	    	@Override
	    	public void handle(Event arg0) {
			// TODO Auto-generated method stub
	    		currentVolume = (audioSlider.getValue() / 100);
		}
    	   
       });
       
	    //handles when audioSlider is clicked
	    audioSlider.setOnMouseClicked(new EventHandler() {
	    	@Override
	    	public void handle(Event arg0) {
	    	// TODO Auto-generated method stub
	    		currentVolume = (audioSlider.getValue() / 100);
	    	}
       	   
       });
       
        //handles when customizeColors is clicked
        customizeColors.setOnAction(new EventHandler() {
		     public void handle(Event t) {
		    	 
		    	 //keeps track of every clicked cell
		    	 ArrayList<Point> clickedSpots = new ArrayList<>();
		    	 for(int x = 0; x < rows; x++) {
			    	for(int y = 0; y < cols; y++) {
			    		if(arr[x][y].getBackground() == clickedBoardBackground) {
			    			clickedSpots.add(new Point(x, y));
			    		}
			    	}
			    }

		    	 //updates colors after function calls
		    	 Color customizationReturn[] = CustomizeColorsView.display(boardColor, strokeColor, hoverColor, outlineColor, flagColor, clickedBoardColor, screenColor);
		    	 boardColor = customizationReturn[0];
		    	 strokeColor = customizationReturn[1];
		    	 hoverColor = customizationReturn[2];
		    	 outlineColor = customizationReturn[3];
		    	 flagColor = customizationReturn[4];
		    	 clickedBoardColor = customizationReturn[5];
		    	 screenColor = customizationReturn[6];
		    	 
		    	 //updates backgrounds
		    	 boardBackground = new Background(new BackgroundFill(boardColor, null, null));
		    	 hoverBackground = new Background(new BackgroundFill(hoverColor, null, null));
		    	 strokeBorder = new Border(new BorderStroke(strokeColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
		    	 flagBackground = new Background(new BackgroundFill(flagColor, null, null));
		    	 clickedBoardBackground = new Background(new BackgroundFill(clickedBoardColor, null, null));
		    	 screenBackground = new Background(new BackgroundFill(screenColor, null, null));
		    	
		    	 //loops through every cell updating colors and backgrounds
		    	 for(int x = 0; x < rows; x++) {
		    		 for(int y = 0; y < cols; y++) {
		    			arr[x][y].setBorder(strokeBorder);
		    			if(flagSpots.contains(new Point(x, y))) {
		    				arr[x][y].setBackground(flagBackground);
		    			} else if(clickedSpots.contains(new Point(x, y))) {
		    				arr[x][y].setBackground(clickedBoardBackground);
		    			} else {
		    				arr[x][y].setBackground(boardBackground);
		    			}
		    		}
		    	}
		    	
		    	 //updates object colors
		    	 layout.setBackground(screenBackground);
		    	 menu.setBackground(screenBackground);
		    	 pauseLeft.setFill(outlineColor);
		    	 pauseRight.setFill(outlineColor);
		    	 outsideTimerRectangle.setFill(outlineColor);
		    	 outsideFlagRectangle.setFill(outlineColor);
		    	 boardOutline.setFill(outlineColor);
		     }
		 });
        
        //handles when changeTheme is clicked
        changeTheme.setOnAction(new EventHandler() {
		     public void handle(Event t) {
		    	 
		    	 //updates colors after function calls
		    	 Color themeReturn[] = ChangeThemeView.display(boardColor, strokeColor, hoverColor, outlineColor); 
		    	 boardColor = themeReturn[0];
		    	 strokeColor = themeReturn[1];
		    	 hoverColor = themeReturn[2];
		    	 outlineColor = themeReturn[3];
		    	
		    	 //updates backgrounds
		    	 boardBackground = new Background(new BackgroundFill(boardColor, null, null));
		    	 hoverBackground = new Background(new BackgroundFill(hoverColor, null, null));
		    	 strokeBorder = new Border(new BorderStroke(strokeColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
		    	
		    	//loops through every cell updating colors and backgrounds
		    	 for(int x = 0; x < rows; x++) {
		    		 for(int y = 0; y < cols; y++) {
		    			 arr[x][y].setBorder(strokeBorder);
		    			 if(arr[x][y].getBackground() != clickedBoardBackground && arr[x][y].getBackground() != flagBackground ) {
		    				arr[x][y].setBackground(boardBackground);
		    			 }
		    		 }
		    	 }

		    	//updates object colors
		    	 pauseLeft.setFill(outlineColor);
		    	 pauseRight.setFill(outlineColor);
		    	 outsideTimerRectangle.setFill(outlineColor);
		    	 outsideFlagRectangle.setFill(outlineColor);
		    	 boardOutline.setFill(outlineColor);
		     }
		 });
        
        //handles when changeAudio is clicked
        changeAudio.setOnAction(new EventHandler() {
		     public void handle(Event t) {
		    	 audioFile = ChangeAudioView.display(audioFile);
		     }
		 });
        
		return menu;
	}
	
	/**
	 * creates the gui HBox holding the status bar.
	 *
	 * creates the gui status bar and adds the pause objects, timer, and flag box
	 * features. this method also holds event handling for when the pause button is
	 * clicked.
	 * 
	 * @return HBox
	 *  				HBox holding the status bar
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HBox initStatusBar() {
		HBox statusBar = new HBox(100);
		statusBar.setAlignment(Pos.CENTER);
		
		//creates pause button
		HBox pauseHBox = new HBox(10);
		pauseHBox.setAlignment(Pos.CENTER);
        pauseLeft = new Rectangle(15,50,outlineColor);
        pauseRight = new Rectangle(15,50,outlineColor);
        pauseHBox.getChildren().addAll(pauseLeft, pauseRight);
        Group pauseGroup = new Group();
        
        //creates timer
        StackPane timerStackPane = new StackPane();
  		outsideTimerRectangle = new Rectangle(120,60,outlineColor);
  		outsideTimerRectangle.setArcWidth(30.0); 
  		outsideTimerRectangle.setArcHeight(20.0);
        Rectangle insideTimerRectangle = new Rectangle(105,45,Color.WHITE);
        insideTimerRectangle.setArcWidth(30.0); 
        insideTimerRectangle.setArcHeight(20.0);
        timerLabel = new Label("00:00");
        timerStackPane.getChildren().addAll(outsideTimerRectangle, insideTimerRectangle, timerLabel);
        
        //creates flag box
        StackPane flagStackPane = new StackPane();
  		outsideFlagRectangle = new Rectangle(120,60,outlineColor);
  		outsideFlagRectangle.setArcWidth(30.0); 
  		outsideFlagRectangle.setArcHeight(20.0);
        HBox flagHBox = new HBox(10);
        flagHBox.setAlignment(Pos.CENTER);
        StackPane internalFlagStackPane = new StackPane();
        Rectangle insideFlagRectangle = new Rectangle(60,45,Color.WHITE);
        insideFlagRectangle.setArcWidth(30.0); 
        insideFlagRectangle.setArcHeight(20.0);
        flagCount.setText(String.valueOf(flagsLeft));
        internalFlagStackPane .getChildren().addAll(insideFlagRectangle, flagCount);
        Label flags = new Label("Flags");
        flagHBox.getChildren().addAll(flags, internalFlagStackPane );
        flagStackPane.getChildren().addAll(outsideFlagRectangle, flagHBox);
        
  	    //adds components to status bar
        statusBar.getChildren().addAll(pauseHBox, timerStackPane, flagStackPane);
		
  	   //handles when the pause button is clicked
        pauseHBox.setOnMouseClicked(new EventHandler() {
 		     public void handle(Event t) {
 		    	
 		    	 //checks if not paused
 		        if(pauseHBox.getChildren().contains(pauseLeft)) {
 		           isGamePaused = true;
 		          
 		           //covers board
 		           Rectangle pauseRectangle = new Rectangle((35 * rows) + rows + 25, (35 * cols) + cols + 25, outlineColor);
 	 		       pauseRectangle.setArcWidth(20.0); 
 	 		       pauseRectangle.setArcHeight(20.0);
 	 		       Label pause = new Label("Game is Paused");
 	 		       pause.setFont(Font.font("Arial", FontWeight.BOLD, 50.0));
 	 		       pauseStack.getChildren().addAll(pauseRectangle, pause);
 	 		       boardStack.getChildren().add(pauseStack);
 	 		        
 	 		       //creates play button
 	 		       Polygon polygon= new Polygon();
 	 		       polygon.getPoints().addAll(new Double[]{
 	 		            0.0, 0.0,
 	 		            40.0, 25.0,
 	 		            0.0, 50.0 });
 	 		        polygon.setFill(outlineColor);
 	 		        
 	 		        pauseGroup.getChildren().add(polygon);
 	 		       
 	 		        pauseHBox.getChildren().removeAll(pauseLeft, pauseRight);
 	 		        pauseHBox.getChildren().add(pauseGroup);
 		        
 		        } else {
 		        	isGamePaused = false;
 		        	pauseHBox.getChildren().remove(pauseGroup);
 		        	
 		        	//creates pause button
 		        	pauseLeft = new Rectangle(15,50,outlineColor);
 		        	pauseRight = new Rectangle(15,50,outlineColor);
 		        	pauseHBox.getChildren().addAll(pauseLeft, pauseRight);
 		        	boardStack.getChildren().remove(pauseStack);
 		        }   
 		     }
 		     
 		 });
		
        return statusBar;
	}
  
	/**
	 * creates and runs the game over bomb animations.
	 *
	 * creates and runs the game over bomb animations by using
	 * transitions on the given spot.
	 * 
	 * @param spot
	 *  				StackPane representing a cell
	 */
	public void gameOverAnimation(StackPane spot) {
		Rectangle alert = new Rectangle(0.1, 0.1, mineColor);
		spot.getChildren().add(alert);
		
		Circle mine = new Circle(0.1, strokeColor);
		spot.getChildren().add(mine);
		
		ScaleTransition popUp = new ScaleTransition(Duration.millis(10), alert);
		popUp.setByX(330);
		popUp.setByY(330);
		popUp.setCycleCount(1);
		
		ScaleTransition scale = new ScaleTransition(Duration.millis(500), mine);
	    scale.setByX(100);
	    scale.setByY(100);
	    scale.setCycleCount(1);
	    
	    ScaleTransition revert = new ScaleTransition(Duration.millis(10), alert);
		revert.setFromX(1);
		revert.setFromY(1);
		revert.setToX(0.01);
		revert.setToY(0.01);
		revert.setCycleCount(1);
	    
	    transition.getChildren().add(popUp);
	    transition.getChildren().add(scale);
	    transition.getChildren().add(revert);
	}

	/**
	 * handles what occurs when observed object is updated.
	 *
	 * handles what occurs when observed object is updated. this tells
	 * what the gui to do in regards to the model being updated. it also handles
	 * when the game is over, whether its by win or loss, and call the animation to
	 * occur. finally the new game option is displayed.
	 * 
	 * @param o
	 *  				an observable object.
	 * @param arg
	 *   				an object representing a argument.
	 */	
	@Override
	public void update(Observable o, Object arg) {
		GameStatus gameStatus = (GameStatus) arg;
		ModelSpace[][] modelBoard = gameStatus.getBoard();
		int status = gameStatus.getGameStatus();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (status != 1 || (modelBoard[row][col] != null && modelBoard[row][col].isRevealed())) {
					arr[row][col].setOnMouseClicked(null);
					arr[row][col].setOnMouseEntered(null);
					arr[row][col].setOnMouseExited(null);
				}
				if (modelBoard[row][col] != null && modelBoard[row][col].isRevealed()) {
					if (modelBoard[row][col].getAdjacentMines() == 0) {
						if (arr[row][col].getBackground() == flagBackground) {
							flagsLeft += 1;
							flagSpots.remove(new Point(row, col));
						}
						arr[row][col].setBackground(clickedBoardBackground);
						arr[row][col].getChildren().set(0, new Label(""));
					} else {
						if (arr[row][col].getBackground() == flagBackground) {
							flagsLeft += 1;
							flagSpots.remove(new Point(row, col));
						}
						Label mines = new Label(((Integer) modelBoard[row][col].getAdjacentMines()).toString());
						mines.setFont(Font.font("Arial", FontWeight.BOLD, 25.0));
						arr[row][col].getChildren().set(0, mines);
						arr[row][col].setBackground(clickedBoardBackground);
					}
				} else {
					arr[row][col].getChildren().set(0, new Label(""));
					if (modelBoard[row][col] == null && status > 1) {
						mines.add(arr[row][col]);
						stop();
						if(arr[row][col].getBackground() == hoverBackground) {
							arr[row][col].setBackground(boardBackground);
						}
					}
				}
			}
		}
	    flagCount.setText(String.valueOf(flagsLeft));
		if(status > 1) {
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					if (arr[row][col].getBackground() == flagBackground  && modelBoard[row][col] == null) {
						arr[row][col].setBackground(boardBackground);
						flagSpots.remove(new Point(row, col));
					}
				}
			}
			for(StackPane mine: mines) {
				gameOverAnimation(mine);
			}
			transition.play();
			timerThread.stop();
			newGameView.display(this, status);
			mines = new HashSet<>();
			transition = new SequentialTransition();
		}
	}
	
	/**
	 * stops the timer and the timerThread.
	 */
	@Override
	public void stop(){
		timerThread.stop();
		timer.cancel();
	}
	
	/**
	 * creates a thread adding an observer to the model.
	 */
	private class AddObserverThread extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(100);
					if (addObserver) {
						model.addObserver(view);
						addObserver = false;
					}
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
		
	}
}
