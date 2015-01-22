package model;

import java.io.Serializable;

import model.domains.SearchDomain;

/**
 * Represents a Problem that will be sent from a Client to a Server
 * @author Nir Meiri, Tal Kramer
 *
 */


public class Problem implements Serializable {

	private static final long serialVersionUID = -6725296077670399438L;
	private SearchDomain domain;
	private String algorithmName;
	
	/**
	 * @return Domain of this Problem
	 */
	public SearchDomain getDomain() {
		return domain;
	}
	/**
	 * Sets the domain of this Problem
	 * @param domain - New domain to be set inside this Problem
	 */
	public void setDomain(SearchDomain domain) {
		this.domain = domain;
	}
	/**
	 * 
	 * @return Algorithm's name of this Problem
	 */
	public String getAlgorithmName() {
		return algorithmName;
	}
	/**
	 * 
	 * @param algorithmName - New Algorithm name to be set inside this Problem
	 */
	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}
	/**
	 * 
	 * @return The domain description of this Problem
	 */
	public String getDescription() {
		return domain.getDomainDescription();
	}
}
