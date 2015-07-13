package spml3;

import java.util.ArrayList;

public class BeliefNetwork {

	private ArrayList<BeliefNode> nodes;
	private ProbabilityCalculationStrategy calcStrategy;

	/**
	 * Initializes the belief network with a default Variable Elimination
	 * Strategy for calculation probabilities.
	 */
	public BeliefNetwork() {
		calcStrategy = new VariableEliminationStrategy();
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
	 * The parents of the node. Should be of the following format: <code><br>
	 * <ul>"parentOne TRUE, parentTwo FALSE, parentThree TRUE"<ul></code>
	 * @return the probability of nodeName being true given the parent in the
	 *         list.
	 */
	public double calcProbability(String nodeName, String parents) {
		return calcStrategy.calculateProbability(nodeName, parents);
	}
}
