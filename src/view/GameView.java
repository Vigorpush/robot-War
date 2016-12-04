package view;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.Game;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Board;
import model.Player;
import model.Robot;
/**
 * Class that creates the GUI for the actual game and all book keeping needed
 * for that class TODO Make list populate TODO update as game changes TODO
 * Populate table TODO display different things for different users
 * 
 * @author Niklaas
 *
 */
@SuppressWarnings("unused")
public class GameView {
	private static final String title = "Robot Wars";
	private Scene gameScene;
	// Stores the list of players for easy access and modification
	private ArrayList<String> playerList;
	// Store the playerList in a format readable by the ListView
	private ObservableList<String> playerObsList;
	// Displays the playerList
	private ListView<String> playerListView;
	// Stores the list of observers for easy access and modification
	private ArrayList<String> observerList;
	// Store the observerList in a format readable by the ListView
	private ObservableList<String> observerObsList;
	// Displays the observerList
	private ListView<String> observerListView;
	// Table that displays the current health of all the players robots
	private TableView<String> tankHealthTable;
	private Label currentTankMoveLabel;
	private Label currentTurnLabel;
	// Currently determines distances between center of each hexagon
	// TODO WIDTH and HEIGHT variables to control size of hexagons
	private double width = 100;// 51.96
	private static final double OFFSET = 15;
	// The number of heaxagons per side of the grid
	private int sideLength = 5;

	private ImageView[] robotImages = new ImageView[18];

	private BorderPane hexBox;
	private Polyline[][] hexagonArray;
	private String onClick = "INSPECT";
	TableViewController tc = new TableViewController();
	
