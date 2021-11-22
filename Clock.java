package clock;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Romeo Garcia
 * @category Personal Project
 * 
 * Date: 11/15/2021
 * Time Taken: 2 days
 * Description: A simple clock that will display either military time, or if requested, regular time based on the user input.
 * Sources Consulted: GeeksForGeeks, Java Oracle
 * Known Bugs: N/A
 * Notes: The program runs nicely with almost no CPU demand, it displays accordingly, and does all that is required
 * of a bare bones clock. The seconds do reset when changing between military or regular, so the time is not as consistent
 * as it could be when switching. There is a possible question of thread safety with the way I am invoking the thread.sleep() function, but due
 * to the simplicity of the program, neither of these are an issue.
 */

/*
 * Military Clock class, this will iterate and run through the 24 hour cycle clock.
 * Its simpler than the 12 hour cycle to create, being that there is no need for 
 * the calculation of AM to PM.
 */
class mil_clock_task extends TimerTask {
	
	public boolean check = true;
	private int hour, minute, seconds = 0;
	
	// This is called automatically, so no iteration is required for the clock to run indefinitely.
	public void run() {
		// iterates through seconds, minutes, then hours based off a 20 base, and then a 24 base for hours.
		seconds++;
		if(seconds > 59) {
			minute++;
			seconds = 0;
		}
		if(minute > 59) {
			hour++;
			minute = 0;
		}
		if(hour > 23) {
			hour = 0;
		}
	}
	
	/*
	 * Getter for the GUI.
	 */
	public String getTime() {
		String dpHour, dpMinute, dpSecond = "";
		
		if(hour < 10) { dpHour = "0" + hour;}
		else { dpHour = "" + hour;}
		
		if(minute < 10) { dpMinute = "0" + minute;}
		else { dpMinute = "" + minute;}
		
		if(seconds < 10) { dpSecond = "0" + seconds;}
		else { dpSecond = "" + seconds;}
		
		return dpHour + ":" + dpMinute + ":" + dpSecond;
	}
	
	/*
	 * This is a hidden functionality, it allows the clock to carry over the 
	 * time when changing between military to regular or vice versa.
	 */
	public String privateGetTime() {
		return hour + ":" + minute;
	}
	
	/*
	 * This functions as a way of changing the base time components either when switching 
	 * time types, or when the user sets the time.
	 */
	public void setTime(String newTime) {
		String[] list = newTime.split(":");
		if(list.length == 2) {
			hour = Integer.parseInt(list[0]);
			minute = Integer.parseInt(list[1]);
		}
		else {
			if(list[2].compareToIgnoreCase("PM") == 0) {
				hour = 12 + Integer.parseInt(list[0]);
				minute = Integer.parseInt(list[1]);
			}
			else {
				hour = Integer.parseInt(list[0]);
				minute = Integer.parseInt(list[1]);
			}
		}
	}
	
} // end of mil_clock_task class


/*
 * Regular Clock class, this will iterate and run through the 12 hour cycle clock, with AM and PM attached.
 */
class reg_clock_task extends TimerTask {
	
	public boolean check = true;
	
	private int hour = 12;
	private int hour_shadow = 0; // this is used for differentiating between AM and PM
	private int minute, seconds = 0;
	private String period = "AM";
	
	public void run() {
		// iterates through the seconds and minutes based off a 60 base.
		seconds++;
		if(seconds > 59) {
			minute++;
			seconds = 0;
		}
		if(minute > 59) {
			hour++;
			hour_shadow++; // increments with hours
			minute = 0;
		}
		
		// section for hours, the hour_shadow is what allows the differentiation between PM and AM
		if(hour > 12) {
			hour = 1;
		}
		if(hour_shadow == 12) {
			period = "PM";
		}
		if(hour_shadow == 24) {
			hour_shadow = 0;
			period = "AM";
			
		}
	}
	
