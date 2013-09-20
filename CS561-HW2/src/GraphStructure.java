


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class GraphStructure {

	private static final int DIRECT_RISK_COST = 2;
	private static final int DIRECT_TIME_COST = 1;
	private static final int RISK_FACTOR = 3;
	private static final int TIME_TAKEN = 2;
	private static final int MUTUAL_FRIEND_NAME = 1;
	private static final int USER_PROFILE_NAME = 0;
	private static final String SPACE = " ";
	private Map<String, List<Node>> graphStructure;

	public Map<String, List<Node>> createGraph(String inputFilePath, String directTimeRiskFile) throws IOException {
		graphStructure = new HashMap<String, List<Node>>();
		BufferedReader directRiskFileReader = new BufferedReader(new FileReader(new File(directTimeRiskFile)));
		String directCostLine;
		Map<String, DirectCost> directCostMap = new HashMap<String, DirectCost>();
		while((directCostLine = directRiskFileReader.readLine()) != null){
			directCostLine = directCostLine.trim();
			if(!directCostLine.equals("")){
				String[] split = directCostLine.split(SPACE);

				if(split.length >= 3){
					String userName = split[USER_PROFILE_NAME];
					int directTimeCost = Integer.parseInt(split[DIRECT_TIME_COST]);
					int directRiskCost = Integer.parseInt(split[DIRECT_RISK_COST]);

					directCostMap.put(userName, new DirectCost(userName, directTimeCost, directRiskCost));
				}
			}
		}

		BufferedReader inputFileReader = new BufferedReader(new FileReader(new File(inputFilePath)));
		String nodeLine;
		while((nodeLine = inputFileReader.readLine()) != null){
			nodeLine = nodeLine.trim();
			if(!nodeLine.equals("")){
				String[] split = nodeLine.split(SPACE);

				if(split.length >= 4){
					String userName = split[USER_PROFILE_NAME];
					String mutualFriendName = split[MUTUAL_FRIEND_NAME];
					int time_taken = Integer.parseInt(split[TIME_TAKEN]);
					int risk_factor = Integer.parseInt(split[RISK_FACTOR]);

					List<Node> connectedNodes;
					//Get Connected nodes for the user 
					connectedNodes = getConnectedNodes(userName);

					int mutualFriendDirectTimeCost = 0;
					int mutualFriendDirectRiskCost = 0;
					DirectCost mutualFriendDirectCost = directCostMap.get(mutualFriendName);
					if(mutualFriendDirectCost != null){
						mutualFriendDirectTimeCost = mutualFriendDirectCost.getDirectTime();
						mutualFriendDirectRiskCost = mutualFriendDirectCost.getDirectRisk();
					}

					//Add the mutual friend in the connected nodes of user
					addConnectedNode(mutualFriendName, time_taken, risk_factor, mutualFriendDirectTimeCost, mutualFriendDirectRiskCost, 
							connectedNodes);

					graphStructure.put(userName, connectedNodes);

					//Similarly, Get Connected nodes for the mutual friend 
					connectedNodes = getConnectedNodes(mutualFriendName);
					
					DirectCost userDirectCost = directCostMap.get(userName);
					int userDirectTimeCost = 0;
					int userDirectRiskCost = 0;

					if(userDirectCost != null){
						userDirectTimeCost = userDirectCost.getDirectTime();
						userDirectRiskCost = userDirectCost.getDirectRisk();
					}

					//Add the user in the connected nodes of mutual friend
					addConnectedNode(userName, time_taken, risk_factor, userDirectTimeCost, userDirectRiskCost,
							connectedNodes);

					//Insert both the elements
					graphStructure.put(mutualFriendName, connectedNodes);
				}
			}
		}

		return graphStructure;
	}

	private void addConnectedNode(String userName, int time_taken,
			int risk_factor, int directTimeCost, int directRiskCost, List<Node> connectedNodes) {
		Node node;
		//Add the connected nodes in the list
		if(connectedNodes != null){
			node = new Node(userName, time_taken, risk_factor);
			node.setDirectTimeCost(directTimeCost);
			node.setDirectRiskCost(directRiskCost);
			connectedNodes.add(node);
		}
	}

	private List<Node> getConnectedNodes(String userName) {
		List<Node> connectedNodes = null;
		if(graphStructure.containsKey(userName)){
			connectedNodes = graphStructure.get(userName);
		}else{
			connectedNodes = new ArrayList<Node>();
		}
		return connectedNodes;
	}

}
