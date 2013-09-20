package com.usc.social.network.search.uniform.cost;

import java.util.Comparator;

import com.usc.social.network.graph.CostNode;

public class PathCostComparator implements Comparator<CostNode> {

	public int compare(CostNode o1, CostNode o2) {
		return o1.getTotalPathCost() < o2.getTotalPathCost() ? -1 : o1.getTotalPathCost() == o2.getTotalPathCost() ? 0 : 1;
	}

}
