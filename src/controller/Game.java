package controller;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ListIterator;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Board;
import model.Player;
import model.Robot;
import model.Scout;
import model.Sniper;
import model.Tank;
import model.Tile;
import view.GameView;
import view.LobbyView;
import view.StartView;

@SuppressWarnings("unused")
public class Game extends Application {
    
    public static final int PORT = 32222;
	public static Stage gameStage;
	public static Board gameBoard;
	public static GameView gameScene;
	public static ArrayList<Robot> defeatedRobots = new ArrayList<Robot>();
	public static int sideLength;
	public static Client myClient;
	public static LobbyView lobbyScene;
	
	public static void main(String[] args) {

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
		gameStage.getScene().getStylesheets().add("file:///" + file.getAbsolutePath().replace("\\", "/"));
		gameStage.setTitle("Robot War");
		gameStage.show();
	}
//TODO store static variable about who you are 
	public boolean joinGame(String name, String address) {
		// TODO Auto-generated method stub
		boolean result = false;
		if (name.length() != 0) {
			result = true;
		}
		
		try{
		    myClient = new Client(address, PORT, name, this);
		    System.out.println("Clinet successfully started");
		    lobbyScene = new LobbyView();
			gameStage.setScene(lobbyScene.init());
		}catch (Exception e){
		    System.out.println("Starting client failed");
		}
		return result;
	}	
	
	public boolean connectUser(String username)
	{
		return lobbyScene.addUser(username);
	}
	
	public void hostGame(String name) {
		// TODO Auto-generated method stub
		lobbyScene = new LobbyView();
		gameStage.setScene(lobbyScene.init());
		
		try {									// @Nico was here I'm trying to create a server
			System.out.println("Server Creation Started");
			Server hostServer = new Server(PORT); // TODO: hardcoded port
		} catch (IOException e) { 				// author Nico was here too
			System.err.println("Server Creation Failed");
		}	
		
		try {
		    myClient = new Client("localhost", PORT, name, this);
		    System.out.println("Client started");
		}catch (Exception e){
		    System.err.println("Failed to start client");
		}
		
		lobbyScene.addUser(name);
		// lobbyScene.setStyle();
	}

	public void exitGame() {
		gameStage.close();
		System.exit(0);
	}

	public boolean beginGame(Integer computerCount, ArrayList<String> playerList, ArrayList<String> observerList)
			throws UnknownHostException {
		// TODO Auto-generated method stub
		boolean result = false;
		sideLength = 5;
		if (playerList.size() + computerCount == 6) {
			sideLength = 7;
		}
		gameBoard = new Board(sideLength);
		int playerCount = computerCount + playerList.size();
		//TODO add computers to playerlist
		gameBoard.players = new ArrayList<Player>();
		int i = 0;
		int j = 0;
		for (String p : playerList) {
			gameBoard.players.add(new Player(p, InetAddress.getLocalHost().toString(), i));
			Tank newTank = new Tank(j++, i, null, null);
			Scout newScout = new Scout(j++, i, null, null);
			Sniper newSniper = new Sniper(j++, i, null, null);
			gameBoard.players.get(i).robotList.add(newTank);
			gameBoard.players.get(i).robotList.add(newScout);
			gameBoard.players.get(i).robotList.add(newSniper);
			gameBoard.players.get(i).robotList.sort(new Comparator<Robot>() {

				@Override
				public int compare(Robot r1, Robot r2) {
					return r2.movement - r1.movement;
				}

			});
			i++;
		}
		if (playerCount == 2 || playerCount == 3) {
			result = true;
			gameScene = new GameView();
			// TODO Add computers to player list;
			gameStage.setScene(gameScene.init(playerList, observerList));
			// Set starting position for two players
			if (playerCount == 2) {
				gameBoard.gameBoard[0][4].addRobot(gameBoard.players.get(0).robotList.get(0));
				gameBoard.gameBoard[0][4].addRobot(gameBoard.players.get(0).robotList.get(1));
				gameBoard.gameBoard[0][4].addRobot(gameBoard.players.get(0).robotList.get(2));
				gameBoard.players.get(0).setFogOfWar(sideLength);
				gameBoard.gameBoard[8][4].addRobot(gameBoard.players.get(1).robotList.get(0));
				gameBoard.gameBoard[8][4].addRobot(gameBoard.players.get(1).robotList.get(1));
				gameBoard.gameBoard[8][4].addRobot(gameBoard.players.get(1).robotList.get(2));
				gameBoard.players.get(1).setFogOfWar(sideLength);
			} else {
				// TODO playercount == 3
			}

			gameBoard.playerTurn = 0;
			gameBoard.currentRobot = 0;
			gameScene.updateGame(gameBoard);

		} else if (playerCount == 6) {
			result = true;
			gameScene = new GameView();
			// TODO Add computers to player list;
			gameStage.setScene(gameScene.init(playerList, observerList));

		}		
		return result;
	}


