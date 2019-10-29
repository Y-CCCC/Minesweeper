package minesweeper;


import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;


public class SmileFace extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// the icon for restarting the game
	private ImageIcon smile;
	// the icon for win
	private ImageIcon win;
	// the icon for lose
	private ImageIcon lose;

	public SmileFace() {
		
		// initialize the JButton
		super();

		// initialize three face types
		smile = new ImageIcon("Image/smile.png");
		win = new ImageIcon("Image/win.png");
		lose = new ImageIcon("Image/lose.png");

		// set the default value to smile
		this.setIcon(smile);
		this.setHorizontalAlignment(CENTER);
		this.setPreferredSize(new Dimension(smile.getIconWidth(), smile.getIconHeight()));

	}

	/**
	 * Set the smile face with lose face
	 */
	public void lose() {
		this.setIcon(lose);
	}

	/**
	 * Set the smile face with win face
	 */
	public void win() {
		this.setIcon(win);
	}

	/**
	 * restart the game and set all variables in playground into default
	 * 
	 * @param playground
	 */
	public void restart(Playground playground) {
		
		// set the three variables in playground into default
		playground.setGameOver(false);
		playground.setMineLeft(true);
		playground.setTimerStart(false);

		// set new squares in the panel2
		playground.setNewMines();
		
		// randomly set the mines
		playground.setDefaultMines();
		// set the number of the surrounding mines in specific square
		playground.setNumSurMineInSquare();

		playground.getMinesLeft().showMinesLeft(playground);
		
		// set the smile face to the smileface component
		this.setIcon(smile);
		
		// restart the timer
		playground.getMyTimer().restart();
		// set the new timer
		playground.setNewMyTimer();
	}
}
