
public class Robot {

	public Robot(int ID, int TEAM, String JPATH, String FPATH) {
		id = ID;
		teamNumber = TEAM;
		jsonPath = JPATH;
		fourthPath = FPATH;
		
		//name from json
		//location = new Tile();
		//matches won from json
		//deathCount from json
		//killcount from json
		health = 0;
		attack = 0;
		movement = 0;
		range = 0;
		distanceTraveled = 0;
	}

	public int id;
	public int teamNumber;
	public String jsonPath;
	public String fourthPath;
	public String name;
	//public Tile location;
	public int matchesWon;
	public int deathCount;
	public int killCount;
	public int health;
	public int attack;
	public int movement;
	public int range;
	public int distanceTraveled;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
