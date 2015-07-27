package tests;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import spml3.Pair;
import spml3.ProbabilityMap;

public class ProbabilityMapTest {
	private ProbabilityMap map;
	private Pair[] q1 = {new Pair("tampering", "true"), new Pair("fire", "true")};
	private Pair[] q2 = {new Pair("tampering", "false"), new Pair("fire", "true")};
	private Pair[] q3 = {new Pair("tampering", "true"), new Pair("fire", "false")};
	private Pair[] q4 = {new Pair("tampering", "false"), new Pair("fire", "false")};

	@Before
	public void setUp() throws Exception {
		map = new ProbabilityMap("alarm");
		
		map.addProbability(q1, 0.05);
		map.addProbability(q2, 0.5);
		map.addProbability(q3, 0.25);
		map.addProbability(q4, 0.2);
		
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void removeObservedTest() {
		ProbabilityMap m2 = (ProbabilityMap) map.clone();
		m2.removeObserved(new Pair("tampering", "true"));
		assert(!m2.containsKey(q2));
		
	}

}
