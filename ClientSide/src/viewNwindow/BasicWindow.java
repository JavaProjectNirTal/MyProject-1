package viewNwindow;

import java.util.Observable;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class BasicWindow extends Observable implements Runnable {
/*	Observable in order to be observed by the Presenter(Observer),
	Runnable in order to run it inside a Thread */
	
	protected Display display;
	protected Shell shell;
	
	public BasicWindow(int width, int height, String title) {		// 1. creates the Window
		display = Display.getCurrent();								//sets the Display of the Program
		if (display == null)
			display = new Display();
		shell = new Shell(display);									//the Window itself
		shell.setSize(width, height);
		shell.setText(title);
	}
	
	abstract void initWidgets();									// 2. implements the Widgets specifically
	@Override
	public void run() {
		
		initWidgets();												//loads the Widgets
		shell.open();												//opens the window
																	// 3. main event loop
		 while(!shell.isDisposed()){ // while window isn't closed

		    // 1. read events, put then in a queue.
		    // 2. dispatch the assigned listener
		    if(!display.readAndDispatch()){ // if the queue is empty
		       display.sleep(); // sleep until an event occurs 
		    }

		 } // shell is disposed

		 display.dispose(); // dispose OS components
	}
	

}
