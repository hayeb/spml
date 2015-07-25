/**
 * 
 */
package spml3;

/**
 * @author mmwvh
 *
 */
public class Factor {
	private String[] variableNames;

	// Use this to store the probabilities for this factor.
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
		variableNames = new String[beliefnode.numberOfParents() + 1];
		variableNames[0] = beliefnode.getName();
		for (int i = 0; i < beliefnode.numberOfParents(); i++) {
			variableNames[i + 1] = beliefnode.getParents().get(i);
		}

		probabilities = new ProbabilityMap();
	}

	/**
	 * Iterate over all the entries in the probability table, and remove all
	 * variables for which the state is not equals to the parameters.
	 */
	public void eliminateVariable(String variableName, String state) {
		String[] newVN;
		for (int i = 0; i < variableNames.length; i++) {
			if (variableNames[i] != variableName) {

			}
		}
	}

	public boolean isObserved() {
		return observed;
	}

	public void setObserved(boolean observed) {
		this.observed = observed;
	}

}
