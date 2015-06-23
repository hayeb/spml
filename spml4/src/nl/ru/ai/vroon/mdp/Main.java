package nl.ru.ai.vroon.mdp;

/**
 * This main is for testing purposes (and to show you how to use the MDP class).
 * 
 * @author Jered Vroon
 *
 */
public class Main {

	/**
	 * @param args, not used
	 */
	public static void main(String[] args) {
		MarkovDecisionProblem mdp = new MarkovDecisionProblem(5, 5);
		mdp.setInitialState(0, 0);
		
//		ValueIterator VI = new ValueIterator(mdp);
//		VI.generatePolicies();
		
		
		QLearner qlearner= new QLearner(mdp);
		qlearner.start();	
		
	}
}
