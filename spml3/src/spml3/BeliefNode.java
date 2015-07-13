package spml3;

import java.util.HashMap;
import java.util.Map;

public class BeliefNode {

	private final String name;
	
	private Map<String, Double> probabilities;

	/**
	 * Creates a BeliefNode with given name and a HashMap as data structure to
	 * store the probabilities.
	 * 
	 * @param name
	 */
	public BeliefNode(String name) {
		this.name = name;
		probabilities = new HashMap<String, Double>();
	}

	/**
	 * Adds a probability to the node. The query should consists of the parents of this node.
	 * 
	 * @param query
	 * The parents of the node. Should be of the following format: <code><br>
	 * <ul>"parentOne TRUE, parentTwo FALSE, parentThree TRUE"<ul></code>
	 * @param probability
	 */
	public void addProbability(String query, double probability) {
		probabilities.put(query, probability);
	}

	/**
	 * Retrieves a probability from this node.
	 * @param query 
	 * The parents of the node. Should be of the following format: <code><br>
	 * <ul>"parentOne TRUE, parentTwo FALSE, parentThree TRUE"<ul></code>
	 * @return
	 */
	public double getProbability(String query) {
		return probabilities.get(query);
	}

	/**
	 * Returns the name of this node
	 * @return
	 */
	public String getName() {
		return name;
	}
}
