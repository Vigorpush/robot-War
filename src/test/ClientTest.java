package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.Game;
import model.Client;

public class ClientTest {
    
    @Before
    public void setUp() throws Exception {
        // TODO: Create a fake server by creating a socket and streams
        // TODO: Create a fake board state
        
        // Testing variables for constructor
        String hostname = "localhost";
        int port = 22222;
        String userName = "Testing123";
        Game game = new Game();
        
        Client client = new Client(hostname, port, userName, game); 
    }

    @Test
    public void testRecieveName() {
        // TODO: write a name from the server to the client
        // TODO: Check the list of lobby names in the game for the new name
    }
    
    @Test
    public void testRecieveGame(){
        // TODO: send a board state to the client from the fake server\
        // TODO: Check that the games current board is the same as the one sent
    }
    
    @Test
    public void testSendGameState(){
        // TODO: send a fake game state to the server
        // TODO: check that the state receive by the server is the same as we sent
    }
    
    @Test
    public void testDisconnect(){
        // TODO: Call the disconnect method and try to send a message from the server
    }
    
    @After
    public void tearDown(){
        // TODO: close socket and streams
    }
}
