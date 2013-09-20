


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Stack;



public class Main {

	private static final String PATH_SEPARATOR = System.getProperty("file.separator");

	public static void main(String[] args) {
		try {
			if(args.length < 4){
				System.err.println("Should have four arguments. The argument format should be: <input-file>" +
						"<direct-time-risk> <start_node> <end_node>");
				return;
			}
			
			String inputFilePath = null;
			String directTimeRiskPath = null;
			String outputPath = null;
			String startNode = null;
			String goalNode = null;

			inputFilePath = args[0];
			directTimeRiskPath = args[1];
			startNode = args[2];
			goalNode = args[3];
			
			Main main = new Main();
			File inputFile = new File(inputFilePath);
			//Check the input file path
			if(inputFilePath == null || !inputFile.exists()){
				System.err.println("Cannot find the input file.");
				return;
			}
			
			//Check the input file path
			if(directTimeRiskPath == null || !new File(directTimeRiskPath).exists()){
				System.err.println("Cannot find the direct time risk file.");
				return;
			}

			inputFile = inputFile.getCanonicalFile();
			outputPath = inputFile.getParentFile().getAbsolutePath();
			//Create the output directory if does not exists
			if(outputPath == null){
				System.err.println("Cannot find the output directory.");
				return;
			}
				
			Map<String, List<Node>> graphStructure = new GraphStructure().createGraph(inputFilePath, directTimeRiskPath);
			if(!graphStructure.containsKey(startNode) || !graphStructure.containsKey(goalNode)){
				System.err.println("Cannot find the start/goal node in the given graph");
				return;
			}
			
			//Greedy Search Time Implementation
			{
				GreedySearchTimeCost greedyTimeCost = new GreedySearchTimeCost(graphStructure);
				Stack<EdgeCost> greedyTimeStack = greedyTimeCost.getGreedySearchByTime(startNode, goalNode);
				Object[] costPathArr = (Object[])greedyTimeStack.toArray();

				String greedyTimeCostPath = main.getTraversalPath(costPathArr);

				String greedyTimeOutputPath = outputPath + PATH_SEPARATOR + "Greedy.time.result.txt";
				main.writeFile(greedyTimeCostPath, greedyTimeOutputPath);
			}
			
			//Greedy Search Risk Implementation
			{
				GreedySearchRiskCost greedyRiskCost = new GreedySearchRiskCost(graphStructure);
				Stack<EdgeCost> greedyRiskStack = greedyRiskCost.getGreedySearchByRisk(startNode, goalNode);
				Object[] costPathArr = (Object[])greedyRiskStack.toArray();

				String greedyRiskCostPath = main.getTraversalPath(costPathArr);

				String greedyTimeOutputPath = outputPath + PATH_SEPARATOR + "Greedy.risk.result.txt";
				main.writeFile(greedyRiskCostPath, greedyTimeOutputPath);
			}
			
			//Greedy Search Time Implementation
			{
				AStarSearchTimeCost astarTimeCost = new AStarSearchTimeCost(graphStructure);
				Stack<EdgeCost> astarSearchTimeStack = astarTimeCost.getAStarSearchByTime(startNode, goalNode);
				Object[] costPathArr = (Object[])astarSearchTimeStack.toArray();

				String astarTimeCostPath = main.getTraversalPath(costPathArr);
				
				String astarTimeOutputPath = outputPath + PATH_SEPARATOR + "A-Star.time.result.txt";
				main.writeFile(astarTimeCostPath, astarTimeOutputPath);
			}
			
			//Greedy Search Risk Implementation
			{
				AStarSearchRiskCost astarRiskCost = new AStarSearchRiskCost(graphStructure);
				Stack<EdgeCost> astarSearchRiskStack = astarRiskCost.getAStarSearchByRisk(startNode, goalNode);
				Object[] costPathArr = (Object[])astarSearchRiskStack.toArray();

				String astarRiskCostPath = main.getTraversalPath(costPathArr);
				
				String astarRiskOutputPath = outputPath + PATH_SEPARATOR + "A-Star.risk.result.txt";
				main.writeFile(astarRiskCostPath, astarRiskOutputPath);
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
				userName = ((EdgeCost)costPathArr[count]).getNode().getUserName();
				pathResult += userName + "-";
			}

			userName = ((EdgeCost)costPathArr[0]).getNode().getUserName();
			pathResult += userName;
		}
		return pathResult;
	}
}
