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

	public JsonArray getCode() {
		return code;
	}

	public void setCode(JsonArray code) {
		this.code = code;
	}

	public GameResult() {

	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public GameResult(List<String> list) {
		super();
		this.list = list;
	}

	public int getStringLived() {
		return lived;
	}

	public void setLived(int lived) {
		this.lived = lived;
	}

	public int getDied() {
		return died;
	}

	public void setDied(int died) {
		this.died = died;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWin() {
		return wins;
	}

	public int getKillCount() {
		return killed;
	}

	public void setKillCount(int killed) {
		this.killed = killed;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getMatches() {
		return matches;
	}

	public void setMatches(int matches) {
		this.matches = matches;
	}

	public int getKilled() {
		return killed;
	}

	public void setKilled(int killed) {
		this.killed = killed;
	}

	public void setWin(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public int getMatch() {
		return matches;
	}

	public void setMatch(int matches) {
		this.matches = matches;
	}

}
