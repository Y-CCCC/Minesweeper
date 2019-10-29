package minesweeper;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Square extends JButton {

	private static final long serialVersionUID = 1L;

	// the row of the square in the playground
	private int row;
	// the column of the square in the playground
	private int column;
	// boolean value for mine
	private boolean isMine;
	// the number of the mines in the surrounding of this square
	private int numSurMine;
	// determining if this square is left-clicked
	private boolean isLeftClicked;
	// the square is marked with flag or not
	private boolean isFlag;


	// the icon for unrevealed square
	private ImageIcon unrevealed;
	// the icon for mine
	private ImageIcon mine;
	// the icon for flag
	private ImageIcon flag;

	// the icon for square that has been flagged
	private ImageIcon mineForFlag;

	// the constructor of the Mine class
	public Square(int row, int column, boolean isMine) {
		super();
		// set the row, column, isMine, isClicked
		this.row = row;
		this.column = column;
		this.isMine = isMine;
		this.isLeftClicked = false;
		this.isFlag = false;

		// initialize all the icons
		unrevealed = new ImageIcon("Image/unrevealed.png");
		mine = new ImageIcon("Image/mine.png");
		flag = new ImageIcon("Image/flag.png");
		mineForFlag = new ImageIcon("Image/mineForFlag.png");

		// in the beginning, all the squares are in unrevealed state
		this.setIcon(unrevealed);
		this.setPreferredSize(new Dimension(unrevealed.getIconWidth(), unrevealed.getIconHeight()));
	}

	/**
	 * When this square is blank and left-clicked,
	 */
	public void blankClicked(Playground playground) {
		// get all the squares
		Square[][] squares = playground.getSquares();
		
		// we need to reach the eight squares around this square

		// left-top-diagonal square if it is within the playground
		if ((this.row - 1 >= 0) && (this.column - 1 >= 0)) {
			if (!squares[this.row - 1][this.column - 1].isFlag()) { // if the square is flagged, it cannot be left-clicked
				squares[this.row - 1][this.column - 1].leftClicked(playground);
			}
		}

		// left square if it is within the playground
		if (this.column - 1 >= 0) {
			if (!squares[this.row][this.column - 1].isFlag()) {
				squares[this.row][this.column - 1].leftClicked(playground);
			}
		}

		// left-bottom-diagonal square if it is within the playground
		if ((this.row + 1 < Playground.PLAYGROUND_ROW) && (this.column - 1 >= 0)) {
			if (!squares[this.row + 1][this.column - 1].isFlag())
				squares[this.row + 1][this.column - 1].leftClicked(playground);
		}

		// bottom square if it is within the playground
		if (this.row + 1 < Playground.PLAYGROUND_ROW) {
			if (!squares[this.row + 1][this.column].isFlag()) {
				squares[this.row + 1][this.column].leftClicked(playground);
			}
		}

		// right-bottom square if it is within the playground
		if ((this.row + 1 < Playground.PLAYGROUND_ROW) && (this.column + 1 < Playground.PLAYGROUND_COLUMN)) {
			if (!squares[this.row + 1][this.column + 1].isFlag()) {
				squares[this.row + 1][this.column + 1].leftClicked(playground);
			}
		}

		// right square if it is within the playground
		if (this.column + 1 < Playground.PLAYGROUND_COLUMN) {
			if (!squares[this.row][this.column + 1].isFlag()) {
				squares[this.row][this.column + 1].leftClicked(playground);
			}

		}

		// right-top-diagonal square if it is within the playground
		if ((this.row - 1 >= 0) && (this.column + 1 < Playground.PLAYGROUND_COLUMN)) {
			if (!squares[this.row - 1][this.column + 1].isFlag()) {
				squares[this.row - 1][this.column + 1].leftClicked(playground);
			}
		}
		// top square if it is within the playground
		if (this.row - 1 >= 0) {
			if (!squares[this.row - 1][this.column].isFlag()) {
				squares[this.row - 1][this.column].leftClicked(playground);
			}

		}

	}

	/**
	 * When the square is left-clicked, return true if the mine is clicked
	 * 
	 * @param playground
	 * @return true if the mine is clicked
	 */
	public boolean leftClicked(Playground playground) {

		if (!playground.isGameOver()) {
			if (this.isMine) { // game over
				
				return true;

			} else { // if the square is not a mine
						// if the number of the mines in the surrounding is greater than 0, just show
						// the number in this square

				if (this.numSurMine > 0) {
					if (!this.isLeftClicked) {
						// remove the icon
						this.setIcon(null);
						// show the surrounding mines' number
						this.setText(this.numSurMine + "");
						// disable the square
						this.setEnabled(false);
						
						this.isLeftClicked = true;

					}

				} else { // there is no mines around the square

					if (!this.isLeftClicked) { // if the square is not left-clicked
						if (!this.isFlag) { // if the square is flagged, keep it original
							this.setIcon(null);
							this.isLeftClicked = true;
							this.blankClicked(playground);
						}
					}
				}

				Square[][] squares = playground.getSquares();
				int temp = 0; // store the number of squares that have been clicked
				
				// iterate over the squares to see how many squares have been left-clicked
				for (int i = 0; i < Playground.PLAYGROUND_ROW; i++) {
					for (int j = 0; j < Playground.PLAYGROUND_COLUMN; j++) {
						if (squares[i][j].isLeftClicked) {
							temp++;
						}
					}
				}
			
				// judge if the user wins
				if (temp == (Playground.PLAYGROUND_ROW * Playground.PLAYGROUND_COLUMN - Playground.MINES_NUM)) { // if all the non-mines are left-clicked

					playground.gameOverWin();
					return false;
				}
				return false;
			}
		}

		playground.minesRefresh();
		
		return false; 
	}

	/** When the square is right-clicked
	 * @param playground
	 * @param isMineLeft: boolean to determine if there are flags to use
	 */
	public void rightClicked(Playground playground, boolean isMineLeft) {
		
		if (!playground.isGameOver()) { // if it is not game over
			if (isMineLeft) { // there is at least a mine left
				if (!this.isFlag) { // if the square has not been flagged
					if (!this.isLeftClicked) { // if the square has not been left-clicked
						this.setIcon(flag);
						this.isFlag = true;
					}
				} else { // if the square has been flagged
					// unflag the square
					this.setIcon(unrevealed);
					this.isFlag = false;
				}
			} else { // there is no mine left
				if (this.isFlag) { // if it is flagged
					// unflag the square
					this.setIcon(unrevealed);
					this.isFlag = false;
				}
			}
		}

		playground.minesRefresh();
	}

	public boolean isFlag() {
		return isFlag;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	public boolean isMine() {
		return isMine;
	}

	public void setNumSurMine(int numSurMine) {
		this.numSurMine = numSurMine;
	}

	public int getNumSurMine() {
		return numSurMine;
	}

	public boolean isLeftClicked() {
		return isLeftClicked;
	}

	/**
	 * When a mine is clicked, all the non-clicked squares are disabled
	 */
	public void disableForNonClicked() {
		if (this.isMine) { // if the square is a mine
			if (this.isFlag) { // if the square is flagged, keep the mineForFlag icon for it
				this.setIcon(mineForFlag);
				this.setEnabled(false);
			} else { // if the square is not flagged, set the mine icon to it
				this.setIcon(mine);
				this.setEnabled(false);
			}

		} else { // not a mine
			if (!this.isFlag) { // if the square is not flagged, disable it
				this.setIcon(null);
				this.setEnabled(false);
			}
		}
	}

	public void flagMarked() {
		this.setIcon(flag);
	}
	
	
	
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	 

	public void setLeftClicked(boolean isLeftClicked) {
		this.isLeftClicked = isLeftClicked;
	}

	public void setFlag(boolean isFlag) {
		this.isFlag = isFlag;
	}

	/**
	 * For restarting the game, set the square into default value
	 */
	public void restart() {
		this.isMine = false;
		this.isLeftClicked = false;
		this.isFlag = false;
		this.setText(null);
		this.setEnabled(true);
		this.setIcon(unrevealed);

	}
}
