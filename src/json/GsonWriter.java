/**
 * 
 */
package json;

import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

/**
 * @author jiz457
 *
 */
public class GsonWriter {

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
	public JsonArray code;
	
	public GsonWriter() {
		super();
	}

	public void GsonWriteGameResult(String addressJson, int wins, int losses, int matches, String name, int died,
			int killed, String team, String classString, int executions, int lived, int absorbed, int moved) {
		//1,1,1,"A",4,2,"D1","Tank", 2,1,1,1
		GameResult gamereult = new GameResult();
		gamereult.setWins(wins);
		gamereult.setLosses(losses);
		gamereult.setMatches(matches);
		gamereult.setName(name);
		gamereult.setDied(died);
		gamereult.setKilled(killed);
		gamereult.setTeam(team);
		gamereult.setClassString(classString);
		gamereult.setExecutions(executions);
		gamereult.setLived(lived);
		gamereult.setAbsorbed(absorbed);
		gamereult.setMoved(moved);
		//gamereult.setCode(code);
		script s = new script();
		s.setGr(gamereult);
		Gson gson = new Gson();

		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(s);
		try {
			FileWriter writer = new FileWriter(addressJson);
			json = json.replaceAll("classString", "class");
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GsonWriter test = new GsonWriter();
		test.GsonWriteGameResult("src/json/test.jsn", 1,1,1,"A",4,2,"D2","Tank", 2,1,1,1);
	}
}
