package spml3;

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
	 * 
	 * 
	 * @return the probability of the corresponding row; e.g. The probability of
	 *         factor A,B,C where !a, !b, c holds.
	 * 
	 *         But when I call it with a different Factor I might want a row
	 *         with corresponding variable states.
	 * 
	 *         Thus perhaps I want a list instead.
	 */
	public double getProbability(Pair[] query, Boolean state);
}
