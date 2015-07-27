/**
 * 
 */
package spml3;

import java.util.ArrayList;

/**
 * @author mmwvh
 *
 */
public class Factor {
	private ArrayList<String> variableNames;

	/**
	 *  Use this to store the probabilities for this factor.
	 */
	private final DataStructuur probabilities;

	/**
	 * Assume the factor is not observed, initially.
	 */
	private boolean observed = false;

	/**
	 * heeft een lijst met parents per node : factor TODO: Improve docs/this
	 * method
	 */
	public Factor(BeliefNode beliefnode) {
		probabilities = beliefnode.cloneProbabilitiesTable();
		variableNames = new ArrayList<String>();
		variableNames.addAll(probabilities.getVariableNames());
	}

	/**
	 * Reduce the specified observed variable from this factor. This will remove
	 * all entries in the probability table which do not contain the observed
	 * variable in its observed state.
	 * 
	 * @param variable
	 */
	public void reduceVariable(Pair observed) {
		if (variableNames.contains(observed.getName())) {
			variableNames.remove(observed.getName());
			probabilities.removeObserved(observed);
		}
	}

	public boolean isObserved() {
		return observed;
	}

	public void setObserved(boolean observed) {
		this.observed = observed;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (variableNames.size() > 0) {
		sb.append(variableNames.get(0));
		for (int i = 1; i < variableNames.size(); i++) {
			sb.append(", " + variableNames.get(i));
		}
		}
		return "f(" + sb.toString() + ")";
	}
}
