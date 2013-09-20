


import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;


public class GreedySearchRiskCost extends Search{
	private PriorityQueue<EdgeCost> priorityQueue;

	public GreedySearchRiskCost(Map<String, List<Node>> graphStructure){
		super(graphStructure);
		priorityQueue = new PriorityQueue<EdgeCost>(100, new DirectRiskCostComparator());
	}

	private EdgeCost getGreedyRiskCost(Node goalNode){
		if(priorityQueue.isEmpty()){
			return null;
		}else{
			while(!priorityQueue.isEmpty()){
				EdgeCost rootPathCostNode = priorityQueue.poll();
				Node rootNode = rootPathCostNode.getNode();
				String userName = rootNode.getUserName();

				visitedNodes.add(userName);
				if(userName.equals(goalNode.getUserName())){
					return rootPathCostNode;
				}

				for(Node childNode : graphStructure.get(userName)){
					String childUserName = childNode.getUserName();
					if(!visitedNodes.contains(childUserName)){
						EdgeCost childPathCostNode = new EdgeCost(rootPathCostNode, 0, 0, childNode);

						priorityQueue.add(childPathCostNode);
					}
				}
			}
		}

		return null;
	}

	public Stack<EdgeCost> getGreedySearchByRisk(String startNode, String goalNode){
		Stack<EdgeCost> pathResult = new Stack<EdgeCost>();

		priorityQueue.add(new EdgeCost(null, 0, 0, new Node(startNode, 0, 0)));
		EdgeCost goalPathCostNode = getGreedyRiskCost(new Node(goalNode, 0, 0));
		pathResult.push(goalPathCostNode);

		EdgeCost parentNode = null;
		while((parentNode = goalPathCostNode.getParentNode()) != null){
			pathResult.push(parentNode);
			goalPathCostNode = parentNode;
		}

		return pathResult;
	}

}
