package com.usc.social.network.search.dfs;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.usc.social.network.graph.CostNode;
import com.usc.social.network.graph.Node;
import com.usc.social.network.search.Search;

public class DepthFirstSearch extends Search{
	private Stack<CostNode> stack;

	public DepthFirstSearch(Map<String, List<Node>> graphStructure){
		super(graphStructure);
		stack = new Stack<CostNode>();
	}

	private CostNode depthFirstSearch(String goalNode){
		if(stack.isEmpty()){
			return null;
		}else{
			while(!stack.isEmpty()){
				CostNode rootNode = stack.peek();

				String rootUserName = rootNode.getNode().getUserName();
				if(rootUserName.equalsIgnoreCase(goalNode)){
					return rootNode;
				}

				Node connectedNode = getUntraversedNode(rootNode.getNode().getUserName());
				if(connectedNode != null){
					CostNode childConnectedNode = new CostNode(rootNode, 0, 0, connectedNode);
					String userName = connectedNode.getUserName();
					stack.push(childConnectedNode);
					visitedNodes.add(userName);
				}else{
					stack.pop();
				}
			}
		}

		return null;
	}


	public Stack<CostNode> getDFSResult(String startNode, String goalNode){
		Stack<CostNode> pathResult = new Stack<CostNode>();

		CostNode startCostNode = new CostNode(null, 0, 0, new Node(startNode, 0, 0));
		if(startNode.equalsIgnoreCase(goalNode)){
			pathResult.add(startCostNode);
			return pathResult;
		}

		stack.push(startCostNode);
		visitedNodes.add(startNode);

		CostNode goalCostNode = depthFirstSearch(goalNode);
		pathResult.push(goalCostNode);

		CostNode parentNode = null;
		while((parentNode = goalCostNode.getParentNode()) != null){
			pathResult.push(parentNode);
			goalCostNode = parentNode;
		}
		return pathResult;
	}

}
