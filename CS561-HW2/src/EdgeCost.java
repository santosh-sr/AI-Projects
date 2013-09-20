
public class EdgeCost {
	private Node node;

	private EdgeCost parentNode;

	private int totalHeuristicTimeCost ;
	
	private int totalHeuristicRiskCost;
	
	private int totalPathCost;
	
	private int totalRiskCost;
	
	public EdgeCost(EdgeCost parentNode, int totalHeuristicTimeCost, int totalHeuristicRiskCost, Node node){
		this.parentNode = parentNode;
		this.totalHeuristicTimeCost = totalHeuristicTimeCost;
		this.totalHeuristicRiskCost = totalHeuristicRiskCost;
		this.node = node;
	}
	
	public EdgeCost getParentNode() {
		return parentNode;
	}

	public void setParentNode(EdgeCost parentNode) {
		this.parentNode = parentNode;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public int getTotalHeuristicTimeCost() {
		return totalHeuristicTimeCost;
	}

	public void setTotalHeuristicTimeCost(int totalHeuristicTimeCost) {
		this.totalHeuristicTimeCost = totalHeuristicTimeCost;
	}

	public int getTotalHeuristicRiskCost() {
		return totalHeuristicRiskCost;
	}

	public void setTotalHeuristicRiskCost(int totalHeuristicRiskCost) {
		this.totalHeuristicRiskCost = totalHeuristicRiskCost;
	}

	public int getTotalPathCost() {
		return totalPathCost;
	}

	public void setTotalPathCost(int totalPathCost) {
		this.totalPathCost = totalPathCost;
	}

	public int getTotalRiskCost() {
		return totalRiskCost;
	}

	public void setTotalRiskCost(int totalRiskCost) {
		this.totalRiskCost = totalRiskCost;
	}

	@Override
	public String toString() {
		String toString = "PathCostNode [node=" + node.getUserName() + ", parentNode="; 

		if(parentNode != null){
			toString += parentNode.getNode().getUserName();
		}else{
			toString += null;
		}
		
		toString += ", total heuristic timeCost=" + totalHeuristicTimeCost + ", total heuristic risk cost=" + totalHeuristicRiskCost + "]";
		
		return toString;
	}


}
