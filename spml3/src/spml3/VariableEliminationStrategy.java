package spml3;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VariableEliminationStrategy implements ProbabilityCalculationStrategy {
	private ArrayList<BeliefNode> eliminationOrdering;
	private BeliefNetwork beliefnetwork;
	private ArrayList<Factor> factors;

	public VariableEliminationStrategy(BeliefNetwork beliefnetwork) {

		// Dit moet niet in de constructor denk ik, maar pas na het
		// identificeren vna de factoren.
		// eliminationOrdering = generateEliminationOrdering(beliefnetwork);
		
		this.beliefnetwork = beliefnetwork;
		factors = new ArrayList<Factor>();
	}

	private ArrayList<BeliefNode> generateEliminationOrdering(BeliefNetwork beliefnetwork) {
		ArrayList<BeliefNode> eliminationordering = new ArrayList<BeliefNode>(beliefnetwork.getNodes().size() - 1);
		String[] str = new String[8];
		str[0] = "A"; // eliminate first
		str[1] = "B";
		str[2] = "C";
		str[3] = "D"; // eliminate last
		for (int a = 0; a < str.length; a++) {
			for (BeliefNode beliefnode : beliefnetwork.getNodes()) {
				if (beliefnode.getName() == str[a])
					eliminationordering.add(beliefnode);
			}
		}
		return eliminationordering;
	}

	private void identifyFactors(String query) {
		// 1. Try to find the query variable in the network.
		BeliefNode n = beliefnetwork.getNode(query);
		if (n != null) {
			// Create a factor for the query variable
			Factor q = new Factor(n);
			ArrayList<String> parents = new ArrayList<String>();
			Collections.copy(parents, n.getParents());
			for (String s : parents) {
				BeliefNode parent = beliefnetwork.getNode(s);
				factors.add(new Factor(parent));
				parents.addAll(parent.getParents());
			}
			
		} else {
			System.err.print("Could not find query node.");
			System.exit(2);
		}
		
	}

	@Override
	public double calculateProbability(String nodeName, List<Boolean> parents) {
		return 0;
	}

	/**
	 * Checks the EO for possible error.
	 * 
	 * @param nodeName
	 * @return
	 */
	public boolean isInEliminationOrdering(String nodeName) {
		for (BeliefNode beliefnode : eliminationOrdering) {
			if (beliefnode.getName().equals(nodeName))
				return true;
		}
		return false;
	}

}
