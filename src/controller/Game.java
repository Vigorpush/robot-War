package controller;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;

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

	public static Stage gameStage;
	public static Board gameBoard;
	public static GameView gameScene;
	public static ArrayList<Robot> defeatedRobots = new ArrayList<Robot>();
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
		// TODO remove these two lines when done
		String test = "file:///" + file.getAbsolutePath().replace("\\", "\"/\"").replaceFirst("\"", "") + "\"";
		System.out.println(test);
		gameStage.getScene().getStylesheets().add("file:///" + file.getAbsolutePath().replace("\\", "/"));
		gameStage.setTitle("Robot War");
		gameStage.show();
	}

	public boolean joinGame(String name, String address) {
		// TODO Auto-generated method stub
		boolean result = false;
		if (name.length() != 0) {
			result = true;
		}
		return result;
	}

	public void hostGame(String name) {
		// TODO Auto-generated method stub
		LobbyView lobbyScene = new LobbyView();
		gameStage.setScene(lobbyScene.init());
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
		int sideLength = 5;
		if (playerList.size() + computerCount == 6) {
			sideLength = 7;
		}
		gameBoard = new Board(sideLength);
		int playerCount = computerCount + playerList.size();
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
				gameBoard.gameBoard[8][4].addRobot(gameBoard.players.get(1).robotList.get(0));
				gameBoard.gameBoard[8][4].addRobot(gameBoard.players.get(1).robotList.get(1));
				gameBoard.gameBoard[8][4].addRobot(gameBoard.players.get(1).robotList.get(2));
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
		// TODO Statement for testing GameView. Remove when finished.
		else {
			result = true;
			gameScene = new GameView();
			// TODO Add computers to player list;
			gameStage.setScene(gameScene.init(playerList, observerList));
		}
		return result;
	}

	private void runGame() {

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
		gameScene.updateGame(gameBoard);
	}

	public void attackTile(int x, int y) {
		// TODO Get player
		int result = 0;
		if (gameBoard.players.get(gameBoard.playerTurn).robotList.get(gameBoard.currentRobot).health > 0) {

			Robot attackingRobot = gameBoard.players.get(gameBoard.playerTurn).robotList.get(gameBoard.currentRobot);
			Tile target = gameBoard.gameBoard[x][y];
			boolean possible = gameBoard.attackPossible(attackingRobot, target);
			if (possible) {
				if(attackingRobot.location.equals(target))
				{
					attackingRobot.health = 0;
					attackingRobot.deathCount++;
					target.robotList.remove(attackingRobot);
				}
				for(Robot r : gameBoard.gameBoard[x][y].robotList)
				{
					r.health -= attackingRobot.attack;
					if(r.health <= 0)
					{
						r.health = 0;
						r.deathCount++;
						gameBoard.gameBoard[x][y].removeRobot(r);
						if(gameBoard.players.get(gameBoard.playerTurn).robotList.indexOf(r) > gameBoard.currentRobot || (gameBoard.players.get(gameBoard.playerTurn).robotList.indexOf(r) == gameBoard.currentRobot && r.teamNumber > gameBoard.playerTurn))
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
				gameScene.updateGame(gameBoard);
				if(attackingRobot.health<= 0)
				{
					endTurn();
				}
			}
			
		}

	}
}
