package json;


/**
 * This class holding the game results
 * */
public class GameResult{
	
	int win;
	int loses;
	int match;
	String name;
	int deathCount;
	int killCount;

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDeathCount() {
		return deathCount;
	}
	public void setDeathCount(int deathCount) {
		this.deathCount = deathCount;
	}
	public int getKillCount() {
		return killCount;
	}
	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}
	public int getWin() {
		return win;
	}
	public void setWin(int win) {
		this.win = win;
	}
	public int getLoses() {
		return loses;
	}
	public void setLoses(int loses) {
		this.loses = loses;
	}
	public int getMatch() {
		return match;
	}
	public void setMatch(int match) {
		this.match = match;
	}

}