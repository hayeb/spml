package spml3;

import java.util.Comparator;

public class PairComparator implements Comparator<Pair> {

	

	@Override
	public int compare(Pair o1, Pair o2) {
		if(o1.getState().equals("true") && o2.getState().equals("false"))
			return -1;
		if(o2.getState().equals("true") && o1.getState().equals("false"))
			return 1;
		//ordering code not correctly written, rewrite please
			
		return 0;
	}

}
