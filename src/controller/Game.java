package controller;

import java.io.File;
import java.util.ArrayList;

import javax.print.DocFlavor.URL;

import javafx.application.Application;
import javafx.stage.Stage;
import view.GameView;
import view.LobbyView;
import view.StartView;

public class Game extends Application{

	public static Stage gameStage;
	public static void main(String[] args)
	{
		launch();
	}

	
	@Override
	public void start(Stage stage) throws Exception {
	    

		gameStage = stage;
		StartView initialScene = new StartView();
		gameStage.setScene(initialScene.init());
		gameStage.setMaximized(true);
		
		File file = new File("src/controller/StartView.css");
		gameStage.centerOnScreen();
		gameStage.getScene().getStylesheets().clear();
		gameStage.getScene().getStylesheets().add("file:///"+file.getAbsolutePath().replace("\\", "/"));
		gameStage.setTitle("Robot War");
		gameStage.show();
	}



	public boolean joinGame(String name, String address) {
		// TODO Auto-generated method stub
		boolean result = false;
		if(name.length() != 0)
		{
			result = true;
		}
		return result;
	}




	public void hostGame(String name) {
		// TODO Auto-generated method stub
		LobbyView lobbyScene = new LobbyView();
		gameStage.setScene(lobbyScene.init());
		lobbyScene.addUser(name);
		//lobbyScene.setStyle();
	}


	public void exitGame() {
		gameStage.close();
		System.exit(0);
		
	}



	public boolean beginGame(Integer computerCount, ArrayList<String> playerList, ArrayList<String> observerList) {
		// TODO Auto-generated method stub
		boolean result = false;
		int playerCount = computerCount + playerList.size();
		if(playerCount == 2 || playerCount == 3)
		{
			result = true;			
			GameView gameScene = new GameView();
			//TODO Add computers to player list;
			gameStage.setScene(gameScene.init(playerList, observerList));
			
		}
		else if(playerCount == 6)
		{
			result = true;
			GameView gameScene = new GameView();
			//TODO Add computers to player list;
			gameStage.setScene(gameScene.init(playerList, observerList));
			
		}
		// TODO Statement for testing GameView. Remove when finished.
		else
		{
			result = true;
			GameView gameScene = new GameView();
			//TODO Add computers to player list;
			gameStage.setScene(gameScene.init(playerList, observerList));
		}
		return result;
	}


	//Needs a way to determine which player is being displayed.
	public int[] getRobotHealths() {
		int[] healths = {1,2,3};
		return healths;
	}
}
