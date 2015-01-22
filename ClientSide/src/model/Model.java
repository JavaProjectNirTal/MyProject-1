package model;



import java.util.Observer;
import tasks.Task;

/**this Interface represents the Model's Layer of the Project's MVP architecture
 * describes the functionality of every Model type within the Project
 * @author Nir Meiri, Tal Kramer
 *@version 1.0
 */

public interface Model extends Task {
	/**
	 * Creates the domain of this model's problem by using the SearchDomainFactory and sets it's specific description  
	 * @param args - This domain's name and arguments
	 */
	void selectDomain(String args);
	/**
	 * Sets the algorithm name of this model's inside the model's problem
	 * @param algorithmName - The algorithm's name of this model's problem
	 */
	void selectAlgorithm(String algorithmName);
	/**
	 * Creates a new Client Class that connects to a specified Server and asks for a Solution to this model's problem
	 */
	void solveDomain();
	/**
	 * 
	 * @return Solution of this model's problem
	 */
	Solution getSolution();
	/**
	 * Sets the argument to be an observer on this model (this model is the observable)
	 * @param o - this model's Observer
	 */
	void addObserver(Observer o);	
	/**
	 * Allows to pass String arguments to the observer
	 * @param args The String to pass on to the model's observer
	 */
	void modelToObserver(String args);
	/**
	 * Stops the Client's connection inside the Model
	 */
	void stopThread();
	/**
	 * Get's this model's Thread
	 * @return Thread of this Model
	 */
	Thread getT();
	/**
	 * Set's this model's Thread
	 * @param t - Thread to run
	 */
	void setT(Thread t);
}
