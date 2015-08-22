package spml3;

import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		BIFParser bp = new BIFParser();
		try {
			Pair query = new Pair("alarm", "T");
			BeliefNetwork bn = bp.parse(new File("fire.bif"));
			Double prob = bn.calculateProbability(query, new Pair[] {new Pair("fire", "F"), new Pair("tampering", "F")});
			System.out.println("The probability of " + query + " = " + prob);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
