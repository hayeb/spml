package spml3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BeliefNode {

	private final String name;
	//private Map<ArrayList<Boolean>, Double> probabilities;
	private ArrayList<String> parents;
	
	private DataStructuur probabilities;

	/**
	 * Creates a BeliefNode with given name and a HashMap as data structure to
	 * store the probabilities.
	 * 
	 * @param name
	 */
	public BeliefNode(String name) {
		this.name = name;
		probabilities = null; // VUL HIER DE IMPLEMENTATIE VAN HET INTERFACE IN
		parents = new ArrayList<String>();
	}

	/**
	 * Adds a probability to the node. The query should consists of the parents
	 * of this node.
	 * 
	 * @param query
	 *            The parents of the node. The list of booleans should follow
	 *            the same ordering as the list of parent in this node.
	 * @param probability
	 */
	public void addProbability(ArrayList<Boolean> query, double probability) {
		probabilities.put(query, probability);
	}

	public void addParent(String p) {
		parents.add(p);
	}

	/**
	 * Retrieves a probability from this node.
	 * 
	 * @param query
	 *            The parents of the node. The list of booleans should follow
	 *            the same ordering as the list of parent in this node.
	 *            Unobserverd variables should be represented by a null entry in
	 *            the list.
	 * @return
	 */
	public double getProbability(ArrayList<Boolean> query) {
		return probabilities.get(query);
	}

	/**
	 * Returns the name of this node
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return The number of parents for this node
	 */
	public int numberOfParents() {
		return parents.size();
	}

	@Override
	public String toString() {
		return "Name: " + this.name + "\nParents: " + this.parents + "\nProbabilities" + this.probabilities;
	}
	
	public ArrayList<String> getParents() {
		return this.parents;
	}
}
