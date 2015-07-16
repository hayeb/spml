package tests;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import spml3.BifParser;

public class BifParserTest {
	
	@Test
	public void bifParseTest() {
		BifParser bf = new BifParser();
		File f = new File("fire.bif");
		try {
			bf.parse(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
