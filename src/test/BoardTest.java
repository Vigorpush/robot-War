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
	Scout robot2;
	Scout robot3;
	Scout robot4;
	Tile tile1;
	Tile tile2;
	Tile tile3;
	Tile tile4;

	@Before
	public void setUp() throws Exception {
		testBoard = new Board(5);
		robot1 = new Scout(0, 0, null, null);
		robot2 = new Scout(0, 0, null, null);
		robot3 = new Scout(0, 0, null, null);
		robot4 = new Scout(0, 0, null, null);
		testBoard.gameBoard[5][5].addRobot(robot1);
		testBoard.gameBoard[3][7].addRobot(robot2);
		testBoard.gameBoard[5][1].addRobot(robot3);
		testBoard.gameBoard[2][2].addRobot(robot4);
		tile1 = new Tile(6, 7);
		tile2 = new Tile(8, 8);
		tile3 = new Tile(2,2);
		tile4 = new Tile(6, 2);
	}

	@Test
	public void testMovePossible() {
		assertTrue(testBoard.movePossible(robot1, tile1) == 2);
		assertTrue(testBoard.movePossible(robot2, tile1) == 3);
		assertTrue(testBoard.movePossible(robot3, tile1) == -1);
		assertTrue(testBoard.movePossible(robot4, tile1) == -1);
		assertTrue(testBoard.movePossible(robot1, tile4) == -1);
		assertTrue(testBoard.movePossible(robot3, tile3) == -1);
	}
	
	@Test
	public void testAttackPossible()
	{
		assertTrue(testBoard.attackPossible(robot1, tile1));
		assertTrue(!testBoard.attackPossible(robot3, tile2));
		assertTrue(testBoard.attackPossible(robot4, tile3));
	}

}
