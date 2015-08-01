package spml3;

import java.util.ArrayList;

public interface DataStructuur {

	
	/**
	 * Adds a probability to the node. The query should consist of a list of
	 * pairs of parent nodes and their states.
	 * 
	 * @param query
	 *            The parents of the node. The list of booleans should follow
	 *            the same ordering as the list of parent in this node.
	 * @param probability
	 */
	public void addProbability(Pair[] query, double probability);

	/**
	 * Returns a probability of the node being in equal to the state given the query pairs.
	 * @param query
	 * 			A list of Pairs representing the query variables.
	 * @param state
	 * @return
	 */
	public double getProbability(Pair[] query);
	
	/**
	 * Removes all entries in the map which do not contain specified pair
	 * @param observed
	 */
	public void removeObserved(Pair pair);
	
	/**
	 * Removes a row from the table
	 */
	public void removeRow(Pair[] row);
	
	public ArrayList<String> getVariableNames();
}
