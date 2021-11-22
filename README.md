### Simple Clock
A simple clock that will display either military time, or if requested, regular time based on the user input.

# README
The display class harbors the main method, so run that. It will automatically set the clock to mode "R", and begin at a base of 12:00 AM. The UI is fairly straight forward, you
have the options button, and the exit button underneath the clock display. Exiting the program either requires pressing the X in the top right of the window, or pressing the exit
button itself. 

The options button takes the user to a hidden menu that allows to switch between AM-PM time, or 'regular', or military time. The back and forth between the two will carry over
the minutes and hours, but not the seconds. There is also the functionality of setting the time of the clock. Made easier for both the GUI and user, setting the clock
is possible through three spinners, one for hours, one for minutes, and one for the period AM or PM. If the user only wants military, there is a radio button 
that hides the regular functions and shows the military options. 

There is more hidden functionality for quality of life purposes that are scattered throughout the code, but will not be discussed here.

# Running the File
Make sure that Clock.java and Display.java are in the same directory, then run the Display.java file. That should be all the user must do so long as they have JDK13 and up installed on their computer.
