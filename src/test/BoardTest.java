package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.Board;
import model.Scout;
import model.Tile;

public class BoardTest {

	Board testBoard;
	Scout robot1;
	Tile tile1;
	@Before
	public void setUp() throws Exception {
		testBoard = new Board(5);
		robot1 = new Scout(0, 0, null, null);
		testBoard.gameBoard[5][5].addRobot(robot1);
		tile1 = new Tile(6,7);
	}

	@Test
	public void testMovePossible() {
		assertTrue(testBoard.movePossible(robot1, tile1) == 2);
	}

}
