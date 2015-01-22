package viewNwindow;

import java.util.Observer;

import tasks.Task;
import model.Solution;

public interface View extends Task {
	void start();
	void displaySolution(Solution solution);
	void addObserver(Observer o);	
	String getUserAction();
	void solutionFoundOrNot(boolean answer);
}
