package spml3;

import java.util.List;

public interface ProbabilityCalculationStrategy {

	/**
	 * Returns the probability of nodeName being true, given the parent nodes.
	 * 
	 * @param nodeName
	 *            The name of the node.
	 * @param parents
	 *            The parents of the node. The list of booleans should follow
	 *            the same ordering as the list of parent in this node.
	 *            Unobserverd variables should be represented by a null entry in
	 *            the list.
	 * @return
	 */
	public double calculateProbability(String nodeName, List<Boolean> parents);
}
