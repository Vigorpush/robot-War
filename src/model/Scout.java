package model;

public class Scout extends Robot {

	public Scout(int ID, int TEAM, String JPATH, String FPATH) {
		super(ID, TEAM, JPATH, FPATH);
		health = 1;
		attack = 1;
		movement = 3;
		range = 2;
		movementLeft = movement;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
