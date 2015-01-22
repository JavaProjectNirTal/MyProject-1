package model.domains;

import java.util.HashMap;
import java.util.Random;

import model.algorithm.Action;
import model.algorithm.State;

public class MazeGameDomain implements SearchDomain {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3896009278010278257L;
	private int	size;
	private MazeGameState start,goal;
	private MazeGameState[][] maze;
	private int walls;
	private StringBuilder domainDescription;
	
	public MazeGameDomain() {}
	
	public MazeGameDomain(String args) {
		String[] arr = args.split(" ");
		String start = arr[0];
		String goal = arr[1];
		String size = arr[2];
		String numOfWalls = arr[3];
		
		this.start=new MazeGameState(start);
		this.goal=new MazeGameState(goal);
		setSizeOfMaze(Integer.parseInt(size));
		setWalls(Integer.parseInt(numOfWalls));
		this.maze=new MazeGameState[this.size][this.size];
		System.out.println("Setting Maze Game: Size of "+this.size+"X"+this.size+" with "+this.walls+" Walls");
		System.out.println("Start Point is: "+start);
		System.out.println("Goal Point is:  "+goal);
		this.domainDescription = new StringBuilder();
		buildMaze(this.size);								
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public MazeGameState getStart() {
		return start;
	}

	public void setStart(MazeGameState start) {
		this.start = start;
	}

	public MazeGameState getGoal() {
		return goal;
	}

	public void setGoal(MazeGameState goal) {
		this.goal = goal;
	}

	public MazeGameState[][] getMaze() {
		return maze;
	}

	public void setMaze(MazeGameState[][] maze) {
		this.maze = maze;
	}

	public int getWalls() {
		return walls;
	}

	public void setDomainDescription(StringBuilder domainDescription) {
		this.domainDescription = domainDescription;
	}
	
	
	
		
	public void setSizeOfMaze(int size) {
		//in case of bad input, i'm setting the Default Size of the Maze to be the Max Value entered in the states: Start / Goal
			int op1=getRowNumber(start.getState());			//op=option
			int op2=getColumnNumber(start.getState());
			int op3=getRowNumber(goal.getState());
			int op4=getColumnNumber(goal.getState());
			int max = Math.max(Math.max(op1, op2), Math.max(op3, op4));
		if (size < max) {			//example: (0,0) (5,5) 4, 4		//therefore we will do size=max=5;
			System.out.println("Bad Input, setting Size to Default");
			this.size = max;
		}
		else
			this.size = size;
	}
	
	public void setWalls(int walls) {
		//in case of bad input, i'm setting the Default Number of Walls to be the same Value entered in the Maze Size
		if(walls < 0) {
			System.out.println("Bad Input, setting Number of Walls to Default");
			this.walls = this.size;
		}
			else
				this.walls = walls;
	}
	
	@Override
	public State getStartState() {
		return start;
	}

	@Override
	public State getGoalState() {
		return goal;
	}

	public void buildMaze(int size) {
		for (int i=0; i<size; i++)
			for (int j=0; j<size; j++) {
				maze[i][j]= new MazeGameState("("+i+","+j+")");
			}
		CreateWalls();
	}
	
	public void CreateWalls() {
		String a = String.valueOf(size-1);				//exmp: if size = 11X11 => goal (10,10) => (9,10) , (10,9)
		String b = String.valueOf(size-2);
		String sizeStr1 = new String("("+b+","+a+")");
		String sizeStr2 = new String("("+a+","+b+")");
		for(int i=0; i<walls; i++) {
			Random r = new Random();
			int row = r.nextInt(size);			//int between 0 - size-1 include
			int col = r.nextInt(size);
			if( maze[row][col].getState().equals("(0,1)") ||				//Surrounding the Start
				maze[row][col].getState().equals("(1,0)") ||				
				maze[row][col].getState().equals(sizeStr1) ||				//Surrounding the Goal
				maze[row][col].getState().equals(sizeStr2) ||
				maze[row][col].getState().equals(start.getState()) ||		//start
				maze[row][col].getState().equals(goal.getState())  ||		//goal
				maze[row][col].getPossibleToPass() == false         ) {		//already a wall
				i--;
				continue;
			}
			maze[row][col].setPossibleToPass(false);
			System.out.println("a wall has been created at: " + maze[row][col].getState());
			domainDescription.append(" "+maze[row][col].getState());								//for DomainDescription()
		}
		System.out.println();
	}


	//Moves:
	public String moveUp(String s) {
		int i=getRowNumber(s) - 1;
		int j=getColumnNumber(s);
		if (i<0 || !this.maze[i][j].getPossibleToPass())
			return "FAILED";
		else
			return new String("("+i+","+j+")");
	}
	public String moveDown(String s) {
		int i=getRowNumber(s) + 1;
		int j=getColumnNumber(s);
		if (i==size || !maze[i][j].getPossibleToPass())
			return "FAILED";
		return new String("("+i+","+j+")");
	}
	public String moveRight(String s) {
		int i=getRowNumber(s);
		int j=getColumnNumber(s) + 1;
		if (j==size || !this.maze[i][j].getPossibleToPass())
			return "FAILED";
		else
			return new String("("+i+","+j+")");
	}
	public String moveLeft(String s) {
		int i=getRowNumber(s);
		int j=getColumnNumber(s) - 1;
		if (j<0 || !this.maze[i][j].getPossibleToPass())
			return "FAILED";
		else
			return new String("("+i+","+j+")");
	}
	
	//extracts the Row/Column number of a String within a State
	public int getRowNumber(String s) {
		int sum=0;
		for (int i=1; i<s.length(); i++) {
			if (s.charAt(i) == ',')
				break;
			sum = sum*10 + Character.getNumericValue(s.charAt(i));
		}
		return sum;
	}
	public int getColumnNumber(String s) {
		int sum=0;
		int lastChar = s.length()-1;
		int firstChar = s.indexOf(',');
		for (int i=firstChar+1; i<lastChar; i++) {
			sum = sum*10 + Character.getNumericValue(s.charAt(i));
		}
		return sum;
	}
	
	@Override
	public HashMap<Action, State> getAllPossibleMoves(State current) {
		HashMap<Action, State> moves=new HashMap<>();
		String state=current.getState();
		Action a;
		MazeGameState newState;
		
		String up= moveUp(state);
		if (!up.equals("FAILED")) {
			a= new Action(state+" moves UP");
			a.setCost(1);
			newState = new MazeGameState(up);
			moves.put(a, newState);
		}
				
		String down= moveDown(state);
		if (!down.equals("FAILED")) {
			a= new Action(state+" moves DOWN");
			a.setCost(1);
			newState = new MazeGameState(down);
			moves.put(a, newState);
		}
			
		String right= moveRight(state);
		if (!right.equals("FAILED")) {
			a= new Action(state+" moves RIGHT");
			a.setCost(1);
			newState = new MazeGameState(right);
			moves.put(a, newState);
		}
			
		String left= moveLeft(state);
		if (!left.equals("FAILED")) {
			a= new Action(state+" moves LEFT");
			a.setCost(1);							//every move costs the same
			newState = new MazeGameState(left);
			moves.put(a, newState);
		}
		return moves;
	}

	//G score
	public double getG(State s) {
		if (s.getCameFrom() == null)
		return 0;
		else
			return (s.getCameFrom().getgScore()+ s.getCameWithAction().getCost());
	}
	
	//H function
	public double getHiuristicValueOfState(State state, State goal) {
		int x1 = getRowNumber(state.getState());
		int x2 = getRowNumber(goal.getState());
		int y1 = getColumnNumber(state.getState());
		int y2 = getColumnNumber(goal.getState());
		return (Math.sqrt(Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2)));
	}
	
	//optional method: prints the Maze Game 
	public void printMaze() {
		System.out.println("The Current Maze Board");
		System.out.println("	S = Start, G = Goal, W = Wall");
		for(int i=0; i<this.size; i++)
			for (int j=0; j<this.size; j++) {
				if (maze[i][j].getState().equals(start.getState()))
					System.out.print(" "+"S-"+maze[i][j]);
				else if (maze[i][j].getState().equals(goal.getState()))
					System.out.print(" "+"G-"+maze[i][j]);
				else if (maze[i][j].getPossibleToPass() == false)
					System.out.print(" "+"W-"+maze[i][j]);
				else
					System.out.print("   "+maze[i][j]);
				if (j == (this.size)-1)
					System.out.println();
			}
	
	}

	@Override
	public String getDomainDescription() {		//every Maze has a different description
		String stringGame = new String ("MazeGame:Start"+start.getState()+" Goal"+goal.getState()+" Size "+this.size+"X"+this.size+" with "+this.walls+" Walls:");
		domainDescription.insert(0, stringGame);
		return (domainDescription.toString());
	}
}
