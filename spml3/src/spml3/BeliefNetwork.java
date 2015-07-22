package spml3;

import java.util.ArrayList;
import java.util.List;

public class BeliefNetwork {

	private ArrayList<BeliefNode> nodes;
	private ProbabilityCalculationStrategy calcStrategy;

	/**
	 * Initializes the belief network with a default Variable Elimination
	 * Strategy for calculation probabilities.
	 */
	public BeliefNetwork() {
		calcStrategy = new VariableEliminationStrategy(this);
		nodes = new ArrayList<BeliefNode>();
	}

	/**
	 * Adds a node to the network
	 * 
	 * @param node
	 */
	public void addNode(BeliefNode node) {
		nodes.add(node);
	}

	/**
	 * Calculates a probability according to the current strategy.
	 * 
	 * @param nodeName
	 *            The name of the node of which you want to calculate the
	 *            probability.
	 * @param parents
	 *            The parents of the node. The list of booleans should follow
	 *            the same ordering as the list of parent in this node.
	 *            Unobserverd variables should be represented by a null entry in
	 *            the list.
	 * @return the probability of nodeName being true given the parent in the
	 *         list.
	 */
	public double calcProbability(String nodeName, List<Boolean> query) {
		return calcStrategy.calculateProbability(nodeName, query);
	}


	public ArrayList<BeliefNode> getNodes() {
		return nodes;
	}
}
