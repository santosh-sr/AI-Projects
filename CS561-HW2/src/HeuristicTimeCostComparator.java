


import java.util.Comparator;


public class HeuristicTimeCostComparator implements Comparator<EdgeCost> {

	public int compare(EdgeCost o1, EdgeCost o2) {
		return o1.getTotalHeuristicTimeCost() < o2.getTotalHeuristicTimeCost() ? -1 : o1.getTotalHeuristicTimeCost() == o2.getTotalHeuristicTimeCost() ? 0 : 1;
	}

}
