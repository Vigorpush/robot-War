import java.util.LinkedList;

public class Interpreter {

	public String[] commands;
	public LinkedList<Object> VariableStack;
	
	
	//preset variables
	public int health;
	public int healthLeft;
	public int moves;
	public int movesLeft;
	public int attack;
	public int range;
	public String team;
	public String type;
	
	public Interpreter(String[] s) {
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
	
	
	public void getVariables(){
		for(int i=0; i<commands.length; i++){
			//find line that starts with "variable"
			if (commands[i] == "variable"){
				//search for second word (": "+word)
				//search for next instance of word (word+value)
				//add value to stack
			}
		}
	}
	
	//find play function
	public void runPlay (){
		
		for(int i=0; i<commands.length; i++){
			if(commands[i] == ": play"){
				runPlayHelper(i);
			}
		}
		
	}
	
	public void runPlayHelper(int x){
		
		if(commands[x+1] == null){
			//end turn
		}
			
		if(commands[x+1] == "dummy text"){
			//execute pre-made function
		}
			
		for(int i= 0; i< commands.length; i++){
			
			if(commands[i] == ": "+commands[x+1]){
				runPlayHelper(x+1);
			}
			
		}
		
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
