


import java.util.Comparator;


public class HeuristicRiskCostComparator implements Comparator<EdgeCost> {

	public int compare(EdgeCost o1, EdgeCost o2) {
		return o1.getTotalHeuristicRiskCost() < o2.getTotalHeuristicRiskCost() ? -1 : o1.getTotalHeuristicRiskCost() == o2.getTotalHeuristicRiskCost() ? 0 : 1;
	}

}
