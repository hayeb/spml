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
	private ArrayList<Pair> observedVariables;

	/**
	 * Use this to store the probabilities for this factor.
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
		observedVariables = new ArrayList<Pair>();
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
			probabilities.removeObserved(observed);
			adjustNameList(observed.getName());
			observedVariables.add(observed);
		}
	}

	private void adjustNameList(String name) {
		variableNames = probabilities.getVariableNames();
		if (variableNames.size() == 1) {
			System.out.println(variableNames.get(0) + " is observed.");
			observed = true;
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
		if (variableNames.size() > 1) {
			for (int i = 0; i < variableNames.size(); i++) {
				boolean observedVariable = false;
				String state = "";
				for (Pair p : observedVariables) {
					if (p.getName().equals(variableNames.get(i))) {
						observedVariable = true;
						state = p.getState();
					}
				}
				if (observedVariable) {
					sb.append(variableNames.get(i) + "=" + state);
				} else {
					sb.append(variableNames.get(i));
				}
				if (!(i == variableNames.size()-1)) {
					sb.append(", ");
				}
			}
		}
		return "f(" + sb.toString() + ")";
	}
}
