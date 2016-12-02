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
import model.Observer;
import model.Player;
import model.Robot;
import model.Scout;
import model.Sniper;
import model.Tank;
import model.Tile;
import view.GameView;
import view.LobbyView;
import view.PostGameView;
import view.StartView;

@SuppressWarnings("unused")
/**
 * The controller class of the project, Game handles all communication between the various pieces of the project.
 * @author Niklaas
 *
 */
public class Game extends Application {
    
    public static final int PORT = 32222;
	// The window that the game is displayed in
	public static Stage gameStage;
	// The board that stores all the tiles, players, and turns of the game
	public static Board gameBoard;
	// The scene used when the game has begun
	public static GameView gameScene;
	// The array that tracks defeated robots to be removed at the end of the turn
	public static ArrayList<Robot> defeatedRobots = new ArrayList<Robot>();
	// The number of tiles on each side of the board.
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
	//TODO return string to display specific error message?
	/**
	 * The method that is called when someone wishes to join the lobby of a game over the network
	 * @param name: The username of the person wishing to join
	 * @param address: The IP address of the lobby they wish to join
	 * @return: If they were able to join
	 */
	public boolean joinGame(String name, String address) {	
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
	
	public void connectUser(ArrayList<String> observerList, ArrayList<String> playerList)
	{
		lobbyScene.updateUserLists(observerList, playerList);
	}
	
	/**
	 * The method that is called when someone wishes to host a game.
	 * @param name The username of the host
	 */
	public void hostGame(String name) {
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
		lobbyScene.addUser("Fake");
		// lobbyScene.setStyle();
	}

	//TODO Graceful shut down?
	/**
	 * The method that is called when someone quits the game
	 */
	public void exitGame() {
		gameStage.close();
		System.exit(0);
	}

	/**
	 * The method that is called when the host clicks begin game.
	 * @param computerCount: The number of computer players that will be in the game.
	 * @param playerList: The list of player usernames that will be in the game
	 * @param observerList: The list of observer usernames that will be in the game
	 * @return True if the proper number of players were present to run the game.
	 * @throws UnknownHostException
	 */
	public boolean beginGame(Integer computerCount, ArrayList<String> playerList, ArrayList<String> observerList)
			throws UnknownHostException {
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
		//A loop to create the players, add the players to the board, and add the robots to the players
		for (String p : playerList) {
			gameBoard.players.add(new Player(p, InetAddress.getLocalHost().toString(), i));
			Tank newTank = new Tank(j++, i, null, null);
			Scout newScout = new Scout(j++, i, null, null);
			Sniper newSniper = new Sniper(j++, i, null, null);
			gameBoard.players.get(i).robotList.add(newTank);
			gameBoard.players.get(i).robotList.add(newScout);
			gameBoard.players.get(i).robotList.add(newSniper);
			//Sort the robots in the descending order of their movement
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
				gameBoard.gameBoard[0][4].addRobot(gameBoard.players.get(0).robotList.get(0));
				gameBoard.gameBoard[0][4].addRobot(gameBoard.players.get(0).robotList.get(1));
				gameBoard.gameBoard[0][4].addRobot(gameBoard.players.get(0).robotList.get(2));
				gameBoard.players.get(0).setFogOfWar(sideLength);
				gameBoard.gameBoard[4][0].addRobot(gameBoard.players.get(1).robotList.get(0));
				gameBoard.gameBoard[4][0].addRobot(gameBoard.players.get(1).robotList.get(1));
				gameBoard.gameBoard[4][0].addRobot(gameBoard.players.get(1).robotList.get(2));
				gameBoard.players.get(1).setFogOfWar(sideLength);
				gameBoard.gameBoard[8][8].addRobot(gameBoard.players.get(2).robotList.get(0));
				gameBoard.gameBoard[8][8].addRobot(gameBoard.players.get(2).robotList.get(1));
				gameBoard.gameBoard[8][8].addRobot(gameBoard.players.get(2).robotList.get(2));
				gameBoard.players.get(2).setFogOfWar(sideLength);
			}

			gameBoard.playerTurn = 0;
			gameBoard.currentRobot = 0;
			gameScene.updateGame(gameBoard);

		} else if (playerCount == 6) {
			result = true;
			gameScene = new GameView();
			// TODO Add computers to player list;
			gameStage.setScene(gameScene.init(playerList, observerList));
			gameBoard.gameBoard[0][4].addRobot(gameBoard.players.get(0).robotList.get(0));
			gameBoard.gameBoard[0][4].addRobot(gameBoard.players.get(0).robotList.get(1));
			gameBoard.gameBoard[0][4].addRobot(gameBoard.players.get(0).robotList.get(2));
			gameBoard.players.get(0).setFogOfWar(sideLength);
			gameBoard.gameBoard[8][4].addRobot(gameBoard.players.get(1).robotList.get(0));
			gameBoard.gameBoard[8][4].addRobot(gameBoard.players.get(1).robotList.get(1));
			gameBoard.gameBoard[8][4].addRobot(gameBoard.players.get(1).robotList.get(2));
			gameBoard.players.get(1).setFogOfWar(sideLength);
			gameBoard.gameBoard[4][0].addRobot(gameBoard.players.get(2).robotList.get(0));
			gameBoard.gameBoard[4][0].addRobot(gameBoard.players.get(2).robotList.get(1));
			gameBoard.gameBoard[4][0].addRobot(gameBoard.players.get(2).robotList.get(2));
			gameBoard.players.get(2).setFogOfWar(sideLength);
			gameBoard.gameBoard[8][8].addRobot(gameBoard.players.get(3).robotList.get(0));
			gameBoard.gameBoard[8][8].addRobot(gameBoard.players.get(3).robotList.get(1));
			gameBoard.gameBoard[8][8].addRobot(gameBoard.players.get(3).robotList.get(2));
			gameBoard.players.get(3).setFogOfWar(sideLength);
			gameBoard.gameBoard[0][0].addRobot(gameBoard.players.get(4).robotList.get(0));
			gameBoard.gameBoard[0][0].addRobot(gameBoard.players.get(4).robotList.get(1));
			gameBoard.gameBoard[0][0].addRobot(gameBoard.players.get(4).robotList.get(2));
			gameBoard.players.get(4).setFogOfWar(sideLength);
			gameBoard.gameBoard[4][8].addRobot(gameBoard.players.get(5).robotList.get(0));
			gameBoard.gameBoard[4][8].addRobot(gameBoard.players.get(5).robotList.get(1));
			gameBoard.gameBoard[4][8].addRobot(gameBoard.players.get(5).robotList.get(2));
			gameBoard.players.get(5).setFogOfWar(sideLength);
		}		
		return result;
	}


