package view;

import java.io.File;
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
	//Used to keep track of the number of computer players that will be in the game when it starts.
	private Spinner<Integer> computerCount;
	/**
	 * Creates the lobbyScene 
	 * @return lobbyScene
	 */
	public Scene init() {
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
		HBox spinnerBox = new HBox(10);
		Label spinnerLabel = new Label("Computer Players: ");
		spinnerLabel.setPadding(new Insets(10, 0, 0, 0));
		computerCount = new Spinner<Integer>(0,6,0,1);
		spinnerBox.getChildren().addAll(spinnerLabel, computerCount);
		leftBox.getChildren().addAll(backBtn, playerListLabel, playerListView, spinnerBox);
		//Center of window
		VBox centerBox = new VBox(60);
		Button switchBtn = new Button("Switch");
		switchBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				switchUser();				
			}
		});
		//TODO Currently does nothing, will change if networking is fixed
		Button kickBtn = new Button("Kick (Does Nothing)");
		Button beginBtn = new Button("Begin Game");
		beginBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Game controller = new Game();
				try {
					if(!controller.beginGame(computerCount.getValue(), playerList, observerList))
					{
						//TODO popup error if incorrect number of players
					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
			}
		});
		centerBox.getChildren().addAll(switchBtn, kickBtn, beginBtn);
		centerBox.setPadding(new Insets(140, 0, 0, 0));
		//Right side of window
		VBox rightBox = new VBox(30);
		Label observerListLabel = new Label("Observers");
		observerListView = new ListView<String>();
		observerList = new ArrayList<String>();
		observerObsList = FXCollections.observableArrayList(observerList);		
		observerListView.setItems(observerObsList );
		//TODO code that makes only one object in observerListView and playerListView selectable
		rightBox.getChildren().addAll(observerListLabel, observerListView);
		rightBox.setPadding(new Insets(50, 50, 0, 0));
		lobbyScreen.getChildren().addAll(leftBox, centerBox, rightBox);
		lobbyScreen.setAlignment(Pos.CENTER);
		conditioner.setPadding(new Insets(100, 0, 400, 0));
		conditioner.getChildren().addAll(lobbyScreen);
		lobbyScene = new Scene(conditioner);
		
		File file = new File("Resources/css/LobbyView.css");
		lobbyScreen.getScene().getStylesheets().add("file:///"+file.getAbsolutePath().replace("\\", "/"));
		
		playerListLabel.getStyleClass().add("text_label");
		spinnerLabel.getStyleClass().add("text_label");
		observerListLabel.getStyleClass().add("text_label");

		backBtn.getStyleClass().add("cancelbutton");
		switchBtn.getStyleClass().add("lobybutton");
		kickBtn.getStyleClass().add("cancelbutton");
		beginBtn.getStyleClass().add("lobybutton");

		return lobbyScene;
	}
	

	/**
	 * Method called by the switchButton, swaps the currently selected observer to become a player and vice versa
	 * TODO Currently list views do not update when user is switched
	 * TODO change so that if the user is an observer they become a player and vice versa
	 */
	private void switchUser() {
		if(observerListView.getSelectionModel().getSelectedItem() != null)
		{
			playerObsList.add(observerListView.getSelectionModel().getSelectedItem());
			observerObsList.remove(observerListView.getSelectionModel().getSelectedItem());
			observerListView.refresh();
			playerListView.refresh();
		}
		else if(playerListView.getSelectionModel().getSelectedItem() != null)
		{
			observerObsList.add(playerListView.getSelectionModel().getSelectedItem());
			playerObsList.remove(playerListView.getSelectionModel().getSelectedItem());
			observerListView.refresh();
			playerListView.refresh();
		}
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
	    
	    System.out.println("GOT TO VIEW");
	    
	    Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	            observerObsList.clear();
	            observerObsList.addAll(newObserverList);
	            for(int i = 0 ; i < observerObsList.size() ; i++)
	            {
	            System.out.println("Obs List: " + observerObsList.get(i));
	            }
	            observerListView.refresh();
	            observerListView.getSelectionModel().selectFirst();
	            
	            playerObsList.clear();
	            playerObsList.addAll(newPlayerList);
	            playerListView.refresh();
	        }
	    });
	    
	}
}
