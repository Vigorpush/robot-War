package model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable{

	public String name;
	public String IP;
	public boolean isHost;
	public boolean[][] fogOfWar;
	
	public User(String name, String IP)
	{
		this.name = name;
		this.IP = IP;
	}
	
	public boolean inspectTile(Tile tile)
	{
		boolean result = false;
		if(fogOfWar[tile.xPosition][tile.yPosition])
		{
			result = true;
		}
		return result;		
	}
	
}
