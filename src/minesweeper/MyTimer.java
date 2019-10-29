package minesweeper;



import javax.swing.JLabel;

import java.awt.Font;
import java.util.Timer;

public class MyTimer extends JLabel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Timer timer;

	
	public MyTimer() {
		timer = new Timer(); 
		
		// set the property of timer
		this.setHorizontalAlignment(CENTER);
		this.setFont(new Font("Calibri", Font.BOLD, 30));
		this.setText("00:00:00");
	}
	
	/**
	 * To start the timer
	 */
	public void timerStart() {
		
		// execute the MyTimeTask every a certain time (every 1 second) after 1 second\
		timer.schedule(new MyTimerTask(this) ,1000, 1000);
	}
	
	/**
	 * cancel the timer when the user wins or loses
	 */
	public void timerStop() {
		timer.cancel();
	}
	
	/**
	 * restart the timer in the frame
	 */
	public void restart() {
		
		this.timerStop();
	
		this.setText("00:00:00");
		
	}
	
}
