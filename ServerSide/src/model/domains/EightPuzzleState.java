package model.domains;

import model.algorithm.State;


public class EightPuzzleState extends State {

	/*An EightPuzzleState will be a 3X3=9 chars string that will be represented like a matrix of chars that would be the current puzzle state
	 * consist out of 8 numbers and 1 letter N == NULL
	 * Example: string "12345678N"  would represent puzzleState  | 1 2 3 |  and so on...
	 * 															 | 4 5 6 |
	 * 															 | 7 8 n |
	 */
	
	private static final long serialVersionUID = -6982439666388529045L;
	
	public EightPuzzleState() {}
	
	public EightPuzzleState(String state) {
		super(state);
	}
}
