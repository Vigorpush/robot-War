package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
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
    
    /**
     * Returns the connection to the server for this client
     * @return
     */
    public ConnectionToServer getConnection() {
        return connection;
    }

    /**
     * Set the connection to server
     * @param connection
     */
    public void setConnection(ConnectionToServer connection) {
        this.connection = connection;
    }

    private ConnectionToServer connection;  // The clients connection to the server
    
    /**
     * This class represents this clients connection to the server and is used to communicate
     * with the server for the game.
     */
    public class ConnectionToServer{
        private Board outgoingState;    // The state of the game being sent to this client
        private Board incomingState;    // The state of the game being sent to us from the client
        private final Socket socket;          // The socket of the client
        private final ObjectInputStream in;   // The input stream for communications with the client
        private final ObjectOutputStream out; // The output stream for communications with the client;
        private boolean closed;
        private boolean sending = false;        //Determines whether to send the current state to the server.
        private LobbyMessage userList;
        int counter = 0;
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
            userList = new LobbyMessage();
            
            out.flush();
            out.reset();
            out.writeObject(userName);       // Send the username to the server to add to the lobby

            // Starting the Threads
            sendThread = new SendThread();
            recieveThread = new RecieveThread();
            sendThread.setDaemon(true);
            sendThread.start();
            
            recieveThread.setDaemon(true);
            recieveThread.start();
            
        }
        
        /**
         * A thread to send messages to the server
         */
        private class SendThread extends Thread{
            public void run(){
                System.out.println("Client send thread started");
                try{
                    while(true){
                        while(!closed && !inLobby){ // While the server is still open and we are not in the lobby....
                            if(sending){    // If it is time to send the state
                                out.writeObject(outgoingState); // Write out the state.
                                out.flush();
                                out.reset();
                                sending = false;
                            }
                      }
                    }
                }catch(Exception e){
                    System.out.println("Unexpected internal error in send thread");
                }
            }
        }
        
        /**
         * A thread to recieved messages from the server
         */
        private class RecieveThread extends Thread{
            public void run(){
                System.out.println("Client: Recieve thread started");
                try{
                    while(!closed){
                        while(inLobby){ // While we are in the lobby...
                            userList = (LobbyMessage) in.readObject();  // Read in the updated userList sent to us
                            // Check that the connection was not rejected
                            if(userList.reject){
                                game.connectionRejected();
                                disconnect();
                            }else{
                                if(!userList.begin){       // Check that we did not recieve the begin code
                                    receiveNames(userList);       // Receive the name 
                                }else{
                                    inLobby = false;            // It is time to begin the game, so leave the lobby
                                    beginGame(userList);  
                                }
                            }
                        }
                        // now we are in the game, so we will constantly read the newest game state.
                        incomingState = (Board) in.readObject();
                        recieveGameState();  // Tell the controller that a new state has been received
                    }
                }catch(Exception e){
                   System.out.println("Client: Internal error in recieve thread"); 
                   System.out.println(e);
                }
            }
        }
        
        /**
         * Tell the controller that a new username has been recieved to add to the lobby 
         * @param name -> The new name to add to the lobby screen
         */
        public void receiveNames(LobbyMessage msg){
        	game.connectUser(msg.observerList, msg.playerList);
        }
        
        /**
         * Tells the controller to begin the game
         */
        public void beginGame(LobbyMessage msg){
            try {
                game.beginGame(msg.playerList, msg.observerList);
            } catch (UnknownHostException e) {
                System.out.println("beginGame(): function problems");
                e.printStackTrace();
            }
        }
        
        /**
         * Called from the controller when the host selects begin game
         */
        public void hostBeginGame(){
            try {
                LobbyMessage begin = userList;
                begin.begin = true;
                out.writeObject(begin);
                out.flush();
                out.reset();
            } catch (IOException e) {
            	System.out.println("Client: hostBeginGame() not working");
            }
            
        }
        
        /**
         * Tell the controller that a new game state has been recieved
         */
        public void recieveGameState(){
            game.recieveGameState(incomingState);
        }
        
        /**
         * Called by the controller to send the newest game state to the server.
         * @param gameState -> The updated game state
         */
        public void sendGameState(Board gameState){
            sending = true;
            outgoingState = gameState;
            try {
                out.writeObject(outgoingState); 
                out.flush();
                out.reset();
            } catch (IOException e) {
            	System.out.println("Client: sendGameState() failed");
                e.printStackTrace();
            } // Write out the state.
        }
        
        /**
         * Disconnect from the server
         */
        public void disconnect(){
        	try {
        		closed = true;
        		socket.close();
        		in.close();
        		out.close();
        	} catch (IOException e) {
            	System.out.println("Client: not disconneting properly");
        	}
        }
    }

    /**
     * Send an updated version of the players to the server.  Called from the controller
     * 
     * @param playerList -> The list of players to send to the server
     * @param observerList -> The list of observers to send to the server
     */
    public void updateUsers(ArrayList<String> playerList, ArrayList<String> observerList) {
        LobbyMessage update = new LobbyMessage();
        update.playerList = playerList;
        update.observerList = observerList;
        try {
            this.connection.out.writeObject(update);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
