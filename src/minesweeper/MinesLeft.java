package minesweeper;

import java.awt.Font;


import javax.swing.JLabel;

public class MinesLeft extends JLabel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MinesLeft() {
		super();
		// set the minesLeft with default values
		this.setHorizontalAlignment(CENTER);
		this.setFont(new Font("Calibri", Font.BOLD, 30));
		this.setText("Mines left: "+Playground.MINES_NUM);

	}
	
	/**
	 * show the number of mines left(available flags you can use) in the playground
	 * @param playground
	 * @return
	 */
	public boolean showMinesLeft(Playground playground) {
		
		int minesLeft = Playground.MINES_NUM; // store the left mines
		
		// get the squares in the playground
		Square[][] mines = playground.getSquares();
		
		// iterate over the squares
		for(int i=0;i<Playground.PLAYGROUND_ROW;i++) {
			for(int j=0; j<Playground.PLAYGROUND_COLUMN;j++) {
				if(mines[i][j].isFlag()) { // if a mine is flagged, minesLeft decreased by 1
					minesLeft--;
				}
			}
		}
		
		// show mines left
		this.setText("Mines left: "+minesLeft);
		
		// if there is no mine left(no flags available), return false. Otherwise, return true
		if(minesLeft == 0) {
			return false;
		} else {
			return true;
		}
	}
}
