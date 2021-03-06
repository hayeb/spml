package spml3;

import java.util.ArrayList;
import java.util.List;

public interface TableDataStructure {

	
	/**
	 * Adds a probability to the node. The query should consist of a list of
	 * pairs of parent nodes and their states.
	 * 
	 * @param query
	 *            The parents of the node. The list of booleans should follow
	 *            the same ordering as the list of parent in this node.
	 * @param probability
	 */
	public void addProbability(List<Pair> query, double probability);

	/**
	 * Returns a probability of the node being in equal to the state given the query pairs.
	 * @param query
	 * 			A list of Pairs representing the query variables.
	 * @param state
	 * @return
	 */
	public double getProbability(List<Pair> query);
	
	/**
	 * Removes all entries in the map which do not contain specified pair
	 * @param observed
	 */
	public void removeObserved(Pair pair);
	
	/**
	 * Removes a row from the table
	 */
	public void removeRow(List<Pair> row);
	
	/**
	 * Returns the variablenames in this data structure
	 * @return
	 */
	public ArrayList<String> getVariableNames();
	
	public Pair[][] getRows();
}
