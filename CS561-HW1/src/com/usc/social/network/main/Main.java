package com.usc.social.network.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.usc.social.network.graph.CostNode;
import com.usc.social.network.graph.Node;
import com.usc.social.network.search.bfs.BreadthFirstSearch;
import com.usc.social.network.search.dfs.DepthFirstSearch;
import com.usc.social.network.search.uniform.cost.UniformSearchPathCost;
import com.usc.social.network.search.uniform.risk.UniformSearchRiskFactor;

public class Main {

	private static final String PATH_SEPARATOR = System.getProperty("file.separator");

	public static void main(String[] args) {
		try {
			if(args.length < 2){
				System.out.println("Should have two arguments. Argument-1 should contain input file" +
						" and Argument-2 should contain the absolute path of output directory.");
				return;
			}

			Main main = new Main();
			String inputFilePath = args[0];
			String outputPath = args[1];

			//Check the input file path
			if(!new File(inputFilePath).exists()){
				System.out.println("Cannot find the input file.");
				return;
			}
			
			//Create the output directory if does not exists
			File outputFile = new File(outputPath);
			if(!outputFile.exists()){
				outputFile.mkdir();
			}
			
			Map<String, List<Node>> graphStructure = new GraphStructure().createGraph(inputFilePath);
			String startNode = "Alice";
			String goalNode = "Noah";

			//Breadth First Search Implementation
			{
				BreadthFirstSearch bfs = new BreadthFirstSearch(graphStructure);
				Stack<CostNode> bfsStack = bfs.getBFSResult(startNode, goalNode);
				Object[] costPathArr = (Object[])bfsStack.toArray();

				String bfsPath = main.getTraversalPath(costPathArr);

				String bfsOutputPath = outputPath + PATH_SEPARATOR + "breadth-first.result.txt";
				main.writeFile(bfsPath, bfsOutputPath);
			}
			//Depth First Search Implementation
			{
				DepthFirstSearch dfs = new DepthFirstSearch(graphStructure);
				Stack<CostNode> dfsStack = dfs.getDFSResult(startNode, goalNode);
				Object[] costPathArr = (Object[])dfsStack.toArray();

				String dfsPath = main.getTraversalPath(costPathArr);
				String dfsOutputPath = outputPath + PATH_SEPARATOR + "depth-first.result.txt";
				main.writeFile(dfsPath, dfsOutputPath);
			}
			//Uniform Cost Search Implementation for Path
			{
				UniformSearchPathCost pathCost = new UniformSearchPathCost(graphStructure);
				Stack<CostNode> uniformSearchByCost = pathCost.getUniformSearchByCost(startNode, goalNode);

				Object[] pathCostNodeArr = (Object[])uniformSearchByCost.toArray();
				String optimalCostTraversalPath = main.getOptimalCostTraversalPath(pathCostNodeArr);
				String uniformCostOutputPath = outputPath + PATH_SEPARATOR + "uniform-cost.time.result.txt";
				main.writeFile(optimalCostTraversalPath, uniformCostOutputPath);
			}
			//Uniform Cost Search Implementation for Risk
			{
				UniformSearchRiskFactor riskCost = new UniformSearchRiskFactor(graphStructure);
				Stack<CostNode> uniformSearchByRisk = riskCost.getUniformSearchByRisk(startNode, goalNode);

				Object[] riskCostNodeArr = (Object[])uniformSearchByRisk.toArray();
				String minRiskTraversalPath = main.getMinRiskTraversalPath(riskCostNodeArr);
				String riskOutputPath = outputPath + PATH_SEPARATOR + "uniform-cost.risk.result.txt";
				main.writeFile(minRiskTraversalPath, riskOutputPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write the file bytes in the output path
	 * 
	 * @param traversal
	 * @param outputPath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void writeFile(String traversal, String outputPath)
			throws FileNotFoundException, IOException {
		File file = new File(outputPath);
		if(!file.exists())
			file.createNewFile();
		FileOutputStream out = new FileOutputStream(file);
		out.write(traversal.getBytes());
		out.close();
	}

	private String getTraversalPath(Object[] costPathArr) {
		String pathResult = "";
		String userName;
		int length = costPathArr.length;

		if(length > 0){
			for(int count=length-1; count>0; count--){
				userName = ((CostNode)costPathArr[count]).getNode().getUserName();
				pathResult += userName + "-";
			}

			userName = ((CostNode)costPathArr[0]).getNode().getUserName();
			pathResult += userName;
		}
		return pathResult;
	}

	private String getOptimalCostTraversalPath(Object[] costPathArr) {
		String pathResult = "";
		String userName;
		int length = costPathArr.length;

		if(length > 0){
			for(int count=length-1; count>0; count--){
				CostNode costNode = (CostNode)costPathArr[count];
				userName = costNode.getNode().getUserName() + "(" + costNode.getTotalPathCost() + ")";
				pathResult += userName + "-";
			}

			CostNode costNode = (CostNode)costPathArr[0];
			userName = costNode.getNode().getUserName() + "(" + costNode.getTotalPathCost() + ")";
			pathResult += userName;
		}
		return pathResult;
	}

	private String getMinRiskTraversalPath(Object[] costPathArr) {
		String pathResult = "";
		String userName;
		int length = costPathArr.length;

		if(length > 0){
			for(int count=length-1; count>0; count--){
				CostNode costNode = (CostNode)costPathArr[count];
				userName = costNode.getNode().getUserName() + "(" + costNode.getTotalRiskFactor() + ")";
				pathResult += userName + "-";
			}

			CostNode costNode = (CostNode)costPathArr[0];
			userName = costNode.getNode().getUserName() + "(" + costNode.getTotalRiskFactor() + ")";
			pathResult += userName;
		}
		return pathResult;
	}
}
