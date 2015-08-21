/**
 * 
 */
package spml3;

import java.util.ArrayList;

/**
 * @author 
 *
 */
public class Factor implements DataStructuur {
	private ArrayList<String> variableNames;

	/**
	 * Use this to store the probabilities for this factor.
	 */
	private final DataStructuur probabilities;

	/**
	 * heeft een lijst met parents per node : factor TODO: Improve docs/this
	 * method
	 */
	public Factor(BeliefNode beliefnode) {
		probabilities = beliefnode.cloneProbabilitiesTable();
		variableNames = new ArrayList<String>();
		variableNames.addAll(probabilities.getVariableNames());
	}
	
	public Factor(DataStructuur map) {
		probabilities = map;
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
		probabilities.removeObserved(observed);
		updateNameList(observed.getName());
	}

	/**
	 * Updates the variable name list according to variables on the probability
	 * map.
	 * 
	 * @param name
	 */
	public void updateNameList(String name) {
		variableNames = probabilities.getVariableNames();
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
	 * Returns if this factor only contains 1 variable and if this variable is
	 * observed according to the string name.
	 * 
	 * @return
	 */
	public boolean isSingletonObserved(String name) {
		return variableNames.contains(name) && variableNames.size() == 1;
	}
	
	/**
	 * 
	 * @return
	 */
	public DataStructuur getProbabilityData() {
		return this.probabilities;
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

	@Override
	public void addProbability(Pair[] query, double probability) {
		this.probabilities.addProbability(query, probability);
		
	}

	@Override
	public double getProbability(Pair[] query) {
		return this.probabilities.getProbability(query);
	}

	@Override
	public void removeObserved(Pair pair) {
		this.probabilities.removeObserved(pair);
		
	}

	@Override
	public void removeRow(Pair[] row) {
		this.removeRow(row);
		
	}

	@Override
	public ArrayList<String> getVariableNames() {
		return this.probabilities.getVariableNames();
	}

	@Override
	public Pair[][] getRows() {
		return this.probabilities.getRows();
	}
}
