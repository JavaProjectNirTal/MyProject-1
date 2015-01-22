package model;

import java.io.Serializable;

import model.domains.SearchDomain;

public class Problem implements Serializable {
//represents a Problem that will be sent (as an Object) to the Server, contains all the info.

	private static final long serialVersionUID = -6725296077670399438L;
	private SearchDomain domain;
	private String algorithmName;
	
	public SearchDomain getDomain() {
		return domain;
	}
	public void setDomain(SearchDomain domain) {
		this.domain = domain;
	}
	public String getAlgorithmName() {
		return algorithmName;
	}
	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}
	
	public String getDescription() {
		return domain.getDomainDescription();
	}
}
