import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class hw4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if(args.length < 2){
				System.err.println("Should have two arguments. Argument-1 should contain input file" +
						" and Argument-2 should contain the absolute path of output directory.");
				return;
			}

			String inputFilePath = args[0];
			String outputPath = args[1];

			//Check the input file path
			File inputFile = new File(inputFilePath);
			if(!inputFile.exists()){
				System.err.println("Cannot find the input file.");
				return;
			}

			//Create the output directory if does not exists
			File outputFile = hw4.getOutputFilePath(outputPath);
			if(!outputFile.exists()){
				outputFile.createNewFile();
			}
			
			//Create the alpha-beta pruning instance
			hw4 hw4 = new hw4();
			AlphaBetaPruning pruning = new AlphaBetaPruning(outputFile);
			List<BoardConfiguration> list = hw4.readInput(inputFile);
			
			int index = 1;
			for(BoardConfiguration config : list){
				pruning.write("Case " + index);
				pruning.write("---------------------------------");
				pruning.writeNewLine();
				pruning.alphaBetaPruning(new CheckerState(Player.BILL, config, null), AlphaBetaPruning.MIN_INFINITY_VALUE, AlphaBetaPruning.MAX_INFINITY_VALUE);
				
				index++;
			}
			
			//Close the stream
			pruning.closeStream();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void print(BoardConfiguration config){
		Character[][] board = config.getBoard();
		for(int row=0; row<BoardConfiguration.ROWS; row++){
			for(int column=0; column<BoardConfiguration.COLUMNS; column++){
				System.out.print(board[row][column]);
			}
			System.out.println();
		}
	}
	
	public List<BoardConfiguration> readInput(File inputFile) throws IOException{
		List<BoardConfiguration> boardStateList = new ArrayList<BoardConfiguration>();
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		Character[][] boardArrangement = null;
		int rowIndex = 0;
		String readLine;
		while((readLine = reader.readLine()) != null){
			if(readLine.startsWith("case")){
				boardArrangement = new Character[BoardConfiguration.ROWS][BoardConfiguration.COLUMNS];
				rowIndex = 0;
			}else if(readLine.trim().equals("")){
				boardArrangement = null;
			}else{
				for(int colIndex=0; colIndex < readLine.length(); colIndex++){
					char piece = readLine.charAt(colIndex);
					boardArrangement[rowIndex][colIndex] = new Character(piece);
				}
				
				rowIndex++;
				if(rowIndex >= BoardConfiguration.ROWS){
					boardStateList.add(new BoardConfiguration(boardArrangement));
				}
			}
		}
		
		reader.close();
		return boardStateList;
	}
	
	private static File getOutputFilePath(String fileName){
		return new File(System.getProperty("user.dir"), fileName);
	}

}
