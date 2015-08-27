package spml3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A map specifically designed to store a list of pairs against a double. Only
 * use the interface DataStructuur with this class. This guarantees that all
 * queries will be handled correctly.
 * 
 * @author hayeb
 *
 */
public class ProbabilityMap extends HashMap<List<Pair>, Double>implements TableDataStructure {

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
	public void addProbability(List<Pair> query, double probability) {
		query.sort(new PairComparator());
		this.put(query, probability);

	}

	@Override
	public double getProbability(List<Pair> query) {
		query.sort(new PairComparator());
		if (this.containsKey(query)) {
			return (Double) get(query);
		} else {
			System.err.print("Map does not contain: " + query + "\n");
			return 0.0;

		}

	}

	@Override
	public void removeObserved(Pair pair) {
		// System.out.println("Running removeObserved in " + variableName +
		// "\nEliminating " + pair.getName() + "\n");

		// Initialise an array which holds the keys of the map
		Set<List<Pair>> rows = this.keySet();

		// For every pair, remove the entry if it has the same name but
		// not the same state.
		ArrayList<List<Pair>> toRemove = new ArrayList<List<Pair>>();
		for (List<Pair> p : rows) {
			boolean keep = true;
			for (int i = 0; i < p.size() && keep; i++) {
				if (p.get(i).getName().equals(pair.getName()) && !p.get(i).getState().equals(pair.getState())) {
					keep = false;
				}
			}
			if (!keep) {
				toRemove.add(p);
			}
		}
		// Not sure about this call
		for (List<Pair> p : toRemove) {
			removeRow(p);
		}
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
		for (List<Pair> p : this.keySet()) {
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

		Set<List<Pair>> rows = keySet();
		ArrayList<List<Pair>> toRemove = new ArrayList<List<Pair>>();
		ArrayList<List<Pair>> toAddKeys = new ArrayList<List<Pair>>();
		ArrayList<Double> toAddValues = new ArrayList<Double>();

		for (List<Pair> plist : rows) {
			double value = this.getProbability(plist);
			ArrayList<Pair> newKeyList = new ArrayList<Pair>();

			for (int j = 0; j < plist.size(); j++) {
				Pair p = plist.get(j);
				if (!plist.get(j).getName().equals(name)) {
					newKeyList.add(p);
				}
			}
			toRemove.add(plist);
			toAddKeys.add(newKeyList);
			toAddValues.add(value);
		}
		for (List<Pair> p : toRemove) {
			removeRow(p);
		}
		for (List<Pair> p : toAddKeys) {
			this.addProbability(p, toAddValues.remove(0));
		}
	}

	@Override
	public ArrayList<String> getVariableNames() {
		List<Pair> p = this.keySet().iterator().next();
		ArrayList<String> names = new ArrayList<String>();
		for (Pair pair : p) {
			names.add(pair.getName());
		}
		return names;
	}

	@Override
	public void removeRow(List<Pair> row) {
		row.sort(new PairComparator());
		this.remove(row);
	}

	@Override
	public Pair[][] getRows() {
		Set<List<Pair>> rowset = keySet();
		Pair[][] rowarray = new Pair[rowset.size()][];
		int i = 0;
		for (List<Pair> p : rowset) {
			rowarray[i] = p.toArray(new Pair[p.size()]);
			i++;
		}
		return rowarray;
	}
}
