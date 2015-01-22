package viewNwindow;

import java.util.Observable;
import java.util.Scanner;

import model.Solution;
import model.algorithm.Action;

//MyConsoleView is a type of Observable and the Presentor is the Observer

public class MyConsoleView extends Observable implements View	 {

	private String action;

	@Override
	public void start() {			//this method is inside a Thread
		int num=0;
		System.out.println("Start Program: Nir & Tal Project");
		System.out.println("Instructions: ");
		System.out.println("Bad Input regarding Parameters Start/Goal was not considered !");
		/*
				for example if the User chooses Maze Game Start State complexed of letters and not
				numbers the Program would fall
		*/
		action = "";
		boolean flag=true;
		while (flag) {
			Scanner scanner = new Scanner(System.in);
			System.out.println();
			num++;
			System.out.print("Please Enter command #"+num+": ");
			action = scanner.nextLine();
			String[] arg = action.split(": ");	//checks only for this local switchCase
			String checkAction = arg[0];
			switch (checkAction) {
			case "selectDomain":		//selectDomain: Maze-(0,0) (3,3) 4 1 	/  selectDomain: Puzzle
				this.setChanged();
				this.notifyObservers();
				break;
			case "selectAlgorithm":		//selectAlgorithm: BFS 					/	selectAlgorithm: Astar
				this.setChanged();
				this.notifyObservers();
				break;
			case "solveDomain":			//solveDomain
				this.setChanged();
				this.notifyObservers();
				break;
			case "isThereASolution":	//isThereASolution: index(int)	for example isThereASolution: 1 ==> meaning is there a solution for domain #1
				this.setChanged();
				this.notifyObservers();
				break;
			case "presentSolution":		// presentSolution: index(int)
				this.setChanged();
				this.notifyObservers();
				break;
			case "exit":				//exit
				this.setChanged();
				this.notifyObservers();
				System.out.println("\nThanks for Playing");
				flag=false;
				break;
			default:
				System.out.println("unvalid Input, please try again");
				num--;
				continue;
			}
			scanner.close();
		}
	}

	public void solutionFoundOrNot(boolean answer) {
		if (answer == false)
			System.out.println("NO! Still searching for solution");
		else
			System.out.println("YES! we have reached a solution");
	}

	@Override
	public void displaySolution(Solution solution) {
		if (solution.getActions().get(0).getDescription().equals("no solution"))
			System.out.println("SORRY, A valid solution can not be found for this specific problem. please Run again!");
		else {
			System.out.println("Path from Start to Goal:");
			for(Action a : solution.getActions())
				System.out.println(a);
			System.out.println("End of Game !");
		}
	}

	@Override
	public String getUserAction() {
		return action;
	}


	@Override
	public void doTask() {
		start();
	}
	
	public void indexNotInRange(boolean b) {
		if (b == false)
			System.out.println("index out of range, please enter a new command with a valid index");
	}
}
