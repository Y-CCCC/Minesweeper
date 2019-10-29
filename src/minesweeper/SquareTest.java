package minesweeper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class SquareTest {
	
	Square square1;
	Square square2;
	Square square3;
	
	@BeforeEach
	void setUp() throws Exception {
		
	square1 = new Square(4, 3, false);
	square2 = new Square(7, 6, true);
	square3 = new Square(1, 9, true);
		
	}
	
	@Test
	void testSquare() {
		// test square1
		assertTrue(square1.getRow() == 4);
		assertTrue(square1.getColumn() == 3);
		assertFalse(square1.isMine());
		
		// test square2
		assertTrue(square2.getRow() == 7);
		assertTrue(square2.getColumn() == 6);
		assertTrue(square2.isMine());
		
		// test square3
		assertTrue(square3.getRow() == 1);
		assertTrue(square3.getColumn() == 9);
		assertTrue(square3.isMine());
		
	}

	
	



	@Test
	void testRestart() {
		
		// square1
		square1.setMine(true);
		square1.setLeftClicked(true);
		square1.setFlag(true);
		
		assertTrue(square1.isMine());
		assertTrue(square1.isLeftClicked());
		assertTrue(square1.isFlag());
		
		// square2
		square2.setMine(false);
		square2.setLeftClicked(true);
		square2.setFlag(true);
		
		assertFalse(square2.isMine());
		assertTrue(square2.isLeftClicked());
		assertTrue(square2.isFlag());
		
		// square3
		square3.setMine(false);
		square3.setLeftClicked(true);
		square3.setFlag(false);
		
		assertFalse(square3.isMine());
		assertTrue(square3.isLeftClicked());
		assertFalse(square3.isFlag());
		
		// restart
		square1.restart();
		square2.restart();
		square3.restart();
		
		// test square1
		assertFalse(square1.isMine());
		assertFalse(square1.isLeftClicked());
		assertFalse(square1.isFlag());
		
		// test square2
		assertFalse(square2.isMine());
		assertFalse(square2.isLeftClicked());
		assertFalse(square2.isFlag());
				
		// test square3
		assertFalse(square3.isMine());
		assertFalse(square3.isLeftClicked());
		assertFalse(square3.isFlag());
	}

}
