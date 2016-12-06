package controller;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ListIterator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.Board;
import model.Observer;
import model.Player;
import model.Robot;
import model.Scout;
import model.Sniper;
import model.Tank;
import model.Tile;
import view.GameView;
import view.InspectView;
import view.LobbyView;
import view.PostGameView;
import view.StartView;

@SuppressWarnings("unused")
public class Game extends Application {

	public static final int PORT = 32222;
	public static Stage gameStage;
	public static Board gameBoard;
	public static GameView gameScene;
	public static int sideLength;
	public static Client myClient;
	public static LobbyView lobbyScene;
	public static boolean isHost;
	public static String playerName;

	public static void main(String[] args) {

		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {

		gameStage = stage;
		StartView initialScene = new StartView();
		gameStage.setScene(initialScene.init());
		gameStage.setMaximized(true);

		String css = this.getClass().getResource("/css/StartView.css").toExternalForm();
		gameStage.centerOnScreen();
		gameStage.getScene().getStylesheets().clear();
		gameStage.getScene().getStylesheets().add(css);
		gameStage.setTitle("Robot War");
		gameStage.show();
	}

	// TODO store static variable about who you are
	public boolean joinGame(String name, String address) {
		Game.playerName = name;
		Game.isHost = false;
		// TODO Auto-generated method stub
		boolean result = false;
		if (name.length() != 0) {
			result = true;
		}

		try {
			myClient = new Client(address, PORT, name, this);
			System.out.println("Clinet successfully started");
			lobbyScene = new LobbyView();
			gameStage.setScene(lobbyScene.init(name));
			gameStage.setMaximized(false);
			gameStage.setMaximized(true);
		} catch (Exception e) {
			System.out.println("Starting client failed");
		}
		return result;
	}

	public void connectUser(ArrayList<String> observerList, ArrayList<String> playerList) {
		lobbyScene.updateUserLists(observerList, playerList);
	}

	public void hostGame(String name) {
		Game.playerName = name;
		// TODO Auto-generated method stub
		Game.isHost = true;
		lobbyScene = new LobbyView();
		gameStage.setScene(lobbyScene.init(name));
		gameStage.setMaximized(false);
		gameStage.setMaximized(true);

		try { // @Nico was here I'm trying to create a server
			System.out.println("Server Creation Started");
			Server hostServer = new Server(PORT); // TODO: hardcoded port
		} catch (IOException e) { // author Nico was here too
			System.err.println("Server Creation Failed");
		}

		try {
			myClient = new Client("localhost", PORT, name, this);
			System.out.println("Client started");
		} catch (Exception e) {
			System.err.println("Failed to start client");
		}

		lobbyScene.addUser(name);

		// lobbyScene.setStyle();
	}

	public void exitGame() {
		gameStage.close();
		System.exit(0);
	}

	public boolean signalGameStart(ArrayList<String> playerList,
			ArrayList<String> observerList) {
		int playerCount = playerList.size();

		if (playerCount == 2 || playerCount == 3 || playerCount == 6) {
			if (Game.isHost) {
				System.out.println("HOST HAS STARTED GAME.  TELLING CLIENTS");
				Game.myClient.getConnection().hostBeginGame();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean beginGame(ArrayList<String> playerList, ArrayList<String> observerList)
			throws UnknownHostException {
		// TODO Auto-generated method stub
		System.out.println("IS HOST is  " + isHost);

		boolean result = false;
		sideLength = 5;
		if (playerList.size() == 6) {
			sideLength = 7;
		}
		gameBoard = new Board(sideLength);
		int playerCount = playerList.size();
		// TODO add computers to playerlist
		gameBoard.players = new ArrayList<Player>();
		int i = 0;
		int j = 0;
		for (String p : playerList) {
			gameBoard.players.add(new Player(p, InetAddress.getLocalHost().toString(), i));			
			Scout newScout = new Scout(j++, i, null, null);
			Sniper newSniper = new Sniper(j++, i, null, null);
			Tank newTank = new Tank(j++, i, null, null);
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
		i = 0;
		gameBoard.observers = new ArrayList<Observer>();		
		for (String o : observerList) {
			gameBoard.observers.add(new Observer(o, InetAddress.getLocalHost().toString()));	
			gameBoard.observers.get(i).setFogOfWar(sideLength);;
		}
		if (playerCount == 2 || playerCount == 3) {
			result = true;
			gameScene = new GameView();
			// TODO Add computers to player list;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					gameStage.setScene(gameScene.init(playerList, observerList, playerName));
					gameStage.setMaximized(false);
					gameStage.setMaximized(true);
				}
			});
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
				gameBoard.gameBoard[0][4].addRobot(gameBoard.players.get(0).robotList.get(0));
				gameBoard.gameBoard[0][4].addRobot(gameBoard.players.get(0).robotList.get(1));
				gameBoard.gameBoard[0][4].addRobot(gameBoard.players.get(0).robotList.get(2));
				gameBoard.players.get(0).setFogOfWar(sideLength);
				gameBoard.gameBoard[8][8].addRobot(gameBoard.players.get(1).robotList.get(0));
				gameBoard.gameBoard[8][8].addRobot(gameBoard.players.get(1).robotList.get(1));
				gameBoard.gameBoard[8][8].addRobot(gameBoard.players.get(1).robotList.get(2));
				gameBoard.players.get(1).setFogOfWar(sideLength);
				gameBoard.gameBoard[4][0].addRobot(gameBoard.players.get(2).robotList.get(0));
				gameBoard.gameBoard[4][0].addRobot(gameBoard.players.get(2).robotList.get(1));
				gameBoard.gameBoard[4][0].addRobot(gameBoard.players.get(2).robotList.get(2));
				gameBoard.players.get(2).setFogOfWar(sideLength);
			}

			gameBoard.playerTurn = 0;
			gameBoard.currentRobot = 0;
			gameScene.updateGame(gameBoard);

		} else if (playerCount == 6) {
			result = true;
			gameScene = new GameView();
			// TODO Add computers to player list;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					gameStage.setScene(gameScene.init(playerList, observerList, playerName));
					gameStage.setMaximized(false);
					gameStage.setMaximized(true);
				}
			});
			// Set starting position for six players

