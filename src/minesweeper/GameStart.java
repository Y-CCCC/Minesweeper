package minesweeper;

public class GameStart {

	public static void main(String[] args) {

		// initialize the playground
		Playground playground = new Playground();
		// randomly set a certain number of mines in the playground
		playground.setDefaultMines();
		// compute the number of surrounding mines in each square
		playground.setNumSurMineInSquare();
	}

}
