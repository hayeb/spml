package spml3;

import java.util.HashMap;

public class BeliefNode {

	private String name;
	private HashMap<String, Double> probabilities;
	
	public BeliefNode(String name) {
		this.name = name;	
		probabilities = new HashMap<String, Double>();
	}
	
	public void addProbability(String query, double probability) {
		probabilities.put(query, probability);
	}
	
	public double getProbability(String query) {
		return probabilities.get(query);
	}
}
