package json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;;

public class robotParser {

	public static void main(String[] args) {

		String myJSONString = "";
		BufferedReader bufferedReader = null;

		try {

			String sCurrentLine;

			bufferedReader = new BufferedReader(new FileReader("src/json/Centralizer.jsn"));

			while ((sCurrentLine = bufferedReader.readLine()) != null) {
				myJSONString = myJSONString + sCurrentLine;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null){
					bufferedReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		JsonElement ele = new JsonParser().parse(myJSONString).getAsJsonObject();
		if (ele.getAsJsonObject().has("script")){
			JsonElement s=  ele.getAsJsonObject().get("script");

			String team = s.getAsJsonObject().get("team").getAsString();
			String class1=  s.getAsJsonObject().get("class").getAsString();
			String name = s.getAsJsonObject().get("name").getAsString();
			
			int matches = s.getAsJsonObject().get("matches").getAsInt();
			int wins = s.getAsJsonObject().get("wins").getAsInt();
			int losses = s.getAsJsonObject().get("losses").getAsInt();
			int executions = s.getAsJsonObject().get("executions").getAsInt();
			int lived = s.getAsJsonObject().get("lived").getAsInt();
			int died = s.getAsJsonObject().get("died").getAsInt();
			int absorbed = s.getAsJsonObject().get("absorbed").getAsInt();
			int killed = s.getAsJsonObject().get("killed").getAsInt();
			int moved = s.getAsJsonObject().get("moved").getAsInt();
			
			System.out.println(team);
			System.out.println(class1);
			System.out.println(name);
			System.out.println(matches);
			System.out.println(wins);
			System.out.println(losses);
			System.out.println(executions);
			System.out.println(lived);
			System.out.println(died);
			System.out.println(absorbed);
			System.out.println(killed);
			System.out.println(moved);
			
		}
	}
}