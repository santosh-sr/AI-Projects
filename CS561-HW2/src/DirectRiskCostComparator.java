


import java.util.Comparator;


public class DirectRiskCostComparator implements Comparator<EdgeCost> {

	public int compare(EdgeCost o1, EdgeCost o2) {
		return o1.getNode().getDirectRiskCost() < o2.getNode().getDirectRiskCost() ? -1 : o1.getNode().getDirectRiskCost() == o2.getNode().getDirectRiskCost() ? 0 : 1;
	}

}
