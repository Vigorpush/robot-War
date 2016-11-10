package controller;

import javafx.application.Application;
import javafx.stage.Stage;
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
		
	}



	public void exitGame() {
		gameStage.close();
		System.exit(0);
		
	}
}
