package test;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import model.Robot;
import model.Scout;
import model.Tile;

public class TileTest {

	Tile tile1;
	Tile tile2;
	Scout robot1;
	Scout robot2;
	Scout robot3;
	Scout robot4;
	
	@Before
	public void setUp() throws Exception {
		
		tile1 = new Tile(3,5);
		tile2 = new Tile(5,3);
		robot1 = new Scout(0,0, null, null);
		robot2 = new Scout(1,0,null,null);
		robot3 = new Scout(2,0,null,null);
		robot4 = new Scout(0,0,null,null);
		
	}

	@Test
	public void testAddRobot() {
		tile1.addRobot(robot1);
		assert(tile1.robotList.get(0).equals(robot1));
		tile1.robotList.clear();
	}

	@Test
	public void testRemoveRobot() {
		tile1.addRobot(robot2);
		tile1.removeRobot(0);
		assert(tile1.robotList.isEmpty());
	}

	@Test
	public void testUpdateRobot() {
		tile1.addRobot(robot1);
		tile1.updateRobot(robot4);
		assert(tile1.robotList.get(0).equals(robot4));
		tile1.updateRobot(robot3);
		assert(tile1.robotList.get(0).equals(robot4) && tile1.robotList.size() == 1);
		tile1.robotList.clear();
	}

	@Test
	public void testGetRobot() {
		tile1.addRobot(robot1);
		tile2.addRobot(robot2);
		assert(tile1.getRobot(0).equals(robot1));
		assert(tile2.getRobot(0).equals(robot2));
		assert(!tile1.getRobot(0).equals(robot2));
	}

}
