package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import netgame.common.Hub.Message;

public class Server {
    // CONNECTION TO CLIENT
    // TODO: Receiving thread
    // TODO: sending thread
    // TODO: input stream
    // TODO: output stream
    // TODO: client socket
    
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
    private LinkedBlockingQueue<Board gameState> messages;
    private Board gameState;
 
    private ServerSocket serverSocket;  // Socket that listens for connections
    private Thread serverThread;    	// Thread to accept connections
    private volatile boolean shutdown;  // Determines whether the server is running
    private int clientNumber = 0;		// Client Counter
    
    public Server(int port) throws IOException{
        connections = new ArrayList<ConnectionToClient>();
        gameState = new Board();
        serverSocket = new ServerSocket(port);
        serverThread = new ServerThread();
        serverThread.start();
    }
    
    private class ServerThread extends Thread{
        public void run(){
            try{
                while(!shutdown){
                    Socket connection = serverSocket.accept();	// IMPORTANT
                    if(shutdown){
                        System.out.println("Server Thread Error: Client is Shutting Down");
                        break;
                    }
                    new ConnectionToClient(gameState, connection);
                    connections.add(e)
                }
            }catch(Exception e){
                System.out.println("Server Thread Error: Client is dead");
            }
        }
    }
    
    /** Server Methods */
    // MAYBE TODO:
    //synchronized public void sendStateToAll(Board gameState) {
    //	if (gameState == null)
    //		throw new IllegalArgumentException("Null cannot be sent");
    //	for(ConnectionToClient clients : connections)
    //		clients.send(gameState);
    //}
    
    /**
     * 
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
     * TODO:
     * @param newConnection
     */
    synchronized private void acceptConnection(ConnectionToClient newConnection) {
    	int ID = newConnection.getClient();
    	connections.add(newConnection);
    	System.out.println("Client: "+ ID + "Accepted!");
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
		void close() throws IOException{
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
        
        /**
         * TODO:
         *
         */
        private class RecieveThread extends Thread{
            public void run(){
                try{
                    while(!closed){
                        try{
                            incomingState = in.readObject();
                         // TODO: Set Recieving to true when incoming state is changed
                         // CREATE A FUNCTION THAT USES THE STATE
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
