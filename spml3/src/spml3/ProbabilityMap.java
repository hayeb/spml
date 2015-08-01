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
public class ProbabilityMap extends HashMap<Pair[], Double> implements
		DataStructuur, Cloneable {

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
		//System.out.println("Running removeObserved in " + variableName + "\nEliminating " + pair.getName() + "\n");
		// Initialise an array which holds the keys of the map
		Pair[][] rows = this.keySet().toArray(
				new Pair[this.keySet().size()][]);

		// For every pair, remove the entry if it has the same name but
		// not the same state.
		for (Pair[] p : rows) {
			boolean keep = true;
			for (int i = 0; i < p.length && keep; i++) {
				if (p[i].getName().equals( pair.getName())) {
					//System.out.print("Found matching pair: " + pair.getName() + "\n");
					if (!p[i].getState().equals(pair.getState())) {
						keep = false;
					}
				}
			}
			if (!keep) {
				//System.out.println("Removing: " + p);
				this.removeRow(p);

			}
		}
		cleanUpVariables(pair.getName());
	}

	public ProbabilityMap cloneMap() {
		ProbabilityMap map = new ProbabilityMap(variableName);
		for (Pair[] p : this.keySet()) {
			map.addProbability(p, this.get(p));
		}
		return map;
	}

	private void cleanUpVariables(String name) {
		Pair[][] rows = Arrays.copyOf(
				this.keySet()
						.toArray(new Pair[this.keySet()
								.size()][]),
				keySet().size());

		// TODO: Implement cleanup
		/*
		 * Steps; 1. For every key-value pair: 2. Copy the key and value
		 * 3. Remove the observed variable from the key 4. Remove the
		 * original key-value pair from the structure 5. add the
		 * copied/adjusted key-value pair
		 */

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
}
