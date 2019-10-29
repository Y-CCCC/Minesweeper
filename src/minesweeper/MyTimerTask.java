package minesweeper;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask{
	MyTimer myTimer;
	private long start;
	private long now;
	private long time;
	
	public MyTimerTask(MyTimer myTimer) {
		this.myTimer = myTimer;
		// record the starting time
		start = System.currentTimeMillis();
	}
	
	
	/**
	 * When the timer task is created, compute the difference between the current time and starting time, and then transform the time to (hh:mm:ss) format in myTimer
	 */
	@Override
	public void run() {
		// acquire the current time
		now = System.currentTimeMillis();
		time = now - start;
		// refresh the timer after a certain time
		this.myTimer.setText(this.time2Date(time));
	}
	
	/**
	 * transform the time into (hh:mm:ss) format
	 * @param time (millisecond)
	 * @return the string type of time (hh:mm:ss)
	 */
	public String time2Date(long time) {
		String date = String.format("%02d:%02d:%02d", time /(1000*1000*60)%60, time / (1000*60) % 60, time/1000 % 60);
		return date;
	}
	
	
}


