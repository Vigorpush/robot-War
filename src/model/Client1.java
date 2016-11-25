package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import controller.Game;
/**
 * Client1 is same as client but
 * - a test class
 * - uses Object instead of sending boards around
 * - uses Client methods to get CTS to send objects
 * @author Nico
 */
public class Client1 {

    private boolean inLobby = true;		// true - inLobby, false - inGame 
    private Game game;					// Controller
    private final ConnectionToServer connection;
    
    // Constructor
    public Client(String hostname, int port, String userName, Game game){
			connection = new ConnectionToServer(hostname, port, userName);
			this.game = game;
    }
    
    // Client methods
    public void send(Object message){
    	if(message == null)
    		 throw new IllegalArgumentException("Null cannot be sent as a message.");
    	if(connection.closed)
    		throw new IllegalStateException("Message cannot be sent because the connection is closed.");
    	connection.send(message);
    }
    
    public String getName() {
    	return connection.userName;
    }
    
    public class ConnectionToServer{
   
    	public final String userName;	// The User name of the Client
    	private final Socket socket;          // The socket of the client
        private final ObjectInputStream in;   // The input stream for communications with the client
        private final ObjectOutputStream out; // The output stream for communications with the client;
        private boolean closed;			// Closing 
        private String beginCode = "13524636780986748937";
        private final Thread sendThread;      // The thread for sending states to the client
        private final Thread recieveThread;   // The thread for receiving states from the client

        private final LinkedBlockingQueue<Object> outgoingMessages;  // Queue of messages waiting to be transmitted.
        
        public ConnectionToServer(String host, int port, String userName) throws IOException{
        	outgoingMessages = new LinkedBlockingQueue<Object>();
        	socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(userName);
            out.flush();
            
            // Starting the Threads
            sendThread = new SendThread();
            recieveThread = new RecieveThread();
            sendThread.start();
            recieveThread.start();
        }
        
        // Connection To Server methods
        void close() {
        	closed = true;
        	sendThread.interrupt();
        	recieveThread.interrupt();
        	try {
        		socket.close();
        	} catch (IOException e) {
        	}
        }
        
        void send(Object message) {
        	outgoingMessages.add(message);
        }
        
        private class SendThread extends Thread{
            public void run(){     
                System.out.println("Client send thread started");
                    while(!closed){
                        while(!inLobby){
                            try {
                            	Object message = outgoingMessages.take();
								out.writeObject(message);
								out.flush();
								} catch (IOException e) {
								}    
                        	}
                    	}  
					}
                }
            }
        
        private class RecieveThread extends Thread{
            public void run(){
                System.out.println("Recieve thread started");
                try{
                    while(!closed){
                        while(inLobby){
                            String newName = in.readUTF();
                            if(newName != beginCode){
                                recieveName(newName);
                            }else{
                                inLobby = false;
                                beginGame();
                            }
                        }
                        incomingState = (Board) in.readObject();
                        // TODO: Client Needs to use STATE and 
                        // Create a function that uses the state
                        recieveGame();
                    }
                }catch(Exception e){
                   System.out.println("Internal error in recieve thread"); 
                }
            }
        }
        
        public void recieveName(String name){
            // call controller recieve names
            game.updateLobbyNames(name);
        }
        
        public void beginGame(){
            // call controller beginGame
            game.beginGame();
        }
        
        public void recieveGame(){
            game.recieveGame(incomingState);
        }
    }
}
