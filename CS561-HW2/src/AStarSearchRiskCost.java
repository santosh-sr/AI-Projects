


import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;


public class AStarSearchRiskCost extends Search{
	private PriorityQueue<EdgeCost> priorityQueue;

	public AStarSearchRiskCost(Map<String, List<Node>> graphStructure){
		super(graphStructure);
		priorityQueue = new PriorityQueue<EdgeCost>(100, new HeuristicRiskCostComparator());
	}

	private EdgeCost getPathByRisk(Node goalNode){
		if(priorityQueue.isEmpty()){
			return null;
		}else{
			while(!priorityQueue.isEmpty()){
				EdgeCost rootRiskCostNode = priorityQueue.poll();
				Node rootNode = rootRiskCostNode.getNode();
				String userName = rootNode.getUserName();
				int totalRootRiskCost = rootRiskCostNode.getTotalRiskCost();
				visitedNodes.add(userName);

				if(userName.equals(goalNode.getUserName())){
					return rootRiskCostNode;
				}

				for(Node childNode : graphStructure.get(userName)){
					String childUserName = childNode.getUserName();
					if(!visitedNodes.contains(childUserName)){

						int totalChildRiskCost = totalRootRiskCost + childNode.getRiskFactor();
						int heuristicRiskCost = totalChildRiskCost + childNode.getDirectRiskCost();

						EdgeCost childRiskCostNode = new EdgeCost(rootRiskCostNode, 0, heuristicRiskCost, childNode);
						childRiskCostNode.setTotalRiskCost(totalChildRiskCost);
						
						priorityQueue.add(childRiskCostNode);
					}
				}
			}
		}

		return null;
	}

	public Stack<EdgeCost> getAStarSearchByRisk(String startNode, String goalNode){
		Stack<EdgeCost> pathResult = new Stack<EdgeCost>();

		priorityQueue.add(new EdgeCost(null, 0, 0, new Node(startNode, 0, 0)));
		EdgeCost goalPathCostNode = getPathByRisk(new Node(goalNode, 0, 0));
		pathResult.push(goalPathCostNode);

		EdgeCost parentNode = null;
		while((parentNode = goalPathCostNode.getParentNode()) != null){
			pathResult.push(parentNode);
			goalPathCostNode = parentNode;
		}

		return pathResult;
	}

}
