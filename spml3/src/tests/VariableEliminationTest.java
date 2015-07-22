package tests;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import spml3.BeliefNetwork;
import spml3.BifParser;

public class VariableEliminationTest {
	private BeliefNetwork bn;

	@Before
	public void setUp() throws Exception {
		BifParser bp = new BifParser();
		bn = bp.parse(new File("fire.bif"));
	}

	@Test
	public void test() {
		
	}

}
