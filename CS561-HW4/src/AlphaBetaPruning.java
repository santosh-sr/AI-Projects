import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AlphaBetaPruning {

	public static final int MAX_INFINITY_VALUE = 999;
	public static final int MIN_INFINITY_VALUE = -999;

	private BufferedWriter writer;

	public AlphaBetaPruning(File outputFile) throws IOException{
		writer = new BufferedWriter(new FileWriter(outputFile));
	}

	public int alphaBetaPruning(CheckerState state, int alpha, int beta) throws CloneNotSupportedException, IOException{
		int value = maxValue(state, alpha, beta);
		
		//Print the Game Tree
		BoardConfiguration utilityConfiguration = null;
		boolean isSet = false;
		for(CheckerState childState : state.getChildStateList()){

			writeMoves(childState);

			if(childState.getUtilityValue() == value && !isSet){
				utilityConfiguration = childState.getBoardConfiguration();
				isSet = true;
			}
		}

		if(utilityConfiguration != null){
			writeNewLine();
			write("First Move: " 
					+ putInBraces(utilityConfiguration.getFromXPos(), utilityConfiguration.getFromYPos(), utilityConfiguration.getNewXPos(), utilityConfiguration.getNewYPos()));
			writeNewLine();
		}

		return value;
	}

	private void writeMoves(CheckerState moveState) throws IOException{
		BoardConfiguration boardConfiguration = moveState.getBoardConfiguration();
		int player_depth = boardConfiguration.getDepth();
		Player player = moveState.getPlayer();
		List<CheckerState> childList = moveState.getChildStateList();
		List<CheckerState> prunedList = moveState.getPrunedStateList();
		int utilityValue = moveState.getUtilityValue();

		//Print the moves
		if(player_depth >= 4 || (childList == null || childList.isEmpty())){
			write(depthSuffix(player_depth) +  getPlayerValue(player) + player_depth + ":" +  putInBraces(boardConfiguration.getFromXPos(), boardConfiguration.getFromYPos(), boardConfiguration.getNewXPos(), boardConfiguration.getNewYPos()) + " ; h=" + utilityValue + ".");
		}else{
			write(depthSuffix(player_depth) +  getPlayerValue(player) + player_depth + ":" +  putInBraces(boardConfiguration.getFromXPos(), boardConfiguration.getFromYPos(), boardConfiguration.getNewXPos(), boardConfiguration.getNewYPos()));
		}
		
		if(childList != null){
			for(CheckerState childState : childList){
				writeMoves(childState);
				List<CheckerState> childPrunedStateList = childState.getPrunedStateList();
				if(childPrunedStateList != null && !childPrunedStateList.isEmpty()){
					break;
				}
			}
		}

		if(prunedList != null && !prunedList.isEmpty()){
			String pruneOutput = "pruning ";
			boolean firstIteration = true;
			int alpha = MIN_INFINITY_VALUE;
			int beta = MAX_INFINITY_VALUE;

			for(CheckerState prunedState : prunedList){
				//Iterate the pruned states and print the moves
				BoardConfiguration cg = prunedState.getBoardConfiguration();
				if(firstIteration){
					alpha = prunedState.getAlpha();
					beta = prunedState.getBeta();
					pruneOutput += putInBraces(cg.getFromXPos(), cg.getFromYPos(), cg.getNewXPos(), cg.getNewYPos());
					firstIteration = false;
				}else{
					pruneOutput += " , " + putInBraces(cg.getFromXPos(), cg.getFromYPos(), cg.getNewXPos(), cg.getNewYPos());
				}
			}

			write(depthSuffix(player_depth) + getPlayerValue(player) + player_depth + ":" + " " + pruneOutput + " ;" + " alpha=" + alpha + " ,beta=" + beta + ".");
		}
	}

	/**
	 * Returns the player value
	 * 
	 * @param player
	 * @return String
	 */
	private String getPlayerValue(Player player){
		return player == Player.ALICE ? "A" : "B";
	}

	public int maxValue(CheckerState state, int alpha, int beta) throws CloneNotSupportedException, IOException{

		BoardConfiguration boardConfiguration = state.getBoardConfiguration();
		int player_depth = boardConfiguration.getDepth();

		if(player_depth >= 4 || boardConfiguration.getNoOfBillPieces() <= 0){
			int utilityValue = boardConfiguration.calcUtilityValue();
			state.setUtilityValue(utilityValue);
			return utilityValue;
		}

		List<CheckerState> aliceChildList = getAliceSuccessors(state);
		state.setChildStateList(aliceChildList);

		if(aliceChildList == null || aliceChildList.isEmpty()){
			int utilityValue = boardConfiguration.calcUtilityValue();
			state.setUtilityValue(utilityValue);
			return utilityValue;
		}

		int value = MIN_INFINITY_VALUE;
		for(int index=0; index<aliceChildList.size(); index++){
			CheckerState childState = aliceChildList.get(index);
			BoardConfiguration childStateConfiguration = childState.getBoardConfiguration();
			player_depth = childStateConfiguration.getDepth();

			value = Math.max(value, minValue(childState, alpha, beta));
			childState.setUtilityValue(value);

			if(value >= beta){
				childState.setAlpha(value);
				childState.setBeta(beta);
				
				int out = index+1;
				if(out < aliceChildList.size()){
					for( out = index+1; out < aliceChildList.size(); out++){
						//Pruned State
						CheckerState prunedState = aliceChildList.get(out);
						prunedState.setAlpha(value);
						prunedState.setBeta(beta);
						childState.addPrunedState(prunedState);
					}
				}

				return value;
			}

			alpha = Math.max(alpha, value);
			childState.setAlpha(alpha);
			childState.setBeta(beta);
		}

		return value;
	}

	public int minValue(CheckerState state, int alpha, int beta) throws CloneNotSupportedException, IOException{
		BoardConfiguration boardConfiguration = state.getBoardConfiguration();
		int player_depth = boardConfiguration.getDepth();
		if(player_depth >= 4 || boardConfiguration.getNoOfAlicePieces() <= 0){
			int utilityValue = boardConfiguration.calcUtilityValue();
			state.setUtilityValue(utilityValue);
			return utilityValue;
		}

		List<CheckerState> billChildList = getBillSuccessors(state);
		state.setChildStateList(billChildList);

		if(billChildList == null || billChildList.isEmpty()){
			int utilityValue = boardConfiguration.calcUtilityValue();
			state.setUtilityValue(utilityValue);
			player_depth = boardConfiguration.getDepth();
			return utilityValue;
		}

		int value = MAX_INFINITY_VALUE;

		for(int index=0; index<billChildList.size(); index++){
			CheckerState childState = billChildList.get(index);
			BoardConfiguration childStateConfiguration = childState.getBoardConfiguration();
			player_depth = childStateConfiguration.getDepth();

			value = Math.min(value, maxValue(childState, alpha, beta));
			childState.setUtilityValue(value);

			if(value <= alpha){
				//Alpha and beta value of the state
				childState.setAlpha(alpha);
				childState.setBeta(value);
				
				//Alpha and beta value for other successors
				int out = index+1;
				if(out < billChildList.size()){
					for( out = index+1; out < billChildList.size(); out++){
						CheckerState prunedState = billChildList.get(out);
						prunedState.setAlpha(alpha);
						prunedState.setBeta(value);
						childState.addPrunedState(prunedState);
					}
				}

				return value;
			}

			beta = Math.min(beta, value);
			childState.setAlpha(alpha);
			childState.setBeta(beta);
		}

		return value;
	}

	private String depthSuffix(int depth){
		switch(depth){
		case 2:
			return "|-";
		case 3:
			return "|-|-";
		case 4:
			return "|-|-|-";
		}

		return "";
	}

	private boolean canJump(Player player, BoardConfiguration boardConfiguration){
		for(int row=0; row<BoardConfiguration.ROWS; row++){
			for(int column=0; column<BoardConfiguration.COLUMNS; column++){
				Character piece = boardConfiguration.getPiece(row, column);

				if(player == Player.ALICE && boardConfiguration.isAlicePiece(piece)){
					//Check there is a jump possible for the player
					if(boardConfiguration.canLeftForwardJump(player, row, column)){
						return true;
					}

					if(boardConfiguration.canRightForwardJump(player, row, column)){
						return true;
					}

					if(boardConfiguration.isAliceKingPiece(piece)){
						if(boardConfiguration.canLeftBackwardJump(player, row, column)){
							return true;
						}

						if(boardConfiguration.canRightBackwardJump(player, row, column)){
							return true;
						}
					}
				}else if(player == Player.BILL && boardConfiguration.isBillPiece(piece)){
					if(boardConfiguration.canLeftBackwardJump(player, row, column)){
						return true;
					}

					if(boardConfiguration.canRightBackwardJump(player, row, column)){
						return true;
					}

					//Check the king piece
					if(boardConfiguration.isBillKingPiece(piece)){
						if(boardConfiguration.canLeftForwardJump(player, row, column)){
							return true;
						}

						if(boardConfiguration.canRightForwardJump(player, row, column)){
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public List<CheckerState> getAliceSuccessors(CheckerState parentState) throws CloneNotSupportedException{
		BoardConfiguration boardConfig = parentState.getBoardConfiguration();
		if(boardConfig.getNoOfAlicePieces() <= 0){
			return null;
		}

		List<CheckerState> childStateList = new ArrayList<CheckerState>();
		Player player = Player.ALICE;
		BoardConfiguration cloneConfig;
		CheckerState childState;
		boolean canJump = canJump(player, boardConfig);
		for(int row=0; row<BoardConfiguration.ROWS; row++){
			for(int column=0; column<BoardConfiguration.COLUMNS; column++){
				Character piece = boardConfig.getPiece(row, column);
				if(boardConfig.isAlicePiece(piece)){
					if(canJump){
						if(boardConfig.canRightForwardJump(player, row, column)){
							childState = moveRightForwardJump(parentState,
									boardConfig, player, row, column);
							childStateList.add(childState);

							BoardConfiguration boardConfiguration = childState.getBoardConfiguration();
							boardConfiguration.decrementBillPiece();
						}
						
						//Move the normal piece
						if(boardConfig.canLeftForwardJump(player, row, column)){
							childState = moveLeftForwardJump(parentState,
									boardConfig, player, row, column);
							childStateList.add(childState);

							BoardConfiguration boardConfiguration = childState.getBoardConfiguration();
							boardConfiguration.decrementBillPiece();
						}

						//Move the king piece
						if(boardConfig.isAliceKingPiece(piece)){
							if(boardConfig.canRightBackwardJump(player, row, column)){
								childState = moveRightBackwardJump(parentState,
										boardConfig, player, row, column);
								childStateList.add(childState);

								BoardConfiguration boardConfiguration = childState.getBoardConfiguration();
								boardConfiguration.decrementBillPiece();
							}
							
							if(boardConfig.canLeftBackwardJump(player, row, column)){
								childState = moveLeftBackwardJump(parentState,
										boardConfig, player, row, column);
								childStateList.add(childState);

								BoardConfiguration boardConfiguration = childState.getBoardConfiguration();
								boardConfiguration.decrementBillPiece();
							}
						}
					}else{
						if(boardConfig.isRightForwardDiagonalEmpty(row, column))
						{
							cloneConfig = boardConfig.clone();
							cloneConfig.movePieceToRightForward(row, column);
							cloneConfig.incrementDepth();
							childState = new CheckerState(player, cloneConfig, parentState);
							childStateList.add(childState);
						}
						
						//Move the Normal Piece
						if(boardConfig.isLeftForwardDiagonalEmpty(row, column))
						{
							cloneConfig = boardConfig.clone();
							cloneConfig.movePieceToLeftForward(row, column);
							cloneConfig.incrementDepth();
							childState = new CheckerState(player, cloneConfig, parentState);
							childStateList.add(childState);
						}

						//Move the king piece
						if(boardConfig.isAliceKingPiece(piece)){
							if(boardConfig.isRightDownwardDiagonalEmpty(row, column))
							{
								cloneConfig = boardConfig.clone();
								cloneConfig.movePieceToRightDownward(row, column);
								cloneConfig.incrementDepth();
								childState = new CheckerState(player, cloneConfig, parentState);
								childStateList.add(childState);
							}
							
							if(boardConfig.isLeftDownwardDiagonalEmpty(row, column))
							{
								cloneConfig = boardConfig.clone();
								cloneConfig.movePieceToLeftDownward(row, column);
								cloneConfig.incrementDepth();
								childState = new CheckerState(player, cloneConfig, parentState);
								childStateList.add(childState);
							}
						}
					}
				}
			}
		}

		return childStateList;
	}

	private String putInBraces(int fromRow, int fromColumn, int toRow, int toColumn){
		return "(" + fromRow + "," + fromColumn + ")" + " => " + "(" + toRow + "," + toColumn + ")";
	}

	public List<CheckerState> getBillSuccessors(CheckerState parentState) throws CloneNotSupportedException{
		BoardConfiguration boardConfig = parentState.getBoardConfiguration();
		//Return null, if the opponent player does not have any coins left
		if(boardConfig.getNoOfBillPieces() <= 0){
			return null;
		}

		List<CheckerState> childStateList = new ArrayList<CheckerState>();
		Player player = Player.BILL;
		BoardConfiguration cloneConfig;
		CheckerState childState;
		boolean canJump = canJump(player, boardConfig);
		for(int row=0; row<BoardConfiguration.ROWS; row++){
			for(int column=0; column<BoardConfiguration.COLUMNS; column++){
				Character piece = boardConfig.getPiece(row, column);
				if(boardConfig.isBillPiece(piece)){
					if(canJump){
						//Move like normal piece
						if(boardConfig.canLeftBackwardJump(player, row, column)){
							childState = moveLeftBackwardJump(parentState,
									boardConfig, player, row, column);
							childStateList.add(childState);

							BoardConfiguration boardConfiguration = childState.getBoardConfiguration();
							boardConfiguration.decrementAlicePiece();
						}

						if(boardConfig.canRightBackwardJump(player, row, column)){
							childState = moveRightBackwardJump(parentState,
									boardConfig, player, row, column);
							childStateList.add(childState);

							BoardConfiguration boardConfiguration = childState.getBoardConfiguration();
							boardConfiguration.decrementAlicePiece();
						}

						//Move the king piece
						if(boardConfig.isBillKingPiece(piece)){
							if(boardConfig.canLeftForwardJump(player, row, column)){
								childState = moveLeftForwardJump(parentState,
										boardConfig, player, row, column);
								childStateList.add(childState);

								BoardConfiguration boardConfiguration = childState.getBoardConfiguration();
								boardConfiguration.decrementAlicePiece();
							}

							if(boardConfig.canRightForwardJump(player, row, column)){
								childState = moveRightForwardJump(parentState,
										boardConfig, player, row, column);
								childStateList.add(childState);

								BoardConfiguration boardConfiguration = childState.getBoardConfiguration();
								boardConfiguration.decrementAlicePiece();
							}
						}
					}
					else{
						if(boardConfig.isBillKingPiece(piece)){
							//Move the Normal Piece
							if(boardConfig.isLeftForwardDiagonalEmpty(row, column))
							{
								cloneConfig = boardConfig.clone();
								cloneConfig.movePieceToLeftForward(row, column);
								childState = new CheckerState(player, cloneConfig, parentState);
								cloneConfig.incrementDepth();
								childStateList.add(childState);
							}
							
							if(boardConfig.isRightForwardDiagonalEmpty(row, column))
							{
								cloneConfig = boardConfig.clone();
								cloneConfig.movePieceToRightForward(row, column);
								childState = new CheckerState(player, cloneConfig, parentState);
								cloneConfig.incrementDepth();
								childStateList.add(childState);
							}
						}
						
						//Move the Normal Piece
						if(boardConfig.isLeftDownwardDiagonalEmpty(row, column))
						{
							cloneConfig = boardConfig.clone();
							cloneConfig.movePieceToLeftDownward(row, column);
							childState = new CheckerState(player, cloneConfig, parentState);
							cloneConfig.incrementDepth();
							childStateList.add(childState);
						}

						if(boardConfig.isRightDownwardDiagonalEmpty(row, column))
						{
							cloneConfig = boardConfig.clone();
							cloneConfig.movePieceToRightDownward(row, column);
							childState = new CheckerState(player, cloneConfig, parentState);
							cloneConfig.incrementDepth();
							childStateList.add(childState);
						}
					}
				}
			}
		}

		return childStateList;
	}

	private CheckerState moveRightBackwardJump(CheckerState parentState,
			BoardConfiguration boardConfig, Player player, int row, int column)
					throws CloneNotSupportedException {
		BoardConfiguration cloneConfig = boardConfig.clone();
		cloneConfig.moveRightBackwardJump(player, row, column);
		cloneConfig.incrementDepth();
		CheckerState childState = new CheckerState(player, cloneConfig, parentState);
		return childState;
	}

	private CheckerState moveLeftBackwardJump(CheckerState parentState,
			BoardConfiguration boardConfig, Player player, int row, int column)
					throws CloneNotSupportedException {
		BoardConfiguration cloneConfig = boardConfig.clone();
		cloneConfig.moveLeftBackwardJump(player, row, column);
		cloneConfig.incrementDepth();
		CheckerState childState = new CheckerState(player, cloneConfig, parentState);
		return childState;
	}

	private CheckerState moveRightForwardJump(CheckerState parentState,
			BoardConfiguration boardConfig, Player player, int row, int column)
					throws CloneNotSupportedException {
		BoardConfiguration cloneConfig = boardConfig.clone();
		cloneConfig.moveRightForwardJump(player, row, column);
		cloneConfig.incrementDepth();
		CheckerState childState = new CheckerState(player, cloneConfig, parentState);
		return childState;
	}

	private CheckerState moveLeftForwardJump(CheckerState parentState,
			BoardConfiguration boardConfig, Player player, int row, int column)
					throws CloneNotSupportedException {
		BoardConfiguration cloneConfig = boardConfig.clone();
		cloneConfig.moveLeftForwardJump(player, row, column);
		cloneConfig.incrementDepth();
		CheckerState childState = new CheckerState(player, cloneConfig, parentState);
		return childState;
	}

	public void closeStream() throws IOException{
		writer.close();
	}

	public void write(String value) throws IOException{
		writer.write(value);
		writer.write("\n");
	}

	public void writeNewLine() throws IOException{
		writer.write("\n");
	}
}
