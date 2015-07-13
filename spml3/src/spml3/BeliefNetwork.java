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
		calcStrategy = new VariableEliminationStrategy();
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
	 *            A list of parent and their state, i.e.: <code> <br> <ul>
	 * 				    [0] = not VariableOne <br>
	 * 				    [1] = VariableTwo <br>
	 * 				    [2] = not VariableThree <br>
	 * 					</ul>
	 * 					</code>
	 * @return the probability of nodeName being true given the parent in the
	 *         list.
	 */
	public double calcProbability(String nodeName, List<String> parents) {
		return calcStrategy.calculateProbability(nodeName, parents);
	}
}
