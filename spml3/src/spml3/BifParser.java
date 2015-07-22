package spml3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BifParser {

	public BifParser() {
		
	}
	
	/**
	 * Takes a .bif file, and returns a BeliefNetwork with the same structure.
	 * @param f
	 * @return
	 * @throws IOException 
	 */
	public BeliefNetwork parse(File f) throws IOException {
		// Fill a String with the contents of the file..
		String stringContents = readFile(f);
		
		ArrayList<String> names = fillNames(stringContents);
		ArrayList<String> probabilities = fillProbabilities(stringContents);
		
		BeliefNetwork bn = new BeliefNetwork() ;
		for (String name : names) {
			BeliefNode bnode = new BeliefNode(findName(name));
			addProbabilities(bnode, probabilities);
			bn.addNode(bnode);
		}
		
		return bn;
	}
	
	/**
	 * Reads in a file and returns a string representation of it.
	 * @param f
	 * @return
	 * @throws IOException
	 */
	private String readFile(File f) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		StringBuilder contents = new StringBuilder();
		while (reader.ready()) {
			contents.append(reader.readLine() + "\n");
		}
		reader.close();
		return contents.toString();
	}
	
	/**
	 * Fills a list with the name-attributes in the contents string
	 * @param contents
	 * @return
	 */
	private ArrayList<String> fillNames(String contents) {
		ArrayList<String> names = new ArrayList<String>();
		final String VARIABLE_REGEX = "^variable.*\\{\\s.*\\s.*\\s\\}";
		int flags = Pattern.MULTILINE;
		Pattern nodeName = Pattern.compile(VARIABLE_REGEX, flags);
		Matcher m1 = nodeName.matcher(contents);
		while (m1.find()) {
			System.out.println("Found match at character " + m1.start() + "to" + m1.end());
			System.out.print("Contents:\n" + contents.substring(m1.start(), m1.end()) + "\n");
			names.add(contents.substring(m1.start(), m1.end()));
		}
		return names;
	}
	
	/**
	 * Fills a list with the name-attributes in the contents string
	 * @param contents
	 * @return
	 */
	private ArrayList<String> fillProbabilities(String contents) {
		ArrayList<String> probabilities = new ArrayList<String>();
		final String PROBABILITY_REGEX = "^probability.*\\{\\s.*\\s.*\\s\\}";
		int flags = Pattern.MULTILINE;
		Pattern nodeDistribution = Pattern.compile(PROBABILITY_REGEX, flags);		
		Matcher m2 = nodeDistribution.matcher(contents);
		while (m2.find()) {
			System.out.println("Found match at character " + m2.start() + " to " + m2.end());
			System.out.print("Contents:\n" + contents.substring(m2.start(), m2.end()) + "\n");
			probabilities.add(contents.substring(m2.start(), m2.end()));
		}
		return probabilities;
	}
	
	private String findName(String name) {
		final String pattern = "^variable.*\\{$";
		Pattern p = Pattern.compile(pattern, Pattern.MULTILINE);
		Matcher m = p.matcher(name);
		m.find();
		
		// Remove some characters from the string
		return name.substring(m.start() + 9, m.end() - 2);
	}
	
	private void addProbabilities(BeliefNode node, ArrayList<String> probabilities) {
		
		
	}
}
