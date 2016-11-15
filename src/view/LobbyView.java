package view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LobbyView {

	private static final String title = "Robot Wars";
	private Scene lobbyScene;
	private ArrayList<String> playerList;
	private Spinner<Integer> computerCount;
	private ArrayList<String> observerList;
	
	public Scene init() {
		HBox lobbyScreen = new HBox(200);
		VBox leftBox = new VBox(30);
		Button backBtn =new Button("Back");
		Label playerListLabel = new Label("Players");
		ListView<String> playerListView = new ListView<String>();
		playerList = new ArrayList<String>();
		ObservableList<String> playerObsList = FXCollections.observableArrayList(playerList);		
		playerListView.setItems(playerObsList );
		HBox spinnerBox = new HBox(10);
		Label spinnerLabel = new Label("Computer Players: ");
		computerCount = new Spinner<Integer>(0,5,0,1);
		spinnerBox.getChildren().addAll(spinnerLabel, computerCount);
		leftBox.getChildren().addAll(backBtn, playerListLabel, playerListView, spinnerBox);
		VBox centerBox = new VBox(60);
		Button switchBtn = new Button("Switch");
		Button kickBtn = new Button("Kick (Does Nothing)");
		Button beginBtn = new Button("Begin Game");
		centerBox.getChildren().addAll(switchBtn, kickBtn, beginBtn);
		VBox rightBox = new VBox(30);
		Label observerListLabel = new Label("Observers");
		ListView<String> observerListView = new ListView<String>();
		observerList = new ArrayList<String>();
		ObservableList<String> observerObsList = FXCollections.observableArrayList(observerList);		
		observerListView.setItems(observerObsList );
		rightBox.getChildren().addAll(observerListLabel, observerListView);
		lobbyScreen.getChildren().addAll(leftBox, centerBox, rightBox);
		lobbyScreen.setAlignment(Pos.CENTER);
		lobbyScene = new Scene(lobbyScreen);
		return lobbyScene;
	}
}
