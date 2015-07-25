/**
 * 
 */
package spml3;

/**
 * @author mmwvh
 *
 */
public class Factor {
	private String[] variableNames;
	
	// Use this to store the probabilities for this factor.
	private final DataStructuur probabilities;
	
	private boolean observed;

	/**
	 * heeft een lijst met parents per node : factor
	 */
	public Factor(BeliefNode beliefnode) {
		variableNames = new String[beliefnode.numberOfParents()+1];
		variableNames[0] = beliefnode.getName();
		for(int i = 0; i < beliefnode.numberOfParents(); i++){
			variableNames[i+1] = beliefnode.getParents().get(i);
		}
		
		probabilities = new ProbabilityMap();
	}
	
	public void eliminateVariable(String e) {
		String[] newVN;
		for (int i =0; i < variableNames.length; i++) {
			if (variableNames[i] != e) {
				
			}
		}
	}
	
	public static Factor multiplyFactor(Factor A, Factor B) {
		
		
		return B;
	}

//	/**
//	 * Geeft een lijst met factoren waar de to be eliminated variable voorkomt en verwijdert deze uit de factor lijst
//	 * @return lijst met probability tabellen die een parent bevat die gelijk is aan to be eliminated variable
//	 */
//	public lijst tbMultiplied() {
//		for alle nodes
//			for alle parents
//				gelijk aan to be eliminated variable?
//						voeg toe aan lijst
//						verwijder uit factor lijst
//						
//		return lijst
//		
//	}
	
	
	/**
	 * 
	 */
	public void addFactor() {
		
	}
	
}
