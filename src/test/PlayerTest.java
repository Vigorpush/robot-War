package test;

import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import model.Board;
import model.Player;
import model.Scout;
import model.Tile;

public class PlayerTest {

	Board testBoard;
	Scout robot1;
	Scout robot2;
	Scout robot3;
	Scout robot4;
	Player player1;
	boolean[][] result1;

	@Before
	public void setUp() throws Exception {
		testBoard = new Board(5);
		robot1 = new Scout(0, 0, null, null);
		robot2 = new Scout(0, 0, null, null);
		player1 = new Player("testName1", "testIP1", 0);
		player1.robotList.add(robot1);
		player1.robotList.add(robot2);
		testBoard.gameBoard[1][2].addRobot(robot1);
		testBoard.gameBoard[1][1].addRobot(robot2);
		result1 = new boolean[][] { { true, true, true, true, false, false, false, false, false },
				{ true, true, true, true, true, false, false, false, false },
				{ true, true, true, true, true, false, false, false, false },
				{ false, true, true, true, true, false, false, false, false },
				{ false, false, false, false, false, false, false, false, false },
				{ false, false, false, false, false, false, false, false, false },
				{ false, false, false, false, false, false, false, false, false },
				{ false, false, false, false, false, false, false, false, false },
				{ false, false, false, false, false, false, false, false, false } };

	}

	@Test
	public void testSetFogOfWar() {
		player1.setFogOfWar(5);
		for(int i = 0 ; i < 9 ; i++)
		{
			for(int j = 0 ; j < 9; j++)
			{					
				assert(player1.fogOfWar[i][j] == result1[i][j]);
			}
		}
		
	}

}
