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
			if (r.health > 0) {
				
				int lowerXBound = r.location.xPosition - r.range;
				if (lowerXBound < 0) {
					lowerXBound = 0;
				}
				int lowerYBound = r.location.yPosition - r.range;
				if (lowerYBound < 0) {
					lowerYBound = 0;
				}
				int offsetRight = 0;
				int offsetLeft = 0;
				for (int currentYCoor = lowerYBound; currentYCoor <= r.location.yPosition+r.range && currentYCoor < sideLength * 2 - 1; currentYCoor++) {
					if (currentYCoor < r.location.yPosition) {
						for (int currentXCoor = lowerXBound; currentXCoor <= r.location.xPosition +offsetRight && currentXCoor < sideLength*2-1; currentXCoor++) {
							fogOfWar[currentXCoor][currentYCoor] = true;
						}
						offsetRight++;
					} else {

						for (int currentXCoor = lowerXBound+offsetLeft; currentXCoor <= r.location.xPosition +offsetRight && currentXCoor < (sideLength * 2 - 1); currentXCoor++) {
							fogOfWar[currentXCoor][currentYCoor] = true;
						}
						offsetLeft++;
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
