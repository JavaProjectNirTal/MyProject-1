package model.domains;

import model.algorithm.State;

public class MazeGameState extends State {
	
	//tells you if the State is a Wall or not
	private static final long serialVersionUID = -87169417029040427L;
	private boolean possibleToPass;				//True => possibleToPass, False =>notPossibleToPass
	
	public MazeGameState() {}
	
	public MazeGameState(String state) {
		super(state);
		setPossibleToPass(true);
	}

	public boolean getPossibleToPass() {
		return possibleToPass;
	}

	public void setPossibleToPass(boolean possibleToPass) {
		this.possibleToPass = possibleToPass;
	}
}
