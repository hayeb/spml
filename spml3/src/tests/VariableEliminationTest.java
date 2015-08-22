package tests;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import spml3.BIFParser;
import spml3.BeliefNetwork;
import spml3.Pair;

public class VariableEliminationTest {
	private BeliefNetwork bn;

	@Before
	public void setUp() throws Exception {
		BIFParser bp = new BIFParser();
		bn = bp.parse(new File("fire.bif"));
		System.out.print("Setup done.\n");
	}

	@Test
	public void test() {
		// Test the fire network
		Pair[] pairs = {new Pair("alarm", "T"), new Pair("tampering", "F")};
		bn.calculateProbability(new Pair("report", "T"), pairs);
		
	}

}
