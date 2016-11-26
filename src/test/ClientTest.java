package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.Game;
import model.Client;

public class ClientTest {
    ServerSocket serverSocket; 
    Thread serverThread;
    Thread recievingThread;
    
    ObjectInputStream in;
    ObjectOutputStream out;
    
    private class ServerThread extends Thread{
        public void run(){
            while(true){
                try {
                    Socket connection = serverSocket.accept();
                    System.out.println("Accepted client");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private class RecieveThread extends Thread{
        public void run(){
            while(true){
                try {
                    Object message = in.readObject();
                } catch (ClassNotFoundException | IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
    Client client;
    
    @Before
    public void setUp() throws Exception {
        // Testing variables
        String hostname = "localhost";
        int port = 22222;
        String userName = "Testing123";
        Game game = new Game();
        
        ServerSocket serverSocket; 
        Thread serverThread;
        Thread recievingThread;
        
        serverSocket = new ServerSocket(port);
        serverThread = new ServerThread();
        recievingThread = new RecieveThread();
        
        serverThread.start();
        recievingThread.start();
        
        // TODO: Create a fake board state
        
        client = new Client(hostname, port, userName, game); 
    }

    @Test
    public void testRecieveName() {
        // TODO: write a name from the server to the client
        try {
            out.writeChars("TestName");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // TODO: Check the list of lobby names in the game for the new name
    }
    
    @Test
    public void testRecieveGame(){
        // TODO: send a board state to the client from the fake server
        Board state = new Board();
        
        out.writeObject(state);
        // TODO: Check that the games current board is the same as the one sent
        assert(game.state == state) //or something
    }
    
    @Test
    public void testSendGameState(){
        // TODO: send a fake board state to the server
        Board state = new Board();
        client.getConnection().sendGameState(state);
        // TODO: check that the state receive by the server is the same as we sent
    }
    
    @Test
    public void testDisconnect(){
        // TODO: try to send a message from the server
        client.getConnection().disconnect();
    }
    
    @After
    public void tearDown(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
