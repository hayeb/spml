package nl.ru.ai.vroon.mdp;

/**
 * Artificial Intelligence: Search, Planning and Machine Learning
 * Task 4: deadline 24/06/15
 * 
 * @author Haye Bohm -- s4290402
 * @author Wesley van Hoorn -- s4018044 
 */

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class ValueIIterationLearner {

	private MarkovDecisionProblem mdp;
	private final double discount_rate = 0.9;
	private final double q0 = 0.0, v0 = 0.0;
	private int k = 0;
	private double[][][] qvalues;
	private double[][] vvalues;
	private double[][] next_vvalues;
	
	private double error_boundary = 0.01;

	public ValueIIterationLearner(MarkovDecisionProblem mdp) {
		this.mdp = mdp;
		mdp.setDeterministic();
		qvalues = new double[mdp.getWidth()][mdp.getHeight()][Action.values().length];
		vvalues = new double[mdp.getWidth()][mdp.getHeight()];
		next_vvalues = new double[mdp.getWidth()][mdp.getHeight()];
		vInit();
		setGoalPosition();
	}

	public void generatePolicy() {
		while (true) {
			this.qvalues = qUpdate();
			this.next_vvalues = vUpdate();
			System.out.println("Error: " + calculateError());
			if (calculateError() < error_boundary) {
				mdp.addPolicy(getPolicy());
				mdp.setPolicyAdded(true);
				mdp.drawMDP();
				break;
			}
			setVValues();
		}
		System.out.println("I HAVE FOUND THE SOLUTION");
	}
	
	private Action[][] getPolicy() {
		Action[][] policy = new Action[mdp.getWidth()][mdp.getHeight()];
		for (int i = 0; i < mdp.getWidth(); i++) {
			for (int j = 0; j < mdp.getHeight(); j++) {
				double max = Double.MIN_VALUE;
				Action maxaction = null;
				for (int h = 0; h < Action.values().length; h++) {
					System.out.println("Max: " + qvalues[i][j][h]);
					if (Double.compare(qvalues[i][j][h], max) <= 0) {
						max = qvalues[i][j][h];
						maxaction = Action.values()[h];
					}
				}
				policy[i][j] = maxaction;
			}
		}
		return policy;
	}
	
	private void setVValues() {
		for (int i = 0; i < mdp.getWidth(); i++) {
			for (int j = 0; j < mdp.getHeight(); j++) {
				vvalues[i][j] = next_vvalues[i][j];
				// Just to be sure..
				next_vvalues[i][j] = 0;
			}
		}
	}

	
	private double calculateError() {
		double error = 0.0;
		for (int i = 0; i < mdp.getWidth(); i++) {
			for (int j = 0; j < mdp.getHeight(); j++) {
				System.out.println(Math.abs(next_vvalues[i][j] - vvalues[i][j]));
				error += Math.abs(next_vvalues[i][j] - vvalues[i][j]);
			}
		}
		
		
		return error / mdp.getWidth() * mdp.getHeight();
	}
	/**
	 * Initialize the V values to 0.
	 */
	private void vInit() {
		for (int i = 0; i < mdp.getWidth(); i++) {
			for (int j = 0; j < mdp.getHeight(); j++) {
				vvalues[i][j] = 0.0;
			}
		}
	}

	/**
	 * Finds and sets the Q and V values of all goal states to 0.
	 */
	private void setGoalPosition() {
		for (int i = 0; i < mdp.getWidth(); i++) {
			for (int j = 0; j < mdp.getHeight(); j++) {
				if (mdp.getField(i, j) == Field.REWARD) {
					vvalues[i][j] = 0.0;
					qvalues[i][j][0] = 0;
					qvalues[i][j][1] = 0;
					qvalues[i][j][2] = 0;
					qvalues[i][j][3] = 0;
				}
			}
		}
	}

	/**
	 * Update Q values for all states and actions.
	 * 
	 * @return qvalues
	 */
	private double[][][] qUpdate() {
		for (int i = 0; i < mdp.getWidth(); i++) {
			for (int j = 0; j < mdp.getHeight(); j++) {
				mdp.setState(i, j);
				if (mdp.getField(i, j) == Field.OBSTACLE || mdp.getField(i, j) == Field.OUTOFBOUNDS) {
					break;
				}
				for (Action a : Action.values()) {
					Action b1 = Action.nextAction(a);
					Action b2 = Action.previousAction(a);
					Action b3 = Action.backAction(a);

					// Intended action
					System.out.println("Performing action a: " + a + " in " + i + "," + j);
					double R = mdp.performAction(a);
					double V = vvalues[mdp.getStateXPosition()][mdp
							.getStateYPosition()];
					mdp.setState(i, j);
					System.out.println("Performing action b1: " + b1 + " in " + i + "," + j);
					double R1 = mdp.performAction(b1);
					double V1 = vvalues[mdp.getStateXPosition()][mdp
							.getStateYPosition()];
					mdp.setState(i, j);
					System.out.println("Performing action b2:" + b2 + " in " + i + "," + j);
					double R2 = mdp.performAction(b2);
					double V2 = vvalues[mdp.getStateXPosition()][mdp
							.getStateYPosition()];
					mdp.setState(i, j);
					System.out.println("Performing action b3: " + b3 + " in " + i + "," + j);
					double R3 = mdp.performAction(b3);
					double V3 = vvalues[mdp.getStateXPosition()][mdp
							.getStateYPosition()];

					// Sum all the rewards of the action, and it's stochastic side effects together.
					qvalues[i][j][a.ordinal()] = mdp.getpPerform()
							* (R + discount_rate * V) + mdp.getpSidestep()
							* (R1 + discount_rate * V1) + mdp.getpSidestep()
							* (R2 + discount_rate * V2) + mdp.getpBackstep()
							* (R3 + discount_rate * V3);

				}
			}
		}
		return qvalues;
	}

	/**
	 * Computes the new value function for all states.
	 * 
	 * @return vvalues
	 */
	private double[][] vUpdate() {		
		for (int i = 0; i < mdp.getWidth(); i++) {
			for (int j = 0; j < mdp.getHeight(); j++) {
				if (!(mdp.getField(i, j) == Field.REWARD)) {
					List b = Arrays.asList(qvalues[i][j]);
					double max = Integer.MIN_VALUE;
					for (int h = 0; h < Action.values().length; h++) {
						if (qvalues[i][j][h] > max) {
							max = qvalues[i][j][h];
						}
					}
					next_vvalues[i][j] = max;
				}
			}

		}
		return next_vvalues;
	}

	/**
	 * 
	 * @param x
	 *            coordinate of the new state
	 * @param y
	 *            coordinate of the new state
	 * @return FALSE when the agent has changed coordinates, TRUE when the agent
	 *         has not moved.
	 */
	private boolean haveMoved(int x, int y) {
		return !(x == mdp.getStateXPosition() && y == mdp.getStateYPosition());
	}
}
