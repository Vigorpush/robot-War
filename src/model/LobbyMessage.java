package model;

import java.io.Serializable;
import java.util.ArrayList;

public class LobbyMessage implements Serializable{
	public ArrayList<String> playerList;
    public ArrayList<String> observerList;
    public boolean begin;
    public int rejectID;
    public boolean reject;
    
    public LobbyMessage(){
        this.playerList = new ArrayList<String>();
        this.observerList = new ArrayList<String>();
        this.begin = false;
        this.rejectID = -1;
        this.reject = false;
    }
}