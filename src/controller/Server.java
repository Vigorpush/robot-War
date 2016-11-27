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
    private LinkedBlockingQueue<Board> boards;
    private Board gameState;
 
    private ServerSocket serverSocket;  // Socket that listens for connections
    private Thread serverThread;    	// Thread to accept connections
    private volatile boolean shutdown;  // Determines whether the server is running
    private int clientNumber = 0;		// Client Counter
    
    public Server(int port) throws IOException{
        connections = new ArrayList<ConnectionToClient>();
        gameState = new Board(5); // TODO:
        serverSocket = new ServerSocket(port); 
        serverThread = new ServerThread();
        serverThread.start();
        Thread shotgunThread = new Thread();
        	public void run() {
        		while(true) {
        			try {
        				Board sendingState = gameState;
        				
        			} catch (Exception e) {
        				System.out.println("Server Shotgun has no ammo");
        				e.printStackTrace();
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
    public void serverRecieveBoard (Board board) {
    	this.gameState = board;
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
						try{									// try: Sending Game State
				            Board sendState = outgoingState;	// sends board state
				            out.writeObject(sendState);
				            out.flush();
				        }catch(Exception e2){					// catch: Sending Game State
				        }
					}
        	}	// end Run
        }	// end Send
        
        private class RecieveThread extends Thread{
            public void run(){
                try{
                    while(!closed){
                        try{
                            incomingState = in.readObject();
                            this.serverRecieveBoard(incomingState);
                        }catch(Exception e){ 
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
}

