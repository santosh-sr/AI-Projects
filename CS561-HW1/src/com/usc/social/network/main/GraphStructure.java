package com.usc.social.network.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.usc.social.network.graph.Node;

public class GraphStructure {

	private static final int RISK_FACTOR = 3;
	private static final int TIME_TAKEN = 2;
	private static final int MUTUAL_FRIEND_NAME = 1;
	private static final int USER_PROFILE_NAME = 0;
	private static final String SPACE = " ";
	private Map<String, List<Node>> graphStructure;

	public Map<String, List<Node>> createGraph(String inputFilePath) throws IOException {
		graphStructure = new HashMap<String, List<Node>>();

		BufferedReader reader = new BufferedReader(new FileReader(new File(inputFilePath)));
		String nodeLine;
		while((nodeLine=reader.readLine()) != null){
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

					//Add the mutual friend in the connected nodes of user
					addConnectedNode(mutualFriendName, time_taken, risk_factor,
							connectedNodes);
					
					graphStructure.put(userName, connectedNodes);

					//Similarly, Get Connected nodes for the mutual friend 
					connectedNodes = getConnectedNodes(mutualFriendName);

					//Add the user in the connected nodes of mutual friend
					addConnectedNode(userName, time_taken, risk_factor,
							connectedNodes);

					//Insert both the elements
					graphStructure.put(mutualFriendName, connectedNodes);
				}
			}
		}
		
		return graphStructure;
	}

	private void addConnectedNode(String userName, int time_taken,
			int risk_factor, List<Node> connectedNodes) {
		Node node;
		//Add the connected nodes in the list
		if(connectedNodes != null){
			node = new Node(userName, time_taken, risk_factor);
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
