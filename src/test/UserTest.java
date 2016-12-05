package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.Tile;
import model.User;

public class UserTest {

	Tile tile1;
	Tile tile2;
	User user1;
	User user2;
	
	@Before
	public void setUp() throws Exception {
		tile1 = new Tile(0,1);
		tile2 = new Tile(0,0);
		user1 = new User("test1","Fake IP1");
		user2 = new User("test2","Fake IP2");
		user1.fogOfWar = new boolean[][]{{true,false},{false,true}};
		user2.fogOfWar = new boolean[][]{{false,true},{true,false}};
	}

	@Test
	public void testInspectTile() {
		assert(!user1.inspectTile(tile1));
		assert(user1.inspectTile(tile2));
		assert(user2.inspectTile(tile1));
	}

}
