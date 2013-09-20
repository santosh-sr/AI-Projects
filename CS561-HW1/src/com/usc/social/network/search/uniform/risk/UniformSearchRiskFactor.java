package com.usc.social.network.search.uniform.risk;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import com.usc.social.network.graph.CostNode;
import com.usc.social.network.graph.Node;
import com.usc.social.network.search.Search;

public class UniformSearchRiskFactor extends Search{
	private PriorityQueue<CostNode> priorityQueue;

	public UniformSearchRiskFactor(Map<String, List<Node>> graphStructure){
		super(graphStructure);
		priorityQueue = new PriorityQueue<CostNode>(100, new RiskFactorComparator());
	}

	private CostNode getOptimalRiskFactor(Node goalNode){
		if(priorityQueue.isEmpty()){
			return null;
		}else{
			while(!priorityQueue.isEmpty()){
				CostNode rootRiskCostNode = priorityQueue.poll();
				Node rootNode = rootRiskCostNode.getNode();
				String userName = rootNode.getUserName();

				visitedNodes.add(userName);
				if(userName.equals(goalNode.getUserName())){
					return rootRiskCostNode;
				}

				for(Node childNode : graphStructure.get(userName)){
					String childUserName = childNode.getUserName();
					if(!visitedNodes.contains(childUserName)){
						
						int totalRiskFactor = childNode.getRiskFactor() + rootRiskCostNode.getTotalRiskFactor();

						CostNode childRiskCostNode = new CostNode(rootRiskCostNode, 0, totalRiskFactor, childNode);
						CostNode frontierNode = getFrontierNode(childUserName);

						if(frontierNode != null){
							if(frontierNode.getTotalRiskFactor() > childRiskCostNode.getTotalRiskFactor()){
								priorityQueue.remove(frontierNode);
								priorityQueue.add(childRiskCostNode);
							}
						}else{
							priorityQueue.add(childRiskCostNode);
						}
					}
				}
			}
		}

		return null;
	}

	private CostNode getFrontierNode(String userName){
		Iterator<CostNode> iterator = priorityQueue.iterator();

		while(iterator.hasNext()){
			CostNode costNode = iterator.next();
			if(costNode.getNode().getUserName().equals(userName))
				return costNode;
		}

		return null;
	}



	public Stack<CostNode> getUniformSearchByRisk(String startNode, String goalNode){
		Stack<CostNode> pathResult = new Stack<CostNode>();

		priorityQueue.add(new CostNode(null, 0, 0, new Node(startNode, 0, 0)));
		CostNode goalRiskCostNode = getOptimalRiskFactor(new Node(goalNode, 0, 0));
		pathResult.push(goalRiskCostNode);

		CostNode parentNode = null;
		while((parentNode = goalRiskCostNode.getParentNode()) != null){
			pathResult.push(parentNode);
			goalRiskCostNode = parentNode;
		}

		return pathResult;
	}

}
