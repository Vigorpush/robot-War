package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.util.Pair;
import model.Board;

public class Server {
     // TODO: check if in lobby
    // TODO: create sendName function sends new name to all clients
    // TODO: create recieveName function for lobby, called in recieve thread while lobby = true, 
    // TODO: create begin game function, sets in lobby false, sends begincode to all clients, called when recieve name recieves begincode
  
    /**
     * List of client connections.
     */
    private List<ConnectionToClient> connections;
   
    
    /**
     * A Queue of objects to be sent
     */
    private LinkedBlockingQueue<Object> bullets;
    
    private LobbyMessage userList;
    
    private Board gameState;
 
    private ServerSocket serverSocket;  // Socket that listens for connections
    private Thread serverThread;    	// Thread to accept connections
    private volatile boolean shutdown;  // Determines whether the server is running
    private int clientNumber = 0;		// Client Counter
    private boolean inLobby = true;
    
    public Server(int port) throws IOException{
        userList = new LobbyMessage();
        connections = new ArrayList<ConnectionToClient>();
        gameState = new Board(5); // TODO:
        serverSocket = new ServerSocket(port); 
        serverThread = new ServerThread();
        serverThread.start();
        bullets = new LinkedBlockingQueue<Object>();
        
        Thread shotgunThread = new Thread(){
        	public void run() {
        		while(true) {
        			while(inLobby){
        			    try {
                            userList = (LobbyMessage) bullets.take();
                            for(ConnectionToClient con : connections){
                                con.sendUserList = true;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
        			}
        		}
        	}
        };
        	shotgunThread.setDaemon(true);
        	shotgunThread.start();
    }
    
    private class ServerThread extends Thread{
        public void run(){
        	 System.out.println("Server Thread Started");
            try{
                while(!shutdown){
                    Socket connection = serverSocket.accept();	// IMPORTANT
                    System.out.println("Client Connected"); // TODO
                    if(shutdown){
                        System.out.println("Server Thread Error: Client is Shutting Down");
                        break;
                    }
                    new ConnectionToClient(gameState, connection);
                }
            }catch(Exception e){
                System.out.println("Server Thread Error: Client is dead");
            }
        }
    }
    
    /** Server Methods */
    public void serverRecieveBoard (Object incomingState) {
    	this.gameState = (Board) incomingState;
    }
    
    public void loadShotgun(Object ammo){
        this.bullets.add(ammo);
    }
    
    public void serverDisconnectUser(ConnectionToClient user){
        connections.remove(user);
        
    }
    
    public void shutdownServer() {
    	if (serverThread == null) {
    		return;
    	}
    	shutdown = true;
    	try {
    		serverSocket.close();
    	} catch (IOException e) {
    	}
    	serverThread = null;
    	serverSocket = null;
    }
    
    synchronized private void acceptConnection(ConnectionToClient newConnection) {
    	int ID = newConnection.getClient();
    	connections.add(newConnection);
    	System.out.println("Client: "+ ID + " Accepted!");
    }
    
     private class ConnectionToClient{
        
        private int clientID;           // The ID number for this client
        private Board outgoingState;    // The state of the game being sent to this client
        private Object incomingState;    // The state of the game being sent to us from the client
        private Socket socket;     	 	// The socket of the client
        private ObjectInputStream in;   // The input stream for communications with the client
        private ObjectOutputStream out; // The output stream for communications with the client;
        private boolean closed;
        private boolean newConnection = true;   // Lobby: already recieved a string when false recieve pairs
        private boolean sendUserList = false;
        
        private Thread sendThread;      // The thread for sending states to the client
        private volatile Thread recieveThread;  // The thread for receiving states from the client
        
        public ConnectionToClient(Board gameState, Socket socket){ // Constructor 
            this.socket= socket;
            this.incomingState = gameState;
            this.outgoingState = gameState;
            sendThread = new SendThread();
            sendThread.start();
        }
        
        // ConnectionToClient Methods
        /**      
         * A getter for the current client ID
         * @return clietID - the ID of the corresponding Client
         */
        int getClient(){
            return clientID;
        }   
        
        public void disconnect(){
            serverDisconnectUser(this);
        }
        
        /**
         * Closes the connection to client
         * @throws IOException 
         */
     // TODO:
		void close() throws IOException {
            closed = true;
            sendThread.interrupt();
            if(recieveThread != null) {
                recieveThread.interrupt();
            }
            socket.close();
        }
        
        /**
         * TODO:
         * @author Nico
         *
         */
        private class SendThread extends Thread{
        	public void run() {				// try: connect
        		try{
        			out = new ObjectOutputStream(socket.getOutputStream());
                    in = new ObjectInputStream(socket.getInputStream());
                    synchronized(Server.this) {					// syncs with Server Thread
                        clientID = clientNumber++;
                    }
                    out.flush();
                    acceptConnection(ConnectionToClient.this);	// Adds CTC to CTC List
                    recieveThread = new RecieveThread();
                    recieveThread.start();
                    
        		} catch(Exception e) {				// catch: connect
        			try{							// try: close connect
        				closed = true;
        				socket.close();
        			} catch(Exception e1){			// catch: close connect
        			} 								// end try&catch: close connect
        			System.out.println("Send Thread: Cannot Connect");
        			return;
        		}									// end try&catch: connect
        		while(!closed){
        			
        		    while(inLobby){
        		        if(sendUserList){
        		            try{
        		                out.writeObject(userList);
        		                out.flush();
        		                sendUserList = false;
        		            }catch(Exception e3){
        		                System.out.println("Could not send name from server");
        		            }
        		        }
        		    }
					try{									// try: Sending Game State
					    Board sendState = outgoingState;	// sends board state
				        out.writeObject(sendState);
				        out.flush();
				    }catch(Exception e2){					// catch: Sending Game State
				        System.out.println("Could not send board state from server");
				    }
				}
        	}	// end Run
        }	// end Send
        
        private class RecieveThread extends Thread{
            public void run(){
                try{
                    while(!closed){
                        while(inLobby){
                            if(newConnection){
                                try{
                                    String newName = (String)in.readObject();   //Receive new username
                                    System.out.println("Server recieved name: " + newName);
                                    // Check for duplicates, refuse connection if there is one
                                    if(userList.observerList.contains(newName) || userList.playerList.contains(newName)){
                                        throw new Exception("Duplicate player");  
                                    }
                                    userList.observerList.add(newName); // add the user to the observer list
                                    newConnection = false;              // Done connecting
                                    loadShotgun(userList);
                                }catch(Exception e){
                                    System.out.println("Could not recieve names in server");
                                    closed = true;
                                    disconnect();
                                }
                            }else{
                                    userList = (LobbyMessage)in.readObject();   // Waiting for an updated list of users
                                    loadShotgun(userList);
                            }
                        }
                        
                        try{
                            incomingState = in.readObject();
                            serverRecieveBoard(incomingState);
                        }catch(Exception e){ 
                            System.out.println("Could not recieve new board state in server");
                        }
                    }
                }catch(Exception e){
                    if(!closed){
                        System.out.println("An unexpected error has occured");
                    }
                }
            }
        }
    }   //End class connection to client
     
     private class LobbyMessage{
         public ArrayList<String> playerList;
         public ArrayList<String> observerList;
         public boolean begin;
         
         public LobbyMessage(){
             this.playerList = new ArrayList<String>();
             this.observerList = new ArrayList<String>();
             begin = false;
         }
     }
}

