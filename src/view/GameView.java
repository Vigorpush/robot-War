package view;

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

public class GameView {
	private static final String title = "Robot Wars";
	private Scene lobbyScene;
	private ArrayList<String> playerList;
	private Spinner<Integer> computerCount;
	private ArrayList<String> observerList;
	private ListView<String> playerListView;
	private ObservableList<String> playerObsList;
	private ListView<String> observerListView;
	private ObservableList<String> observerObsList;
	private TableView<String> tankHealthTable;
	private Label currentTankMoveLabel;
	private Label currentTurnLabel;
	private int currentXCoor;
	private int currentYCoor;
	private static final double WIDTH = 51.96;
	private int sideLength;

	public Scene init(ArrayList<String> playerList, ArrayList<String> observerList) {
		int playerCount = playerList.size();
		sideLength = 5;
		if (playerCount == 6) {
			sideLength = 7;
		}
		HBox lobbyScreen = new HBox(200);
		VBox leftBox = new VBox(30);
		Button backBtn = new Button("Back");
		tankHealthTable = new TableView<String>();
		currentTankMoveLabel = new Label("Scouts Move: 3/3");
		Button moveBtn = new Button("Move");
		Button attackBtn = new Button("Attack");
		Button inspectBtn = new Button("Inspect");
		Button endTurnBtn = new Button("End Turn");
		leftBox.getChildren().addAll(backBtn, tankHealthTable, currentTankMoveLabel, moveBtn, attackBtn, inspectBtn,
				endTurnBtn);
		VBox centerBox = new VBox(60);
		currentTurnLabel = new Label(playerList.get(0) + " turn");

		centerBox.getChildren().addAll(currentTurnLabel, generateBoard());
		centerBox.setMinWidth(WIDTH*(sideLength*2-1));
		VBox rightBox = new VBox(30);
		Label playerListLabel = new Label("Players");
		playerListView = new ListView<String>();
		playerList = new ArrayList<String>();
		playerObsList = FXCollections.observableArrayList(playerList);
		playerListView.setItems(playerObsList);
		HBox spinnerBox = new HBox(10);
		Label spinnerLabel = new Label("Computer Players: ");
		computerCount = new Spinner<Integer>(0, 6, 0, 1);
		spinnerBox.getChildren().addAll(spinnerLabel, computerCount);
		Label observerListLabel = new Label("Observers");
		observerListView = new ListView<String>();
		observerList = new ArrayList<String>();
		observerObsList = FXCollections.observableArrayList(observerList);
		observerListView.setItems(observerObsList);
		Button forfeit = new Button("Forfeit");
		rightBox.getChildren().addAll(playerListLabel, playerListView, observerListLabel, observerListView, forfeit);
		lobbyScreen.getChildren().addAll(leftBox, centerBox, rightBox);
		lobbyScreen.setMargin(centerBox, new Insets(5,5,5,5));
		lobbyScreen.setAlignment(Pos.CENTER);
		lobbyScene = new Scene(lobbyScreen);
		return lobbyScene;
	}

	private BorderPane generateBoard() {
		// TODO Auto-generated method stub
		BorderPane hexBox = new BorderPane();
		

		
		double height = 45.00;
		double xOffset = sideLength * WIDTH / 2;
		System.out.println(sideLength);
		for (currentYCoor = 0; currentYCoor < sideLength * 2 - 1; currentYCoor++) {
			if (currentYCoor < sideLength) {
				xOffset = xOffset - WIDTH / 2;
				for (currentXCoor = 0; currentXCoor < currentYCoor + sideLength; currentXCoor++) {
					Polyline hexagon = new Polyline(xOffset + currentXCoor * WIDTH + 40.0, height * currentYCoor,
							xOffset + currentXCoor * WIDTH + 65.98, height * currentYCoor + 15.0,
							xOffset + currentXCoor * WIDTH + 65.98, height * currentYCoor + 45.0,
							xOffset + currentXCoor * WIDTH + 40.0, height * currentYCoor + 60.0,
							xOffset + currentXCoor * WIDTH + 14.02, height * currentYCoor + 45.0,
							xOffset + currentXCoor * WIDTH + 14.02, height * currentYCoor + 15.0,
							xOffset + currentXCoor * WIDTH + 40.0, +height * currentYCoor + 0.0);
					int x = currentXCoor;
					int y = currentYCoor;
					hexagon.setFill(Color.WHITE);
					hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent t) {

							System.out.println("x: " + x + " y: " + y);
						}
					});
					hexBox.getChildren().add(hexagon);

				}

			} else {
				xOffset = xOffset + WIDTH / 2;
				for (currentXCoor = 0; currentXCoor < (sideLength * 2 - 1) - currentYCoor - 1
						+ sideLength; currentXCoor++) {
					Polyline hexagon = new Polyline(xOffset + currentXCoor * WIDTH + 40.0, height * currentYCoor,
							xOffset + currentXCoor * WIDTH + 65.98, height * currentYCoor + 15.0,
							xOffset + currentXCoor * WIDTH + 65.98, height * currentYCoor + 45.0,
							xOffset + currentXCoor * WIDTH + 40.0, height * currentYCoor + 60.0,
							xOffset + currentXCoor * WIDTH + 14.02, height * currentYCoor + 45.0,
							xOffset + currentXCoor * WIDTH + 14.02, height * currentYCoor + 15.0,
							xOffset + currentXCoor * WIDTH + 40.0, +height * currentYCoor + 0.0);
					int x = currentXCoor + currentYCoor - 4;
					int y = currentYCoor;
					hexagon.setFill(Color.WHITE);
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

		// hexBox.getChildren().add(hexagon);
		return hexBox;
	}

	private void switchUser() {
		if (observerListView.getSelectionModel().getSelectedItem() != null) {
			playerList.add(observerListView.getSelectionModel().getSelectedItem());
			observerList.remove(observerListView.getSelectionModel().getSelectedItem());
			observerListView.refresh();
			playerListView.refresh();
		} else if (playerListView.getSelectionModel().getSelectedItem() != null) {
			observerList.add(playerListView.getSelectionModel().getSelectedItem());
			playerList.remove(playerListView.getSelectionModel().getSelectedItem());
			observerListView.refresh();
			playerListView.refresh();
		}
	}

	public boolean addUser(String name) {
		boolean result = false;
		if (!(observerList.contains(name) || playerList.contains(name))) {
			observerObsList.add(name);
			observerListView.refresh();
			result = true;
		}
		return result;
	}
	
	private TableView<String> createRobotTable()
	{
		TableColumn<Integer,String> scoutColumn = new TableColumn<Integer,String>("Scout");
		TableColumn<Integer,String> sniperColumn = new TableColumn<Integer,String>("Sniper");
		TableColumn<Integer,String> tankColumn = new TableColumn<Integer,String>("Tank");
		Game controller = new Game();
		int[] robotHealths = controller.getRobotHealths();
		
		return tankHealthTable;
	}
}
