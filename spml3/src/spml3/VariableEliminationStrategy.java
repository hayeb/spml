package spml3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VariableEliminationStrategy implements ProbabilityCalculationStrategy {
	private ArrayList<String> eliminationOrdering;
	private BeliefNetwork beliefnetwork;
	private ArrayList<Factor> factors;

	public VariableEliminationStrategy(BeliefNetwork beliefnetwork) {
		this.beliefnetwork = beliefnetwork;
		factors = new ArrayList<Factor>();
		eliminationOrdering = new ArrayList<String>();
	}

	/**
	 * Generates a elimination ordering according to the total number of parents
	 * each node has.
	 */
	private void generateEliminationOrderingParents() {
		ArrayList<String> noParent = new ArrayList<String>();
		ArrayList<String> oneParent = new ArrayList<String>();
		ArrayList<String> remainder = new ArrayList<String>();
		for (BeliefNode node : beliefnetwork.getNodes()) {
			if (beliefnetwork.getNumberOfParentsRecursive(node) == 0) {
				noParent.add(node.getName());
			} else if (beliefnetwork.getNumberOfParentsRecursive(node) == 1) {
				oneParent.add(node.getName());
			} else {
				remainder.add(node.getName());
			}
		}
		oneParent.addAll(remainder);
		noParent.addAll(oneParent);
		eliminationOrdering = noParent;
	}

	/**
	 * Generates a factor for every node in the network.
	 * 
	 * @param query
	 */
	private void identifyFactors(String query) {
		// Try to find the query variable in the network.
		BeliefNode n = beliefnetwork.getNode(query);
		if (n != null) {
			// Create a factor for the query variable
			Factor q = new Factor(n);
			factors.add(q);
			for (BeliefNode node : beliefnetwork.getNodes()) {
				System.out.println("Creating factor: " + node.getName());
				if (!node.getName().equals(query)) {
					factors.add(new Factor(node));
				}
			}

		} else { // If the query variable can't be found..
			System.err.print("Could not find query node.");
			System.exit(2);
		}
	}

	/**
	 * Removes all observed factors from the factor list
	 * 
	 * @param parents
	 * 
	 *            TODO; move the observed calculation from the factor itsself
	 */
	public void reduceObserved(Pair[] observed) {
		ArrayList<Factor> remove = new ArrayList<Factor>();
		for (Factor f : factors) {
			for (Pair p : observed) {
				if (f.isSingletonObserved(p.getName())) {
					remove.add(f);
				} else {
					if (f.hasVariable(p.getName())) {
						f.reduceVariable(p);
					}
				}

			}
		}
		factors.removeAll(remove);
	}

	@Override
	public double calculateProbability(String nodeName, Pair[] observedNodes) {
		identifyFactors(nodeName);
		generateEliminationOrderingParents();
		System.out.println(
				"\nElimination ordering:\n " + eliminationOrdering + "\n");

		System.out.print("Reducing observed variables\n");
		
		/* Reduce observed variables and remove all observed variables from the elimination ordering */
		reduceObserved(observedNodes);
		for (Factor f : factors) {
			f.updateNameList(null);
		}
		// For each factor in the elimination ordering..
		for (String var : eliminationOrdering) {
			System.out.println("\nEliminating variable " + var);
			ArrayList<Factor> toMultiply = new ArrayList<Factor>();
			for (int i = 0; i < factors.size(); i++) {
				Factor f = factors.get(i);
				if (f.hasVariable(var)) {
					toMultiply.add(f);
				}
			}
			factors.removeAll(toMultiply);
			System.out.println("Multiplying: " + toMultiply);
			if ((toMultiply.size() == 1 && !(factors.size() == 1))) {
				System.out.print("variable " + toMultiply.get(0)
						+ " has no influence on the outcome. We will leave this alone.\n");
			} else if (!toMultiply.isEmpty()) {
				factors.add(multiplyFactors(toMultiply, var));
			} else {
				System.out.println("We are done!");
			}

		}

		return 0.00;
	}

	private Factor multiplyFactors(ArrayList<Factor> toMultiply, String var) {
		while (toMultiply.size() != 1) {
			Factor f = toMultiply.remove(0);
			Factor f2 = toMultiply.remove(0);
			toMultiply.add(0, f.multiplyFactor(f2, var));
		}
		return toMultiply.get(0);
	}
}
