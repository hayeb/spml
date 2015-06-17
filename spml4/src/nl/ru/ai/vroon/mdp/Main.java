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
		MarkovDecisionProblem mdp = new MarkovDecisionProblem();
		mdp.setInitialState(0, 0);
		ValueIIterationLearner learner = new ValueIIterationLearner(mdp);
		learner.generatePolicy();
		
		
//		MarkovDecisionProblem mdp2 = new MarkovDecisionProblem(10, 10);
//		mdp2.setField(5, 5, Field.REWARD);
		
	}
}
