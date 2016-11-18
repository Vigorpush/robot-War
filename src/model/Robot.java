package model;

import com.google.gson.Gson;



public class Robot {

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
	
	public Robot(int ID, int TEAM, String JPATH, String FPATH) {
		id = ID;
		teamNumber = TEAM;
		jsonPath = JPATH;
		fourthPath = FPATH;
		
		Object gson = new Gson().fromJson(jsonPath, );
		
		
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
	
	//to be checked on and implemented outright
	//String[] interpreter(String[] s){
	//	return s;
		
	//}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
