import java.util.ArrayList;
import java.util.List;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

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
 * Display class that handles the GUI needed for the clock. Keeps track of action listeners,
 * and also is what updates the ticking frames for the clock.
 */
@SuppressWarnings("serial")
public class Display extends JFrame implements ItemListener{
	
	// this is how we access the clock and keep the clock ticking.
	private static Clock clk;
	private static boolean isRunning = true;
	
	private static final int HEIGHT = 200;
	private static final int WIDTH = 350;
	
	private static JLabel clockLabel;
	
	private SpinnerListModel hourModelM; // this is the mil hours
	private SpinnerListModel hourModelR; // this is the reg hours
	private SpinnerListModel minuteModel; // this is for the minutes
	private SpinnerListModel periodModel; // for regular time
	private JSpinner spinnerHM; // military time hours
	private JSpinner spinnerHR; // regular time hours
	private JSpinner spinnerM;
	private JSpinner spinnerP;
	private JCheckBox periodButton;
	
	private JButton optionsButton, exitButton, backButton, milButton, regButton, stButton;
	private OptionsButtonHandler optionsHandler;
	private ExitButtonHandler exitButtonHandler;
	private BackButtonHandler backButtonHandler;
	private MilButtonHandler milButtonHandler;
	private RegButtonHandler regButtonHandler;
	private SetTimeButtonHandler stButtonHandler;
	
	/*
	 * This is the display main that harbors all the primary GUI components.
	 */
	public Display() {
		// -------------------[clock display]----------------------- \\
		// the label that displays the clock itself
		clockLabel = new JLabel("1:00 AM", SwingConstants.CENTER); // centered
		clockLabel.setFont(new Font("Times New Roman", Font.PLAIN, 50)); // set the font
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Clock"); // create a title border
		title.setTitlePosition(TitledBorder.ABOVE_TOP);
		title.setTitleJustification(TitledBorder.CENTER); // adjustments
		clockLabel.setBorder(title); // officially set it all on the new label

		// options button and its listener
		optionsButton = new JButton("Options");
		optionsHandler = new OptionsButtonHandler();
		optionsButton.addActionListener(optionsHandler);
		
		// exit button and its listener
		exitButton = new JButton("Exit");
		exitButtonHandler = new ExitButtonHandler();
		exitButton.addActionListener(exitButtonHandler);
		
		// -------------------[options display]----------------------- \\
		
		// back button and its listener
		backButton = new JButton("Back");
		backButtonHandler = new BackButtonHandler();
		backButton.addActionListener(backButtonHandler);
		
		// military button and its listener
		milButton = new JButton("Military");
		milButtonHandler = new MilButtonHandler();
		milButton.addActionListener(milButtonHandler);
		
		// regular button and its listener
		regButton = new JButton("Regular");
		regButtonHandler = new RegButtonHandler();
		regButton.addActionListener(regButtonHandler);
		
		// set time button and its listener
		stButton = new JButton("Set Time");
		stButtonHandler = new SetTimeButtonHandler();
		stButton.addActionListener(stButtonHandler);
		
		
		// this is the input field for military hours
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < 24; i++) {
			list.add("" + i);
		}
		hourModelM = new SpinnerListModel(list);
		spinnerHM = new JSpinner(hourModelM);
		((JSpinner.DefaultEditor) spinnerHM.getEditor()).getTextField().setEditable(false);
		
		// this is the input field for regular hours
		List<String> list2 = new ArrayList<String>();
		for(int i = 1; i < 13; i++) {
			list2.add("" + i);
		}
		hourModelR = new SpinnerListModel(list2);
		spinnerHR = new JSpinner(hourModelR);
		((JSpinner.DefaultEditor) spinnerHR.getEditor()).getTextField().setEditable(false);
		
		// this is the input field for minutes
		List<String> list3 = new ArrayList<String>();
		for(int i = 0; i < 60; i++) {
			list3.add("" + i);
		}
		minuteModel = new SpinnerListModel(list3);
		spinnerM = new JSpinner(minuteModel);
		((JSpinner.DefaultEditor) spinnerM.getEditor()).getTextField().setEditable(false);
		
		// This is the input field for period
		List<String> list4 = new ArrayList<String>();
		list4.add("AM");
		list4.add("PM");
		periodModel = new SpinnerListModel(list4);
		spinnerP = new JSpinner(periodModel);
		((JSpinner.DefaultEditor) spinnerP.getEditor()).getTextField().setEditable(false);
		
