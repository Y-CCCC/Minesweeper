package minesweeper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class PlaygroundTest {
	
	Playground playground;
	
	@BeforeEach
	void setUp() throws Exception {
		// initialize the playground
		playground = new Playground();
	}

	@Test
	void testPlayground() {
		assertFalse(playground.isGameOver());
		assertTrue(playground.isMineLeft());
		assertFalse(playground.isTimerStart());
	}


	@Test
	void testCheckFlag() {
		// set the square[4][6] with a flag and a mine
		playground.getSquares()[4][6].setFlag(true);
		playground.getSquares()[4][6].setMine(true);
		assertFalse(playground.checkFlag());
		
		// set the squares between [0][0] and [0][18] with flags and mines
		for(int i=0; i<19;i++) {
			playground.getSquares()[0][i].setFlag(true);
			playground.getSquares()[0][i].setMine(true);
		}
		
		// the total flags are 20
		assertTrue(playground.checkFlag());
		
	}
	

	@Test
	void testSetNumSurMineInSquare() {
		
		// set the squares between [1][2] and [1][17] with flags and mines
		for(int i=2; i<18;i++) {
			playground.getSquares()[1][i].setFlag(true);
			playground.getSquares()[1][i].setMine(true);
		}
		
		playground.setNumSurMineInSquare();
		assertTrue(playground.getSquares()[0][1].getNumSurMine() == 1);
		assertTrue(playground.getSquares()[1][2].getNumSurMine() == 1);
		assertTrue(playground.getSquares()[2][10].getNumSurMine() == 3);
		
	}

	@Test
	void testGetSurroundingNum() {
		// set the squares between [0][0] and [0][18] with flags and mines
		for(int i=0; i<19;i++) {
			playground.getSquares()[0][i].setFlag(true);
			playground.getSquares()[0][i].setMine(true);
		}
		
		// square[0][1] has two mines in surrounding
		assertTrue(playground.getSurroundingNum(playground.getSquares(), 0, 1) == 2);
		// square[1][3] has three mines in surrounding
		assertTrue(playground.getSurroundingNum(playground.getSquares(), 1, 3) == 3);
		// square[8][9] has no mines in surrounding
		assertTrue(playground.getSurroundingNum(playground.getSquares(), 8, 9) == 0);
	}

}
