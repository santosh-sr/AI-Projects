package com.usc.social.network.search.uniform.cost;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import com.usc.social.network.graph.CostNode;
import com.usc.social.network.graph.Node;
import com.usc.social.network.search.Search;

public class UniformSearchPathCost extends Search{
	private PriorityQueue<CostNode> priorityQueue;

	public UniformSearchPathCost(Map<String, List<Node>> graphStructure){
		super(graphStructure);
		priorityQueue = new PriorityQueue<CostNode>(100, new PathCostComparator());
	}

	private CostNode getOptimalPathCost(Node goalNode){
		if(priorityQueue.isEmpty()){
			return null;
		}else{
			while(!priorityQueue.isEmpty()){
				CostNode rootPathCostNode = priorityQueue.poll();
				Node rootNode = rootPathCostNode.getNode();
				String userName = rootNode.getUserName();

				visitedNodes.add(userName);

				if(userName.equals(goalNode.getUserName())){
					return rootPathCostNode;
				}

				for(Node childNode : graphStructure.get(userName)){
					String childUserName = childNode.getUserName();
					if(!visitedNodes.contains(childUserName)){

						int totalPathCost = childNode.getTimeTaken() + rootPathCostNode.getTotalPathCost();

						CostNode childPathCostNode = new CostNode(rootPathCostNode, totalPathCost, 0, childNode);
						CostNode frontierNode = getFrontierNode(childUserName);

						if(frontierNode != null){
							if(frontierNode.getTotalPathCost() > childPathCostNode.getTotalPathCost()){
								priorityQueue.remove(frontierNode);
								priorityQueue.add(childPathCostNode);
							}
						}else{
							priorityQueue.add(childPathCostNode);
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



	public Stack<CostNode> getUniformSearchByCost(String startNode, String goalNode){
		Stack<CostNode> pathResult = new Stack<CostNode>();

		priorityQueue.add(new CostNode(null, 0, 0, new Node(startNode, 0, 0)));
		CostNode goalPathCostNode = getOptimalPathCost(new Node(goalNode, 0, 0));
		pathResult.push(goalPathCostNode);

		CostNode parentNode = null;
		while((parentNode = goalPathCostNode.getParentNode()) != null){
			pathResult.push(parentNode);
			goalPathCostNode = parentNode;
		}

		return pathResult;
	}

}