	// Needs a way to determine which player is being displayed.
	public int[] getRobotHealths() {
		int[] healths = { 1, 2, 3 };
		return healths;
	}

	public int moveRobot(int x, int y) {
		// TODO Get player
		int result = 0;
		if (gameBoard.players.get(gameBoard.playerTurn).robotList.get(gameBoard.currentRobot).health > 0) {

			Robot robotToMove = gameBoard.players.get(gameBoard.playerTurn).robotList.get(gameBoard.currentRobot);
			Tile destination = gameBoard.gameBoard[x][y];
			int distance = gameBoard.movePossible(robotToMove, destination);
			if (distance > 0) {

				gameBoard.players.get(gameBoard.playerTurn).moveRobot(robotToMove, destination, distance);
				gameBoard.players.get(gameBoard.playerTurn).setFogOfWar(sideLength);
				gameScene.updateGame(gameBoard);
				result = robotToMove.movementLeft;
			}
		}

		return result;

	}

	public void endTurn() {
		do {
			gameBoard.playerTurn++;
			if (gameBoard.playerTurn == gameBoard.players.size()) {
				gameBoard.playerTurn = 0;
				gameBoard.currentRobot++;

				if (gameBoard.currentRobot == gameBoard.players.get(gameBoard.playerTurn).robotList.size()) {
					gameBoard.currentRobot = 0;
					for(Robot r : defeatedRobots)
					{
						gameBoard.players.get(r.teamNumber).robotList.remove(r);
						gameBoard.players.get(r.teamNumber).robotList.add(r);			
					}
					defeatedRobots.clear();
				}
			}
		} while (gameBoard.players.get(gameBoard.playerTurn) != null
				&& gameBoard.players.get(gameBoard.playerTurn).robotList.get(gameBoard.currentRobot).health < 1);
		
		gameBoard.players.get(gameBoard.playerTurn).robotList
				.get(gameBoard.currentRobot).movementLeft = gameBoard.players.get(gameBoard.playerTurn).robotList
						.get(gameBoard.currentRobot).movement;
		
		gameBoard.players.get(gameBoard.playerTurn).hasShot = false;
		//TODO Set fog of war for everyone?
		gameBoard.players.get(gameBoard.playerTurn).setFogOfWar(sideLength);
		gameScene.updateGame(gameBoard);
	}

	public void attackTile(int x, int y) {
		// TODO Get player
		//int result = 0;
		if (gameBoard.players.get(gameBoard.playerTurn).robotList.get(gameBoard.currentRobot).health > 0) {

			Robot attackingRobot = gameBoard.players.get(gameBoard.playerTurn).robotList.get(gameBoard.currentRobot);
			Tile target = gameBoard.gameBoard[x][y];
			boolean possible = gameBoard.attackPossible(attackingRobot, target);
			if (possible) {
				if(attackingRobot.location.equals(target))
				{
					attackingRobot.health = 0;
					attackingRobot.deathCount++;		
					//TODO Do suicides give kills?
					target.robotList.remove(attackingRobot);
				}
				
				//for(Robot r : gameBoard.gameBoard[x][y].robotList)
				for(ListIterator<Robot> rIterator = gameBoard.gameBoard[x][y].robotList.listIterator(); rIterator.hasNext();)
				{
					Robot r = rIterator.next();
					r.health -= attackingRobot.attack;
					if(r.health <= 0)
					{
						r.health = 0;
						r.deathCount++;
						rIterator.remove();
						//TODO Green scout died, red sniper killed by green sniper, red tank did not go next
						if (gameBoard.players.get(gameBoard.playerTurn).robotList.indexOf(r) > gameBoard.currentRobot
								|| (gameBoard.players.get(r.teamNumber).robotList.indexOf(r) == gameBoard.currentRobot
										&& r.teamNumber > gameBoard.playerTurn))
						{
							gameBoard.players.get(r.teamNumber).robotList.remove(r);
							gameBoard.players.get(r.teamNumber).robotList.add(r);
						}
						else
						{
							defeatedRobots.add(r);
						}
						
						attackingRobot.killCount++;						
					}
				}
				gameBoard.players.get(gameBoard.playerTurn).setFogOfWar(sideLength);
				gameScene.updateGame(gameBoard);
				if(attackingRobot.health<= 0)
				{
					endTurn();
				}
			}
			
		}

	}
}