	/*
	 * Getter for the GUI.
	 */
	public String getTime() {
		if(minute < 10) {
			return "" + hour + ":0" + minute + " " + period;
		}
		return "" + hour + ":" + minute + " " + period;
	}
	
	/*
	 * This is a hidden functionality, it allows the clock to carry over the 
	 * time when changing between military to regular or vice versa.
	 */
	public String privateGetTime() {
		return "" + hour + ":" + minute + ":" + period;
	}
	
	public void setTime(String newTime) {
		String[] list = newTime.split(":");
		if(list.length == 3) {
			hour = Integer.parseInt(list[0]);
			minute = Integer.parseInt(list[1]);
			period = list[2];
		}
		else {
			if(Integer.parseInt(list[0]) > 12) {
				hour = Integer.parseInt(list[0]) - 12;
				minute = Integer.parseInt(list[1]);
				period = "PM";
			}
			else {
				hour = Integer.parseInt(list[0]);
				minute = Integer.parseInt(list[1]);
			}
		}
	}
	
} // end of clock_task class


/*
 * class Clock
 * 
 * calls on helper methods from two other classes, reg_clock_task, and mil_clock_task.
 */
public class Clock {
	
	TimerTask task1 = new mil_clock_task();
	TimerTask task2 = new reg_clock_task();
	String savedTime = ""; // stores the previous time when switching types
	String mode = "R"; // helps keep track of whether we are in military or regular mode.
	
	/* initializer method, not necessary but keeps things organized. Takes given type and switches to that type's method.
	 *  Defaults to the regular clock.
	 */
	public void init(String type) {
		if(type.compareTo("M") == 0) {
			mode = "M";
			timer_mil();
		}
		else {
			mode = "R";
			timer_reg();
		}
	}
	
	/*
	 * Over loaded initializer. It does the same as the previous one, but takes in a saved time for if the user
	 * decides to switch the clock type.
	 */
	public void init(String type, String save) {
		savedTime = save;
		if(type.compareTo("M") == 0) {
			mode = "M";
			timer_mil();
		}
		else {
			mode = "R";
			timer_reg();
		}
	}
	
	// cancels the tasks so that they can be re-intialized.
	public void cancelM() {
		if(((mil_clock_task)task1).check == true) {
			task1.cancel();
		}
	}
	
	// cancels the tasks so that they can be re-intialized.
	public void cancelR() {
		if(((reg_clock_task)task2).check == true) {
			task2.cancel();
		}
	}
	
	// calls onto the helper that will display military time
	private void timer_mil() {
		Timer timer = new Timer();
		task1 = new mil_clock_task();
		timer.schedule(task1, 0, 1000);
		
		if(savedTime != "") {
			milSetTime(savedTime);
			savedTime = "";
		}
	}
	
	// calls onto the helper that will display regular time
	private void timer_reg() {
		Timer timer = new Timer();
		task2 = new reg_clock_task();
		timer.schedule(task2, 0, 1000);
		
		if(savedTime != "") {
			regSetTime(savedTime);
			savedTime = "";
		}
	}
	
	// ----------------------[getters and setters]------------------------ \\
	
	// this returns the current mode.
	public String mode() {
		return mode;
	}
	
	public String regGetTime() {
		return ((reg_clock_task) task2).getTime();
	}
	
	// private getter for switching modes.
	public String regGetTimePrivate() {
		return ((reg_clock_task) task2).privateGetTime();
	}
	
	public void regSetTime(String newTime) {
		((reg_clock_task) task2).setTime(newTime);
	}
	
	public String milGetTime() {
		return ((mil_clock_task) task1).getTime();
	}
	
	// private getter for switching modes.
	public String milGetTimePrivate() {
		return ((mil_clock_task) task1).privateGetTime();
	}
	
	public void milSetTime(String newTime) {
		((mil_clock_task) task1).setTime(newTime);
	}
	
} // end of the clock class
