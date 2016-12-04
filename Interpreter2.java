import java.util.ArrayList;
import java.util.LinkedList;

public class Interpreter2 {
	
	////////////////////////////////////////
	
	public ArrayList<String> commands;
	
	//list of "fourthVariable" classes
	//name
	//String value

	public LinkedList<FourthVariable> VariableList;
	
	
	//preset variables
	public int health;
	public int healthLeft;
	public int moves;
	public int movesLeft;
	public int attack;
	public int range;
	public String team;
	public String type;
	
	public void Interpreter(ArrayList<String> s) {
		// TODO Auto-generated constructor stub
		commands= s;
	}
	
	//preset actions
	public void turn(){
		
	}
	public void move(){
		
	}
	public void shoot(){
		
	}
	public void check(){
		
	}
	public void scan(){
		
	}
	public void identify(){
		
	}
	public void send(){
		
	}
	public void mesg(){
		
	}
	public void recv(){
		
	}
	
	public void runFile(){
		//iterate through list
		for(int i=0; i<commands.size(); i++){
			
		String[] array = commands.get(i).split(" ");
		
		//if contains "variable"
		if(array[0] == "variable"){
			//make word into variable
			FourthVariable f1 = new FourthVariable();
			f1.name = array[1];
			VariableList.add(f1);
		}
		
		//if contains ":"
		//if (array[0] == ":"){
			//skip
		//}
		
		//if contains variable name
		while(VariableList.iterator().hasNext()){
			
			if(array[0] == VariableList.iterator().next().name){
				
				//look at symbol
				if(array[2] == "!"){
					VariableList.iterator().next().value = array[1];
				}
				//get next word
				//follow symbol instructions with previous word
				
			}
			
		}
	
		//if is prefab function or variable
			//use saved version
	
		//if ": play"
		if(array[1] == "play"){
			while(VariableList.iterator().hasNext()){
				if(array[2] == VariableList.iterator().next().name){
				
					if(array[3] == "?"){
					
						if(array[4] == "if"){
							if(VariableList.iterator().next().value == "false"){
								int j = i;
								while(commands.get(j) != "else"){
									commands.remove(j);
									j++;
								}
							}
							if(VariableList.iterator().next().value == "true"){
								int j = i;
								while(commands.get(j) != "then"){
									commands.remove(j);
									j++;
								}
							}
								
						}
					}
						
						
					
				}
				
			}
			
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
		}
		}
	}
	
	
	public Interpreter2() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
