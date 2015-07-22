package spml3;

public interface DataStructuur {

	/**
	 * 
	 * 
	 * @return the probability of the corresponding row; e.g. The probability of
	 *         factor A,B,C where !a, !b, c holds.
	 * 
	 *         But when I call it with a different Factor I might want a row
	 *         with corresponding variable states.
	 *         
	 *         Thus perhaps I want a list instead.
	 */
	public double rowProbablity(variable A, State True);
}
