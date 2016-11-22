package view;

import java.io.File;
import java.util.ArrayList;

import controller.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

/**
 * Class that creates the GUI for the actual game and all book keeping needed
 * for that class
 * TODO Make list populate
 * TODO update as game changes
 * TODO Populate table
 * TODO display different things for different users
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
	//Currently determines distances between center of each hexagon
	//TODO WIDTH and HEIGHT variables to control size of hexagons
	private static final double WIDTH = 51.96;
	//The number of heaxagons per side of the grid
	private int sideLength = 5;

	/**
	 * Method for creating the GameView Scene
	 * @return the gameScene
	 */
	public Scene init(ArrayList<String> playerList, ArrayList<String> observerList) {
		int playerCount = playerList.size();
		//Sets side length to 7 if there are six players
		if (playerCount == 6) {
			sideLength = 7;
		}
		HBox lobbyScreen = new HBox(355);
		//Left side of window
		VBox leftBox = new VBox(30);
		Button backBtn = new Button("Back");
		//backBtn.setStyle(arg0);
		tankHealthTable = new TableView<String>();
		currentTankMoveLabel = new Label("Scouts Move: 3/3");
		Button moveBtn = new Button("Move");
		Button attackBtn = new Button("Attack");
		Button inspectBtn = new Button("Inspect");
		Button endTurnBtn = new Button("End Turn");
		leftBox.getChildren().addAll(backBtn, tankHealthTable, currentTankMoveLabel, moveBtn, attackBtn, inspectBtn,
				endTurnBtn);
		//Center of window
		VBox centerBox = new VBox(60);
		//TODO make reflect current turn
		currentTurnLabel = new Label(playerList.get(0) + " turn");
		centerBox.getChildren().addAll(currentTurnLabel, generateBoard());
		centerBox.setMinWidth(WIDTH * (sideLength * 2 - 1));
		//Right side of window
		VBox rightBox = new VBox(30);
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
		//TODO quit button for observers
		Button forfeit = new Button("Forfeit");
		rightBox.getChildren().addAll(playerListLabel, playerListView, observerListLabel, observerListView, forfeit);
		lobbyScreen.getChildren().addAll(leftBox, centerBox, rightBox);
		//Sets margin to give the board room to be seen
		VBox.setMargin(centerBox, new Insets(5, 5, 5, 5));
		lobbyScreen.setAlignment(Pos.CENTER);
		gameScene = new Scene(lobbyScreen);
		
		

		File file = new File("Resources/css/GameView.css");
		gameScene.getStylesheets().add("file:///"+file.getAbsolutePath().replace("\\", "/"));
		
		currentTurnLabel.getStyleClass().add("text_label");
		currentTankMoveLabel.getStyleClass().add("text_label");
		playerListLabel.getStyleClass().add("text_label");
		observerListLabel.getStyleClass().add("text_label");
		
		backBtn.getStyleClass().add("button");
		moveBtn.getStyleClass().add("button");
		attackBtn.getStyleClass().add("button");
		inspectBtn.getStyleClass().add("button");
		endTurnBtn.getStyleClass().add("button");
		forfeit.getStyleClass().add("button");
		currentTurnLabel.getStyleClass().add("centred_label");
		return gameScene;
	}

	/**
	 * Method to generate the hexagon board
	 * @return the BorderPane containing the board
	 */
	private BorderPane generateBoard() {
		// TODO Auto-generated method stub
		BorderPane hexBox = new BorderPane();
		//The distance between the tops of two adjacent rows
		double height = 45.00;
		//The current distance between the first tile of a row and the far left of the board
		double xOffset = sideLength * WIDTH / 2;
		//loop for each row of the board
		for (int currentYCoor = 0; currentYCoor < sideLength * 2 - 1; currentYCoor++) {
			//If in the upper half, including the middle
			if (currentYCoor < sideLength) {
				//Reduce the offset by half the width of a hexagon
				xOffset = xOffset - WIDTH / 2;
				//loop for each hexagon in the row
				for (int currentXCoor = 0; currentXCoor < currentYCoor + sideLength; currentXCoor++) {
					//Draw the hexagon based on its points
					Polyline hexagon = new Polyline(xOffset + currentXCoor * WIDTH + 40.0, height * currentYCoor,
							xOffset + currentXCoor * WIDTH + 65.98, height * currentYCoor + 15.0,
							xOffset + currentXCoor * WIDTH + 65.98, height * currentYCoor + 45.0,
							xOffset + currentXCoor * WIDTH + 40.0, height * currentYCoor + 60.0,
							xOffset + currentXCoor * WIDTH + 14.02, height * currentYCoor + 45.0,
							xOffset + currentXCoor * WIDTH + 14.02, height * currentYCoor + 15.0,
							xOffset + currentXCoor * WIDTH + 40.0, +height * currentYCoor + 0.0);
					//Variables to store the location of the current hexagon
					int x = currentXCoor;
					int y = currentYCoor;
					//Fill the hexagon so that it can be clicked on
					hexagon.setFill(Color.WHITE);
					//The event that controls what happens when the hexagon is clicked
					hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent t) {

							System.out.println("x: " + x + " y: " + y);
						}
					});
					hexBox.getChildren().add(hexagon);

				}

			} else {
				//Increase the offset by half the width of a hexagon
				xOffset = xOffset + WIDTH / 2;
				//loop for each hexagon in the row
				for (int currentXCoor = 0; currentXCoor < (sideLength * 2 - 1) - currentYCoor - 1
						+ sideLength; currentXCoor++) {
					//Draw the hexagon based on its points
					Polyline hexagon = new Polyline(xOffset + currentXCoor * WIDTH + 40.0, height * currentYCoor,
							xOffset + currentXCoor * WIDTH + 65.98, height * currentYCoor + 15.0,
							xOffset + currentXCoor * WIDTH + 65.98, height * currentYCoor + 45.0,
							xOffset + currentXCoor * WIDTH + 40.0, height * currentYCoor + 60.0,
							xOffset + currentXCoor * WIDTH + 14.02, height * currentYCoor + 45.0,
							xOffset + currentXCoor * WIDTH + 14.02, height * currentYCoor + 15.0,
							xOffset + currentXCoor * WIDTH + 40.0, +height * currentYCoor + 0.0);
					//Variables to store the location of the current hexagon
					int x = currentXCoor + currentYCoor - 4;
					int y = currentYCoor;
					//Fill the hexagon so that it can be clicked on
					hexagon.setFill(Color.WHITE);
					hexagon.getStyleClass().add("hex_button");
					//The event that controls what happens when the hexagon is clicked
					hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent t) {

							System.out.println("x: " + x + " y: " + y);
						}
					});
					hexBox.getChildren().add(hexagon);
				}

			}
		}

		return hexBox;
	}


	/**
	 * Method to create the table that show the HP of the user's robots
	 * TODO Make display the table
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
}
