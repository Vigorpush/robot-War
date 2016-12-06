package view;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.sun.xml.internal.ws.dump.LoggingDumpTube.Position;

import controller.Game;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import view.ResourceLoader;
/**
 * Class that creates the LobbyView GUI and all methods to interact with it.
 * @author Niklaas
 *
 */

@SuppressWarnings("unused")
public class LobbyView {
	private static final String title = "Robot Wars";
	private Scene lobbyScene;
	//Stores the list of players for easy access and modification
	private static ArrayList<String> playerList;
	//Store the playerList in a format readable by the ListView
	private ObservableList<String> playerObsList;
	//Displays the playerList
	private ListView<String> playerListView;
	//Stores the list of observers for easy access and modification
	private static ArrayList<String> observerList;	
	//Store the observerList in a format readable by the ListView
	private ObservableList<String> observerObsList;
	//Displays the observerList
	private ListView<String> observerListView;
	
	private String name;
	/**
	 * Creates the lobbyScene 
	 * @return lobbyScene
	 */
	public Scene init(String name) {
		this.name = name;
		HBox lobbyScreen = new HBox(490);
		VBox conditioner = new VBox();
		//Left side of the window
		VBox leftBox = new VBox(30);
		Button backBtn =new Button("Back");
		Label playerListLabel = new Label("Players");
		playerListView = new ListView<String>();
		playerList = new ArrayList<String>();
		playerObsList = FXCollections.observableArrayList(playerList);		
		playerListView.setItems(playerObsList );
		Label ipAddressLabel = new Label();
		try {
			ipAddressLabel.setText("Your IP Address: " + InetAddress.getLocalHost().toString());
			ipAddressLabel.setTextFill(Color.SILVER);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		leftBox.getChildren().addAll(backBtn, ipAddressLabel, playerListLabel, playerListView);
		//Center of window
		VBox centerBox = new VBox(60);
		Button switchBtn = new Button("Switch");
		switchBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				switchUser();				
			}
		});
		Button beginBtn = new Button("Begin Game");
		beginBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Game controller = new Game();
				if(!controller.signalGameStart(playerList, observerList))
                {
                	//TODO popup error if incorrect number of players
                }
                System.out.println("PUSHED BEGIN BUTTON");
						
			}
		});
		centerBox.getChildren().addAll(switchBtn, beginBtn);
		centerBox.setPadding(new Insets(140, 0, 0, 0));
		//Right side of window
		VBox rightBox = new VBox(30);
		Label observerListLabel = new Label("Observers");
		observerListView = new ListView<String>();
		observerList = new ArrayList<String>();
		observerObsList = FXCollections.observableArrayList(observerList);		
		observerListView.setItems(observerObsList );
		rightBox.getChildren().addAll(observerListLabel, observerListView);
		rightBox.setPadding(new Insets(50, 50, 0, 0));
		lobbyScreen.getChildren().addAll(leftBox, centerBox, rightBox);
		lobbyScreen.setAlignment(Pos.CENTER);
		conditioner.setPadding(new Insets(100, 0, 400, 0));
		conditioner.getChildren().addAll(lobbyScreen);
		lobbyScene = new Scene(conditioner);
		
		String css = this.getClass().getResource("/css/LobbyView.css").toExternalForm();
		lobbyScreen.getScene().getStylesheets().add(css);
		
		playerListLabel.getStyleClass().add("text_label");
		observerListLabel.getStyleClass().add("text_label");

		backBtn.getStyleClass().add("cancelbutton");
		switchBtn.getStyleClass().add("lobybutton");
		beginBtn.getStyleClass().add("lobybutton");

		return lobbyScene;
	}
	

	/**
	 * Method called by the switchButton, swaps the user from observer to player and vice versa
	 */
	private void switchUser() {
		if(observerList.contains(name))
		{
			playerObsList.add(name);
			observerObsList.remove(name);
			observerListView.refresh();
			playerListView.refresh();
		}
		else 
		{
			observerObsList.add(name);
			playerObsList.remove(name);
			observerListView.refresh();
			playerListView.refresh();
		}
		
		Game controller = new Game();
		playerList.clear();
		observerList.clear();
		playerList.addAll(playerObsList);
		observerList.addAll(observerObsList);
		controller.updateUsers(playerList, observerList);
	}
	
	/**Method called by the controller to add a new User to the lobby as an observer if someone with that name does not already exist
	 * @param name The user to be added
	 * @return if the user was added
	 */
	public boolean addUser(String name)
	{
		boolean result = false;
		if(!(observerList.contains(name)|| playerList.contains(name)))
		{
			observerObsList.add(name);
			observerListView.refresh();
			observerListView.getSelectionModel().selectFirst();  // HEY NIKLAAS THERES AN ERROR WITH THIS FOR SOME RESON
			result = true;
		}
		return result;
	}
	
	public ArrayList<String> getPlayerList(){
	    return playerList;
	}
	
	public ArrayList<String> getObserverList(){
	    return observerList;
	}
	
	public void updateUserLists(ArrayList<String> newObserverList, ArrayList<String> newPlayerList) {
	    
	    Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	            observerObsList.clear();
	            observerObsList.addAll(newObserverList);
	            observerList.clear();
	            observerList.addAll(newObserverList);
	            
	            observerListView.refresh();
	            observerListView.getSelectionModel().selectFirst();
	            
	            playerObsList.clear();
	            playerObsList.addAll(newPlayerList);
	            playerList.clear();
	            playerList.addAll(newPlayerList);
	           
	            playerListView.refresh();
	        }
	    });
	    
	}
}
