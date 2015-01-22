package model;

import java.util.Observer;

import model.domains.SearchDomain;
import tasks.Task;

public interface Model extends Task {
	void selectDomain(SearchDomain domain);
	void selectAlgorithm(String algorithmName);
	void solveDomain();
	Solution getSolution();
	void addObserver(Observer o);	
	void stopDomain();
	
}
