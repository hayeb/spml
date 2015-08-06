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
	 * Reduce the specified observed variable from this factor. This will
	 * remove all entries in the probability table which do not contain the
	 * observed variable in its observed state.
	 * 
	 * @param variable
	 */
	public void reduceVariable(Pair observed) {
		probabilities.removeObserved(observed);
		adjustNameList(observed.getName());
		observedVariables.add(observed);
	}

	/**
	 * Updates the variable name list according to variables on the
	 * probability map.
	 * 
	 * @param name
	 */
	public void adjustNameList(String name) {
		variableNames = probabilities.getVariableNames();
	}

	public boolean isObserved() {
		return observed;
	}

	public void setObserved(boolean observed) {
		this.observed = observed;
	}

	/**
	 * Returns if this factor has a variable with given name. Does not
	 * account for case sensitivity or trailing whitespace etc.
	 * 
	 * @param variableName
	 * @return
	 */
	public boolean hasVariable(String variableName) {
		return variableNames.contains(variableName);
	}

	/**
	 * Returns if this factor only contains 1 variable and if this variable
	 * is observed according to the string name.
	 * 
	 * @return
	 */
	public boolean isSingletonObserved(String name) {
		return variableNames.contains(name)
				&& variableNames.size() == 1;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (variableNames.size() > 0) {
			for (int i = 0; i < variableNames.size(); i++) {
				boolean observedVariable = false;
				String state = "";
				sb.append(variableNames.get(i));

				if (!(i == variableNames.size() - 1)) {
					sb.append(", ");
				}
			}
		}
		return "f(" + sb.toString() + ")";
	}
}
