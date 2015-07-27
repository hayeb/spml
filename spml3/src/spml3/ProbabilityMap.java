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

	@Override
	public void addProbability(Pair[] query, double probability) {
		Arrays.sort(query);
		this.put(query, probability);

	}

	@Override
	public double getProbability(Pair[] query, Boolean state) {
		Arrays.sort(query);
		return this.get(query);
	}

	@Override
	public void removeObserved(Pair pair) {
		// Initialise an array which holds the keys of the map
		Pair[][] rows = this.keySet().toArray(new Pair[this.keySet().size()][]);
		for (Pair[] p : rows) {
			boolean keep = true;
			for (int i = 0; i < p.length && keep; i++) {
				if (p[i].getName() == pair.getName() && p[i].getState() != pair.getState()) {
					keep = false;
				}
			}
			if (!keep) {
				this.remove(p);
			}
		}
	}

	public ProbabilityMap cloneMap() {
		ProbabilityMap map = new ProbabilityMap(variableName);
		for (Pair[] p : this.keySet()) {
			map.addProbability(p, this.get(p));
		}
		return map;
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
}