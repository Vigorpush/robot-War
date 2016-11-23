package model;

public class Observer extends User {

	public Observer(String name, String IP) {
		super(name, IP);
	}

	public void setFogOfWar(int sideLength) {
		fogOfWar = new boolean[sideLength*2-1][sideLength*2-1];
		for(int i = 0; i <fogOfWar.length; i++)
		{
			for(int j = 0; j< fogOfWar.length; j++)
			{
				fogOfWar[i][j] = true;
			}
		}
	}

	//TODO Leave game was cut because it was a redundant method
}
