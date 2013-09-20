


import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


/**
 * Abstract class to hold the graph structure and visited nodes
 *  
 * @author santosh
 */
public abstract class Search {
	
	protected Queue<String> visitedNodes;
	protected Map<String, List<Node>> graphStructure;
	
	public Search(Map<String, List<Node>> graphStructure){
		this.graphStructure = graphStructure;
		visitedNodes = new LinkedList<String>();
	}

	/**
	 * Returns the non-traversed node from the given node
	 * @param userName the current node [fb username]
	 * @return
	 */
	protected Node getUntraversedNode(String userName){
		List<Node> connectedNodes = graphStructure.get(userName);

		if(connectedNodes != null){
			for(Node node : connectedNodes){
				if(!visitedNodes.contains(node.getUserName())){
					return node;
				}
			}
		}

		return null;
	}
	
}
