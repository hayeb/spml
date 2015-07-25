package spml3;

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
public class ProbabilityMap extends HashMap<Pair[], Double>implements DataStructuur {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void addProbability(Pair[] query, double probability) {
		Arrays.sort(query);
		this.put(query, probability);

	}

	@Override
	public double getProbability(Pair[] query, Boolean state) {
		Arrays.sort(query);
		if (state) {
			return this.get(query);
		} else {
			return 1 - this.get(query);
		}
	}
}