    public static final String Column1MapKey = "Scout";
    public static final String Column2MapKey = "Sniper";
    public static final String Column3MapKey = "Tank";
	public static String playerName;
    private Button moveButton = new Button("Move");
	private Button attackButton = new Button("Attack");
	private Button inspectButton = new Button("Inspect");
	private Color[] numberColors = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.PURPLE, Color.ORANGE, Color.BLACK};

	/**
	 * Method for creating the GameView Scene
	 * 
	 * @return the gameScene
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Scene init(ArrayList<String> playerList, ArrayList<String> observerList, String playerName) {
		this.playerName = playerName;
		int playerCount = playerList.size();
		// Sets side length to 7 if there are six players
		if (playerCount == 6) {
			sideLength = 7;
		}
		width = (Screen.getPrimary().getVisualBounds().getHeight()/(sideLength*2-1))*0.9;
		hexagonArray = new Polyline[sideLength * 2 - 1][sideLength * 2 - 1];
		HBox gameScreen = new HBox(10);// 355
		// Left side of window
		VBox leftBox = new VBox(30);
		Button backBtn = new Button("Back");
		//adding staff
        final Label label = new Label("DashBoard");
        label.setFont(new Font("Arial", 20));
 
        TableColumn<Map, String> firstDataColumn = new TableColumn<>("Scout");
        TableColumn<Map, String> secondDataColumn = new TableColumn<>("Sniper");
        TableColumn<Map, String> thirdDataColumn = new TableColumn<>("Tank");
        firstDataColumn.setCellValueFactory(new MapValueFactory(Column1MapKey));
        firstDataColumn.setMinWidth(100);
        secondDataColumn.setCellValueFactory(new MapValueFactory(Column2MapKey));
        secondDataColumn.setMinWidth(100);
        thirdDataColumn.setCellValueFactory(new MapValueFactory(Column3MapKey));
        thirdDataColumn.setMinWidth(100);
       
 
        final VBox vbox = new VBox();
 
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, tc.robotHealthTable);
        
        //end of adding staff
		currentTankMoveLabel = new Label("Scouts Move: 3/3");

		moveButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				onClick = "MOVE";
				moveButton.getStyleClass().add("clicked_button");
				attackButton.getStyleClass().remove("clicked_button");
				inspectButton.getStyleClass().remove("clicked_button");
			}
		});
		attackButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				onClick = "ATTACK";
				attackButton.getStyleClass().add("clicked_button");
				moveButton.getStyleClass().remove("clicked_button");
				inspectButton.getStyleClass().remove("clicked_button");
			}
		});
		inspectButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				onClick = "INSPECT";
				inspectButton.getStyleClass().add("clicked_button");
				attackButton.getStyleClass().remove("clicked_button");
				moveButton.getStyleClass().remove("clicked_button");
			}
		});
		Button endTurnBtn = new Button("End Turn");
		endTurnBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Game controller = new Game();
				controller.endTurn();
			}
		});
		leftBox.getChildren().addAll(backBtn, vbox, currentTankMoveLabel, moveButton, attackButton,
				inspectButton, endTurnBtn);
		leftBox.setPadding(new Insets(0, 30, 0, 30));
		leftBox.setAlignment(Pos.TOP_LEFT);
		// Center of window
		VBox centerBox = new VBox(60);
		// TODO make reflect current turn
		currentTurnLabel = new Label(playerList.get(0) + " turn");
		currentTurnLabel.setAlignment(Pos.CENTER);
		currentTurnLabel.setFont(new Font(22));
		centerBox.getChildren().addAll(currentTurnLabel, generateBoard());
		centerBox.setMinWidth(width * (sideLength * 2 - 1)+OFFSET*2);
		centerBox.setAlignment(Pos.TOP_CENTER);
		// Right side of window
		VBox rightBox = new VBox(30);
		rightBox.setPadding(new Insets(0, 30, 0, 30));
		Label playerListLabel = new Label("Players");
		playerListView = new ListView<String>();
		playerList = new ArrayList<String>();
		playerObsList = FXCollections.observableArrayList(playerList);
		playerListView.setItems(playerObsList);
		Label observerListLabel = new Label("Observers");
		observerListView = new ListView<String>();
		observerList = new ArrayList<String>();
		observerObsList = FXCollections.observableArrayList(observerList);
		observerListView.setItems(observerObsList);
		rightBox.setAlignment(Pos.TOP_RIGHT);
		// TODO quit button for observers
		Button forfeit = new Button("Forfeit");
		rightBox.getChildren().addAll(playerListLabel, playerListView, observerListLabel, observerListView, forfeit);
		gameScreen.getChildren().addAll(leftBox, centerBox, rightBox);
		// Sets margin to give the board room to be seen
		//VBox.setMargin(centerBox, new Insets(5, 5, 5, 5));
		gameScreen.setAlignment(Pos.CENTER);
		gameScene = new Scene(gameScreen);
		
		File file = new File("Resources/css/GameView.css");
		gameScene.getStylesheets().add("file:///" + file.getAbsolutePath().replace("\\", "/"));

		currentTurnLabel.getStyleClass().add("text_label");
		currentTankMoveLabel.getStyleClass().add("text_label");
		playerListLabel.getStyleClass().add("text_label");
		observerListLabel.getStyleClass().add("text_label");
		backBtn.getStyleClass().add("cancelbutton");
		currentTurnLabel.getStyleClass().add("centred_label");
		moveButton.getStyleClass().add("button");
		attackButton.getStyleClass().add("button");
		inspectButton.getStyleClass().add("button");
		endTurnBtn.getStyleClass().add("button");
		forfeit.getStyleClass().add("cancelbutton");
		return gameScene;
	}

	/**
	 * Method to generate the hexagon board
	 * 
	 * @return the BorderPane containing the board
	 */
	private BorderPane generateBoard() {
		// TODO Auto-generated method stub
		hexBox = new BorderPane();
		// The distance between the tops of two adjacent rows
		double height = width / Math.sqrt(3)
				+ Math.sqrt((Math.pow(width / Math.sqrt(3), 2)) - Math.pow((width / 2), 2));// 45
		// The current distance between the first tile of a row and the far left
		// of the board
		double xOffset = sideLength * width / 2 + OFFSET;
		// loop for each row of the board
		for (int currentYCoor = 0; currentYCoor < sideLength * 2 - 1; currentYCoor++) {
			// If in the upper half, including the middle
			if (currentYCoor < sideLength) {
				// Reduce the offset by half the width of a hexagon
				xOffset = xOffset - width / 2;
				// loop for each hexagon in the row
				for (int currentXCoor = 0; currentXCoor < currentYCoor + sideLength; currentXCoor++) {
					// Draw the hexagon based on its points
					Polyline hexagon = new Polyline(xOffset + currentXCoor * width + width / Math.sqrt(3),
							height * currentYCoor, xOffset + currentXCoor * width + width / Math.sqrt(3) + width / 2,
							height * currentYCoor + width / Math.sqrt(3) / 2,
							xOffset + currentXCoor * width + width / Math.sqrt(3) + width / 2,
							height * currentYCoor + width / Math.sqrt(3) / 2 + width / Math.sqrt(3),
							xOffset + currentXCoor * width + width / Math.sqrt(3),
							height * currentYCoor + width / Math.sqrt(3) * 2,
							xOffset + currentXCoor * width + width / Math.sqrt(3) - width / 2,
							height * currentYCoor + width / Math.sqrt(3) / 2 + width / Math.sqrt(3),
							xOffset + currentXCoor * width + width / Math.sqrt(3) - width / 2,
							height * currentYCoor + width / Math.sqrt(3) / 2,
							xOffset + currentXCoor * width + width / Math.sqrt(3), +height * currentYCoor + 0.0);
					// Variables to store the location of the current hexagon
					int x = currentXCoor;
					int y = currentYCoor;
					// Fill the hexagon so that it can be clicked on
					hexagon.setFill(Color.WHITE);
					// The event that controls what happens when the hexagon is
					// clicked
					hexagonArray[y][x] = hexagon;
					hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent t) {
							Game controller = new Game();
							switch (onClick) {
							case "INSPECT":
								break;

							case "MOVE":
								controller.moveRobot(x, y);
								break;
							case "ATTACK":
								controller.attackTile(x, y);
								break;

							}
						}
					});
					hexBox.getChildren().add(hexagon);

				}

			} else {
				// Increase the offset by half the width of a hexagon
				xOffset = xOffset + width / 2;
				// loop for each hexagon in the row
				for (int currentXCoor = 0; currentXCoor < (sideLength * 2 - 1) - currentYCoor - 1
						+ sideLength; currentXCoor++) {
					// Draw the hexagon based on its points
					Polyline hexagon = new Polyline(xOffset + currentXCoor * width + width / Math.sqrt(3),
							height * currentYCoor, xOffset + currentXCoor * width + width / Math.sqrt(3) + width / 2,
							height * currentYCoor + width / Math.sqrt(3) / 2,
							xOffset + currentXCoor * width + width / Math.sqrt(3) + width / 2,
							height * currentYCoor + width / Math.sqrt(3) / 2 + width / Math.sqrt(3),
							xOffset + currentXCoor * width + width / Math.sqrt(3),
							height * currentYCoor + width / Math.sqrt(3) * 2,
							xOffset + currentXCoor * width + width / Math.sqrt(3) - width / 2,
							height * currentYCoor + width / Math.sqrt(3) / 2 + width / Math.sqrt(3),
							xOffset + currentXCoor * width + width / Math.sqrt(3) - width / 2,
							height * currentYCoor + width / Math.sqrt(3) / 2,
							xOffset + currentXCoor * width + width / Math.sqrt(3), +height * currentYCoor + 0.0);
					// Variables to store the location of the current hexagon
					int x = currentXCoor + currentYCoor - 4;
					int y = currentYCoor;
					// Fill the hexagon so that it can be clicked on
					hexagon.setFill(Color.WHITE);
					hexagon.getStyleClass().add("hex_button");
					hexagonArray[y][x] = hexagon;
					// The event that controls what happens when the hexagon is
					// clicked
					hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent t) {
							Game controller = new Game();
							switch (onClick) {
							case "INSPECT":
								break;

							case "MOVE":

								controller.moveRobot(x, y);
								break;

							case "ATTACK":
								controller.attackTile(x, y);
								break;
							}

						}
					});
					hexBox.getChildren().add(hexagon);
				}

			}
		}

		return hexBox;
	}

	/**
	 * Method to create the table that show the HP of the user's robots TODO
	 * Make display the table
	 * 
	 * @return the populated table
	 */
	private TableView<String> createRobotTable() {
		TableColumn<Integer, String> scoutColumn = new TableColumn<Integer, String>("Scout");
		TableColumn<Integer, String> sniperColumn = new TableColumn<Integer, String>("Sniper");
		TableColumn<Integer, String> tankColumn = new TableColumn<Integer, String>("Tank");
		Game controller = new Game();
		int[] robotHealths = controller.getRobotHealths();

		return tankHealthTable;
	}
	//TODO listening HP for all robot
	 @SuppressWarnings("rawtypes")
		private ObservableList<Map> generateDataInMap() {
	        ObservableList<Map> allData = FXCollections.observableArrayList();
	        Game controller = new Game();
	        int[] healths = controller.getRobotHealths();
	            Map<String, String> dataRow = new HashMap<>();
	 
	            String value1 = "" + healths[0];
	            String value2 = "" + healths[1];
	            String value3 = "" + healths[2];
	 
	            dataRow.put(Column1MapKey, value1);
	            dataRow.put(Column2MapKey, value2);
	            dataRow.put(Column3MapKey, value3);
	            allData.add(dataRow);
	        
	        return allData;
	    }
	
	
	
		public void updateGame(Board board) {
			  Platform.runLater(new Runnable() {
			        @Override
			        public void run() {
			tc.refreshTable();
			currentTurnLabel.setText(board.players.get(board.playerTurn).name + "'s turn");
			currentTurnLabel.setTextFill(numberColors[board.playerTurn]);		
			if (board.players.get(board.playerTurn).robotList.get(board.currentRobot).movementLeft <= 0) {
				moveButton.setDisable(true);
			} else {
				moveButton.setDisable(false);
			}

			if (board.players.get(board.playerTurn).hasShot) {
				attackButton.setDisable(true);
			} else {
				attackButton.setDisable(false);
			}

			currentTankMoveLabel.setText(
					board.players.get(board.playerTurn).robotList.get(board.currentRobot).getClass().getSimpleName()
							+ " Move:" + board.players.get(board.playerTurn).robotList.get(board.currentRobot).movementLeft
							+ "/" + board.players.get(board.playerTurn).robotList.get(board.currentRobot).movement);
			// Remove all existing robot images from the board
			for (int i = 0; i < robotImages.length; i++) {
				if (robotImages[i] != null) {
					hexBox.getChildren().remove(robotImages[i]);
				}
			}
			boolean[][] fogOfWar = board.players.get(board.playerTurn).fogOfWar;
			int imageCount = 0;
			double height = width / Math.sqrt(3)
					+ Math.sqrt((Math.pow(width / Math.sqrt(3), 2)) - Math.pow((width / 2), 2));
			// The current distance between the first tile of a row and the far left
			// of the board
			double xOffset = sideLength * width / 2 + OFFSET;
			// loop for each row of the board
			for (int currentYCoor = 0; currentYCoor < sideLength * 2 - 1; currentYCoor++) {
				// Reduce the offset by half the width of a hexagon
				xOffset = xOffset - width / 2;
				// loop for each hexagon in the row
				for (int currentXCoor = 0; currentXCoor < sideLength * 2 - 1; currentXCoor++) {
					int robotCount = 0;
					boolean sameOwner = true;
					Robot previousRobot = null;
					if (board.gameBoard[currentXCoor][currentYCoor] != null) {
						if (fogOfWar[currentXCoor][currentYCoor]) {
							hexagonArray[currentYCoor][currentXCoor].setFill(Color.WHITE);
							for (Robot r : board.gameBoard[currentXCoor][currentYCoor].robotList) {
								Player robotOwner = null;
								int i = 0;
								while (robotOwner == null) {
									if (r.teamNumber == board.players.get(i).teamNumber) {
										robotOwner = board.players.get(i);
									}
									i++;
								}
								// TODO currently displays robots from the
								// perspective of the current player, may want to
								// display from perspective of client's playerW
								// try {
								if (robotOwner.teamNumber == board.playerTurn) {
									// if
									// (robotOwner.IP.equals(InetAddress.getLocalHost().toString()))
									// {

									String robotType = r.getClass().getSimpleName();
									File file;
									int x = currentXCoor;// + currentYCoor - 4;
									int y = currentYCoor;
									switch (robotType) {
									case "Scout":
										robotImages[imageCount] = new ImageView();
										file = new File("Resources/images/" + i + "Scout.png");
										Image scout = new Image("file:///" + file.getAbsolutePath().replace("\\", "/"));
										robotImages[imageCount].setImage(scout);
										robotImages[imageCount].setScaleX(0.60);
										robotImages[imageCount].setScaleY(0.60);
										robotImages[imageCount].setLayoutX(
												xOffset + currentXCoor * width + width / Math.sqrt(3) - width / 2);
										robotImages[imageCount].setLayoutY(height * currentYCoor + width / Math.sqrt(3) / 2
												+ width / Math.sqrt(3) - scout.getHeight() * 0.80);									
										robotImages[imageCount].setOnMouseClicked(new EventHandler<MouseEvent>() {

											@Override
											public void handle(MouseEvent t) {
												Game controller = new Game();
												switch (onClick) {
												case "INSPECT":
													break;

												case "MOVE":
													controller.moveRobot(x, y);
													break;
												case "ATTACK":
													controller.attackTile(x, y);
													break;

												}
											}
										});
										hexBox.getChildren().add(robotImages[imageCount++]);
										imageCount++;
										break;
									case "Sniper":
										robotImages[imageCount] = new ImageView();
										file = new File("Resources/images/" + i + "Sniper.png");
										Image sniper = new Image("file:///" + file.getAbsolutePath().replace("\\", "/"));
										robotImages[imageCount].setImage(sniper);
										robotImages[imageCount].setScaleX(0.60);
										robotImages[imageCount].setScaleY(0.60);
										robotImages[imageCount].setLayoutX(
												xOffset + currentXCoor * width + width / Math.sqrt(3) - width / 2);
										robotImages[imageCount].setLayoutY(height * currentYCoor + width / Math.sqrt(3) / 2
												- sniper.getHeight() * 0.20);
										robotImages[imageCount].setOnMouseClicked(new EventHandler<MouseEvent>() {

											@Override
											public void handle(MouseEvent t) {
												Game controller = new Game();
												switch (onClick) {
												case "INSPECT":
													break;

												case "MOVE":
													controller.moveRobot(x, y);
													break;
												case "ATTACK":
													controller.attackTile(x, y);
													break;

												}
											}
										});
										hexBox.getChildren().add(robotImages[imageCount++]);
										imageCount++;
										break;
									case "Tank":
										robotImages[imageCount] = new ImageView();
										file = new File("Resources/images/" + i + "Tank.png");
										Image tank = new Image("file:///" + file.getAbsolutePath().replace("\\", "/"));
										robotImages[imageCount].setImage(tank);
										robotImages[imageCount].setScaleX(0.60);
										robotImages[imageCount].setScaleY(0.60);
										robotImages[imageCount].setLayoutX(xOffset + currentXCoor * width
												+ width / Math.sqrt(3) + width / 2 - tank.getWidth());
										robotImages[imageCount].setLayoutY(
												height * currentYCoor + width / Math.sqrt(3) / 2 - tank.getHeight() * 0.20);									
										robotImages[imageCount].setOnMouseClicked(new EventHandler<MouseEvent>() {

											@Override
											public void handle(MouseEvent t) {
												Game controller = new Game();
												switch (onClick) {
												case "INSPECT":
													break;

												case "MOVE":
													controller.moveRobot(x, y);
													break;
												case "ATTACK":
													controller.attackTile(x, y);
													break;

												}
											}
										});
										hexBox.getChildren().add(robotImages[imageCount++]);
										imageCount++;
									}
								} else {
									robotCount++;
									if (previousRobot != null && previousRobot.teamNumber != r.teamNumber) {
										sameOwner = false;
									} else {
										previousRobot = r;
									}
								}
								/*
								 * } catch (UnknownHostException e) { // TODO
								 * Auto-generated catch block e.printStackTrace(); }
								 */

							}
							// TODO draw other number
							if (robotCount == 1) {
								String robotType = previousRobot.getClass().getSimpleName();
								File file;
								int x = currentXCoor;// + currentYCoor - 4;
								int y = currentYCoor;
								switch (robotType) {
								case "Scout":
									robotImages[imageCount] = new ImageView();
									file = new File("Resources/images/" + (previousRobot.teamNumber + 1) + "Scout.png");
									Image scout = new Image("file:///" + file.getAbsolutePath().replace("\\", "/"));
									robotImages[imageCount].setImage(scout);
									break;
								case "Sniper":
									robotImages[imageCount] = new ImageView();
									file = new File("Resources/images/" + (previousRobot.teamNumber + 1) + "Sniper.png");
									Image sniper = new Image("file:///" + file.getAbsolutePath().replace("\\", "/"));
									robotImages[imageCount].setImage(sniper);
									break;
								case "Tank":
									robotImages[imageCount] = new ImageView();
									file = new File("Resources/images/" + (previousRobot.teamNumber + 1) + "Tank.png");
									Image tank = new Image("file:///" + file.getAbsolutePath().replace("\\", "/"));
									robotImages[imageCount].setImage(tank);
									break;
								}
								robotImages[imageCount].setScaleX(0.60);
								robotImages[imageCount].setScaleY(0.60);
								robotImages[imageCount].setLayoutX(xOffset + currentXCoor * width + width / Math.sqrt(3)
										+ width / 2 - robotImages[imageCount].getImage().getWidth());
								robotImages[imageCount].setLayoutY(height * currentYCoor + width / Math.sqrt(3) / 2
										+ width / Math.sqrt(3) - robotImages[imageCount].getImage().getHeight() * 0.80);							
								robotImages[imageCount].setOnMouseClicked(new EventHandler<MouseEvent>() {

									@Override
									public void handle(MouseEvent t) {
										Game controller = new Game();
										switch (onClick) {
										case "INSPECT":
											break;

										case "MOVE":
											controller.moveRobot(x, y);
											break;
										case "ATTACK":
											controller.attackTile(x, y);
											break;

										}
									}
								});
								hexBox.getChildren().add(robotImages[imageCount++]);
								//imageCount++;

							} else if (robotCount > 1) {
								int x = currentXCoor;// + currentYCoor - 4;
								int y = currentYCoor;
								int robotColor = previousRobot.teamNumber;
								if(!sameOwner)
								{
									robotColor = 6;
								}
								robotImages[imageCount] = new ImageView();
								robotImages[imageCount].setScaleX(0.60);
								robotImages[imageCount].setScaleY(0.60);
								robotImages[imageCount].setImage(textToImage(""+robotCount, numberColors[robotColor]));
								robotImages[imageCount].setLayoutX(xOffset + currentXCoor * width + width / Math.sqrt(3)
								+ width / 2 - robotImages[imageCount].getImage().getWidth());
						robotImages[imageCount].setLayoutY(height * currentYCoor + width / Math.sqrt(3) / 2
								+ width / Math.sqrt(3) - robotImages[imageCount].getImage().getHeight() * 0.80);
						robotImages[imageCount].setOnMouseClicked(new EventHandler<MouseEvent>() {

							@Override
							public void handle(MouseEvent t) {
								Game controller = new Game();
								switch (onClick) {
								case "INSPECT":
									break;

								case "MOVE":
									controller.moveRobot(x, y);
									break;
								case "ATTACK":
									controller.attackTile(x, y);
									break;

								}
							}
						});
						hexBox.getChildren().add(robotImages[imageCount++]);
						//imageCount++;

							}
						} else {
							if (hexagonArray[currentYCoor][currentXCoor] != null) {
								hexagonArray[currentYCoor][currentXCoor].setFill(Color.DARKGREY);
							}
						}
					}

				}
			}}});
		}

		private Image textToImage(String text, Color color) {
			Label label = new Label(text);
			label.setFont(new Font(30));
			label.setMinSize(55, 47);
			label.setMaxSize(55, 47);
			label.setPrefSize(55, 47);
			label.setTextFill(color);
			label.setWrapText(true);
			label.setTranslateX(23);
			Rectangle rect = new Rectangle(55,47);
			rect.setFill(null);
			rect.setStroke(color);
			rect.setStrokeWidth(10);
			Scene scene = new Scene(new Group(label,rect));
			WritableImage img = new WritableImage(55, 47);
			scene.snapshot(img);		
			return img;
		}
}