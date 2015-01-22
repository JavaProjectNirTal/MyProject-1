package model;

import java.util.ArrayList;
import java.util.Observable;

import model.algorithm.Action;
import model.algorithm.Searcher;
import model.domains.SearchDomain;



//MyModel is a type of Observable and the Presenter is the Observer

public class MyModel extends Observable implements Model {
	
	private SearchDomain domain;
	private String domainDescription;				//for returning the description to the client
	private Searcher algorithm;
	private SearchAlgorithmsFactory SAF;
	private Solution solution;
	private SolutionManager solutionManager;		//the global SolutionManager
	private Thread t;							//for the solveDomain()
	

	public MyModel() {
		this.SAF = new SearchAlgorithmsFactory();
		this.solutionManager = SolutionManager.getInstance();
	}

	public String getDomainDescription() {
		return domainDescription;
	}
	
	public void setT(Thread t) {
		this.t = t;
	}
	
	public Thread getT() {
		return t;
	}
	public void selectDomain(SearchDomain domain) {
		this.domain = domain;
	}
	
/*
	@Override
	public void selectDomain(String args) {							
		String[] arr = args.split("-");
		String domainName = arr[0];									//Type Of Game
		String domainArgs = "";
		if(arr.length > 1)
			domainArgs = arr[1];								//Parameters, start, goal, etc..
		this.domain = SDF.createSearchDomain(domainName, domainArgs);
	}*/

	@Override
	public void selectAlgorithm(String algorithmName) {
		this.algorithm = SAF.createSearcherAlgorithm(algorithmName);
	}

	@Override
	public void solveDomain() {											//this method is inside a Thread
		domainDescription = domain.getDomainDescription();
		solution = solutionManager.getSolution(domainDescription);		//checks if we already saved the specific solution 
		if (solution == null) {											//the solution of this problem isn't in the file => new solution
			ArrayList<Action> actions = algorithm.search(domain);
			System.out.println("Server solvDomain() - search has finished");
			if(actions == null) {										//if the search was not successful
				actions = new ArrayList<Action>();
				actions.add(new Action("no solution")); }				//Assign to this specific problem the string "no solution"
			solution = new Solution();									//builds a new Solution Object
			solution.setActions(actions);								//sets the actions inside
			solution.setProblemDescription(domainDescription);			//sets the description for the solution
			solutionManager.addSolution(solution);						//puts the specific solution in the solution hashMap
		}
	}
	
	@Override
	public Solution getSolution() {
		return solution;
	}

	@Override
	public void doTask() {
		solveDomain();
	}
	
	@Override
	public void stopDomain() {		//stops the search in this current model
		if (algorithm != null)
			algorithm.stopSearch();
	}
}
