/**
 * 
 */
package spml3;

import java.util.ArrayList;

/**
 * @author 
 *
 */
public class Factor {
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

	/**
	 * Multiplies factor f with this factor.
	 * 
	 * @param f
	 * @return A factor which contains all possible combinations of variables of
	 *         this factor and factor f and their probability.
	 */
	public Factor multiplyFactor(Factor f, String var) {
		ProbabilityMap m = new ProbabilityMap();
		DataStructuur fmap = f.getProbabilityData();
		Pair[][] thisrows = probabilities.getRows();
		Pair[][] frows = fmap.getRows();
		
		for (Pair[] thisp: thisrows) {
			for (Pair[] fp : frows ) {
				System.out.print("This row: " + thisp + "\nF row: " + fp + "\nVariable to eliminate: " + var + "\n");
			}
		}
		
		
		
		Factor newFactor = new Factor(m);
		return newFactor;
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
