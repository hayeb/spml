package spml3;

import java.util.List;

public interface ProbabilityCalculationStrategy {

	public double calculateProbability(String nodeName, List<String> parents);
}
