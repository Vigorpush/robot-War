package controller;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Board;
import model.Player;
import model.Scout;
import model.Sniper;
import model.Tank;
import view.GameView;
import view.LobbyView;
import view.StartView;

@SuppressWarnings("unused")
public class Game extends Application{

	public static Stage gameStage;
	public static Board gameBoard;
	
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
		
		File file = new File("Resources/css/StartView.css");
		gameStage.centerOnScreen();
		gameStage.getScene().getStylesheets().clear();
		String test = "file:///"+file.getAbsolutePath().replace("\\", "\"/\"").replaceFirst("\"", "")+"\"";
		System.out.println(test);
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



	public boolean beginGame(Integer computerCount, ArrayList<String> playerList, ArrayList<String> observerList) throws UnknownHostException {
		// TODO Auto-generated method stub
		boolean result = false;
		int sideLength = 5;
		if(playerList.size()+computerCount == 6)
		{
			sideLength = 7;
		}
		gameBoard = new Board(sideLength);
		int playerCount = computerCount + playerList.size();		
		gameBoard.players = new ArrayList<Player>();
		int i = 0;
		int j = 0;
		for(String p : playerList)
		{
			gameBoard.players.add(new Player(p,InetAddress.getLocalHost().toString(),i));
			Tank newTank = new Tank(j++,i, null, null);			
			Scout newScout = new Scout(j++,i, null, null);
			Sniper newSniper = new Sniper(j++,i, null, null);
			gameBoard.players.get(i).robotList.add(newTank);
			gameBoard.players.get(i).robotList.add(newScout);
			gameBoard.players.get(i).robotList.add(newSniper);
		}
		if(playerCount == 2 || playerCount == 3)
		{			
			result = true;			
			GameView gameScene = new GameView();
			//TODO Add computers to player list;			
			gameStage.setScene(gameScene.init(playerList, observerList));			
			//Set starting position for two players
			if(playerCount == 2)
			{
				gameBoard.gameBoard[0][4].addRobot(gameBoard.players.get(0).robotList.get(0));
				gameBoard.gameBoard[0][4].addRobot(gameBoard.players.get(0).robotList.get(1));
				gameBoard.gameBoard[0][4].addRobot(gameBoard.players.get(0).robotList.get(2));
				gameBoard.gameBoard[8][4].addRobot(gameBoard.players.get(1).robotList.get(0));
				gameBoard.gameBoard[8][4].addRobot(gameBoard.players.get(1).robotList.get(1));
				gameBoard.gameBoard[8][4].addRobot(gameBoard.players.get(1).robotList.get(2));
			}
			else
			{
				//TODO playercount == 3
			}
			
			gameBoard.players.get(0).isTurn = true;
			
			
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


	private void runGame() {
				
	}


	//Needs a way to determine which player is being displayed.
	public int[] getRobotHealths() {
		int[] healths = {1,2,3};
		return healths;
	}
}
