package com.usc.social.network.graph;

import com.usc.social.network.graph.Node;

public class CostNode {
	private Node node;

	private CostNode parentNode;

	private int totalPathCost ;
	
	private int totalRiskFactor;

	public CostNode(CostNode parentNode, int totalPathCost, int totalRiskFactor, Node node){
		this.parentNode = parentNode;
		this.totalPathCost = totalPathCost;
		this.totalRiskFactor = totalRiskFactor;
		this.node = node;
	}

	public CostNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(CostNode parentNode) {
		this.parentNode = parentNode;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public int getTotalPathCost() {
		return totalPathCost;
	}

	@Override
	public String toString() {
		String toString = "PathCostNode [node=" + node.getUserName() + ", parentNode="; 

		if(parentNode != null){
			toString += parentNode.getNode().getUserName();
		}else{
			toString += null;
		}
		
		toString += ", totalPathCost=" + totalPathCost + "]";
		
		return toString;
	}

	public void setTotalPathCost(int totalPathCost) {
		this.totalPathCost = totalPathCost;
	}

	public int getTotalRiskFactor() {
		return totalRiskFactor;
	}

	public void setTotalRiskFactor(int totalRiskFactor) {
		this.totalRiskFactor = totalRiskFactor;
	}
}
