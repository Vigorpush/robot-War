package model;

public class Tank extends Robot {

	public Tank(int ID, int TEAM, String JPATH, String FPATH) {
		super(ID, TEAM, JPATH, FPATH);
		health = 3;
		attack = 3;
		movement = 1;
		range = 1;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