	// Needs a way to determine which player is being displayed.
	public int[] getRobotHealths() {
		int[] healths = { 1, 2, 3 };
		return healths;
	}

	/**
	 * Method to move a robot to the selected tile if possible
	 * @param x: The x position of the selected tile
	 * @param y: The y position of the selected tile
	 */
	public void moveRobot(int x, int y) {
		// TODO Get player
		if (gameBoard.players.get(gameBoard.playerTurn).robotList.get(gameBoard.currentRobot).health > 0) {

			Robot robotToMove = gameBoard.players.get(gameBoard.playerTurn).robotList.get(gameBoard.currentRobot);
			Tile destination = gameBoard.gameBoard[x][y];
			int distance = gameBoard.movePossible(robotToMove, destination);
			if (distance > 0) {

				gameBoard.players.get(gameBoard.playerTurn).moveRobot(robotToMove, destination, distance);
				gameBoard.players.get(gameBoard.playerTurn).setFogOfWar(sideLength);
				gameScene.updateGame(gameBoard);
			}
		}		

	}

	/**
	 * Method to end the current players turn and go to the next one
	 */
	public void endTurn() {
		//Do until you find a robot that is not dead.
		do {
			gameBoard.playerTurn++;
			//If you have reached the last player move to the next type of robot
			if (gameBoard.playerTurn == gameBoard.players.size()) {
				gameBoard.playerTurn = 0;
				gameBoard.currentRobot++;
				//If you have reached the last robot go back to the first
				if (gameBoard.currentRobot == gameBoard.players.get(gameBoard.playerTurn).robotList.size()) {
					gameBoard.currentRobot = 0;
					//When you reach the end of a round move all defeated robots to the bottom of their players robot lists.
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
		
		//Set the new player's movement left equal to their current robots movement
		gameBoard.players.get(gameBoard.playerTurn).robotList
				.get(gameBoard.currentRobot).movementLeft = gameBoard.players.get(gameBoard.playerTurn).robotList
						.get(gameBoard.currentRobot).movement;
		
		gameBoard.players.get(gameBoard.playerTurn).hasShot = false;
		//TODO Set fog of war for everyone?
		gameBoard.players.get(gameBoard.playerTurn).setFogOfWar(sideLength);
		gameScene.updateGame(gameBoard);
	}

	/**
	 * Method to have a robot attack the target tile if possible
	 * @param x The x value of the tile being attacked
	 * @param y The y value of the tile being attacked
	 */
	public void attackTile(int x, int y) {
		// TODO Get player
		if (gameBoard.players.get(gameBoard.playerTurn).robotList.get(gameBoard.currentRobot).health > 0) {

			Robot attackingRobot = gameBoard.players.get(gameBoard.playerTurn).robotList.get(gameBoard.currentRobot);
			Tile target = gameBoard.gameBoard[x][y];
			boolean possible = gameBoard.attackPossible(attackingRobot, target);
			//If the target tile is in range and the player has not yet attacked this turn
			if (possible&& !gameBoard.players.get(gameBoard.playerTurn).hasShot) {
				//If the robot is attacking itself, remove it before the loop to avoid errors
				if(attackingRobot.location.equals(target))
				{
					attackingRobot.health = 0;
					attackingRobot.deathCount++;		
					target.robotList.remove(attackingRobot);
				}
				//For each robot in the target tile
				for(ListIterator<Robot> rIterator = gameBoard.gameBoard[x][y].robotList.listIterator(); rIterator.hasNext();)
				{
					Robot r = rIterator.next();
					r.health -= attackingRobot.attack;
					//If a robot is killed
					if(r.health <= 0)
					{
						r.health = 0;
						r.deathCount++;
						rIterator.remove();
						// If the target robot has not already went this round,
						// remove it from the player's list immediately, else wait till the end of the round  
						if (gameBoard.players.get(r.teamNumber).robotList.indexOf(r) > gameBoard.currentRobot
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
						playerLose(r.teamNumber);
					}
				}
				gameBoard.players.get(gameBoard.playerTurn).setFogOfWar(sideLength);
				gameBoard.players.get(gameBoard.playerTurn).hasShot = true;
				gameScene.updateGame(gameBoard);
				if(attackingRobot.health<= 0)
				{
					endTurn();
				}
			}
			
		}

	}
	
	/**
	 * Method that checks if a player has lost, and creates an observer for them if they have.
	 * @param index The team number of the losing player
	 * @return If they lost
	 */
	public boolean playerLose(int index)
	{
		boolean result = true;
		for(Robot r : gameBoard.players.get(index).robotList)
		{
			if(r.health != 0)
			{
				result = false;
			}
		}
		
		if(result)
		{
			Observer newObserver = new Observer(gameBoard.players.get(index).name, gameBoard.players.get(index).IP);
			gameOver();
		}
		return result;
	}
	
	/**
	 * Method that destroys all a player's robots and calls playerLose to turn them into an observer
	 */
	public void forfeit()
	{
		//TODO Get controlling player
		for(Robot r : gameBoard.players.get(gameBoard.playerTurn).robotList)
		{
			r.health = 0;
		}
		playerLose(gameBoard.playerTurn);
	}
	
	/**
	 * Method that checks if more than one player has living robots, and if not, ends the game
	 */
	public void gameOver()
	{
		boolean result = true;
		Robot livingRobot = null;
		for(Player p : gameBoard.players)
		{
			for(Robot r : p.robotList)
			{
				if(r.health>0 && livingRobot == null)
				{
					livingRobot = null;
				}
				else if(r.health >0 && livingRobot.teamNumber != r.teamNumber)
				{
					result = false;
				}
			}
		}
		if(result)
		{
			PostGameView postGameScene = new PostGameView();
			gameStage.setScene(postGameScene.init());
		}
	}
}
