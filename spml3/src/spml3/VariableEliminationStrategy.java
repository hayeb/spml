package spml3;

import java.util.ArrayList;
import java.util.Arrays;
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
	private void generateEliminationOrderingParents(String nodeName) {
		ArrayList<String> noParent = new ArrayList<String>();
		ArrayList<String> oneParent = new ArrayList<String>();
		ArrayList<String> remainder = new ArrayList<String>();
		for (BeliefNode node : beliefnetwork.getNodes()) {
			if (!node.getName().equals(nodeName)) {
				if (beliefnetwork.getNumberOfParentsRecursive(node) == 0) {
					noParent.add(node.getName());
				} else if (beliefnetwork.getNumberOfParentsRecursive(node) == 1) {
					oneParent.add(node.getName());
				} else {
					remainder.add(node.getName());
				}
			}
		}
		oneParent.addAll(remainder);
		noParent.addAll(oneParent);
		noParent.add(nodeName);
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
	public double calculateProbability(Pair query, Pair[] observedNodes) {
		identifyFactors(query.getName());

		/*
		 * This can be replaced by: eliminationOrdering.add("VAR1");
		 * eliminationOrdering.add("VAR2"); etc etc..
		 */
		generateEliminationOrderingParents(query.getName());

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
				if (!s.equals(query.getName())) {
					Factor d = sumOut(m, s);
					if (d != null) {
						factors.add(d);
					}
				} else {
					factors.add(m);
				}
			}
		}
		ArrayList<Pair> querylist = new ArrayList<Pair>();
		querylist.add(query);
		return factors.get(0).getProbability(querylist);
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
						List<Pair> newKey = Arrays.asList(concatAndSet(row, otherrow)) ;
						Double newValue = f1.getProbability(Arrays.asList(row)) * f2.getProbability(Arrays.asList(otherrow));
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
		ArrayList<Pair[]> done = new ArrayList<Pair[]>();
		ProbabilityMap map = new ProbabilityMap();
		boolean changed = false;
		for (Pair[] row : rows) {
			if (row.length == 1) {
				return null;
			}
			for (Pair[] otherrow : rows) {
				if (summableRow(row, otherrow, variable) && !done.contains(row) && !done.contains(otherrow)
						&& !row.equals(otherrow)) {
					changed = true;
					done.add(row);
					done.add(otherrow);
					List<Pair> newrow = new ArrayList<Pair>();
					for (int i = 0; i < row.length; i++) {
						if (!row[i].getName().equals(variable)) {
							newrow.add(row[i]);
						}
					}
					map.addProbability(newrow, f.getProbability(Arrays.asList(row)) + f.getProbability(Arrays.asList(otherrow)));
				}
			}
		}
		if (changed) {
			return new Factor(map);
		} else {
			return f;
		}
	}

	private boolean summableRow(Pair[] row1, Pair[] row2, String sumvar) {
		Arrays.sort(row1);
		Arrays.sort(row2);
		if (row1.length == 1) {
			return false;
		}
		for (Pair p1 : row1) {
			for (Pair p2 : row2) {
				if (!p1.getName().equals(sumvar) && p1.getName().equals(p2.getName())
						&& !p1.getState().equals(p2.getState())) {
					return false;
				}
			}
		}
		return true;
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
