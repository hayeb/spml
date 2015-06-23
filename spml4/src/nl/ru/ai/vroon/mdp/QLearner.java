package nl.ru.ai.vroon.mdp;

import java.util.Arrays;

public class QLearner {
	private MarkovDecisionProblem mdp;
	private double[][][] Qvalues;
	private double learnRate;
	private double gamma;

	public QLearner(MarkovDecisionProblem mdp) {
		this.mdp = mdp;
		mdp.setWaittime(20);
		Qvalues = new double[mdp.getWidth()][mdp.getHeight()][4];
		setDefaultZeroQ();
		double learnRate = 0.7;
		double gamma = 0.7;
	}
	
	public void start(){
		mdp.restart();
		int xpos = mdp.getStateXPosition();
		int ypos = mdp.getStateYPostion();
		while(true){
			Action a = chooseAction(xpos, ypos);
			mdp.performAction(a);
			int newxpos = mdp.getStateXPosition();
			int newypos = mdp.getStateYPostion();
			Qvalues[xpos][ypos][a.ordinal()] = newQvalue(xpos, ypos, newxpos, newypos, a);
			xpos = newxpos;
			ypos = newypos;
			if(mdp.getField(xpos, ypos) == Field.REWARD || mdp.getField(xpos, ypos) == Field.NEGREWARD){
				mdp.restart();
				xpos = mdp.getStateXPosition();
				ypos = mdp.getStateYPostion();
			}
		}
	}

	private void setDefaultZeroQ() {
		for (int i = 0; i < Qvalues.length; i++) {
			for (int j = 0; j < Qvalues[0].length; j++)
				Arrays.fill(Qvalues[i][j], 0);
		}
	}
	
	private double bestValue(int xpos, int ypos){
			double bestValue = Qvalues[xpos][ypos][0];
			if(Qvalues[xpos][ypos][0] < Qvalues[xpos][ypos][1])
				bestValue = Qvalues[xpos][ypos][1];
			if(Qvalues[xpos][ypos][0] < Qvalues[xpos][ypos][1] && Qvalues[xpos][ypos][1] < Qvalues[xpos][ypos][2])
				bestValue = Qvalues[xpos][ypos][2];
			if(Qvalues[xpos][ypos][0] < Qvalues[xpos][ypos][1] && Qvalues[xpos][ypos][1] < Qvalues[xpos][ypos][2] && Qvalues[xpos][ypos][2] < Qvalues[xpos][ypos][3])
				bestValue = Qvalues[xpos][ypos][3];
			return bestValue;
	}
	
	private Action chooseAction(int xpos, int ypos){
		if(Qvalues[xpos][ypos][0] == Qvalues[xpos][ypos][1] && Qvalues[xpos][ypos][1] == Qvalues[xpos][ypos][2] && Qvalues[xpos][ypos][2] == Qvalues[xpos][ypos][3]){
			return Action.randomAction();
		}
		else{
			Action bestAction = Action.values()[0];
			if(Qvalues[xpos][ypos][0] < Qvalues[xpos][ypos][1]){
				bestAction = Action.values()[1];
			}
			if(Qvalues[xpos][ypos][0] < Qvalues[xpos][ypos][1] && Qvalues[xpos][ypos][1] < Qvalues[xpos][ypos][2]){
				bestAction = Action.values()[2];
			}
			if(Qvalues[xpos][ypos][0] < Qvalues[xpos][ypos][1] && Qvalues[xpos][ypos][1] < Qvalues[xpos][ypos][2] && Qvalues[xpos][ypos][2] < Qvalues[xpos][ypos][3]){
				bestAction = Action.values()[3];
			}
			return bestAction;
		}
			
	}
	
	private double newQvalue(int oldxpos, int oldypos, int xpos, int ypos, Action a){
		return Qvalues[oldxpos][oldypos][a.ordinal()] + learnRate*(mdp.getReward() + (gamma*bestValue(xpos, ypos)) - Qvalues[oldxpos][oldypos][a.ordinal()]);
		
	}
	
	

}