			gameBoard.gameBoard[0][6].addRobot(gameBoard.players.get(0).robotList.get(0));
			gameBoard.gameBoard[0][6].addRobot(gameBoard.players.get(0).robotList.get(1));
			gameBoard.gameBoard[0][6].addRobot(gameBoard.players.get(0).robotList.get(2));
			gameBoard.players.get(0).setFogOfWar(sideLength);
			gameBoard.gameBoard[12][6].addRobot(gameBoard.players.get(1).robotList.get(0));
			gameBoard.gameBoard[12][6].addRobot(gameBoard.players.get(1).robotList.get(1));
			gameBoard.gameBoard[12][6].addRobot(gameBoard.players.get(1).robotList.get(2));
			gameBoard.players.get(1).setFogOfWar(sideLength);
			gameBoard.gameBoard[12][12].addRobot(gameBoard.players.get(2).robotList.get(0));
			gameBoard.gameBoard[12][12].addRobot(gameBoard.players.get(2).robotList.get(1));
			gameBoard.gameBoard[12][12].addRobot(gameBoard.players.get(2).robotList.get(2));
			gameBoard.players.get(2).setFogOfWar(sideLength);
			gameBoard.gameBoard[6][0].addRobot(gameBoard.players.get(3).robotList.get(0));
			gameBoard.gameBoard[6][0].addRobot(gameBoard.players.get(3).robotList.get(1));
			gameBoard.gameBoard[6][0].addRobot(gameBoard.players.get(3).robotList.get(2));
			gameBoard.players.get(3).setFogOfWar(sideLength);
			gameBoard.gameBoard[6][12].addRobot(gameBoard.players.get(4).robotList.get(0));
			gameBoard.gameBoard[6][12].addRobot(gameBoard.players.get(4).robotList.get(1));
			gameBoard.gameBoard[6][12].addRobot(gameBoard.players.get(4).robotList.get(2));
			gameBoard.players.get(4).setFogOfWar(sideLength);
			gameBoard.gameBoard[0][0].addRobot(gameBoard.players.get(5).robotList.get(0));
			gameBoard.gameBoard[0][0].addRobot(gameBoard.players.get(5).robotList.get(1));
			gameBoard.gameBoard[0][0].addRobot(gameBoard.players.get(5).robotList.get(2));
			gameBoard.players.get(5).setFogOfWar(sideLength);
			gameBoard.playerTurn = 0;
			gameBoard.currentRobot = 0;
			gameScene.updateGame(gameBoard);

		}
		return result;
	}

	// Needs a way to determine which player is being displayed.
	public int[] getRobotHealths() {

		boolean found = false;
		int playerIndex = 0;
		while (!found && playerIndex < gameBoard.players.size()) {
			if (gameBoard.players.get(playerIndex).name.equals(playerName)) {
				found = true;
			} else {
				playerIndex++;
			}
		}
		if(playerIndex == gameBoard.players.size())
		{
			playerIndex = gameBoard.playerTurn;
		}
		int[] healths = new int[gameBoard.players.get(playerIndex).robotList.size()];
		for (int i = 0; i < healths.length; i++) {
			Robot r = gameBoard.players.get(playerIndex).robotList.get(i);
			healths[(r.id)%gameBoard.players.get(playerIndex).robotList.size()] = r.health;
		}
		return healths;
	}

	public int moveRobot(int x, int y) {
		// TODO Get player
		int result = 0;
		if (gameBoard.players.get(gameBoard.playerTurn).name.equals(playerName)) {

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

		Game.myClient.getConnection().sendGameState(gameBoard);
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
					for (Robot r : gameBoard.defeatedRobots) {
						boolean found = false;
						int i = 0;
						while (!found) {
							if (gameBoard.players.get(r.teamNumber).robotList.get(i).id == r.id) {
								found = true;
							} else {
								i++;
							}
						}
						gameBoard.players.get(r.teamNumber).robotList.remove(i);
						gameBoard.players.get(r.teamNumber).robotList.add(r);
					}
					gameBoard.defeatedRobots.clear();
				}
			}
		} while (gameBoard.players.get(gameBoard.playerTurn) != null
				&& gameBoard.players.get(gameBoard.playerTurn).robotList.get(gameBoard.currentRobot).health < 1);

		gameBoard.players.get(gameBoard.playerTurn).robotList
				.get(gameBoard.currentRobot).movementLeft = gameBoard.players.get(gameBoard.playerTurn).robotList
						.get(gameBoard.currentRobot).movement;

		gameBoard.players.get(gameBoard.playerTurn).hasShot = false;
		// TODO Set fog of war for everyone?
		gameBoard.players.get(gameBoard.playerTurn).setFogOfWar(sideLength);
		gameScene.updateGame(gameBoard);

		Game.myClient.getConnection().sendGameState(gameBoard);
		// TODO This is a hack because without it, if you end turn immediately without doing anything on the first turn of the game, the game desysncs
		Game.myClient.getConnection().sendGameState(gameBoard);
	}

	public void attackTile(int x, int y) {
		// TODO Get player
		// int result = 0;
		if (gameBoard.players.get(gameBoard.playerTurn).name.equals(playerName)
				&& !gameBoard.players.get(gameBoard.playerTurn).hasShot) {
			gameBoard.players.get(gameBoard.playerTurn).hasShot = true;

			Robot attackingRobot = gameBoard.players.get(gameBoard.playerTurn).robotList.get(gameBoard.currentRobot);
			Tile target = gameBoard.gameBoard[x][y];
			boolean possible = gameBoard.attackPossible(attackingRobot, target);
			if (possible) {
				if (attackingRobot.location.equals(target)) {
					attackingRobot.health = 0;
					attackingRobot.deathCount++;
					// TODO Do suicides give kills?
					target.robotList.remove(attackingRobot);
				}

				// for(Robot r : gameBoard.gameBoard[x][y].robotList)
				for (ListIterator<Robot> rIterator = gameBoard.gameBoard[x][y].robotList.listIterator(); rIterator
						.hasNext();) {
					Robot r = rIterator.next();
					r.health -= attackingRobot.attack;
					if (r.health <= 0) {
						r.health = 0;
						r.deathCount++;
						rIterator.remove();
						// TODO Green scout died, red sniper killed by green
						// sniper, red tank did not go next
						if (gameBoard.players.get(gameBoard.playerTurn).robotList.indexOf(r) > gameBoard.currentRobot
								|| (gameBoard.players.get(r.teamNumber).robotList.indexOf(r) == gameBoard.currentRobot
										&& r.teamNumber > gameBoard.playerTurn)) {
							boolean found = false;
							int i = 0;
							while (!found) {
								if (gameBoard.players.get(r.teamNumber).robotList.get(i).id == r.id) {
									found = true;
								} else {
									i++;
								}
							}
							gameBoard.players.get(r.teamNumber).robotList.remove(i);
							gameBoard.players.get(r.teamNumber).robotList.add(r);
						} else {
							gameBoard.defeatedRobots.add(r);
						}

						attackingRobot.killCount++;
					}
				}
				gameBoard.players.get(gameBoard.playerTurn).setFogOfWar(sideLength);
				gameScene.updateGame(gameBoard);
				if (attackingRobot.health <= 0) {
					endTurn();
				}
			}

		}
		Game.myClient.getConnection().sendGameState(gameBoard);

	}

	/**
	 * Method that checks if a player has lost, and creates an observer for them
	 * if they have.
	 * 
	 * @param index
	 *            The team number of the losing player
	 * @return If they lost
	 */
	public boolean playerLose(int index) {
		boolean result = true;
		for (Robot r : gameBoard.players.get(index).robotList) {
			if (r.health != 0) {
				result = false;
			}
		}

		if (result) {
			Observer newObserver = new Observer(gameBoard.players.get(index).name, gameBoard.players.get(index).IP);
			gameOver();
		}
		if(gameBoard.gameover)
		{
			if(isHost)
			{
				Server test;
				try {
					test = new Server(PORT);
					test.shutdownServer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			StartView initialScene = new StartView();
			gameStage.setScene(initialScene.init());
			gameStage.setMaximized(false);
			gameStage.setMaximized(true);
		}
		Game.myClient.getConnection().sendGameState(gameBoard);
		return result;
	}

	/**
	 * Method that destroys all a player's robots and calls playerLose to turn
	 * them into an observer
	 */
	public void forfeit() {
		// TODO Get controlling player
		for (Robot r : gameBoard.players.get(gameBoard.playerTurn).robotList) {
			r.health = 0;
		}
		playerLose(gameBoard.playerTurn);
		Game.myClient.getConnection().sendGameState(gameBoard);
	}

	/**
	 * Method that checks if more than one player has living robots, and if not,
	 * ends the game
	 */
	public void gameOver() {
		boolean result = true;
		Robot livingRobot = null;
		for (Player p : gameBoard.players) {
			for (Robot r : p.robotList) {
				if (r.health > 0 && livingRobot == null) {
					livingRobot = null;
				} else if (r.health > 0 && livingRobot.teamNumber != r.teamNumber) {
					result = false;
				}
			}
		}
		if (result) {
			//TODO Use postGameScrene instead of jsut sending back to start game 
			/*
			PostGameView postGameScene = new PostGameView();
			gameStage.setScene(postGameScene.init());
			*/
			gameBoard.gameover = true;
		}
		Game.myClient.getConnection().sendGameState(gameBoard);
	}

	public void connectionRejected() {
		// Pop up a dialogue box that says we were rejected and return to the
		// start view.
		System.out.println("Connection rejected: user name has already been used");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				StartView initialScene = new StartView();
				gameStage.setScene(initialScene.init());
				gameStage.setMaximized(false);
				gameStage.setMaximized(true);
			}
		});
	}

	public void updateUsers(ArrayList<String> playerList, ArrayList<String> observerList) {
		Game.myClient.updateUsers(playerList, observerList);

	}

	public void recieveGameState(Board incomingState) {
		for (int i = 0; i < incomingState.players.size(); i++) {
			System.out.println("CLIENT RECIEVED STATE : " + incomingState.players.get(i).name);
		}
		System.out.println("PLAYER TURN: " + incomingState.playerTurn);

		GameView newView = new GameView();
		gameBoard = incomingState;
		newView.updateGame(incomingState);
	}

	public void inspectTile(int x, int y) {

		boolean found = false;
		int playerIndex = 0;
		while (!found) {
			if (gameBoard.players.get(playerIndex).name.equals(playerName)) {
				found = true;
				if (gameBoard.players.get(playerIndex).fogOfWar[x][y]) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							InspectView popup = new InspectView();
							popup.init(gameStage, x, y);
						}
					});
				}
			} else {
				playerIndex++;
			}
		}

	}
}
