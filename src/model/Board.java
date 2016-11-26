package model;

import java.util.List;

public class Board {

	public Tile[][] gameBoard;
	public List<Observer> observers;
	public List<Player> players;
	public int playerTurn;
	public int currentRobot;

	public Board(int sideLength) {
		gameBoard = new Tile[sideLength * 2 - 1][sideLength * 2 - 1];
		for (int currentYCoor = 0; currentYCoor < sideLength * 2 - 1; currentYCoor++) {
			if (currentYCoor < sideLength) {
				for (int currentXCoor = 0; currentXCoor < currentYCoor + sideLength; currentXCoor++) {
					Tile currentTile = new Tile(currentXCoor, currentYCoor);
					gameBoard[currentXCoor][currentYCoor] = currentTile;
				}

			} else {

				for (int currentXCoor = 0; currentXCoor < (sideLength * 2 - 1) - currentYCoor - 1
						+ sideLength; currentXCoor++) {

					int x = currentXCoor + currentYCoor - 4;
					Tile currentTile = new Tile(x, currentYCoor);
					gameBoard[x][currentYCoor] = currentTile;

				}

			}
		}
	}

	public int movePossible(Robot robotToMove, Tile destination) {
		int result = -1;
		int xDistance = robotToMove.location.xPosition - destination.xPosition;
		int yDistance = robotToMove.location.yPosition - destination.yPosition;
		boolean xPossible = ((robotToMove.movementLeft) >= Math.abs(xDistance));
		boolean yPossible = ((robotToMove.movementLeft) >= Math.abs(yDistance));
		if (xPossible && yPossible) {
			if (xDistance * yDistance >= 0 ) {
				xDistance = Math.abs(xDistance);
				yDistance = Math.abs(yDistance);
				if (xDistance > yDistance) {
					result = xDistance;
				} else {
					result = yDistance;
				}
			}
			else if(Math.abs(xDistance) + Math.abs(yDistance) < robotToMove.movementLeft)
			{
				result = xDistance = Math.abs(xDistance) + Math.abs(yDistance);
			}
		}
		return result;
	}

	public boolean attackPossible(Robot attackingRobot, Tile target) {
		boolean result = false;
		int xDistance = Math.abs(attackingRobot.location.xPosition - target.xPosition);
		int yDistance = Math.abs(attackingRobot.location.yPosition - target.yPosition);
		boolean xPossible = ((attackingRobot.range) >= xDistance);
		boolean yPossible = ((attackingRobot.range) >= yDistance);

		if (xPossible && yPossible) {
			result = true;
		}

		return result;
	}
}
