package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import model.Board;
/**
 * The client used to connect to the server in the robot game
 * @author kts135 - Kyle Seidenthal
 * @author ned948 - Nico Dimaano
 */
public class Client {

    private boolean inLobby = true;		// true - inLobby, false - inGame 
    
    private Game game;					// Controller
    
    /**
     * Constructor for the client object
     * @param hostname -> The ip of the host/server
     * @param port -> The port to communicate on
     * @param userName -> The username for the client
     * @param game -> The controller for the client to communicate with
     */
    public Client(String hostname, int port, String userName, Game game){
        try {// Try to create a connection to the server
			setConnection(new ConnectionToServer(hostname, port, userName));
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.game = game;
    }
    
    public ConnectionToServer getConnection() {
        return connection;
    }

    public void setConnection(ConnectionToServer connection) {
        this.connection = connection;
    }

    private ConnectionToServer connection;  // The clients connection to the server
    
    public class ConnectionToServer{
        private Board outgoingState;    // The state of the game being sent to this client
        private Board incomingState;    // The state of the game being sent to us from the client
        private final Socket socket;          // The socket of the client
        private final ObjectInputStream in;   // The input stream for communications with the client
        private final ObjectOutputStream out; // The output stream for communications with the client;
        private boolean closed;
        private String beginCode = "13524636780986748937";  // The code to signal the start of the game
        private boolean sending = false;        //Determines wether to send the current state to the server.
        
        private final Thread sendThread;      // The thread for sending states to the client
        private final Thread recieveThread;   // The thread for receiving states from the client

        
        /**
         * Constructor for connection to server.  Creates and starts sending and recieving threads
         * @param host -> The ip of the host/server to connect to
         * @param port -> The port to communicate on
         * @param userName -> The user name for the client
         * @throws IOException
         */
        public ConnectionToServer(String host, int port, String userName) throws IOException{
            socket = new Socket(host, port);    // Bind to the socket
            out = new ObjectOutputStream(socket.getOutputStream()); // Create an output stream to write to the socket
            in = new ObjectInputStream(socket.getInputStream());    // Create an input stream to read from the socket
            userName = "ERROR"; // TODO: NICO WHAT IS THIS?
            
            out.flush();                
            out.writeChars(userName);       // Send the username to the server to add to the lobby

            // Starting the Threads
            sendThread = new SendThread();
            recieveThread = new RecieveThread();
            sendThread.start();
            recieveThread.start();
        }
        
        private class SendThread extends Thread{
            public void run(){
                
                System.out.println("Client send thread started");
                try{
                    while(!closed && !inLobby){ // While the server is still open and we are not in the lobby....
                        if(sending){    // If it is time to send the state
                            out.writeObject(outgoingState); // Write out the state.
                            out.flush();
                            sending = false;
                        }
                    }
                }catch(Exception e){
                    System.out.println("Unexpected internal error");
                }
            }
        }
        
        private class RecieveThread extends Thread{
            public void run(){
                System.out.println("Recieve thread started");
                try{
                    while(!closed){
                        while(inLobby){ // While we are in the lobby...
                            String newName = in.readUTF();  // Read in the next username sent to us
                            if(newName != beginCode){       // Check that we did not recieve the begin code
                                receiveName(newName);       // Receive the name 
                            }else{
                                inLobby = false;            // It is time to begin the game, so leave the lobby
                                beginGame();                
                            }
                        }
                        // now we are in the game, so we will constantly read the newest game state.
                        incomingState = (Board) in.readObject();
                        recieveGame();  // Tell the controller that a new state has been received
                    }
                }catch(Exception e){
                   System.out.println("Internal error in recieve thread"); 
                }
            }
        }
        
        /**
         * Tell the controller that a new username has been recieved to add to the lobby 
         * @param name -> The new name to add to the lobby screen
         */
        public void receiveName(String name){
            // call controller recieve names
            game.updateLobbyNames(name);
        }
        
        /**
         * Tells the controller to begin the game
         */
        public void beginGame(){
            // call controller beginGame
            game.beginGame();
        }
        
        /**
         * Tell the controller that a new game state has been recieved
         */
        public void recieveGame(){
            game.recieveGame(incomingState);
        }
        
        /**
         * Called by the controller to send the newest game state to the server.
         * @param gameState -> The updated game state
         */
        public void sendGameState(Board gameState){
            sending = true;
            outgoingState = gameState;
        }
        
        /**
         * Disconnect from the server
         */
        public void disconnect(){
            closed = true;
            socket.close();
            in.close();
            out.close();
        }
    }
}
