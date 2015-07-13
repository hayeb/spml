package spml3;

public interface ProbabilityCalculationStrategy {

	/**
	 * Returns the probability of nodeName being true, given the parent nodes.
	 * @param nodeName
	 * The name of the node.
	 * @param parents
	 * The parents of the node. Should be of the following format: <code><br>
	 * <ul>"parentOne TRUE, parentTwo FALSE, parentThree TRUE"<ul></code>
	 * @return
	 */
	public double calculateProbability(String nodeName, String parents);
}
