

import java.util.Comparator;


public class DirectTimeCostComparator implements Comparator<EdgeCost> {

	public int compare(EdgeCost o1, EdgeCost o2) {
		return o1.getNode().getDirectTimeCost() < o2.getNode().getDirectTimeCost() ? -1 : o1.getNode().getDirectTimeCost() == o2.getNode().getDirectTimeCost() ? 0 : 1;
	}

}
