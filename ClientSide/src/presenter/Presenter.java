package presenter;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.widgets.Display;

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
	private Model model;				//Model
	private View view;					//a Gui Window = View
	private WelcomeWindow w;			//welcomeWindow
	private UserCommands commands;		//the Factory of Commands

	public Presenter(Model model, WelcomeWindow w) {
		this.model = model;
		this.w = w;
		this.commands = new UserCommands();
	}
	
	public void setView(View view) {
		this.view = view;
		this.view.addObserver(this);
	}

	public void showSolutionInModel(int i) {						//presents the solution to the User
			if (model.getSolution() != null)
				view.displaySolution(model.getSolution());
			else
				view.solutionFoundOrNot(false);
	}
	
	public void exitSafetlyFromAllModels() {
		if (model.getT() != null)
			model.stopThread();
	}
	
	public void fromModel(Object arg1) {
		if (arg1 != null) {											
			if ( ((String)arg1).startsWith("presentSolution") ) {					//op1
				String s = ((String)arg1).substring(((String)arg1).length()-1);
				int index = Integer.parseInt(s);
				showSolutionInModel(index); }
			else if ( ((String)arg1).equals("safeExit") )							//op2
				exitSafetlyFromAllModels();
			else if ( ((String)arg1).startsWith("Server") ) {
				boolean b = ((MyModel)model).checkConnection();
				((GameWindow)view).setPossibleToConnect(b);
			}
			else if ( ((String)arg1).startsWith("afterMoves") ) {					//op3
				String[] a = ((String)arg1).split(" ");
				sendDescription(a[1]);
			}
			else
				 sendDescription(null);									//op4- getDescription
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
			this.model.addObserver(this); 		//the presenter itself is the Observer
		}
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
