package json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;;

public class GsonReader {

	GameResult gr;

	public GsonReader() {

	}

	@SuppressWarnings("null")
	public void JsonReader(String Address) {
		String myJSONString = "";
		BufferedReader bufferedReader = null;
		try {

			String sCurrentLine;

			bufferedReader = new BufferedReader(new FileReader(Address));

			while ((sCurrentLine = bufferedReader.readLine()) != null) {
				myJSONString = myJSONString + sCurrentLine;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		JsonElement ele = new JsonParser().parse(myJSONString).getAsJsonObject();
		if (ele.getAsJsonObject().has("script")) {
			JsonElement s = ele.getAsJsonObject().get("script");
			List<String> codeArray = new ArrayList<String>();
			this.gr = new GameResult(codeArray);

			gr.setTeam(s.getAsJsonObject().get("team").getAsString());
			gr.setClassString(s.getAsJsonObject().get("class").getAsString());
			gr.setName(s.getAsJsonObject().get("name").getAsString());
			gr.setMoved(s.getAsJsonObject().get("moved").getAsInt());
			gr.setKilled(s.getAsJsonObject().get("killed").getAsInt());
			gr.setAbsorbed(s.getAsJsonObject().get("absorbed").getAsInt());
			gr.setDied(s.getAsJsonObject().get("died").getAsInt());
			gr.setLived(s.getAsJsonObject().get("lived").getAsInt());
			gr.setExecutions(s.getAsJsonObject().get("executions").getAsInt());
			gr.setLosses(s.getAsJsonObject().get("losses").getAsInt());
			gr.setWin(s.getAsJsonObject().get("wins").getAsInt());
			gr.setMatch(s.getAsJsonObject().get("matches").getAsInt());
			Iterator<JsonElement> it = s.getAsJsonObject().get("code").getAsJsonArray().iterator();
			gr.setCode(s.getAsJsonObject().get("code").getAsJsonArray());
			while (it.hasNext()) {
				codeArray.add((it.next().getAsString()));
			}
			gr.setList(codeArray);
		}
	}

	public List<String> getcode(String Address) {
		List<String> codeArray = new ArrayList<String>();
		String myJSONString = "";
		BufferedReader bufferedReader = null;
		try {

			String sCurrentLine;

			bufferedReader = new BufferedReader(new FileReader(Address));

			while ((sCurrentLine = bufferedReader.readLine()) != null) {
				myJSONString = myJSONString + sCurrentLine;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		JsonElement ele = new JsonParser().parse(myJSONString).getAsJsonObject();
		if (ele.getAsJsonObject().has("script")) {
			JsonElement s = ele.getAsJsonObject().get("script");
			Iterator<JsonElement> it = s.getAsJsonObject().get("code").getAsJsonArray().iterator();
			while (it.hasNext()) {
				codeArray.add((it.next().getAsString()));
			}

		}
		return codeArray;
	}

	public static void main(String[] args) {

		Gson g = new Gson();
		GsonReader rp = new GsonReader();
		rp.JsonReader("src/json/Centralizer.jsn");
		System.out.println(g.toJson(rp.getcode("src/json/Centralizer.jsn")));
		
		System.out.println(rp.gr.getWins());
		System.out.println(rp.gr.getList().size());
		
		GsonWriter gw = new GsonWriter();

	}
}