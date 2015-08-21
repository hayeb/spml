package spml3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VariableEliminationStrategy implements ProbabilityCalculationStrategy {
	private List<String> eliminationOrdering;
	private BeliefNetwork beliefnetwork;
	private List<Factor> factors;

	/**
	 * Initializes this strategy with given belief network and arraylists for
	 * factors and elimination Ordering
	 * 
	 * @param beliefnetwork
	 */
	public VariableEliminationStrategy(BeliefNetwork beliefnetwork) {
		this.beliefnetwork = beliefnetwork;
		factors = new ArrayList<Factor>();
		eliminationOrdering = new ArrayList<String>();
	}

	/*
	 * Generates a elimination ordering according to the total number of parents
	 * each node has. Places the ordering in the elimination ordering list
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
	 * Generates a factor for every node in the network. Exits if the query node
	 * could not be found.
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

		/*
		 * This can be replaced by: eliminationOrdering.add("VAR1");
		 * eliminationOrdering.add("VAR2"); etc etc..
		 */
		generateEliminationOrderingParents();

		System.out.println("\nElimination ordering:\n " + eliminationOrdering + "\n");
		for (String s : eliminationOrdering) {
			boolean observed = false;
			int observedIndex = -1;

			/* Find out if its observed.. */
			for (int i = 0; i < observedNodes.length && !observed; i++) {
				if (observedNodes[i].getName().equals(s)) {
					observed = true;
					observedIndex = i;
				}
			}

			if (observed) {
				ArrayList<Factor> toRemove = new ArrayList<Factor>();
				for (Factor f : factors) {
					if (f.hasVariable(s)) {
						f.reduceVariable(observedNodes[observedIndex]);
						if (f.getVariableNames().size() == 0) {
							toRemove.add(f);
						}
					}
				}
				factors.removeAll(toRemove);
			} else {
				ArrayList<Factor> toMultiply = new ArrayList<Factor>();
				for (Factor f : factors) {
					if (f.hasVariable(s)) {
						toMultiply.add(f);
					}
				}
				factors.removeAll(toMultiply);
				/* Multiple the factors containing the variable into one */
				Factor m = multiplyFactors(toMultiply, s);
				/* Sum out the variable.. */
				Factor d = sumOut(m, s);
				factors.add(d);
			}
		}
		return 0.00;
	}

	/**
	 * Multiplies the factors in the toMultiply list. Assumes that {}
	 * 
	 * @param toMultiply
	 * @param var
	 * @return
	 */
	private Factor multiplyFactors(ArrayList<Factor> toMultiply, String var) {
		while (toMultiply.size() != 1) {
			Factor f1 = toMultiply.remove(0);
			Factor f2 = toMultiply.remove(0);
			toMultiply.add(multiplyFactor(f1, f2, var));
		}
		return toMultiply.get(0);
	}

	private Factor multiplyFactor(Factor f1, Factor f2, String var) {
		Pair[][] r1 = f1.getProbabilityData().getRows();
		Pair[][] r2 = f2.getProbabilityData().getRows();
		ProbabilityMap map = new ProbabilityMap();
		for (Pair[] row : r1) {
			Pair target = findIndexOf(row, var);
			for (Pair[] otherrow : r2) {
				for (int j = 0; j < otherrow.length; j++) {
					if (otherrow[j].equals(target)) {
						Pair[] newKey = concatAndSet(row, otherrow);
						Double newValue = f1.getProbability(row) * f2.getProbability(otherrow);
						map.addProbability(newKey, newValue);
					}
				}
			}
		}
		Factor bla = new Factor(map);
		return bla;
	}

	private Factor sumOut(Factor f, String variable) {
		Pair[][] rows = f.getProbabilityData().getRows();
		Pair[][] otherrows = new Pair[rows.length][];
		System.arraycopy(rows, 0, otherrows, 0, rows.length);
		ProbabilityMap map = new ProbabilityMap();
		for (Pair[] row : rows) {
			for (Pair[] otherrow : otherrows) {
				// TODO: Complete
			}
		}
		return f;
	}

	private Pair[] concatAndSet(Pair[] a, Pair[] b) {
		int aLen = a.length;
		int bLen = b.length;
		Pair[] c = new Pair[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		int end = c.length;
		Set<Pair> set = new HashSet<Pair>();
		for (int i = 0; i < end; i++) {
			set.add(c[i]);
		}
		return set.toArray(new Pair[set.size()]);
	}

	private Pair findIndexOf(Pair[] row, String goal) {
		for (int i = 0; i < row.length; i++) {
			if (row[i].getName().equals(goal)) {
				return row[i];
			}
		}
		return null;
	}
}
