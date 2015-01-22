package presenter;

import java.util.HashMap;

import tasks.TaskRunnable;
import model.Model;
import model.MyModel;

public class UserCommands {

	private HashMap<String, Command> commands = new HashMap<String, Command>();
	
	public UserCommands() {
		commands.put("selectDomain", new SelectDomainCommand());
		commands.put("selectAlgorithm", new SelectAlgorithmCommand());
		commands.put("solveDomain", new SolveDomainCommand());
		commands.put("presentSolution", new PresentSolutionCommand());
		commands.put("exit", new ExitCommand());
		commands.put("getDescription", new getDescriptionCommand());
		commands.put("selectMoves",new SelectMovesCommand());
		commands.put("CheckConnection",new CheckConnectionCommand());
	}
	
	public Command selectCommand(String commandName) {
		Command command = commands.get(commandName);
		return command;
	}

	public interface Command {
		Model doCommand(Model model, String args);
	}
	
	private class SelectDomainCommand implements Command {
		@Override
		public Model doCommand(Model model, String args) {
			Model m = new MyModel();
			m.selectDomain(args);
			return m;
		}
	}
	
	private class SelectAlgorithmCommand implements Command {
		@Override
		public Model doCommand(Model model, String args) {
			model.selectAlgorithm(args);
			return model;
		}
	}

	private class SolveDomainCommand implements Command {
		@Override
		public Model doCommand(Model model, String args) {
			model.setT(new Thread(new TaskRunnable(model)));
			model.getT().start();
			return model;
		}
	}

	private class PresentSolutionCommand implements Command {
		@Override
		public Model doCommand(Model model, String args) {
			model.modelToObserver("presentSolution "+args);
			return model;
		}
	}
	
	private class ExitCommand implements Command {
		@Override
		public Model doCommand(Model model, String args) {
			model.modelToObserver("safeExit");
			return model;
		}
	}
	private class getDescriptionCommand implements Command {

		@Override
		public Model doCommand(Model model, String args) {
			model.modelToObserver("getDescription");
			return model;
		}
	}
	private class SelectMovesCommand implements Command{
		@Override
		public Model doCommand(Model model, String args) {
			String description = ((MyModel)model).selectMoves(args);
			model.modelToObserver("afterMoves "+description);
			return model;
		}	
	}
	
	private class CheckConnectionCommand implements Command{
		@Override
		public Model doCommand(Model model, String args) {
			model.modelToObserver("Server");
		return model;
		}	
	}
}
