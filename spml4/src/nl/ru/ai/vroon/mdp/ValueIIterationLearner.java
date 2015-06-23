package nl.ru.ai.vroon.mdp;

/**
 * Artificial Intelligence: Search, Planning and Machine Learning Task 4:
 * deadline 24/06/15
 * 
 * @author Haye Bohm -- s4290402
 * @author Wesley van Hoorn -- s4018044
 */

public class ValueIIterationLearner {

	private MarkovDecisionProblem mdp;
	private final double discount_rate = 0.9;
	private double[][][] qvalues;
	private double[][] vvalues;
	private double[][] next_vvalues;

	private double error_boundary = 1;

	public ValueIIterationLearner(MarkovDecisionProblem mdp) {
		this.mdp = mdp;
		mdp.setNegReward(Double.MIN_VALUE);
		mdp.setDeterministic();
		qvalues = new double[mdp.getWidth()][mdp.getHeight()][Action
				.values().length];
		vvalues = new double[mdp.getWidth()][mdp.getHeight()];
		next_vvalues = new double[mdp.getWidth()][mdp.getHeight()];
		setGoalPosition();
	}

	public void generatePolicy() {
		while (true) {
			this.qvalues = qUpdate();
			this.next_vvalues = vUpdate();
			System.out.println("Error: " + calculateError());
			if (calculateError() < error_boundary) {
				break;
			}
			setVValues();
		}
		mdp.addPolicy(getPolicy());
		mdp.setPolicyAdded(true);
		mdp.drawMDP();
		System.out.println("I HAVE FOUND THE SOLUTION");
	}

	private Action[][] getPolicy() {
		Action[][] policy = new Action[mdp.getWidth()][mdp.getHeight()];
		for (int i = 0; i < mdp.getWidth(); i++) {
			for (int j = 0; j < mdp.getHeight(); j++) {
				double max = Double.MIN_VALUE;
				Action maxaction = null;
				for (int h = 0; h < Action.values().length; h++) {
					System.out.println("Max: "
							+ qvalues[i][j][h]);
					if (Double.compare(qvalues[i][j][h],
							max) < 0) {
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
				System.out.println(Math.abs(next_vvalues[i][j]
						- vvalues[i][j]));
				error += Math.abs(next_vvalues[i][j]
						- vvalues[i][j]);
			}
		}

		return error / mdp.getWidth() * mdp.getHeight();
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
				// Set the mdp to the desired state..
				mdp.setState(i, j);

				// If the current field we're looking at is out
				// of bounds or an obstacle, don't do anything.
				if (mdp.getField(i, j) == Field.OBSTACLE
						|| mdp.getField(i, j) == Field.OUTOFBOUNDS) {
					break;
				} else if (mdp.getField(i, j) == Field.NEGREWARD) {
					qvalues[i][j] = new double[] {
							Integer.MIN_VALUE,
							Integer.MIN_VALUE,
							Integer.MIN_VALUE,
							Integer.MIN_VALUE };
				}

				// For every action possible in this state...
				for (Action a : Action.values()) {
					double utility = 0.0;
					Action[] actionlist = {
							a,
							Action.nextAction(a),
							Action.previousAction(a),
							Action.backAction(a) };

					// Calculate the possible outcomes of
					// the action, sum it together and add
					// that ad the value of the current
					// action.
					for (Action act : actionlist) {
						double R = mdp.performAction(act);
						mdp.setTerminated(false);
						System.out.print("R( " + mdp.getStateXPosition()+ "," + mdp.getStateYPosition() + "): " + R + ",\ndoing action: " + act + "\n");
						double V = 0.0;
						if (!(mdp.getStateXPosition() == i && mdp
								.getStateYPosition() == j)) {
							V = vvalues[mdp.getStateXPosition()][mdp
									.getStateYPosition()];
						}

						if (Action.nextAction(a) == act) {
							utility += mdp.getpSidestep()
									* (R + discount_rate
											* V);
						} else if (Action
								.previousAction(a) == act) {
							utility += mdp.getpSidestep()
									* (R + discount_rate
											* V);
						} else if (Action.backAction(a) == act) {
							utility += mdp.getpBackstep()
									* (R + discount_rate
											* V);
						} else {
							utility += mdp.getpPerform()
									* (R + discount_rate
											* V);
						}
						mdp.setState(i, j);
					}
					qvalues[i][j][a.ordinal()] = utility;
				}
			}
		}
		return qvalues;
	}

	/**
	 * Computes the new values for all states.
	 * 
	 * @return vvalues
	 */
	private double[][] vUpdate() {
		for (int i = 0; i < mdp.getWidth(); i++) {
			for (int j = 0; j < mdp.getHeight(); j++) {
				if (!(mdp.getField(i, j) == Field.REWARD)) {
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
}
