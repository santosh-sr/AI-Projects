package com.usc.social.network.search.uniform.risk;

import java.util.Comparator;

import com.usc.social.network.graph.CostNode;

public class RiskFactorComparator implements Comparator<CostNode> {

	public int compare(CostNode o1, CostNode o2) {
		return o1.getTotalRiskFactor() < o2.getTotalRiskFactor() ? -1 : o1.getTotalRiskFactor() == o2.getTotalRiskFactor() ? 0 : 1;
	}

}
