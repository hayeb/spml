package spml3;

import java.util.ArrayList;

public class VariableEliminationStrategy implements ProbabilityCalculationStrategy {
	private ArrayList<BeliefNode> eliminationOrdering;
	private ArrayList<Factor> factorlist;
	
	
	public VariableEliminationStrategy(BeliefNetwork beliefnetwork){
		eliminationOrdering = generateEliminationOrdering(beliefnetwork);
	}

	@Override
	public double calculateProbability(String nodeName, String parents) {
		if(!isInEliminationOrdering(nodeName))
			for(BeliefNode beliefnode : eliminationOrdering){
				//Factor[] factorlist
				//Factor newfactor = multiplyFactors(factorlist);
				//sum out beliefnode variable from newfactor.
				//remove factors in factorlist from main factorlist.
				//add newfactor
			}
		return 0;
	}
	
	/**
	 * The current node/variable, before getting eliminated, multiply all factors including this variable,
	 * creating one final variable.
	 * @param beliefnode
	 */
	private Factor multiplyFactors(factorlist){
		
	}
	
	/**
	 * Checks the EO for possible error.
	 * @param nodeName
	 * @return
	 */
	private boolean isInEliminationOrdering(String nodeName){
		for(BeliefNode beliefnode : eliminationOrdering){
			if(beliefnode.getName().equals(nodeName))
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param beliefnetwork
	 * @return
	 */
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

}
