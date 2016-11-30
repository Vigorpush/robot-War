
package json;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 *
 */
public class Parser {

	String file = "/tmp_mnt/student/jiz457/git/370-16D1/jsonexample/Centralizer.jsn";
	
	
	
	JsonElement ele = new JsonParser().parse(file.toString()).getAsJsonObject();
	
	public static void main(String[] args) {
		
		Parser p = new Parser();
		
		System.out.println(p.ele.getAsJsonObject().get("name").getAsString());

	}

}
