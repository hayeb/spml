package nl.ru.ai.vroon.mdp;

import java.util.Arrays;
import java.util.Random;

public class QLearner {
	private MarkovDecisionProblem mdp;
	private double[][][] Qvalues;
	private double learnRate;
	private double gamma;
	private double temperature;

	public QLearner(MarkovDecisionProblem mdp) {
		this.mdp = mdp;
		mdp.setWaittime(100);
		Qvalues = new double[mdp.getWidth()][mdp.getHeight()][4];
		setDefaultZeroQ();
		learnRate = 0.7;
		gamma = 0.7;
		double temperature = 100;
	}
	
	public void start(){
		mdp.restart();
		int xpos = mdp.getStateXPosition();
		int ypos = mdp.getStateYPostion();
		int goalcount = 0;
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
				System.out.println("goal" + goalcount++);
				temperature--;
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
		Random random = new Random();
		if(Qvalues[xpos][ypos][0] == Qvalues[xpos][ypos][1] && Qvalues[xpos][ypos][1] == Qvalues[xpos][ypos][2] && Qvalues[xpos][ypos][2] == Qvalues[xpos][ypos][3]){
			return Action.randomAction();
		}
		else{
			double totalSumQ = 0;
			for(int i = 0; i < 4; i++)
				totalSumQ += Math.pow(Math.E, (Qvalues[xpos][ypos][i]/temperature));
			
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
			double chance = (Math.pow(Math.E, Qvalues[xpos][ypos][bestAction.ordinal()]/temperature)) / totalSumQ;
			double randomDouble = random.nextDouble();
			if(randomDouble < chance){
				return Action.randomAction();
			}
			else return bestAction;
		}
			
	}
	
	private double newQvalue(int oldxpos, int oldypos, int xpos, int ypos, Action a){
			return (Qvalues[oldxpos][oldypos][a.ordinal()] + learnRate*(mdp.getReward() + (gamma*bestValue(xpos, ypos)) - Qvalues[oldxpos][oldypos][a.ordinal()]));
		
	}
	
	

}
