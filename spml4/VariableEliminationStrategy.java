package spml3;

import java.awt.List;
import java.util.ArrayList;

public class VariableEliminationStrategy implements ProbabilityCalculationStrategy {
	private ArrayList<BeliefNode> EliminationOrdering;
	
	public VariableEliminationStrategy(BeliefNetwork beliefnetwork){
		EliminationOrdering = new ArrayList<BeliefNode>();
	}
	
	@Override
	public double calculateProbability(String nodeName, String parents) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public ArrayList<BeliefNode> generateEliminationOrdering(BeliefNetwork beliefnetwork){
		
		return null;
	}
}
