package spml3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author haye
 *
 */
public class BIFParser {

	/**
	 * Regular expression for finding a single probability table entry within a probability
	 * entry.
	 */
	private final String TABLE_REGEX = "\\stable\\s\\d\\.\\d{1,}\\,";
	private Pattern TABLE_PATTERN;
	
	private final String ROW_REGEX = "\\([TF][^\\n]*\\;";
	private Pattern ROW_PATTERN;

	/**
	 * Regular expression for finding variable entries in the .bif file.
	 */
	private final String NAME_REGEX = "\\s\\(.*?(\\)|\\s\\|)";
	private Pattern NAME_PATTERN;

	/**
	 * Regular expression for finding probability entries in the .bif file
	 */
	private final String PROBABILITY_REGEX = "^probability.*?\\{.*?^\\}";
	private Pattern PROBABILITY_PATTERN;

	/**
	 * Constructor for the parser.
	 */
	public BIFParser() {
		TABLE_PATTERN = Pattern.compile(TABLE_REGEX);
		NAME_PATTERN = Pattern.compile(NAME_REGEX, Pattern.DOTALL);
		PROBABILITY_PATTERN = Pattern.compile(PROBABILITY_REGEX, Pattern.DOTALL | Pattern.MULTILINE);
		ROW_PATTERN = Pattern.compile(ROW_REGEX, Pattern.DOTALL);
	}

	/**
	 * Takes a .bif file, and returns a BeliefNetwork with the same structure.
	 * Expects the .bif file to be from AISpace. ONly accepts files which have
	 * only boolean variables.
	 * 
	 * @param f
	 * 			File from which to parse the .bif file.
	 * @return
	 * @throws IOException
	 */
	public BeliefNetwork parse(File f) throws IOException {
		String stringContents = readFile(f);
		BeliefNetwork bn = new BeliefNetwork();
		addNodes(stringContents, bn);
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
	 * Adds parent to the parameter node from the probability entry. 
	 * @param node
	 * 			Node to add the parents to
	 * @param probabilityEntry
	 * 			String from which to read the parents from.
	 */
	private void addParents(BeliefNode node, String probabilityEntry) {
		final String pattern = "\\|\\s.*\\)\\s\\{";
		Pattern p = Pattern.compile(pattern, Pattern.DOTALL | Pattern.MULTILINE);
		Matcher m = p.matcher(probabilityEntry);
		if (m.find()) {
			String found = probabilityEntry.substring(m.start() + 2, m.end() - 3);
			String[] parent = found.split(", ");
			for (String st : parent) {
				node.addParent(st);
			}
		}
	}
	
	/**
	 * Adds the probability from a single-row table to the parameter node.
	 * @param bnode
	 * @param entry
	 */
	private void addSingleTableEntry(BeliefNode bnode, String entry) {
		Matcher tableMatcher = TABLE_PATTERN.matcher(entry);
		if (tableMatcher.find()) {
			bnode.addProbability(new Pair[0],
					Double.parseDouble(entry.substring(tableMatcher.start() + 7, tableMatcher.end() - 1)));
		}
	}
	
	/**
	 * Adds the probabilities from a multi-row table to the parameter node.
	 * @param bnode
	 * @param entry
	 * @param names
	 */
	private void addMultipleTableEntries(BeliefNode bnode, String entry, ArrayList<String> names) {
		Matcher tableMatcher = ROW_PATTERN.matcher(entry);
		while (tableMatcher.find()) {
			String row = entry.substring(tableMatcher.start(), tableMatcher.end());
			row = row.replaceAll("[\\(\\)\\;\\,]", "");
			String[] splitted = row.split("\\s");
			Pair[] pairs = new Pair[names.size()];
			int i = 0 ;
			for (; i < names.size(); i++) {
				pairs[i] = new Pair(names.get(i), splitted[i]);
			}
			bnode.addProbability(pairs, Double.parseDouble(splitted[i]));
			
		}
	}

	/**
	 * Finds all probability entries in the .bif file and creates a node for the
	 * entry.
	 * 
	 * @param node
	 * @param probabilities
	 */
	private void addNodes(String contents, BeliefNetwork bn) {
		Matcher probMatcher = PROBABILITY_PATTERN.matcher(contents);
		while (probMatcher.find()) {
			
			String entry = contents.substring(probMatcher.start(), probMatcher.end());
			Matcher nameMatcher= NAME_PATTERN.matcher(entry);
			if (nameMatcher.find()) {
				String found = entry.substring(nameMatcher.start(), nameMatcher.end());
				
				// get rid of all non alphabet characters
				String name = found.replaceAll("\\P{L}+", "");
				BeliefNode bnode = new BeliefNode(name);
				
				System.out.print("Found node: " + name + "\n");
				addParents(bnode, entry);

				if (bnode.numberOfParents() == 0) {
					addSingleTableEntry(bnode, entry);
				} else {
					// Extract all variable names.
					ArrayList<String> names = bnode.getParents();
					addMultipleTableEntries(bnode, entry, names);
				}
				bn.addNode(bnode);
			}
		}
		System.out.println("Done parsing!");
	}
}
