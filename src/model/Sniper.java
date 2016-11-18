package model;

public class Sniper extends Robot {

	public Sniper(int ID, int TEAM, String JPATH, String FPATH) {
		super(ID, TEAM, JPATH, FPATH);
		health = 2;
		attack = 2;
		movement = 2;
		range = 3;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
