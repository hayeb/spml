/**
 * 
 */
package spml3;

import java.util.Map;

/**
 * @author mmwvh
 *
 */
public class Factor {
	private String[] variableNames;
	private Map<boolean[], Double> probabilities;

	/**
	 * heeft een lijst met parents per node : factor
	 */
	public Factor(BeliefNode beliefnode) {
		variableNames = new String[beliefnode.getParentNames().length+1];
		variableNames[0] = beliefnode.getName();
		for(int i = 0; i < beliefnode.getParentNames().length; i++){
			variableNames[i+1] = beliefnode.getParentNames()[i];
		}
		
	}

	/**
	 * Geeft een lijst met factoren waar de to be eliminated variable voorkomt en verwijdert deze uit de factor lijst
	 * @return lijst met probability tabellen die een parent bevat die gelijk is aan to be eliminated variable
	 */
	public lijst tbMultiplied() {
		for alle nodes
			for alle parents
				gelijk aan to be eliminated variable?
						voeg toe aan lijst
						verwijder uit factor lijst
						
		return lijst
		
	}
	
	
	/**
	 * 
	 */
	public void addFactor() {
		
	}
	
}