		// this is the checkbox for am-pm (or period)
		periodButton = new JCheckBox("Military");
		periodButton.setSelected(false);
		periodButton.addItemListener(this);
		
		// -------------------[setting the pan and grid]----------------------- \\
		// this sets all the dimensions of the GUI
		setTitle("Clock");
		Container pane = getContentPane();
		pane.setLayout(new GridBagLayout()); // favorite type of layout, allows room for adjustment
		GridBagConstraints a = new GridBagConstraints(); // think of this as our layout manager
		
		// for the clock display
		a.fill = GridBagConstraints.HORIZONTAL;
		a.gridx = 0;
		a.gridy = 1;
		a.gridwidth = 3;
		a.ipady = 40; // makes this next component taller.
		a.ipadx = 120; // makes this component wider
		pane.add(clockLabel, a);
		
		// for the options button
		a.anchor = GridBagConstraints.PAGE_END; // bottom of the space in the GUI
		a.insets = new Insets(10, 0, 0, 0); // top padding
		a.gridx = 0;
		a.gridy = 2;
		a.gridwidth = 1;
		a.ipady = 0; // reset
		a.ipadx = 80;
		a.weighty = 1.0; // requests any vertical space
		pane.add(optionsButton, a);
		
		// the exit button
		a.gridx = 2;
		a.ipadx = 0; // reset
		pane.add(exitButton, a);
		
		// -------------------[hidden options menu]----------------------- \\
		GridBagConstraints b = new GridBagConstraints();
		
		// for the back button
		b.anchor = GridBagConstraints.FIRST_LINE_START; // top of the space
		b.insets = new Insets(0, 0, 10, 0); // bottom padding
		b.gridx = 0;
		b.gridy = 0;
		b.gridwidth = 1;
		b.gridheight = 1;
		b.ipadx = 0;
		b.ipady = 0;
		b.weightx = 1;
		b.weighty = 1;
		pane.add(backButton, b);
		
		// regular button
		b.insets = new Insets(0, 10, 0, 10); // reset
		b.gridx = 0;
		b.gridy = 1;
		b.gridwidth = 1;
		b.gridheight = 2;
		b.ipadx = 80;
		b.ipady = 10;
		b.weightx = 1;
		b.weighty = 1;
		pane.add(regButton, b);
		
		// military button
		b.gridx = 1;
		b.gridy = 1;
		b.gridwidth = 2;
		b.gridheight = 2;
		b.ipadx = 80;
		b.ipady = 10;
		b.weightx = 1;
		b.weighty = 1;
		pane.add(milButton, b);
		
		// set time button
		b.anchor = GridBagConstraints.PAGE_END; // bottom of the space in the GUI
		b.insets = new Insets(0, 20, 10, 0);
		b.gridx = 0;
		b.gridy = 3;
		b.gridwidth = 1;
		b.gridheight = 3;
		b.ipadx = 60;
		b.ipady = 20;
		b.weightx = 1;
		b.weighty = 1;
		pane.add(stButton, b);
		
		// the hour spinner
		b.insets = new Insets(0, 0, 9, 110);
		b.gridx = 1;
		b.gridy = 3;
		b.gridwidth = 1;
		b.gridheight = 3;
		b.ipadx = 24;
		b.ipady = 27;
		b.weightx = 1;
		b.weighty = 1;
		pane.add(spinnerHM, b);
		
		// the reg hour spinner (which is hidden underneath the HM)
		pane.add(spinnerHR, b);
		
		// the minute spinner
		b.insets = new Insets(0, 10, 9, 10);
		b.gridx = 1;
		b.gridy = 4;
		b.gridwidth = 3;
		b.gridheight = 4;
		b.ipadx = 22;
		b.ipady = 27;
		b.weightx = 1;
		b.weighty = 1;
		pane.add(spinnerM, b);
		
		// the period spinner
		b.insets = new Insets(0, 10, 9, -84);
		b.gridx = 1;
		b.gridy = 4;
		b.gridwidth = 3;
		b.gridheight = 4;
		b.ipadx = 0;
		b.ipady = 27;
		b.weightx = 1;
		b.weighty = 1;
		pane.add(spinnerP, b);
		
		// period button
		b.insets = new Insets(0, 0, 60, -82);
		b.gridx = 1;
		b.gridy = 4;
		b.gridwidth = 3;
		b.gridheight = 4;
		b.ipadx = 0;
		b.ipady = 0;
		b.weightx = 0;
		b.weighty = 0;
		pane.add(periodButton, b);
		
