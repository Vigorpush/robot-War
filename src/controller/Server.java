package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import model.Board;

public class Server {
	
    /**
     * List of client connections.
     */
    private List<ConnectionToClient> connections;
    
    /**
     * A Queue of objects to be sent
     */
    private LinkedBlockingQueue<Object> bullets;
    
    /**
     * A class containing Player and Observer List to be passed to the view from controller to be displayed
     */
    private LobbyMessage userList;
    
    private Board gameState;			// The Board that gets passed around all over server
    private ServerSocket serverSocket;  // Socket that listens for connections
    private Thread serverThread;    	// Thread to accept connections
    private volatile boolean shutdown;  // Determines whether the server is running
    private int clientNumber = 0;		// Client Counter
    private static boolean inLobby = true; // Check if the server is in the lobby
    
    /**
     * Server Constructor()
     * The Server thread will create ...
     *  	userList 	- List of Observer and Players while inside the lobby to be used by the view
     *  	connections - List of Connection to Clients used by the server thread to contain connected clients
     *  	gameState	- which is the board that gets passed around 
     *  	serverSocket- The Server Socket that is used to accept socket connections from clients
     *  	bullets		- A Queue that is used by the shotgun thread
     *  
     * Threads:
     * 		serverThread -
     * 
     * 		shotgunThread - In Lobby: The shotgun thread checks if bullets has anything in it then takes what's on top of the Queue 
     * 								  then sends it to all connected clients. If the player is disconneted the msg will not be sent.
     * 								  Finally the shotgun thread checks if the message is the begin message then it starts all connected
     * 								  player.
     * 
     * 					  	In Game: The shotgun thread checks if the messages is a Board 
     * @param port - port number to be used in creating connections
     * @throws IOException
     */
    public Server(int port) throws IOException{
        userList = new LobbyMessage();						// 
        connections = new ArrayList<ConnectionToClient>();	// List of Connection to Clients
        gameState = new Board(5); 							// The Board that gets passed around all over server
        serverSocket = new ServerSocket(port); 				// The Server Socket that is used to accept sockets from clients
        bullets = new LinkedBlockingQueue<Object>();
        serverThread = new ServerThread();					
        serverThread.setDaemon(true);
        serverThread.start();
        
        // This is the thread that sends messages to all clients
        Thread shotgunThread = new Thread(){
        	public void run() {
        	    ConnectionToClient rejectedUser = null;    // If a user is rejected, they are stored here
        		while(true) {
        		    // While we are in the lobby.....
        			while(inLobby){
        			    try {
        			        // Get the next new message to send if the queue is not empty
        			        if(!bullets.isEmpty()){
        			            Object msg = bullets.take();
        			            if(msg instanceof LobbyMessage){
        			                userList = (LobbyMessage)msg;
        			                for(ConnectionToClient con : connections){
        			                    System.out.println("SERVER IS SENDING: " + userList.observerList.toString() + " TO CLIENT: " + con.clientID);
        			                    // Send the reject message if the client has been rejected
        			                    if(userList.rejectID == con.clientID){
        			                        userList.reject = true;
        			                    }else{
        			                        userList.reject = false;
        			                    }
        			            	
        			                    try{
        			                        con.out.writeObject(userList);
        			                        con.out.flush();
        			                        con.out.reset();
        			                        // Set the rejected user so we can disconnect them
        			                        if(userList.reject == true){
                		                    rejectedUser = con;
        			                        } 
                		                
        			                        if(userList.begin){
        			                            inLobby = false;
        			                            System.out.println(inLobby);
        			                        }
                		                
        			                    }catch(Exception e3){
        			                        System.out.println(e3);
        			                        System.out.println("Shotgun Thread: Failed to loop through connections for sending the userList");
        			                    }
        			                }
        			               
        			                // Disconnect the rejected user
        			                if(rejectedUser != null){
        			                    serverDisconnectUser(rejectedUser);
        			                }
        			                // Reject Reset
        			                rejectedUser = null;
        			                userList.rejectID = -1;
        			                
        			            } else if(msg instanceof Board) {
        			                try {
                                        gameState = (Board) msg;
                                        for(ConnectionToClient con : connections){
                                            try {
                                                con.out.writeObject(gameState);
                                                con.out.flush();
                                                con.out.reset();
                                            } catch (IOException e) {
                                            	System.out.println("Shotgun Thread: Game State failed to send through the Out Stream");
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (Exception e) {
                                    	System.out.println("Shotgun Thread: Failed to loop through connections for sending the board");
                                        e.printStackTrace();
                                    }
        			            }
        			        }
        			        
                        } catch (InterruptedException e) {
                        	System.out.println("Shotgun Thread: Internal Unknown Error");
                            e.printStackTrace();
                        }
        			}
        			// while inGame
        			while(!shutdown) {
        				if(!bullets.isEmpty()) {
        					try {
        						gameState = (Board) bullets.take();
        						System.out.println("SERVER RECIEVED NEW GAME STATE");
        						for(ConnectionToClient con : connections){
        							try {
        								con.out.writeObject(gameState);
        								con.out.flush();
        								con.out.reset();
        							} catch (IOException e) {
										e.printStackTrace();
									}
    			            	}
        					} catch (InterruptedException e) {
        						e.printStackTrace();
        					}
        				}
        			} 
        		}
        	}
        };
        	shotgunThread.setDaemon(true);
        	shotgunThread.start();
    }
    
    /**
     * The thread that accepts new connections
     */
    private class ServerThread extends Thread{
        public void run(){
        	 System.out.println("Server Thread Started");
            try{
                while(!shutdown){
                    Socket connection = serverSocket.accept();	// accept the new connection
                    System.out.println("Server Thread: Client Connected");
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
    
    /**
     * Receive a new board state from the controller
     * @param incomingState -> The board coming from the controller
     */
    public void serverRecieveBoard (Object incomingState) {
    	this.gameState = (Board) incomingState;
    	loadShotgun(incomingState);
    	System.out.println("SERVER RECIEVE BOARD");
    }
    
    /**
     * Add a new message to the sending queue
     * @param ammo -> The message to send
     */
    public void loadShotgun(Object ammo){
        this.bullets.add(ammo);
    }
    
    /**
     * Disconnect a user from the server
     * @param user -> The connection to disconnect
     */
    public void serverDisconnectUser(ConnectionToClient user){
        connections.remove(user);
        user.closed = true;
        if(inLobby){
            if(userList.playerList.contains(user.name)){
                userList.playerList.remove(user.name);
            }else if(userList.observerList.contains(user.name)){
                userList.observerList.remove(user.name);
            }
            loadShotgun(userList);
        }
    }
    
    /**
     * Kill the server 
     */
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
    
    /**
     * Accept a new connection to the server
     * @param newConnection -> The connection to accept
     */
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
        private boolean closed;         // Is the connection still open?
        private boolean newConnection = true;   // Lobby: already recieved a string when false recieve pairs
        public boolean sendUserList = false;
        public String name;
        
        private Thread sendThread;      // The thread for sending states to the client
        private volatile Thread recieveThread;  // The thread for receiving states from the client
        
        /**
         * Constructor for connection to client
         * @param gameState -> The game state to start with
         * @param socket -> The socket to communicate on
         */
        public ConnectionToClient(Board gameState, Socket socket){ // Constructor 
            this.socket= socket;
            this.incomingState = gameState;
            this.outgoingState = gameState;
            sendThread = new SendThread();
            sendThread.setDaemon(true);
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
        
        /**
         * Tell the server to remove this user
         */
        public void disconnect(){
            serverDisconnectUser(this);
        }
        
        /**
         * Closes the connection to client
         * @throws IOException 
         */
		void close() throws IOException {
            closed = true;
            sendThread.interrupt();
            if(recieveThread != null) {
                recieveThread.interrupt();
            }
            socket.close();
        }
        
        /**
         * The thread that sends messages to this client
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
                    acceptConnection(ConnectionToClient.this);	// Adds this.CTC to server.CTC List
                    recieveThread = new RecieveThread();
                    recieveThread.setDaemon(true);
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
        		
        		// TODO: THIS IS A MESS AND I DON'T THINK WE NEED IT ANYMORE CAUSE OF THE SHOTGUN THREAD
        		while(!closed){
        		    while(inLobby){
        		    	/**
        		        if(sendUserList){
    			            System.out.println("ERROR EHRE");
        		            try{
        		                out.writeObject(userList);
        		                out.flush();
        		                sendUserList = false;
        		            }catch(Exception e3){
        		            	System.out.println(e3);
        		                System.out.println("Could not send name from server");
        		            }
        		        }*/
        		    } 
        		      // while ingame
					  // loadShotgun(outgoingState);
        		    try {
        		    	Board sendState = outgoingState;
        		    	out.writeObject(sendState);
        		    	out.flush();
        		    } catch (Exception e2) {
        		    	System.out.println("Could not send board state from server");
        		    }
				}
        	}	// end Run
        }	// end Send
        
        /**
         * The thread that recieves new messages from this client
         */
        private class RecieveThread extends Thread{
            public void run(){
                try{
                    System.out.println("START OF RECIEVE IN SEREVER");
                    // TODO:  HEY NICO THERE IS AN ERROR SOMEWHERE IN HERE THAT IS PREVENTING IT FROM GETTING TO THE GAME PART
                    while(!closed){
                        System.out.println("SERVER IN LOBBY: " + inLobby + " CLOSED: " + closed);
                        while(inLobby){
                            System.out.println("SERVER IN LOBBY");
                            if(newConnection){
                                try{
                                    String newName = (String)in.readObject();   //Receive new username
                                    System.out.println("Server recieved name: " + newName);
                                    // Check for duplicates, refuse connection if there is one
                                    if(userList.observerList.contains(newName) || userList.playerList.contains(newName)){
                                        userList.rejectID = clientID;
                                    }else{
                                        userList.observerList.add(newName); // add the user to the observer list
                                        name = newName;
                                    }
                                    newConnection = false;              // Done connecting
                                    loadShotgun(userList);              // Add the message to the server message queue
                                } catch(Exception e) {
                                    System.out.println("Could not recieve names in server");
                                    closed = true;
                                    disconnect();
                                }
                            }else{
                                    Object msg = in.readObject();
                                    if(msg instanceof LobbyMessage){
                                        //userList = (LobbyMessage)in.readObject();   // Waiting for an updated list of users
                                        userList = (LobbyMessage) msg;
                                        System.out.println("SERVER GOT AN UPDATED LIST OF USERS");
                                        loadShotgun(userList);                      // Load the queue with the new users
                                        System.out.println("LOADED SHOTGUN WITH NEW LIST");
                                    }else if (msg instanceof Board){
                                        System.out.println("GOT TO GAME PART");
                                        try{
                                            incomingState = (Board)in.readObject();
                                            serverRecieveBoard(incomingState);
                                        }catch(Exception e){ 
                                            System.out.println("Could not recieve new board state in server");
                                        }
                                    }else{
                                        throw new Exception("Error recieveing in server");
                                    }
                            }
                        }
                        // TODO:  THIS IS THE GAME PART.... THIS NEVER GETS CALLED
                        System.out.println("GOT TO GAME PART");
                        try{
                            incomingState = (Board)in.readObject();
                            serverRecieveBoard(incomingState);
                        }catch(Exception e){ 
                            System.out.println("Could not recieve new board state in server");
                        }
                    }
                }catch(Exception e){
                    System.out.println(e);
                    if(!closed){
                        System.out.println("Connection to client " + clientID + " has closed");
                        disconnect();
                    }
                }
            }
        }
    }   //End class connection to client
}

