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
        private boolean sending = false;        //Determines wether to send the current state to the server.
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
            out.writeObject(userName);       // Send the username to the server to add to the lobby

            // Starting the Threads
            sendThread = new SendThread();
            recieveThread = new RecieveThread();
            sendThread.setDaemon(true);
            sendThread.start();
            
            recieveThread.setDaemon(true);
            recieveThread.start();
            
        }
        
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
                                System.out.println("CLIENT SENT GAME STATE");
                                for(int i = 0; i < outgoingState.players.size(); i ++){
                                    System.out.println(outgoingState.players.get(i));
                                }
                            }
                      }
                    }
                }catch(Exception e){
                    System.out.println("Unexpected internal error in send thread");
                }
            }
        }
        
        private class RecieveThread extends Thread{
            public void run(){
                System.out.println("Client: Recieve thread started");
                try{
                    while(!closed){
                        while(inLobby){ // While we are in the lobby...
                           // System.out.println("IN LOBBY WAITING FOR GAME START");
                            userList = (LobbyMessage) in.readObject();  // Read in the updated userList sent to us
                            System.out.println("CLIENT RECIEVED: " + userList.observerList.toString() + userList.playerList.toString());
                            System.out.println("Begin GAME was recieved as: " + userList.begin);
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
                                    System.out.println("CLIENT RECIEVED BEGIN GAME");
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
                   // TODO: notify and send back to start
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
                game.beginGame(msg.computerPlayers, msg.playerList, msg.observerList);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
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
            	System.out.println("Client: hostBeginGame not working");
            }
            
        }
        
        /**
         * Tell the controller that a new game state has been recieved
         */
        public void recieveGameState(){
           
            System.out.println("CLIENT REVEIBE GAME STATE CALLED");
            game.recieveGameState(incomingState);
            System.out.println("CLIENT RECIEVE GAME STATE FINISHED");
        }
        
        /**
         * Called by the controller to send the newest game state to the server.
         * @param gameState -> The updated game state
         */
        public void sendGameState(Board gameState){
            System.out.println("CLIENT SEND GAME STATE CALLED");
            sending = true;
            outgoingState = gameState;
            try {
                out.writeObject(outgoingState); 
                out.flush();
                out.reset();
                for(int i = 0; i < outgoingState.players.size(); i ++){
                    System.out.println(outgoingState.players.get(i).name);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // Write out the state.
           
            System.out.println("CLIENT SEND GAME STATE RETURNED");
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

    public void updateUsers(ArrayList<String> playerList, ArrayList<String> observerList) {
        LobbyMessage update = new LobbyMessage();
        update.playerList = playerList;
        update.observerList = observerList;
        System.out.println(playerList.toString());
        System.out.println(observerList.toString());
        try {
            this.connection.out.writeObject(update);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
