package presenter;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import config.LoadProperties;
import presenter.UserCommands.Command;
import viewNwindow.GameWindow;
import viewNwindow.MyConsoleView;
import viewNwindow.View;
import viewNwindow.WelcomeWindow;
import model.Model;
import model.MyModel;

public class Presenter implements Observer {
	//the Observables
	private Model model;
	private View view;					//a view window
	private WelcomeWindow w;			//welcomeWindow
	private ArrayList<Model> models;	//all running models
	private static Thread t;			//for the View
	private UserCommands commands;		//the Factory of Commands

	public Presenter(Model model, WelcomeWindow w) {
		this.model = model;
		this.w = w;
		this.commands = new UserCommands();
		this.models = new ArrayList<Model>();
	}
	
	public void setView(View view) {
		this.view = view;
		this.view.addObserver(this);
	}
	
	public  boolean indexInRange(int index) {
		if (index>0 && index<=this.models.size())		//the index is valid
			return true;
		return false;
	}
	
	public void findSolutionInAModel(int i) {						//checks if there's a solution for the specific problem at this time point
			if ( models.get(i-1).getSolution() == null )				//there's no solution yet
				((MyConsoleView) view).solutionFoundOrNot(false);
			else													//case: regular solution or "no solution"
				view.solutionFoundOrNot(true);
			}
	
	public void showSolutionInModel(int i) {						//presents the solution to the User
			if (models.get(i-1).getSolution() != null) {
				view.displaySolution(models.get(i-1).getSolution());
			}
			else {
				view.solutionFoundOrNot(false);
				
			}
	}
	
	public void exitSafetlyFromAllModels() {
		if (models != null) {
			for(int i=0; i<models.size(); i++)
				if(models.get(i).getT() != null && models.get(i).getT().isAlive())
					models.get(i).stopThread();
		}
	}
	
	public void fromModel(Object arg1) {
		if (arg1 != null) {											
			if ( ((String)arg1).startsWith("isThereASolution") ) {					//option 1
				String s = ((String)arg1).substring(((String)arg1).length()-1);
				int index = Integer.parseInt(s);
				findSolutionInAModel(index); }
			else if ( ((String)arg1).startsWith("presentSolution") ) {				//op2
				String s = ((String)arg1).substring(((String)arg1).length()-1);
				int index = Integer.parseInt(s);
				showSolutionInModel(index); }
			else if ( ((String)arg1).equals("safeExit") )							//op3
				exitSafetlyFromAllModels();
			else if ( ((String)arg1).startsWith("afterMoves") ) {					//op4
				String[] a = ((String)arg1).split(" ");
				sendDescription(a[1]);
			}
			else
				 sendDescription(null);									//op5- getDescription
		}
	}
	
	public void fromWelcomeWindow(Object arg1) {
		if(w.getGameWindow() != null && view == null)
			setView(w.getGameWindow());
		if (arg1 != null) {
			String s = ((String)arg1).toString();
			LoadProperties.setFILE_NAME(s);
		}
	}
	
	public void fromView(Object arg1) {
		String action = view.getUserAction();
		String[] arr = action.split(": ");
		
		String commandName = arr[0];
		String args = "";							//extra Parameters
		if (arr.length > 1)
				args = arr[1];
		Command command = commands.selectCommand(commandName);
		Model m = command.doCommand(this.model, args);
		//check if we got a new model from the command
		if (m != this.model) {
			this.model = m;
			models.add(m);
			this.model.addObserver(this); 		//the presenter itself is the Observer
		}
		if(models != null)
			System.out.println("number of models in models arr is: "+models.size());
	}
	
	@Override
	public void update(Observable observable, Object arg1) {
		if (observable instanceof Model) {
			fromModel(arg1);
		}
		else if (observable instanceof WelcomeWindow) {
			fromWelcomeWindow(arg1);
		}
		else if (observable instanceof View) {
			fromView(arg1);
		}
	}

	private void sendDescription(String str) {
		if (str != null){
			((GameWindow)view).setDescription(str);
		}
		else {
			String s = ((MyModel)model).getDomainDescription();
			((GameWindow)view).setDescription(s);
		}
	}

	//Main()
	public static void main(String[] args) {
		MyModel model = new MyModel();
		WelcomeWindow w = new WelcomeWindow(700, 700, "Welcome Window");
		Presenter presenter = new Presenter(model, w);
		model.addObserver(presenter);
		w.addObserver(presenter);
		w.run();
		System.out.println("end main");
	}
}
