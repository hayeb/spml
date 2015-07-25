package tests;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import spml3.BIFParser;

/**
 * Test class for the BIF parser. Not finished.
 * @author haye
 *
 */
public class BifParserTest {
	
	
	// TODO: Add some way to check automatically if the parsing has completed.
	@Test
	public void bifParseTest() {
		BIFParser bf = new BIFParser();
		File f = new File("wheather.bif");
		try {
			bf.parse(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
