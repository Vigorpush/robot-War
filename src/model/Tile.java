package model;

import java.util.LinkedList;
import java.util.List;

public class Tile {

	public int xPosition;
	public int yPosition;
	public List<Robot> robotList;
	
	public Tile(int x, int y)
	{		
		xPosition = x;
		yPosition = y;
		robotList = new LinkedList<Robot>();
	}
	
	public void addRobot(Robot newRobot)
	{		
		robotList.add(newRobot);
	}
}
