package nl.ru.ai.vroon.mdp;

import java.util.Arrays;

public class ValueIterator {
	private MarkovDecisionProblem mdp;
	private double[][][] Qvalues;
	private double[][] Vvalues;
	private Action[][] Policies;
	private double gamma;
	private double maxIterationDelta;

	public ValueIterator(MarkovDecisionProblem mdp) {
		this.mdp = mdp;
		mdp.setWaittime(0);
		Qvalues = new double[mdp.getWidth()][mdp.getHeight()][4];
		Vvalues = new double[mdp.getWidth()][mdp.getHeight()];
		setDefaultZeroQ();
		setDefaultZeroV();
		gamma = 0.5;
		maxIterationDelta = 0.05; // default epsilon.
	}
	
	public void generatePolicies(){
		generateValues();
		setPolicies();
		addPolicies();
		mdp.restart();
		mdp.drawMDP();
	}

	private void setDefaultZeroV() {
		for (int i = 0; i < Vvalues.length; i++) {
			Arrays.fill(Vvalues[i], 0);
		}
	}
	
	private void setDefaultZeroQ() {
		for (int i = 0; i < Qvalues.length; i++) {
			for(int j = 0; j < Qvalues[0].length; j++){
				Arrays.fill(Qvalues[i][j], 0);
			}
		}
	}
	
	private void addPolicies() {
		mdp.setPolicies(Policies);
		
	}

	private void setPolicies(){
		Policies = new Action[mdp.getWidth()][mdp.getHeight()];
		for(int x = 0; x < mdp.getWidth(); x++){
			for(int y = 0; y < mdp.getHeight(); y++){
				double maxvalue = Double.NEGATIVE_INFINITY;
				for(int a = 0; a < 4; a++){
					if(Qvalues[x][y][a] > maxvalue){
						maxvalue = Qvalues[x][y][a];
						Policies[x][y] = Action.values()[a];
					}
				}
			}
		}
		
	}

	private void generateValues() {
		double currentV = 0;
		double newV = 0;
		do{
			currentV = totalVValue();
			for(int x = 0; x < mdp.getWidth(); x++){
				for(int y = 0; y < mdp.getHeight(); y++){
					if(mdp.getField(x, y) != Field.OUTOFBOUNDS && mdp.getField(x,  y) != Field.OBSTACLE){
						for(Action a : Action.values())
							setNewQValue(x, y, a);
					}
				}
			}
			for(int x = 0; x < mdp.getWidth(); x++){
				for(int y = 0; y < mdp.getHeight(); y++){
					Vvalues[x][y] = Math.max(Math.max(Math.max(Qvalues[x][y][0], Qvalues[x][y][1]), Qvalues[x][y][2]), Qvalues[x][y][3]);
				}
			}
			newV = totalVValue();
		}while (Math.abs(newV - currentV) > maxIterationDelta);
	}
	
	private double totalVValue(){
		double totalV = 0;
		for(int x = 0;x < mdp.getWidth();x++){
			for(int y = 0;y < mdp.getHeight();y++){
				totalV += Vvalues[x][y];
			}
		}
		return totalV;
	}
	
	private void setNewQValue(int xpos, int ypos, Action a){
		
		double total = 0;
			mdp.setState(xpos, ypos);
			double sum1 = getTransitionChance(xpos, ypos, a, xpos-1, ypos);
			if(mdp.getField(xpos-1, ypos) != Field.OUTOFBOUNDS && mdp.getField(xpos-1,  ypos) != Field.OBSTACLE)
				mdp.setState(xpos-1, ypos);
			sum1 = sum1 * (mdp.getReward() + gamma*Vvalues[mdp.getStateXPosition()][mdp.getStateYPostion()]);
			total += sum1;
				
			mdp.setState(xpos, ypos);
			double sum2 = getTransitionChance(xpos, ypos, a, xpos+1, ypos);
			if(mdp.getField(xpos+1, ypos) != Field.OUTOFBOUNDS && mdp.getField(xpos+1,  ypos) != Field.OBSTACLE)
				mdp.setState(xpos+1, ypos);
			sum2 = sum2 * (mdp.getReward() + gamma*Vvalues[mdp.getStateXPosition()][mdp.getStateYPostion()]);
			total += sum2;
				
			mdp.setState(xpos, ypos);
			double sum3 = getTransitionChance(xpos, ypos, a, xpos, ypos-1);
			if(mdp.getField(xpos, ypos-1) != Field.OUTOFBOUNDS && mdp.getField(xpos,  ypos-1) != Field.OBSTACLE)
				mdp.setState(xpos, ypos-1);
			sum3 = sum3 * (mdp.getReward() + gamma*Vvalues[mdp.getStateXPosition()][mdp.getStateYPostion()]);
			total += sum3;
				
			mdp.setState(xpos, ypos);
			double sum4 = getTransitionChance(xpos, ypos, a, xpos, ypos+1);
			if(mdp.getField(xpos, ypos+1) != Field.OUTOFBOUNDS && mdp.getField(xpos,  ypos+1) != Field.OBSTACLE)
				mdp.setState(xpos, ypos+1);
			sum4 = sum4 * (mdp.getReward() + gamma*Vvalues[mdp.getStateXPosition()][mdp.getStateYPostion()]);
			total += sum4;
			
				
		Qvalues[xpos][ypos][a.ordinal()] = total;	
	}
	
	private double getTransitionChance(int initx, int inity, Action a, int resultx, int resulty){
		if(initx == resultx && inity == resulty){
			return mdp.pNoStep;
		}
		if(a == Action.UP){
			if(inity < resulty){
				return mdp.pPerform;
			}
			if(inity > resulty){
				return mdp.pBackstep;
			}
			if(initx != resultx){
				return mdp.pSidestep;
			}
		}
		if(a == Action.DOWN){
			if(inity > resulty){
				return mdp.pPerform;
			}
			if(inity < resulty){
				return mdp.pBackstep;
			}
			if(initx != resultx){
				return mdp.pSidestep;
			}
		}
		if(a == Action.LEFT){
			if(initx > resultx){
				return mdp.pPerform;
			}
			if(initx < resultx){
				return mdp.pBackstep;
			}
			if(inity != resulty){
				return mdp.pSidestep;
			}
		}
		if(a == Action.RIGHT){
			if(initx < resultx){
				return mdp.pPerform;
			}
			if(initx > resultx){
				return mdp.pBackstep;
			}
			if(inity != resulty){
				return mdp.pSidestep;
			}
		}
		return 0;
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
	}
	
	public void setMaxIterationDelta(double maxIterationDelta) {
		this.maxIterationDelta = maxIterationDelta;
	}
	

}
