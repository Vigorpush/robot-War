
public class Interpreter2 {

	//iterate through list
	
	//if contains "variable"
		//make word into variable
	
	//if contains ":"
		//skip
	
	//if contains variable name
		//get next word
			//look at symbol
				//follow symbol instructions with previous word
	
	//if is prefab function or variable
		//use saved version
	
	//if play
		//find (":"+word)
				//look at next word
					//look at symbol (? or !, etc)
						//apply function symbol represents to word
							//function returns this value
					//if next word is "if"
						//if word returns true
							//continue
							//when "else" reached
								//go to "then"
						//else
							//go to else
							//continue
	
	////////////////////////////////////////
	
	//list of "fourthVariable" classes
		//name
		//String value
	
	//list of "fourthFunctions" classes
		//name
		//value
	
	public Interpreter2() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