		// -------------------[final setup for display]----------------------- \\
		
		// setting them invisible as to not disrupt the clock
		backButton.setVisible(false);
		regButton.setVisible(false);
		milButton.setVisible(false);
		stButton.setVisible(false);
		
		spinnerHM.setVisible(false);
		spinnerHR.setVisible(false);
		spinnerM.setVisible(false);
		spinnerP.setVisible(false);
		periodButton.setVisible(false);
		
		// setting final size, making it visible, and then setting default closing operations.
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/*
	 * Makes the clock display invisible, and makes the hidden options display visible.
	 *
	 */
	private class OptionsButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			clockLabel.setVisible(false);
			optionsButton.setVisible(false);
			exitButton.setVisible(false);
			
			backButton.setVisible(true);
			regButton.setVisible(true);
			milButton.setVisible(true);
			stButton.setVisible(true);
			spinnerHR.setVisible(true);
			spinnerM.setVisible(true);
			spinnerP.setVisible(true);
			periodButton.setVisible(true);
		}
	}
	
	/*
	 * Makes the clock visible again after the options menu has been brought up. Makes all options invisible as well.
	 */
	private class BackButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(periodButton.isSelected()) {
				periodButton.doClick(1);
			}
			backButton.setVisible(false);
			regButton.setVisible(false);
			milButton.setVisible(false);
			stButton.setVisible(false);
			spinnerHM.setVisible(false);
			spinnerHR.setVisible(false);
			spinnerM.setVisible(false);
			spinnerP.setVisible(false);
			periodButton.setVisible(false);
			
			clockLabel.setVisible(true);
			optionsButton.setVisible(true);
			exitButton.setVisible(true);
		}
	}
	
	/*
	 * Changes the display time to the military clock.
	 */
	private class MilButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(clk.mode().compareToIgnoreCase("M") == 0) {
				return;
			}
			else {
				String savedTime = clk.regGetTimePrivate();
				clk.cancelR();
				clk.init("M", savedTime);
			}
		}
	}
	
	/*
	 * Changes display time to the regular clock.
	 */
	private class RegButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(clk.mode().compareToIgnoreCase("R") == 0) {
				return;
			}
			else {
				String savedTime = clk.milGetTimePrivate();
				clk.cancelM();
				clk.init("R", savedTime);
			}
		}
	}
	
	/*
	 * Since there is only one button attached to any item listener, we just 
	 * update the GUI here, but in other cases we would want to seperate the update.
	 */
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.DESELECTED) {
			// make the period spinner invisible
			spinnerP.setVisible(true);
			// unchange the hour spinner
			spinnerHM.setVisible(false);
			spinnerHR.setVisible(true);
		}
		else {
			// make the period spinner visible
			spinnerP.setVisible(false);
			// cap the hour spinner to 12
			spinnerHM.setVisible(true);
			spinnerHR.setVisible(false);
		}
	}
	
	/*
	 * Takes the time that is given from the user and sets the label to that new time.
	 * Updates the clock as well so that there is proper iteration.
	 */
	private class SetTimeButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(periodButton.isSelected()) { // use the for military time
				String newTime = (String) spinnerHM.getValue();
				newTime += ":" + spinnerM.getValue();
				clk.milSetTime(newTime);
			}
			else { // regular time
				String newTime = (String) spinnerHR.getValue();
				newTime += ":" + spinnerM.getValue();
				newTime += ":" + spinnerP.getValue();
				clk.regSetTime(newTime);
			}
		}
	}
	
	/*
	 * Updates via the current time.
	 */
	private static void setTime() {
		if(clk.mode().compareToIgnoreCase("R") == 0) {
			clockLabel.setText(clk.regGetTime());
		}
		else {
			clockLabel.setText(clk.milGetTime());
		}
	}
	
	/*
	 * Exits the program.
	 */
	private class ExitButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			isRunning = false;
			System.exit(0);
		}
	}
	
	/*
	 * The initilizer for the updating of the GUI.
	 */
	synchronized public void init() {
		while(isRunning) {
			try {
				Thread.sleep(500);
				setTime();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * the initializer/main for the GUI to be updated. Thread safety would be located here, but due to the 
	 * simplicity of this program, thread safety isn't much of a worry.
	 */
	public static void main(String[] args) {
		Display obj = new Display();
		clk = new Clock();
		clk.init("R");
		
		new Thread() {
			@Override public void run() {obj.init();}
		}.start();
	}// main

} // end of the display class
