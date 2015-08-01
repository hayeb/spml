package spml3;

import java.util.ArrayList;

public class BeliefNode implements DataStructuur {

	/**
	 * Name of the node
	 */
	private final String name;

	/**
	 * The list of parents of the node
	 */
	private ArrayList<String> parents;

	/**
	 * The data structure containing a probability table.
	 */
	private ProbabilityMap probabilities;
	private DataStructuur probStructure;

	/**
	 * Creates a BeliefNode with given name and a ProbabilityMap as data
	 * structure to store the probabilities.
	 * 
	 * @param name
	 *            The name of the node. Usually, this is the name of the
	 *            variable the node represents.
	 */
	public BeliefNode(String name) {
		this.name = name;
		probabilities = new ProbabilityMap(name);
		probStructure = probabilities;
		parents = new ArrayList<String>();
	}

	@Override
	public void addProbability(Pair[] query, double probability) {
		probStructure.addProbability(query, probability);
	}

	@Override
	public double getProbability(Pair[] query) {
		return probStructure.getProbability(query);
	}

	/**
	 * Adds a parent to the list of parents. Converts to lower case and trims
	 * the parent name.
	 * 
	 * @param p
	 */
	public void addParent(String p) {
		parents.add(p.toLowerCase().trim());
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
		return "Name: " + this.name + "\nParents: " + this.parents + "\nProbabilities" + this.probStructure;
	}

	/**
	 * Returns a list containing all parents.
	 * 
	 * @return
	 */
	public ArrayList<String> getParents() {
		return this.parents;
	}

	@Override
	public void removeObserved(Pair pair) {
		probStructure.removeObserved(pair);
	}
	
	public ProbabilityMap cloneProbabilitiesTable() {
		return probabilities.cloneMap();
	}

	@Override
	public ArrayList<String> getVariableNames() {
		return probStructure.getVariableNames();
	}

	@Override
	public void removeRow(Pair[] row) {
		// TODO Auto-generated method stub
		
	}
	
	
}
