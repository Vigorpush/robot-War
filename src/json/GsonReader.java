/**
 * 
 */
package json;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson; 

/**
 * @author jiz457
 *
 */
public class GsonReader {

	String jsonAddress ;
	
	public GsonReader(String jsonAddress) {
		super();
		this.jsonAddress = jsonAddress;
	}

	public GameResult GsonReaderRobot() {
		GameResult gamereult = null;
		Gson gson = new Gson();
		try 
		{ 
			BufferedReader br = new BufferedReader( new FileReader(jsonAddress)); //convert the json string back to object 
			gamereult= gson.fromJson(br, GameResult.class); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		} 
		return gamereult;
	}
	
	


	/**
	 * @param args
	 */
	public static void main(String[] args){
		
	}
}
