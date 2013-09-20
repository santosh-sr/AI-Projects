package com.usc.social.network.search.bfs;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import com.usc.social.network.graph.CostNode;
import com.usc.social.network.graph.Node;
import com.usc.social.network.search.Search;

/**
 * Class to implement the Breadth First Search Algorithm
 * 
 * @author santosh
 */
public class BreadthFirstSearch extends Search{
	private Queue<CostNode> queue;

	public BreadthFirstSearch(Map<String, List<Node>> graphStructure){
		super(graphStructure);

		queue = new LinkedList<CostNode>();
	}

	/**
	 * Returns the goal node by traversing the graph structure through breadth-first search
	 * 
	 * @param goalNode
	 * @return
	 */
	private CostNode breadthFirstSearch(String goalNode){
		if(queue.isEmpty())
			return null;
		else{
			while(!queue.isEmpty()){
				CostNode rootNode = queue.remove();
				
				//Check the removed node succeeds the goal test
				String userName = rootNode.getNode().getUserName();
				if(userName.equalsIgnoreCase(goalNode))
					return rootNode;

				//Add the connected nodes in queue by traversing from the parent node
				Node connectedNode = null;
				while((connectedNode = getUntraversedNode(userName)) != null){
					String connectedUser = connectedNode.getUserName();

					CostNode childCostNode = new CostNode(rootNode, 0, 0, connectedNode);
					queue.add(childCostNode);
					visitedNodes.add(connectedUser);
				}
			}
		}

		return null;
	}

	/**
	 * Returns the Breadth First Search traversal result on succeeding the goal test
	 * 
	 * @param startNode node to start traversing from
	 * @param goalNode node to reach destination
	 * @return Stack<CostNode>
	 */
	public Stack<CostNode> getBFSResult(String startNode, String goalNode){
		Stack<CostNode> pathResult = new Stack<CostNode>();
		
		CostNode rootNode = new CostNode(null, 0, 0, new Node(startNode, 0, 0));
		if(startNode.equalsIgnoreCase(goalNode)){
			pathResult.add(rootNode);
			return pathResult;
		}

		queue.add(rootNode);
		visitedNodes.add(startNode);		
		CostNode goalCostNode = breadthFirstSearch(goalNode);
		pathResult.push(goalCostNode);
		
		CostNode parentNode = null;
		while((parentNode = goalCostNode.getParentNode()) != null){
			pathResult.push(parentNode);
			goalCostNode = parentNode;
		}
		
		return pathResult;
	}
}
