package spml3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A map specifically designed to store a list of pairs against a double. Only
 * use the interface DataStructuur with this class. This guarantees that all
 * queries will be handled correctly.
 * 
 * @author haye
 *
 */
public class ProbabilityMap extends HashMap<Pair[], Double>implements DataStructuur, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the variable where this map belongs to.
	 */
	private String variableName;

	public ProbabilityMap(String name) {
		this.variableName = name;
	}

	public ProbabilityMap() {
		variableName = "default name";
	}

	@Override
	public void addProbability(Pair[] query, double probability) {
		Arrays.sort(query);
		this.put(query, probability);

	}

	@Override
	public double getProbability(Pair[] query) {
		Arrays.sort(query);
		return this.get(query);
	}

	@Override
	public void removeObserved(Pair pair) {
		// System.out.println("Running removeObserved in " + variableName +
		// "\nEliminating " + pair.getName() + "\n");

		// Initialise an array which holds the keys of the map
		Pair[][] rows = this.keySet().toArray(new Pair[this.keySet().size()][]);

		// For every pair, remove the entry if it has the same name but
		// not the same state.
		for (Pair[] p : rows) {
			boolean keep = true;
			for (int i = 0; i < p.length && keep; i++) {
				if (p[i].getName().equals(pair.getName()) && !p[i].getState().equals(pair.getState())) {
					keep = false;
				}
			}
			if (!keep) {
				this.removeRow(p);
			}
		}
		//Not sure about this call
		cleanUpVariables(pair.getName());
	}

	/**
	 * Returns a new probability map, with the same name and contents equal to
	 * this probability map.
	 * 
	 * @return
	 */
	public ProbabilityMap cloneMap() {
		ProbabilityMap map = new ProbabilityMap(variableName);
		for (Pair[] p : this.keySet()) {
			map.addProbability(p, this.get(p));
		}
		return map;
	}

	/**
	 * Cleans up variables in the map. removes all variables in the map which
	 * name equals the string name
	 * 
	 * @param name
	 *            All variables of which the name equals this string will be
	 *            removed from the map.
	 */
	private void cleanUpVariables(String name) {

		Pair[][] rows = Arrays.copyOf(this.keySet().toArray(new Pair[this.keySet().size()][]), keySet().size());

		for (int i = 0; i < rows.length; i++) {
			Pair[] value = rows[i];
			double key = this.getProbability(value);
			ArrayList<Pair> newKeyList = new ArrayList<Pair>();

			for (int j = 0; j < value.length; j++) {
				Pair p = value[j];
				if (!value[j].getName().equals(name)) {
					newKeyList.add(p);
				}
			}
			this.removeRow(value);
			Pair[] newKey = new Pair[newKeyList.size()];
			this.addProbability(newKeyList.toArray(newKey), key);
		}
	}

	@Override
	public ArrayList<String> getVariableNames() {
		Pair[] p = this.keySet().iterator().next();
		ArrayList<String> names = new ArrayList<String>();
		for (Pair pair : p) {
			names.add(pair.getName());
		}
		return names;
	}

	@Override
	public void removeRow(Pair[] row) {
		Arrays.sort(row);
		this.remove(row);
	}

	@Override
	public Pair[][] getRows() {
		return Arrays.copyOf(this.keySet().toArray(new Pair[this.keySet().size()][]), keySet().size());
	}
}
