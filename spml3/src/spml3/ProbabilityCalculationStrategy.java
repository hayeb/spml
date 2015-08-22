package spml3;

public interface ProbabilityCalculationStrategy {

	/**
	 * Returns the probability of nodeName being true, given the observed nodes.
	 * 
	 * @param nodeName
	 *            The name of the node. We only support one query variable.
	 * @param observedNodes
	 *            Observed variables in the network. The list should consist of pairs of the observed variable name and the state of the observed variable
	 * @return
	 */
	public double calculateProbability(Pair query, Pair[] observedNodes);
}
