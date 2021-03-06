# Simple Clock
A simple clock that will display either military time, or if requested, regular time based on the user input.

### README
The display class harbors the main method, so run that. It will automatically set the clock to mode "R", and begin at a base of 12:00 AM. The UI is fairly straight forward, you
have the options button, and the exit button underneath the clock display. Exiting the program either requires pressing the X in the top right of the window, or pressing the exit
button itself. 

The options button takes the user to a hidden menu that allows to switch between AM-PM time, or 'regular', or military time. The back and forth between the two will carry over
the minutes and hours, but not the seconds. There is also the functionality of setting the time of the clock. Made easier for both the GUI and user, setting the clock
is possible through three spinners, one for hours, one for minutes, and one for the period AM or PM. If the user only wants military, there is a radio button 
that hides the regular functions and shows the military options. 

There is more hidden functionality for quality of life purposes that are scattered throughout the code, but will not be discussed here. 

### Recollection
If there was anything else to be added to
the code, it would be serialization. As the code demonstrates skills of methods, classes, overloading, GUI components, java swing, timers and tasks, thread use, initializers, 
getters and setters, helper methods, polymorphism, and data structures such as ArrayLists. Serialization, and recursion are the only other concepts that are lacking 
in this particular project, and indeed could bleed there way into this project, but for time sake I chose to leave those out.

Serialization would be the easiest to implement, and would be started at the exit button action listener, where we would make the new file "savedtime" and store it. Then in the
main of display, have a try-catch that would ask if that file exists, and if it does, call the initializer that takes in a saved time and give it the file that was created, and 
then delete the file. Otherwise, proceed with first time boot-up, being how it proceeds currently. Recursion would be slightly harder to implement give that it doesn't
have any necessary use for a clock, but would be useful for a timer, but again since we already use the timer class, why not just use the timer class.

### Running the File
Make sure that Clock.java and Display.java are in the same directory, then run the Display.java file. That should be all the user must do so long as they have JDK13 and up installed on their computer.
