


import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;


public class AStarSearchTimeCost extends Search{
	private PriorityQueue<EdgeCost> priorityQueue;

	public AStarSearchTimeCost(Map<String, List<Node>> graphStructure){
		super(graphStructure);
		priorityQueue = new PriorityQueue<EdgeCost>(100, new HeuristicTimeCostComparator());
	}

	private EdgeCost getPathByTime(Node goalNode){
		if(priorityQueue.isEmpty()){
			return null;
		}else{
			while(!priorityQueue.isEmpty()){
				EdgeCost rootEdgeCostNode = priorityQueue.poll();
				Node rootNode = rootEdgeCostNode.getNode();
				String userName = rootNode.getUserName();
				int totalTimeTaken = rootEdgeCostNode.getTotalPathCost();
				visitedNodes.add(userName);

				if(userName.equals(goalNode.getUserName())){
					return rootEdgeCostNode;
				}

				for(Node childNode : graphStructure.get(userName)){
					String childUserName = childNode.getUserName();
					if(!visitedNodes.contains(childUserName)){

						int childTimeTaken = totalTimeTaken + childNode.getTimeTaken();
						int heuristicTimeCost = childTimeTaken + childNode.getDirectTimeCost();

						EdgeCost childPathCostNode = new EdgeCost(rootEdgeCostNode, heuristicTimeCost, 0, childNode);
						childPathCostNode.setTotalPathCost(childTimeTaken);
						priorityQueue.add(childPathCostNode);
					}
				}
			}
		}

		return null;
	}

	public Stack<EdgeCost> getAStarSearchByTime(String startNode, String goalNode){
		Stack<EdgeCost> pathResult = new Stack<EdgeCost>();

		priorityQueue.add(new EdgeCost(null, 0, 0, new Node(startNode, 0, 0)));
		EdgeCost goalPathCostNode = getPathByTime(new Node(goalNode, 0, 0));
		pathResult.push(goalPathCostNode);

		EdgeCost parentNode = null;
		while((parentNode = goalPathCostNode.getParentNode()) != null){
			pathResult.push(parentNode);
			goalPathCostNode = parentNode;
		}

		return pathResult;
	}

}
