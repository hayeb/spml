package spml3;

import java.util.ArrayList;

public class BeliefNetwork implements ProbabilityCalculationStrategy {

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
	 * Returns the node in the network according to the node name. REturns null
	 * if the node can't be found.
	 * 
	 * @param name
	 * @return
	 */
	public BeliefNode getNode(String name) {
		for (BeliefNode n : nodes) {
			if (n.getName().equals(name)) {
				return n;
			}
		}
		return null;
	}

	public ArrayList<BeliefNode> getNodes() {
		return nodes;
	}

	@Override
	public double calculateProbability(String nodeName, Pair[] query) {
		return calcStrategy.calculateProbability(nodeName, query);
	}
}
