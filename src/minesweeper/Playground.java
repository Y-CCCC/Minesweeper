package minesweeper;


import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Playground implements MouseListener {

	// the frame for GUI framework
	private JFrame frame;
	// panel1 for including status information
	private JPanel panel1;
	// panel2 for including mines
	private JPanel panel2;
	// the two-dimensional array
	private Square[][] squares;
	// icon for starting the game
	private SmileFace smileFace;
	// the status for mines left
	private MinesLeft minesLeft;
	// timer for the game
	private MyTimer myTimer;

	// true for game over
	private boolean gameOver;
	// if there is at least a mine left, true
	private boolean isMineLeft;
	// if the timer is started, true
	private boolean isTimerStart;

	
	// the row of playground
	static final int PLAYGROUND_ROW = 16;
	// the column of playground
	static final int PLAYGROUND_COLUMN = 30;
	// the number of mines in the playground
	static final int MINES_NUM = 20;

	public Playground() {
		
		// set the default values
		gameOver = false;
		isMineLeft = true;
		isTimerStart = false;
		// initialize the new frame component and set the default properties
		frame = new JFrame("Minesweeper");
		frame.setLayout(null);
		frame.setBounds(100, 150, 900, 750);
		frame.setResizable(false);

		// initialize the panel1 for including mines left, the smile face in the middle to restart the game and the timer in the right
		// Also, it's grid-layout
		panel1 = new JPanel(null);
		panel1.setBounds(0, 50, 900, 100);
		panel1.setLayout(new GridLayout(1, 3));
		
		// initialize three components
		minesLeft = new MinesLeft();
		smileFace = new SmileFace();
		smileFace.addMouseListener(this); // add mouse listener to the smile face to catch the mouse change
		myTimer = new MyTimer();
		// add them to the panel1
		panel1.add(minesLeft);
		panel1.add(smileFace);
		panel1.add(myTimer);
		// add the panel1 to the frame
		frame.add(panel1);

		// panel2 is for accommodating the squares
		panel2 = new JPanel(null);
		panel2.setBounds(0, 200, 900, 480);
		panel2.setLayout(new GridLayout(PLAYGROUND_ROW, PLAYGROUND_COLUMN)); // grid-layout
		// initialize the squares
		squares = new Square[PLAYGROUND_ROW][PLAYGROUND_COLUMN];
		
		// iterate every square in the array and add them to the panel2 one by one
		for (int i = 0; i < PLAYGROUND_ROW; i++) {
			for (int j = 0; j < PLAYGROUND_COLUMN; j++) {
				squares[i][j] = new Square(i, j, false);
				squares[i][j].addMouseListener(this); // add listener to catch the mouse
				panel2.add(squares[i][j]);
			}
		}
		
		// add panel2 to the frame
		frame.add(panel2);
		// make the frame to be closed by clicking the close button on the window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// set the frame visible to the user
		frame.setVisible(true);

	}

	/**
	 * return the boolean value of gameOver
	 * @return
	 */
	public boolean isGameOver() {
		return gameOver;
	}
	
	
	/**
	 * return the variable myTimer
	 * @return myTimer
	 */
	public MyTimer getMyTimer() {
		return myTimer;
	}

	/**
	 * return the variable minesLeft
	 * @return
	 */
	public MinesLeft getMinesLeft() {
		return minesLeft;
	}

	/**
	 * Every time clicking action happens, update the Mine array in playground
	 */
	public void minesRefresh() {

		Square[][] newSquares = new Square[16][30];
		// get the total number of the components in panel2
		int Count = this.panel2.getComponentCount();
		// iterate every square in the playground and store them in the temporary array
		for (int i = 0; i < Count; i++) {
			newSquares[i / 30][i % 30] = (Square) panel2.getComponent(i);
		}
		
		this.squares = newSquares;
	}
	
	/**
	 * monitor the mouse clicking: left-clicking and right-clicking
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		
		// the square is left-clicked
		if (e.getButton() == MouseEvent.BUTTON1) {
			
			if (smileFace == e.getSource()) { // the smile face is left-clicked
				// restart the game
				smileFace.restart(this);
				
			}  else { // the square is left-clicked
				
				// if the timer is not started, just start it
				if (!this.isTimerStart) {
					myTimer.timerStart(); // start the timer
					this.isTimerStart = true;
				}
				
				// make sure the square is left-clicked, not other components
				// iterate all the mines in the playground to make sure which one is clicked
				for (int i = 0; i < this.panel2.getComponentCount(); i++) {
					if (this.panel2.getComponent(i) == e.getSource()) {
		
						Square clickedMine = (Square) e.getSource(); // save the clicked square into temporary variable
						if (clickedMine.leftClicked(this)) { // if the mine is clicked
							// game over
							this.gameOverLose();
						}
						
						// update the mines array in the playground
						this.minesRefresh();

					}
				}

			}

		} else if (e.getButton() == MouseEvent.BUTTON3) { // the square is right-clicked
			
			// if the timer is not started, just start it
			if (!this.isTimerStart) {
				myTimer.timerStart(); // start the timer
				this.isTimerStart = true;
			}
			
			// make sure the square is right-clicked, not other components
			for (int i = 0; i < this.panel2.getComponentCount(); i++) {
				if (this.panel2.getComponent(i) == e.getSource()) {
					Square clickedMine = (Square) e.getSource();
			
					clickedMine.rightClicked(this, this.isMineLeft);
					// every time we right-click the square, the number of the counter will change
					this.isMineLeft = minesLeft.showMinesLeft(this);
					// update the mines array
					this.minesRefresh();
					
					// check if all the mines have been marked with flags
					if (this.checkFlag()) {
						this.gameOverWin(); // if it is, the user win the game
					}

				}
			}
		}
	}

	/**
	 * if the user wins the game, mark the rest mines with flags and disable the rest unrevealed squares
	 */
	public void gameOverWin() {

		Square[][] mines = this.getSquares();

		// iterate all the mines
		for (int i = 0; i < PLAYGROUND_ROW; i++) {
			for (int j = 0; j < PLAYGROUND_COLUMN; j++) {
				
				// if the square is not left-clicked
				if (!mines[i][j].isLeftClicked()) {

					if (mines[i][j].isMine()) {// mark the rest mines with flags
						mines[i][j].flagMarked();
						
					} else { // the rest unclicked square is disabled
						mines[i][j].disableForNonClicked();
					}
				}

			}
		}
		
		// stop the timer
		this.myTimer.timerStop();
		
		// set the smile face to win face
		this.smileFace.win();
		// show the message box to the user
		this.winMesgBox();
		
		// set the variable gameOver to true
		this.gameOver = true;

	}

	/**
	 * check if all the available flags have been used
	 * @return true if all the flags have been used
	 */
	public boolean checkFlag() {

		Square[][] mines = this.getSquares();
		int temp = 0; // initialize a temporary integer to count the mines that have been marked with flags

		for (int i = 0; i < PLAYGROUND_ROW; i++) {
			for (int j = 0; j < PLAYGROUND_COLUMN; j++) {
				if (mines[i][j].isMine() && mines[i][j].isFlag()) { // if the mine is with flag, add up 1 to the temp
					temp++;
				}
			}
		}

		if (temp == Playground.MINES_NUM) { // if all the mines have been marked
			return true;
		}

		return false;

	}

	/**
	 * Game over when the user clicks on the mine (Lose). 
	 * Disable all the unrevealed squares
	 */
	public void gameOverLose() {

		Square[][] mines = this.getSquares();

		// iterate all the mines
		for (int i = 0; i < PLAYGROUND_ROW; i++) {
			for (int j = 0; j < PLAYGROUND_COLUMN; j++) {

				if (!mines[i][j].isLeftClicked()) { // if the square is not left-clicked

					mines[i][j].disableForNonClicked(); // disable all the non-clicked mines
				}

			}
		}

		// stop the timer
		this.myTimer.timerStop();
		
		// set the smile face to lose face
		this.smileFace.lose();
		
		// show the message box to the user
		this.loseMesgBox();
		
		// set the gameOver to true
		this.gameOver = true;

	}

	

	
	@Override
	public void mousePressed(MouseEvent e) {
	// Not used but have to exist

	}

	@Override
	public void mouseReleased(MouseEvent e) {
	// Not used but have to exist

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	// Not used but have to exist

	}

	@Override
	public void mouseExited(MouseEvent e) {
	// Not used but have to exist	

	}

	

	public Square[][] getSquares() {
		return squares;
	}

	public void setSquares(Square[][] squares) {
		this.squares = squares;
	}

	/**
	 * Generate a certain number of randomly chosen integers between
	 * lowLim(inclusive) and upLim(exclusive)
	 * 
	 * @param lowLim: the lower limits
	 * @param upLim: the upper limits
	 * @param num: the number of integers you wan to choose
	 * @return
	 */
	public ArrayList<Integer> randPerm(int lowLim, int upLim, int num) {
		// initialize a int arraylist to accommodate the randomly chosen numbers
		ArrayList<Integer> randNum = new ArrayList<Integer>();

		int index = 0;
		// add new randomly generated number into the array
		while (index < num) {
			Random random = new Random();
			int temp = random.nextInt(upLim - lowLim) + lowLim;

			// to mark if the number has been generated before
			boolean flag = false;
			for (int i = 0; i < index; i++) {
				if (randNum.get(i) == temp) {
					flag = true;
					break;
				}
			}

			// if the number hasn't been generated, just add the new number into the array list
			if (!flag) {
				randNum.add(temp);
				index++;
			}
		}

		return randNum;

	}
	
	/**
	 * show the message box to the user about wining information
	 */
	public void winMesgBox() {
		JOptionPane.showMessageDialog(frame, "You win!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * show the message box to the user about losing information
	 */
	public void loseMesgBox() {
		JOptionPane.showMessageDialog(frame, "You lose!", "Boom ~", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * randomly generate the mines in the playground
	 */
	public void setDefaultMines() {
		
		// generate different indexes matched with mines' number
		ArrayList<Integer> randNum = this.randPerm(0, PLAYGROUND_COLUMN * PLAYGROUND_ROW, MINES_NUM);
		Square[][] mines = this.getSquares();

		// set the isMine to true according to randomly chosen numbers
		for (int i = 0; i < randNum.size(); i++) {
			int temp = randNum.get(i);
			// set the mine to the square
			mines[temp / 30][temp % 30].setMine(true);
		}

		// refresh the mines array in the playground
		this.minesRefresh();

	}
	
	/**
	 * set the number of surrounding mines in each square
	 */
	public void setNumSurMineInSquare() {

		Square[][] mines = this.getSquares();

		// iterate the mines to set the number of surrounding mines around some specific
		// square
		for (int i = 0; i < PLAYGROUND_ROW; i++) {
			for (int j = 0; j < PLAYGROUND_COLUMN; j++) {
				int numSurMine = this.getSurroundingNum(mines, i, j);
				mines[i][j].setNumSurMine(numSurMine);
			}
		}

		// refresh the mines array in the playground
		this.minesRefresh();
	}

	/**
	 * get the number of surrounding mines in a square
	 * @param mines: mines array
	 * @param i : the row
	 * @param j : the column
	 * @return the number of surrounding mines based on the given row and column
	 */
	public int getSurroundingNum(Square[][] squares, int i, int j) {

		int temp = 0;

		// left-top-diagonal square if it is within the playground
		if ((i - 1 >= 0) && (j - 1 >= 0)) {
			if (squares[i - 1][j - 1].isMine()) { // if the square is a mine, add the temp by 1
				temp++;
			}
		}

		// left square if it is within the playground
		if (j - 1 >= 0) {
			if (squares[i][j - 1].isMine()) { // if the square is a mine, add the temp by 1
				temp++;
			}
		}

		// left-bottom-diagonal square if it is within the playground
		if ((i + 1 < Playground.PLAYGROUND_ROW) && (j - 1 >= 0)) {
			if (squares[i + 1][j - 1].isMine()) { // if the square is a mine, add the temp by 1
				temp++;
			}
		}

		// bottom square if it is within the playground
		if (i + 1 < Playground.PLAYGROUND_ROW) { // if the square is a mine, add the temp by 1
			if (squares[i + 1][j].isMine()) {
				temp++;
			}
		}

		// right-bottom square if it is within the playground
		if ((i + 1 < Playground.PLAYGROUND_ROW) && (j + 1 < Playground.PLAYGROUND_COLUMN)) {
			if (squares[i + 1][j + 1].isMine()) { // if the square is a mine, add the temp by 1
				temp++;
			}
		}

		// right square if it is within the playground
		if (j + 1 < Playground.PLAYGROUND_COLUMN) { // if the square is a mine, add the temp by 1
			if (squares[i][j + 1].isMine()) {
				temp++;
			}
		}

		// right-top-diagonal square if it is within the playground
		if ((i - 1 >= 0) && (j + 1 < Playground.PLAYGROUND_COLUMN)) {
			if (squares[i - 1][j + 1].isMine()) { // if the square is a mine, add the temp by 1
				temp++;
			}
		}

		// top square if it is within the playground
		if (i - 1 >= 0) {
			if (squares[i - 1][j].isMine()) { // if the square is a mine, add the temp by 1
				temp++;
			}
		}

		return temp;
	}
	
	
	
	public void setMyTimer(MyTimer myTimer) {
		this.myTimer = myTimer;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void setMineLeft(boolean isMineLeft) {
		this.isMineLeft = isMineLeft;
	}

	public void setTimerStart(boolean isTimerStart) {
		this.isTimerStart = isTimerStart;
	}
	
	/**
	 * when restarting the game, we need to set new timer to the panel
	 */
	public void setNewMyTimer() {
		// remove  the old timer
		this.panel1.remove(2);
		// initialize the new timer and update it to the playground variables
		MyTimer newTimer = new MyTimer();
		this.myTimer = newTimer;
		// update the new timer to the panel1 
		this.panel1.add(newTimer);
		this.panel1.revalidate();
		this.panel1.repaint();
	}

	/**
	 * set the new mines in the playground when starting the fame
	 */
	public void setNewMines() {
		
		// remove the panel2
		this.frame.remove(this.panel2);
		
		// generate a new panel to accommodate the squares
		JPanel newPanel = new JPanel(null);
		newPanel.setBounds(0, 200, 900, 480);
		newPanel.setLayout(new GridLayout(PLAYGROUND_ROW, PLAYGROUND_COLUMN));

		squares = new Square[PLAYGROUND_ROW][PLAYGROUND_COLUMN];
		
		// generate new mines
		for (int i = 0; i < PLAYGROUND_ROW; i++) {
			for (int j = 0; j < PLAYGROUND_COLUMN; j++) {
				squares[i][j] = new Square(i, j, false);
				squares[i][j].addMouseListener(this);
				newPanel.add(squares[i][j]);
			}
		}
		
		this.panel2 = newPanel;
		// add new panel2 with new generated mines into the frame
		this.frame.add(this.panel2);
		// update the frame
		this.frame.revalidate();
		this.frame.repaint();
		
	}

	public boolean isMineLeft() {
		return isMineLeft;
	}

	public boolean isTimerStart() {
		return isTimerStart;
	}
	
	
}

