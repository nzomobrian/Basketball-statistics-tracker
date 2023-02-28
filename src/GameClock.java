// Brian Nzomo
// Basketball Statistics Tracker Program
// GameClock Class
// The GameClock class creates a count down timer.

import java.util.*;

public class GameClock {
	
	private Timer timer;		// The timer
	private TimerTask task;		// Updates the time
	private int time;			// The current time of the timer measured in tenths-seconds
	private int period;			// The current period of the game (1st, 2nd, 3rd ... quarter/half)
	private boolean running;	// If the clock is currently on/off
	
	// Pre:        'duration' is greater than 0, otherwise throws an IllegalArgumentException.
	// Parameters: 'duration' is an integer giving the length of one period in tenths-seconds.
	// Post:       Constructs a GameClock object initializing the period to be one and the duration
	//             as defined by the parameter 'duration'.
	public GameClock(int duration) {
		checkTime(duration);
		this.time = duration;
		this.period = 1;
	}
	
	// Post: Returns the current period of the game.
	public int getPeriod() {
		return this.period;
	}
	
	// Post: Returns the current time remaining in tenths of seconds.
	public int getTenthsTime() {
		return this.time;
	}
	
	// Post: Returns the current time remaining in a String format that is specific
	//       to basketball time keeping.
	public String getTime() {
		return getBasketBallTime(this.time);
	}
	
	// Pre:  'time' parameter is greater than or equal to 0, otherwise throws an IllegalArgumentException.
	// Post: Adds 'time' amount of time in tenths-seconds to the current time remaining.
	public void addTime(int time) {
		checkTime(time);
		this.time += time;
	}
	
	// Pre:  Subtracting the 'time' parameter from the current time will not result in a negative time.
	//       Otherwise throws an IllegalStateException.
	// Post: Subtracts 'time' amount of time in tenths-seconds to the current time remaining.
	public void subtractTime(int time) {
		if (this.time - time < 0) {
			throw new IllegalStateException("Subtracting " + time + " time would make the total time negative.");
		}
		this.time -= time;
	}
	
	// Pre:  'time' is greater than 0, otherwise throws an IllegalArgumentException.
	// Post: Sets 'time' amount of time in tenths-seconds to the current time remaining.
	public void setTime(int time) {
		checkTime(time);
		this.time = time;
	}
	
	// Post: Runs the timer which updates every tenth of a second. 
	//       Sets running to true.
	public void startTimer() {
		this.timer = new Timer();
		this.task = new TimerTask() {
			@Override
			public void run() {
				if(time <= 0) { 	
					// If the time is 0, then the timer is stopped and the period is incremented
					timer.cancel();
					period++;
				} else {
					time--;
				}
			}
		};
		this.timer.schedule(this.task, 0, 100);
		this.running = true;
	}
	
	// Post: Stops the timer and sets running to false.
	public void stopTimer() {
		this.timer.cancel();
		this.running = false;
	}
	
	// Post: Returns true if the timer is running and false if it is not.
	public boolean isRunning() {
		return this.running;
	}
	
	// Post: Returns a String representation of the given 'time' parameter in the standard basketball
	//       time keeping manner. The timer runs as normal while there is more than one minute left.
	//       Once there is less than a minute left, the tenths of seconds remaining are shown as well. 
	private String getBasketBallTime(int time) {
		checkTime(time);
		if(time < 600) {
			return (time / 10) + "." + (time % 60 % 10);
		} else {
			if(time % 600 < 100) {
				return (time / 600) + ":0" + (time / 10 % 60);
			} else {
				return (time / 600) + ":" + (time / 10 % 60);
			}
		}
	}
	
	// Post: Throws an IllegalArgumentException is the 'time' parameter is negative.
	private void checkTime(int time) {
		if (time < 0) {
			throw new IllegalArgumentException("Time cannot be negative.");
		}
	}
    
	// Post: Returns a String representation of the given 'time'.
	private String getTime(int time) {
		if(time < 600) {
			if(time < 100) {
				return "00:0" + time;
			} else {
				return "00:" + (time % 60);
			}
		} else if(time >= 600) {
			if(time < 6000) {
				if(time % 60 < 100) {
					return "0" + (time / 60) + ":0" + (time % 60); 
				} else {
					return "0" + (time / 60) + ":" + (time % 60); 
				}
			} else {
				if(time % 600 < 100) {
					return (time / 60) + ":0" + (time % 60); 
				} else {
					return (time / 60) + ":" + (time % 60); 
				}
			}
		} else {
			return "";
		}
	}
}
