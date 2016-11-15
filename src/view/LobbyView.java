package view;

import java.util.ArrayList;

import controller.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	private ListView<String> playerListView;
	private ObservableList<String> playerObsList;
	private ListView<String> observerListView;
	private ObservableList<String> observerObsList;
	public Scene init() {
		HBox lobbyScreen = new HBox(200);
		VBox leftBox = new VBox(30);
		Button backBtn =new Button("Back");
		Label playerListLabel = new Label("Players");
		playerListView = new ListView<String>();
		playerList = new ArrayList<String>();
		playerObsList = FXCollections.observableArrayList(playerList);		
		playerListView.setItems(playerObsList );
		HBox spinnerBox = new HBox(10);
		Label spinnerLabel = new Label("Computer Players: ");
		computerCount = new Spinner<Integer>(0,6,0,1);
		spinnerBox.getChildren().addAll(spinnerLabel, computerCount);
		leftBox.getChildren().addAll(backBtn, playerListLabel, playerListView, spinnerBox);
		VBox centerBox = new VBox(60);
		Button switchBtn = new Button("Switch");
		switchBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				switchUser();				
			}
		});
		Button kickBtn = new Button("Kick (Does Nothing)");
		Button beginBtn = new Button("Begin Game");
		beginBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Game controller = new Game();
				if(!controller.beginGame(computerCount.getValue(), playerList, observerList))
				{
					//popup error
				}
						
			}
		});
		centerBox.getChildren().addAll(switchBtn, kickBtn, beginBtn);
		VBox rightBox = new VBox(30);
		Label observerListLabel = new Label("Observers");
		observerListView = new ListView<String>();
		observerList = new ArrayList<String>();
		observerObsList = FXCollections.observableArrayList(observerList);		
		observerListView.setItems(observerObsList );
		rightBox.getChildren().addAll(observerListLabel, observerListView);
		lobbyScreen.getChildren().addAll(leftBox, centerBox, rightBox);
		lobbyScreen.setAlignment(Pos.CENTER);
		lobbyScene = new Scene(lobbyScreen);
		return lobbyScene;
	}

	private void switchUser() {
		if(observerListView.getSelectionModel().getSelectedItem() != null)
		{
			playerList.add(observerListView.getSelectionModel().getSelectedItem());
			observerList.remove(observerListView.getSelectionModel().getSelectedItem());
			observerListView.refresh();
			playerListView.refresh();
		}
		else if(playerListView.getSelectionModel().getSelectedItem() != null)
		{
			observerList.add(playerListView.getSelectionModel().getSelectedItem());
			playerList.remove(playerListView.getSelectionModel().getSelectedItem());
			observerListView.refresh();
			playerListView.refresh();
		}
	}
	
	public boolean addUser(String name)
	{
		boolean result = false;
		if(!(observerList.contains(name)|| playerList.contains(name)))
		{
			observerObsList.add(name);
			observerListView.refresh();
			result = true;
		}
		return result;
	}
}
