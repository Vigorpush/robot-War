package model;
import java.io.Serializable;

import json.GsonWriter;


/**
 * 
 *
 */
public abstract class Robot implements Serializable{

	public int id;
	public int teamNumber;
	public String jsonPath;
	public String fourthPath;
	public String name;
	public Tile location;
	public int matchesWon;
	public int deathCount;
	public int killCount;
	public int health;
	public int attack;
	public int lose;
	public int movement;
	public int range;
	public int movementLeft;
	public int match;
	
	public Robot(int ID, int TEAM, String JPATH, String FPATH) {
//		GsonReader gsonReader = new GsonReader(JPATH);
		id = ID;
		teamNumber = TEAM;
		jsonPath = JPATH;
		fourthPath = FPATH;
//		name= gsonReader.GsonReaderRobot().getName();
//		matchesWon = gsonReader.GsonReaderRobot().getWin();
//		match = gsonReader.GsonReaderRobot().getMatch();
//		deathCount = gsonReader.GsonReaderRobot().getDeathCount();
//		killCount = gsonReader.GsonReaderRobot().getKillCount();
//		lose = gsonReader.GsonReaderRobot().getLoses();
		health = 0;
		attack = 0;
		movement = 0;
		range = 0;
		movementLeft = 0;
	}
	//TODO adding more variables from json file
	//this function will update stats into json file
	public void updatetoJson() {
		GsonWriter jsonWriter = new GsonWriter();
//		/jsonWriter.GsonWriteGameResult(this.jsonPath, matchesWon, lose, match, deathCount, killCount);
	}
	//to be checked on and implemented outright
	//String[] interpreter(String[] s){
	//	return s;
	//}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
