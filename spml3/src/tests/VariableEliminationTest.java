package tests;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import spml3.BIFParser;
import spml3.BeliefNetwork;

public class VariableEliminationTest {
	private BeliefNetwork bn;

	@Before
	public void setUp() throws Exception {
		BIFParser bp = new BIFParser();
		bn = bp.parse(new File("fire.bif"));
		System.out.print("Setup done.'n");
	}

	@Test
	public void test() {
		// Test the fire network
		ArrayList<Boolean> parents = new ArrayList<Boolean>();
		parents.add(true);
		parents.add(null);
		parents.add(null);
		parents.add(null);
		parents.add(null);
		parents.add(null);
		bn.calculateProbability("report", parents);
		
	}

}
