package spml3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BIFParser {

	public BIFParser() {

	}

	/**
	 * Takes a .bif file, and returns a BeliefNetwork with the same structure.
	 * Expects the .bif file to be from AISpace. ONly accepts files which have only boolean variables.
	 * @author Haye
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public BeliefNetwork parse(File f) throws IOException {
		// Fill a String with the contents of the file..
		String stringContents = readFile(f);

		ArrayList<String> names = fillNames(stringContents);
		ArrayList<String> probabilities = fillProbabilities(stringContents);

		BeliefNetwork bn = new BeliefNetwork();
		for (String name : names) {
			BeliefNode bnode = new BeliefNode(findName(name));
			addProbabilities(bnode, probabilities);
			bn.addNode(bnode);
		}
		return bn;
	}

	/**
	 * Reads in a file and returns a string representation of it.
	 * 
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
	 * 
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
			names.add(contents.substring(m1.start(), m1.end()));
		}
		return names;
	}

	/**
	 * Fills a list with the separate name-attributes in the contents string
	 *  
	 * @param contents
	 * @return
	 */
	private ArrayList<String> fillProbabilities(String contents) {
		ArrayList<String> probabilities = new ArrayList<String>();
		final String PROBABILITY_REGEX = "^probability.*?\\{.*?^\\}";
		int flags = Pattern.MULTILINE | Pattern.DOTALL;
		Pattern nodeDistribution = Pattern.compile(PROBABILITY_REGEX, flags);
		Matcher m2 = nodeDistribution.matcher(contents);
		while (m2.find()) {
			probabilities.add(contents.substring(m2.start(), m2.end()));
		}
		return probabilities;
	}

	/**
	 * Takes a string with a name attribute form the .bif file and extracts the
	 * name of the variable.
	 * 
	 * @param name
	 * @return
	 */
	private String findName(String name) {
		final String pattern = "^variable.*\\{$";
		Pattern p = Pattern.compile(pattern, Pattern.MULTILINE);
		Matcher m = p.matcher(name);
		m.find();

		// Remove some characters from the string
		return name.substring(m.start() + 9, m.end() - 2);
	}

	/**
	 * Finds the correct probability table from the probabilities list.
	 * 
	 * @param name
	 * @return
	 */
	private String findProbability(String name, ArrayList<String> probabilities) {
		final String pattern = "^probability\\s\\(" + name;
		Pattern p = Pattern.compile(pattern, Pattern.MULTILINE);
		for (String s : probabilities) {
			Matcher m = p.matcher(s);
			if (m.find()) {
				return s;
			}
		}
		return null;
	}

	private void addParents(BeliefNode node, String probabilityEntry) {
		final String pattern = "\\|\\s.*\\)\\s\\{";
		Pattern p = Pattern.compile(pattern, Pattern.DOTALL | Pattern.MULTILINE);
		Matcher m = p.matcher(probabilityEntry);
		if (m.find()) {
			String found = probabilityEntry.substring(m.start()+2, m.end() - 3);
			String[] parent = found.split(", ");
			for (String st : parent) {
				node.addParent(st);
				// Perhaps store references instead of string names?
			}
		}
	}

	/**
	 * Finds the corresponding probability table to the node and extracts it.
	 * @param node
	 * @param probabilities
	 */
	private void addProbabilities(BeliefNode node, ArrayList<String> probabilities) {
		String corresponding = findProbability(node.getName(), probabilities);
		addParents(node, corresponding);
		
		if (node.numberOfParents() == 0) { // Extract the true probability
			String TABLE_PATTERN = "\\stable\\s\\d\\.\\d{1,}\\,";
			Pattern p = Pattern.compile(TABLE_PATTERN);
			Matcher m = p.matcher(corresponding);
			if (m.find()) {
				node.addProbability(null, Double.parseDouble(corresponding.substring(m.start() + 7, m.end() - 1)));
			}
		} else {
			String ROW_REGEX = "\\([TF][^\\n]*\\;";
			Pattern p = Pattern.compile(ROW_REGEX, Pattern.DOTALL);
			Matcher m = p.matcher(corresponding);
			while (m.find()) {
				String[] matches = corresponding.substring(m.start(), m.end()).trim().split("[\\(\\)(\\,\\s)]");
				ArrayList<Boolean> query = new ArrayList<Boolean>();
				Double d = -1.0;
				for (String match : matches) {
					if (!match.isEmpty()) {
						if (match.equals("T")) {
							query.add(true);
						} else if (match.equals("F")) {
							query.add(false);
						} else if (match.matches("\\d\\.\\d{1,}") && (d.equals(-1.0))) {
							d = Double.parseDouble(match);
						}
					}
				}
				node.addProbability(query, d);
			}
			
		}
		
	}
}
