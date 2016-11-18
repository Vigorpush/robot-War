package model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    // CONNECTION TO SERVERclear
    
    // TODO: class connection to client
    // TODO: Receiving thread
    // TODO: sending thread
    // TODO: input stream
    // TODO: output stream
    // TODO: client socket
    private final ConnectionToServer connection;
    
    public Client(String hostname, int port){
        connection = new ConnectionToServer(hsotname, port);
    }
    
    public class ConnectionToServer{
        private final int clientID;
        private Board outgoingState;    // The state of the game being sent to this client
        private Board incomingState;    // The state of the game being sent to us from the client
        private final Socket socket;          // The socket of the client
        private final ObjectInputStream in;   // The input stream for communications with the client
        private final ObjectOutputStream out; // The output stream for communications with the client;
        private boolean closed;
        
        private final Thread sendThread;      // The thread for sending states to the client
        private final Thread recieveThread;   // The thread for receiving states from the client

        public ConnectionToServer(String host, int port){
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            out.flush();
            
            sendThread = new SendThread();
            recieveThread = new RecieveThread();
            sendThread.start();
            recieveThread.start();
        }
        
        private class SendThread extends Thread{
            public void run(){
                System.out.println("Client send thread started");
                try{
                    while(!closed){
                        out.writeObject(outgoingState);
                        out.flush();
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
                        incomingState = in.readObject();
                    }
                }catch(Exception e){
                   System.out.println("Internal error in recieve thread"); 
                }
            }
        }
    }
}
