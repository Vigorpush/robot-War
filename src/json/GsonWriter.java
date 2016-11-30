/**
 * 
 */
package json;

import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

/**
 * @author jiz457
 *
 */
public class GsonWriter {

	String addressJson;
	int matches;
	int win;
	int loses;
	int deathCount;
	int killCount;
	

	public GsonWriter() {
		super();
	}

	
	public void GsonWriteGameResult(String addressJson, int win, int loses, int matches, int deathCount, int killCount) {
		GameResult gamereult=new GameResult(); 
		gamereult.setLoses(loses);
		gamereult.setWin(win);
		gamereult.setMatch(matches);
		gamereult.setDeathCount(deathCount);
		gamereult.setKillCount(killCount);
		
		Gson gson = new Gson(); 
		// convert java object to JSON format, 
		// and returned as JSON formatted string 
		String json = gson.toJson(gamereult); 
		try { 
			FileWriter writer = new FileWriter(addressJson); 
			writer.write(json); 
			writer.close(); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		} 
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GsonWriter test = new GsonWriter();
		test.GsonWriteGameResult("src/json/test.json", 1, 1, 1, 1, 1);
	}
}
