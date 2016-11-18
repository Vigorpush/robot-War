package model;

import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

public class Board {

	public Tile[][] gameBoard;
	public List<User> users;

	public Board(int sideLength) {
		gameBoard = new Tile[sideLength*2-1][sideLength*2-1];
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
		int xDistance = Math.abs(robotToMove.location.xPosition - destination.xPosition);
		int yDistance = Math.abs(robotToMove.location.yPosition - destination.yPosition);
		boolean xPossible = ((robotToMove.movement - robotToMove.distanceTraveled) >= xDistance);
		boolean yPossible = ((robotToMove.movement - robotToMove.distanceTraveled) >= yDistance);
		if (xPossible && yPossible) {
			if (xDistance > yDistance) {
				result = xDistance;
			} else {
				result = yDistance;
			}
		}
		return result;
	}
}
