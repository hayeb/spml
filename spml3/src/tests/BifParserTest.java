package tests;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import spml3.BIFParser;

public class BifParserTest {
	
	@Test
	public void bifParseTest() {
		BIFParser bf = new BIFParser();
		File f = new File("fire.bif");
		try {
			bf.parse(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
