package model;

import java.util.Observable;
import model.domains.EightPuzzleDomain;
import model.domains.MazeGameDomain;
import model.domains.SearchDomainFactory;
import network.Client;

/**  MyModel is a type of Observable and the Presenter is the Observer
 *
 * @see {@link Model}
 * 
 *
 */

public class MyModel extends Observable implements Model {

	/**
	 * @param problem 
	 */
	
	
	private Problem problem;
	private String domainDescription;
	private Solution solution;
	private SearchDomainFactory SDF;
	private Client client;
	private Thread t;
	
	/**
	 * sets a new Problem and a new SearchDomainFactory.
	 */
	public MyModel() {
		problem = new Problem();
		SDF = new SearchDomainFactory();
	}
	
	@Override
	public void selectDomain(String args) {
		String[] arr = args.split("-");
		String domainName = arr[0];
		String domainArgs = "";
		if (arr.length > 1)
			domainArgs = arr[1];
		problem.setDomain(SDF.createSearchDomain(domainName, domainArgs));
		this.domainDescription = problem.getDomain().getDomainDescription();
	}

	@Override
	public void selectAlgorithm(String algorithmName) {
		problem.setAlgorithmName(algorithmName);
	}

	@Override
	public void solveDomain() {
		client = new Client();
		solution = client.getSolution(problem);
	}
	
	@Override
	public void modelToObserver(String args) {
		this.setChanged();
		this.notifyObservers(args);
	}

	@Override
	public Solution getSolution() {
		return solution;
	}

	@Override
	public Thread getT() {
		return this.t;
	}

	@Override
	public void setT(Thread t) {
		this.t = t;
	}

	@Override
	public void doTask() {
		solveDomain(); // inside a Thread
	}

	@Override
	public void stopThread() {
		client.stopClient();
	}
	
	/**
	 * Get's the GameCharacter's move and current state coordinates and sets this new move coordinates
	 * @param args - A string consist of the GameCharacter's move and current state coordinates
	 * @return New state coordinates of the GameCharacter
	 */
	public String selectMoves(String args) { // the key moves of the player in which move + state:
		String[] a = args.split(" ");
		String move = a[0];
		String description = a[1];
		String tmpDescription = description;
		
		//which Domain:
		String domain = problem.getDescription();
		if (domain.startsWith("Eight")) {
			switch (move) {
			case "up": tmpDescription = ((EightPuzzleDomain)problem.getDomain()).moveUp(tmpDescription);
				if (!(tmpDescription.equals("FAILED")))	//if successed
					description = tmpDescription;							//set the new switched string temp to s				
				break;
			case "down": tmpDescription = ((EightPuzzleDomain)problem.getDomain()).moveDown(tmpDescription);
				if (!(tmpDescription.equals("FAILED")))	//if successed
					description = tmpDescription;							//set the new switched string temp to s				
				break;
			case "left": tmpDescription = ((EightPuzzleDomain)problem.getDomain()).moveLeft(tmpDescription);
				if (!(tmpDescription.equals("FAILED")))	//if successed
					description = tmpDescription;							//set the new switched string temp to s				
				break;
			case "right": tmpDescription = ((EightPuzzleDomain)problem.getDomain()).moveRight(tmpDescription);
				if (!(tmpDescription.equals("FAILED")))	//if successed
					description = tmpDescription;							//set the new switched string temp to s				
				break;
			}
		}
		else if (domain.startsWith("Maze")) {
			System.out.println("My Model : Maze description "+description);
			switch (move) {
			case "up": tmpDescription = ((MazeGameDomain)problem.getDomain()).moveUp(tmpDescription);
				if (!(tmpDescription.equals("FAILED")))	//if successed
					description = tmpDescription;							//set the new switched string temp to s				
				break;
			case "down": tmpDescription = ((MazeGameDomain)problem.getDomain()).moveDown(tmpDescription);
				if (!(tmpDescription.equals("FAILED")))	//if successed
					description = tmpDescription;							//set the new switched string temp to s				
				break;
			case "left": tmpDescription = ((MazeGameDomain)problem.getDomain()).moveLeft(tmpDescription);
				if (!(tmpDescription.equals("FAILED")))	//if successed
					description = tmpDescription;							//set the new switched string temp to s				
				break;
			case "right": tmpDescription = ((MazeGameDomain)problem.getDomain()).moveRight(tmpDescription);
				if (!(tmpDescription.equals("FAILED")))	//if successed
					description = tmpDescription;							//set the new switched string temp to s				
				break;
			}
			
		}
		return description;	
	}

	/**
	 * Get's the domain description of this model
	 * @return this model's domain description
	 */
	public String getDomainDescription() {
		return domainDescription;
	}
	
	public boolean checkConnection() {
		Client c = new Client();					//opens temp Client
		boolean b = c.getClientIsConnected();		//checks if the server is alive
		return b;
	}

}
