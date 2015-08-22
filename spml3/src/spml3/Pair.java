package spml3;

public class Pair implements Comparable<Pair>{

	private final String name;
	private final String state;

	/**
	 * Constructor to attach a state to a variable name.
	 * 
	 * @param name
	 *            The name of the variable. This will be converted to lowercase
	 *            and trimmed by built-in string methods.
	 * @param state
	 */
	public Pair(String name, String state) {
		this.name = name.toLowerCase().trim();
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public String getState() {
		return state;
	}

	@Override
	public int compareTo(Pair o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Pair)) {
			return false;
		}
		Pair other = (Pair) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (state == null) {
			if (other.state != null) {
				return false;
			}
		} else if (!state.equals(other.state)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return name + ": " + state;
	}
}
