package json;


/**
 * This class holding the game results
 * */
public class GameResult{
	
//	Class script(int wins){
//		String script = "script";
//		GameResult g = new GameResult();
//		return null;
//	}
	
	
	

	int wins;
	int loses;
	int matches;
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
		return wins;
	}
	public void setWin(int wins) {
		this.wins = wins;
	}
	public int getLoses() {
		return loses;
	}
	public void setLoses(int loses) {
		this.loses = loses;
	}
	public int getMatch() {
		return matches;
	}
	public void setMatch(int matches) {
		this.matches = matches;
	}

}