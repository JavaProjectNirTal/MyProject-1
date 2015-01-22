package model;

import java.io.Serializable;
import java.util.ArrayList;

import model.algorithm.Action;




public class Solution implements Serializable{
	
	private static final long serialVersionUID = -1273366738930484533L;
	private String problemDescription;
	private ArrayList<Action> actions;
	
	public Solution(){}
	
	public ArrayList<Action> getActions() { return actions; }
	public void setActions(ArrayList<Action> actions) { this.actions = actions; }
	
	public String getProblemDescription() {
		return problemDescription;
	}
	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}
}
