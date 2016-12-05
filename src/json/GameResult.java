package json;

import java.util.List;

import com.google.gson.JsonArray;

/**
 * This class holding the game results
 */
public class GameResult {

	JsonArray code;
	int wins;
	int losses;
	int matches;
	String name;
	int died;
	int killed;
	String team;
	String classString;
	int executions;
	int lived;
	int absorbed;
	int moved;
	List<String> list;
	public GameResult(List<String> list) {
		super();
		this.list = list;
	}
	public GameResult() {
		
	}
	public JsonArray getCode() {
		return code;
	}
	public void setCode(JsonArray code) {
		this.code = code;
	}
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public int getLosses() {
		return losses;
	}
	public void setLosses(int losses) {
		this.losses = losses;
	}
	public int getMatches() {
		return matches;
	}
	public void setMatches(int matches) {
		this.matches = matches;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDied() {
		return died;
	}
	public void setDied(int died) {
		this.died = died;
	}
	public int getKilled() {
		return killed;
	}
	public void setKilled(int killed) {
		this.killed = killed;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public String getClassString() {
		return classString;
	}
	public void setClassString(String classString) {
		this.classString = classString;
	}
	public int getExecutions() {
		return executions;
	}
	public void setExecutions(int executions) {
		this.executions = executions;
	}
	public int getLived() {
		return lived;
	}
	public void setLived(int lived) {
		this.lived = lived;
	}
	public int getAbsorbed() {
		return absorbed;
	}
	public void setAbsorbed(int absorbed) {
		this.absorbed = absorbed;
	}
	public int getMoved() {
		return moved;
	}
	public void setMoved(int moved) {
		this.moved = moved;
	}
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}


}
