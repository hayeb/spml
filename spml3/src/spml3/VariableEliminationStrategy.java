package spml3;

import java.util.ArrayList;

public class VariableEliminationStrategy implements ProbabilityCalculationStrategy {
	private ArrayList<BeliefNode> eliminationOrdering;
	
	public VariableEliminationStrategy(BeliefNetwork beliefnetwork){
		eliminationOrdering = generateEliminationOrdering(beliefnetwork);
	}
	
	private ArrayList<BeliefNode> generateEliminationOrdering(BeliefNetwork beliefnetwork) {
		ArrayList<BeliefNode> eliminationordering = new ArrayList<BeliefNode>(beliefnetwork.getNodes().size()-1);
		String[] str = new String[8];
		str[0] = "A"; //eliminate first
		str[1] = "B";
		str[2] = "C";
		str[3] = "D"; // eliminate last
		for(int a = 0; a < str.length; a++){
			for(BeliefNode beliefnode : beliefnetwork.getNodes()){
				if(beliefnode.getName() == str[a])
					eliminationordering.add(beliefnode);
			}
		}
		return eliminationordering;
		
	}

	@Override
	public double calculateProbability(String nodeName, String parents) {
		// TODO Auto-generated method stub
		return 0;
	}

}
