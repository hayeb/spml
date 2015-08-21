package spml3;

import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		BIFParser bp = new BIFParser();
		try {
			BeliefNetwork bn = bp.parse(new File("fire.bif"));
			bn.calculateProbability("leaving", new Pair[] {new Pair("fire", "T"), new Pair("tampering", "F")});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
