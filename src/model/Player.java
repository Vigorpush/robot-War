package model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Player extends User {

	public List<Robot> robotList;
	public boolean hasShot;
	public int teamNumber;
	public boolean isTurn;

	public Player(String name, String IP, int teamNumber) {
		super(name, IP);
		this.teamNumber = teamNumber;
		robotList = new LinkedList<Robot>();
	}

	public void moveRobot(Robot robotToMove, Tile destination, int distance) {
		robotToMove.location.removeRobot(robotToMove);
		robotToMove.movementLeft -= distance;
		destination.addRobot(robotToMove);
	}

	public void setFogOfWar(int sideLength) {
		fogOfWar = new boolean[sideLength * 2 - 1][sideLength * 2 - 1];
		for (Robot r : robotList) {
			
			// A series of if statements to make sure it does not try to set
			// nonexistent variables
			int lowerXBound = r.location.xPosition - r.range;
			if (lowerXBound < 0) {
				lowerXBound = 0;
			}
			int lowerYBound = r.location.yPosition - r.range;
			if (lowerYBound < 0) {
				lowerYBound = 0;
			}
			int upperXBound = r.location.xPosition + r.range;
			if (upperXBound > sideLength * 2 - 2) {
				upperXBound = sideLength * 2 - 2;
			}
			int upperYBound = r.location.yPosition + r.range;
			if (upperYBound > sideLength * 2 - 2) {
				upperYBound = sideLength * 2 - 2;
			}
			for (int i = lowerXBound; i <= upperXBound; i++) {
				for (int j = lowerYBound; j <= upperYBound; j++) {
					int xDistance = r.location.xPosition - i;
					int yDistance = r.location.yPosition - j;
					boolean xPossible = ((r.range) >= Math.abs(xDistance));
					boolean yPossible = ((r.range) >= Math.abs(yDistance));
					if (xPossible && yPossible) {
						if (xDistance * yDistance > 0 || Math.abs(xDistance) + Math.abs(yDistance) < r.movementLeft) {
							fogOfWar[i][j] = true;
						}
					}

				}
			}
		}
	}

	// TODO The attack method was extraneous, and required the player to access
	// information they did not have, moved to controller.

	// TODO endTurn is not a method the player can do, because it does not have
	// access to variables
	// it needs to end a turn. migrated to controller.

	// TODO forfeit is not something the player needs to do, migrated to
	// controller.
}
