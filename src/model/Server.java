package model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    // CONNECTION TO CLIENT
    // TODO: Receiving thread
    // TODO: sending thread
    // TODO: input stream
    // TODO: output stream
    // TODO: client socket
    
    /**
     * List of client connections.
     */
    private List<ConnectionToClient> connections;
    
    /**
     * Current game state
     */
    private Board gameState;
    
    private volatile boolean shutdown;  // Determines whether the server is running
    
    private ServerSocket serverSocket;  // Socket that listens for connections
    
    private Thread serverThread;    // Thread to accept connections
    
    private int clientNumber = 0;
    
    public Server(int port){
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
                    Socket connection = serverSocket.accept();
                    if(shutdown){
                        System.out.println("Client is dead");
                        break;
                    }
                    new ConnectionToClient(gameState, connection);
                }
            }catch(Exception e){
                System.out.println("Client is dead");
            }
        }
    }
    
    private class ConnectionToClient{
        
        private int clientID;           // The ID number for this client
        private Board outgoingState;    // The state of the game being sent to this client
        private Board incomingState;    // The state of the game being sent to us from the client
        private Socket socket;      // The socket of the client
        private ObjectInputStream in;   // The input stream for communications with the client
        private ObjectOutputStream out; // The output stream for communications with the client;
        private boolean closed;
        
        private Thread sendThread;      // The thread for sending states to the client
        private volatile Thread recieveThread;  // The thread for receiving states from the client
        
        public ConnectionToClient(Board gameState, Socket socket){
            this.socket= socket;
            this.incomingState = gameState;
            this.outgoingState = gameState;
            sendThread = new SendThread();
            sendThread.start();
        }
        
        int getClient(){
            return clientID;
        }
        
        void close(){
            closed = true;
            sendThread.interrupt();
            if(recieveThread != null){
                recieveThread.interrupt();
            }
            socket.close();
        }
        
        private class SendThread extends Thread{
            public void run(){
                try{
                    out = new ObjectOutputStream(socket.getOutputStream());
                    in = new ObjectInputStream(socket.getInputStream());
                    
                    synchronized(Server.this){
                        clientID = clientNumber++;
                    }
                    
                    out.writeObject(clientID);
                    out.flush();
                    acceptConnection(ConnectionToClient.this);
                    recieveThread = new RecieveThread();
                    recieveThread.start();
                }catch(Exception e){
                    try{
                        closed = true;
                        socket.close();
                    }catch(Exception e1){
                    }
                    System.out.println("Cannot connect");
                    return;
                }
                
                try{
                    while(!closed){
                        try{
                            Board sendState = outgoingState;
                            out.writeObject(sendState);
                            out.flush();
                            
                        }catch(Exception e2){
                            
                        }
                    }
                }catch(IOException e){
                    if(!closed){
                        System.out.println("IO exception killed the server");
                    }
                }catch(Exception e){
                    if(!closed){
                        System.out.println("An unexpected error has occured");
                    }
                }
            }

            private void acceptConnection(ConnectionToClient connectionToClient) {
                connections.add(connectionToClient);                
            }
        }
        
        private class RecieveThread extends Thread{
            public void run(){
                try{
                    while(!closed){
                        try{
                            incomingState = in.readObject();
                        }catch(Exception e){
                            
                        }
                    }
                }catch(IOException e){
                    if(!closed){
                        System.out.println("IO exception killed the server");
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
