import java.util.Arrays;



public class BoardConfiguration implements Cloneable{

	public static final int ROWS = 8; 
	public static final int COLUMNS = 8;

	public enum BoardPieces{
		ALICE_PIECE('A'), BILL_PIECE('B'), ALICE_KING_PIECE('k'), BILL_KING_PIECE('K'), 
		WHITE('+'), BLACK('O'); 

		private Character pieceValue;

		BoardPieces(char pieceValue){
			this.pieceValue = pieceValue;
		}

		public char getValue(){
			return pieceValue;
		}
	}

	private Character[][] board;
	private int newXPos;
	private int newYPos;
	private int fromXPos;
	private int fromYPos;
	private int noOfAlicePieces;
	private int noOfBillPieces;
	private int depth;

	public BoardConfiguration(){
		board = new Character[ROWS][COLUMNS];

		for(int row=0; row < ROWS; row++){
			for(int column=0; column < COLUMNS; column++){
				board[row][column] = getSquareColor(row, column);
			}
		}

		fromXPos = fromYPos = newXPos = newYPos = -1;
	}

	private Character getSquareColor(int row, int column) {
		Character piece;
		int mod = row + column % 2;
		if(mod == 0){
			piece = BoardPieces.WHITE.getValue();
		}else{
			piece = BoardPieces.BLACK.getValue();
		}
		return piece;
	}

	public BoardConfiguration(Character[][] board){
		this.board = board;

		for(int row=0; row < ROWS; row++){
			for(int column=0; column < COLUMNS; column++){
				if(isAlicePiece(this.board[row][column])){
					incrementAlicePiece();
				}else if(isBillPiece(this.board[row][column])){
					incrementBillPiece();
				}
			}
		}
	}

	public Character[][] getBoard(){
		return board;
	}

	public boolean containsPiece(int row, int column){
		char piece = getPiece(row, column);

		if(piece == BoardPieces.WHITE.getValue() || piece == BoardPieces.BLACK.getValue()){
			return false;
		}

		return true;
	}

	public char getPiece(int row, int column){
		return board[row][column];
	}

	public boolean movePieceToLeftForward(int row, int column){
		if(isLeftForwardDiagonalEmpty(row, column)){
			fromXPos = row;
			fromYPos = column;
			newXPos = row - 1;
			newYPos = column - 1;

			Character piece;
			if(newXPos <= 0){
				piece = BoardPieces.ALICE_KING_PIECE.getValue();
			}else{
				piece = board[row][column];
			}

			removePiece(row, column);
			board[newXPos][newYPos] = piece;
			return true;
		}

		return false;
	}

	public boolean movePieceToLeftForward(Character piece, int row, int column){
		if(isLeftForwardDiagonalEmpty(row, column)){
			fromXPos = row;
			fromYPos = column;
			newXPos = row - 1;
			newYPos = column - 1;

			if(newXPos <= 0){
				piece = BoardPieces.ALICE_KING_PIECE.getValue();
			}

			removePiece(row, column);
			board[newXPos][newYPos] = piece;
			return true;
		}

		return false;
	}

	public boolean movePieceToRightForward(int row, int column){
		if(isRightForwardDiagonalEmpty(row, column)){
			fromXPos = row;
			fromYPos = column;
			newXPos = row - 1;
			newYPos = column + 1;

			Character piece;
			if(newXPos <= 0){
				piece = BoardPieces.ALICE_KING_PIECE.getValue();
			}else{
				piece = board[row][column];
			}

			removePiece(row, column);
			board[newXPos][newYPos] = piece;
			return true;
		}

		return false;
	}

	public boolean movePieceToRightForward(Character piece, int row, int column){
		if(isRightForwardDiagonalEmpty(row, column)){
			fromXPos = row;
			fromYPos = column;
			newXPos = row - 1;
			newYPos = column + 1;

			if(newXPos <= 0){
				piece = BoardPieces.ALICE_KING_PIECE.getValue();
			}

			removePiece(row, column);
			board[newXPos][newYPos] = piece;
			return true;
		}

		return false;
	}

	public boolean movePieceToLeftDownward(int row, int column){
		if(isLeftDownwardDiagonalEmpty(row, column)){
			fromXPos = row;
			fromYPos = column;
			newXPos = row + 1;
			newYPos = column + 1;

			Character piece;
			if(newXPos >= ROWS-1){
				piece = BoardPieces.BILL_KING_PIECE.getValue();
			}else{
				piece = board[row][column];
			}

			removePiece(row, column);
			board[newXPos][newYPos] = piece;
			return true;
		}

		return false;
	}

	public boolean movePieceToLeftDownward(Character piece, int row, int column){
		if(isLeftDownwardDiagonalEmpty(row, column)){
			fromXPos = row;
			fromYPos = column;
			newXPos = row + 1;
			newYPos = column + 1;

			if(newXPos >= ROWS-1){
				piece = BoardPieces.BILL_KING_PIECE.getValue();
			}

			removePiece(row, column);
			board[newXPos][newYPos] = piece;
			return true;
		}

		return false;
	}

	public boolean movePieceToRightDownward(int row, int column){
		if(isRightDownwardDiagonalEmpty(row, column)){
			fromXPos = row;
			fromYPos = column;
			newXPos = row + 1;
			newYPos = column - 1;

			Character piece;
			if(newXPos >= ROWS-1){
				piece = BoardPieces.BILL_KING_PIECE.getValue();
			}else{
				piece = board[row][column];
			}

			removePiece(row, column);
			board[newXPos][newYPos] = piece;
			return true;
		}

		return false;
	}

	public boolean movePieceTORightDownward(Character piece, int row, int column){
		if(isRightDownwardDiagonalEmpty(row, column)){
			fromXPos = row;
			fromYPos = column;
			newXPos = row + 1;
			newYPos = column - 1;

			if(newXPos >= ROWS-1){
				piece = BoardPieces.BILL_KING_PIECE.getValue();
			}

			removePiece(row, column);
			board[newXPos][newYPos] = piece;
			return true;
		}

		return false;
	}

	public boolean isLeftForwardDiagonalEmpty(int row, int column){
		int lRow = row - 1;
		int lColumn = column - 1;

		if(isValidLeftForward(lRow, lColumn)){
			return !containsPiece(lRow, lColumn);
		}

		return false;
	}

	public boolean isRightForwardDiagonalEmpty(int row, int column){
		int lRow = row - 1;
		int lColumn = column + 1;

		if(isValidRightForward(lRow, lColumn)){
			return !containsPiece(lRow, lColumn);
		}

		return false;
	}

	public boolean isLeftDownwardDiagonalEmpty(int row, int column){
		int lRow = row + 1;
		int lColumn = column + 1;

		if(isValidLeftDownward(lRow, lColumn)){
			return !containsPiece(lRow, lColumn);
		}

		return false;
	}

	public boolean isRightDownwardDiagonalEmpty(int row, int column){
		int lRow = row + 1;
		int lColumn = column - 1;

		if(isValidRightDownward(lRow, lColumn)){
			return !containsPiece(lRow, lColumn);
		}

		return false;
	}

	public Character getLeftForwardPiece(int row, int column){
		int lRow = row - 1;
		int lColumn = column - 1;

		if(isValidLeftForward(lRow, lColumn)){
			return board[lRow][lColumn];
		}

		return null;
	}

	public Character getRightForwardPiece(int row, int column){
		int lRow = row - 1;
		int lColumn = column + 1;

		if(isValidRightForward(lRow, lColumn)){
			return board[lRow][lColumn];
		}

		return null;
	}

	public Character getLeftDownwardPiece(int row, int column){
		int lRow = row + 1;
		int lColumn = column + 1;

		if(isValidLeftDownward(lRow, lColumn)){
			return board[lRow][lColumn];
		}

		return null;
	}

	public Character getRightDownwardPiece(int row, int column){
		int lRow = row + 1;
		int lColumn = column - 1;

		if(isValidRightDownward(lRow, lColumn)){
			return board[lRow][lColumn];
		}

		return null;
	}

	public boolean canLeftForwardJump(Player player, int row, int column){
		Character piece = board[row][column];

		if(isAlice(player)){
			if(isAlicePiece(piece)){
				if(isBillPiece(getLeftForwardPiece(row, column))){
					int lrow = row - 1;
					int lcolumn = column - 1;

					return isLeftForwardDiagonalEmpty(lrow, lcolumn);
				}
			}
		}else{
			if(isBillPiece(piece)){
				if(isAlicePiece(getLeftForwardPiece(row, column))){
					int lrow = row - 1;
					int lcolumn = column - 1;

					return isLeftForwardDiagonalEmpty(lrow, lcolumn);
				}
			}
		}

		return false;
	}

	public boolean canRightForwardJump(Player player, int row, int column){
		Character piece = board[row][column];

		if(isAlice(player)){
			if(isAlicePiece(piece)){
				if(isBillPiece(getRightForwardPiece(row, column))){
					int lrow = row - 1;
					int lcolumn = column + 1;

					return isRightForwardDiagonalEmpty(lrow, lcolumn);
				}
			}
		}else{
			if(isBillPiece(piece)){
				if(isAlicePiece(getRightForwardPiece(row, column))){
					int lrow = row - 1;
					int lcolumn = column + 1;

					return isRightForwardDiagonalEmpty(lrow, lcolumn);
				}
			}
		}

		return false;
	}

	public boolean canLeftBackwardJump(Player player, int row, int column){
		Character piece = board[row][column];

		if(isAlice(player)){
			if(isAlicePiece(piece)){
				if(isBillPiece(getLeftDownwardPiece(row, column))){
					int lrow = row + 1;
					int lcolumn = column + 1;

					return isLeftDownwardDiagonalEmpty(lrow, lcolumn);
				}
			}
		}else{
			if(isBillPiece(piece)){
				if(isAlicePiece(getLeftDownwardPiece(row, column))){
					int lrow = row + 1;
					int lcolumn = column + 1;

					return isLeftDownwardDiagonalEmpty(lrow, lcolumn);
				}
			}
		}

		return false;
	}

	public boolean canRightBackwardJump(Player player, int row, int column){
		Character piece = board[row][column];

		if(isAlice(player)){
			if(isAlicePiece(piece)){
				if(isBillPiece(getRightDownwardPiece(row, column))){
					int lrow = row + 1;
					int lcolumn = column - 1;

					return isRightDownwardDiagonalEmpty(lrow, lcolumn);
				}
			}
		}else{
			if(isBillPiece(piece)){
				if(isAlicePiece(getRightDownwardPiece(row, column))){
					int lrow = row + 1;
					int lcolumn = column - 1;

					return isRightDownwardDiagonalEmpty(lrow, lcolumn);
				}
			}
		}

		return false;
	}

	public void moveLeftForwardJump(Player player, int row, int column){
		if(canLeftForwardJump(player, row, column)){
			int lRow = row - 1;
			int lColumn = column - 1;

			Character piece = board[row][column];
			removePiece(row, column);
			removePiece(lRow, lColumn);
			fromXPos = row;
			fromYPos = column;
			newXPos = lRow-1;
			newYPos = lColumn-1;

			if(newXPos <= 0){
				if(isAlice(player) && !isAliceKingPiece(piece)){
					piece = BoardPieces.ALICE_KING_PIECE.getValue();
				}
			}
			board[newXPos][newYPos]= piece;
		}
	}

	private boolean isAlice(Player player) {
		return player == Player.ALICE;
	}

	public void moveRightForwardJump(Player player, int row, int column){
		if(canRightForwardJump(player, row, column)){
			int lRow = row - 1;
			int lColumn = column + 1;

			Character piece = board[row][column];
			removePiece(row, column);
			removePiece(lRow, lColumn);
			fromXPos = row;
			fromYPos = column;
			newXPos = lRow-1;
			newYPos = lColumn+1;
			
			if(newXPos <= 0){
				if(isAlice(player) && !isAliceKingPiece(piece)){
					piece = BoardPieces.ALICE_KING_PIECE.getValue();
				}
			}
			board[newXPos][newYPos]= piece;
		}
	}

	public int getNewXPos() {
		return newXPos;
	}

	public int getNoOfAlicePieces() {
		return noOfAlicePieces;
	}

	public int getNoOfBillPieces() {
		return noOfBillPieces;
	}

	public void decrementAlicePiece(){
		--noOfAlicePieces;
	}

	public void decrementBillPiece(){
		--noOfBillPieces;
	}

	public void incrementAlicePiece(){
		++noOfAlicePieces;
	}

	public void incrementBillPiece(){
		++noOfBillPieces;
	}

	public int getNewYPos() {
		return newYPos;
	}

	public void incrementDepth(){
		++depth;
	}

	public void moveLeftBackwardJump(Player player, int row, int column){
		if(canLeftBackwardJump(player, row, column)){
			int lRow = row + 1;
			int lColumn = column + 1;

			Character piece = board[row][column];
			removePiece(row, column);
			removePiece(lRow, lColumn);
			fromXPos = row;
			fromYPos = column;
			newXPos = lRow+1;
			newYPos = lColumn+1;
			
			if(newXPos >= ROWS-1){
				if(isBill(player) && !isBillKingPiece(piece)){
					piece = BoardPieces.BILL_KING_PIECE.getValue();
				}
			}
			board[newXPos][newYPos]= piece;
		}
	}

	private boolean isBill(Player player) {
		return player == Player.BILL;
	}

	public void moveRightBackwardJump(Player player, int row, int column){
		if(canRightBackwardJump(player, row, column)){
			int lRow = row + 1;
			int lColumn = column - 1;

			Character piece = board[row][column];
			removePiece(row, column);
			removePiece(lRow, lColumn);
			fromXPos = row;
			fromYPos = column;
			newXPos = lRow+1;
			newYPos = lColumn-1;
			
			if(newXPos >= ROWS-1){
				if(isBill(player) && !isBillKingPiece(piece)){
					piece = BoardPieces.BILL_KING_PIECE.getValue();
				}
			}
			board[newXPos][newYPos]= piece;
		}
	}

	private boolean isValidLeftForward(int lRow, int lColumn) {
		return lRow >= 0 && lColumn >= 0;
	}

	private void removePiece(int row, int column){
		if(containsPiece(row, column)){
			board[row][column] = getSquareColor(row, column);
		}
	}

	private boolean isValidRightForward(int lRow, int lColumn) {
		return lRow >= 0 && lColumn <= (COLUMNS-1);
	}

	private boolean isValidLeftDownward(int lRow, int lColumn) {
		return lRow <= (ROWS-1) && lColumn <= (COLUMNS-1);
	}

	private boolean isValidRightDownward(int lRow, int lColumn) {
		return lRow <= (ROWS-1) && lColumn >= 0;
	}

	public boolean isEmpty(Character pieceValue){
		char charValue = pieceValue.charValue();
		return (pieceValue == null) ? false : ((BoardPieces.BLACK.getValue() == charValue) || (BoardPieces.WHITE.getValue() == charValue));

	}

	public boolean isAlicePiece(Character pieceValue){
		return (pieceValue == null) ? false : isAliceNormalPiece(pieceValue) || isAliceKingPiece(pieceValue);
	}

	private boolean isAliceNormalPiece(Character pieceValue) {
		return (pieceValue == null) ? false : pieceValue.equals(BoardPieces.ALICE_PIECE.getValue());
	}

	public boolean isAliceKingPiece(Character pieceValue){
		return (pieceValue == null) ? false : pieceValue.equals(BoardPieces.ALICE_KING_PIECE.getValue());
	}

	public boolean isPlayerPiece(Player player, Character pieceValue){
		if(isAlice(player)){
			return isAlicePiece(pieceValue);
		}

		return isBillPiece(pieceValue);
	}

	@Override
	protected BoardConfiguration clone() throws CloneNotSupportedException {

		Character[][] cloneBoard = new Character[ROWS][COLUMNS];
		for (int i = 0; i < COLUMNS; i++) {
			cloneBoard[i] = Arrays.copyOf(board[i], board[i].length);
		}

		BoardConfiguration cloneConfiguration = new BoardConfiguration(cloneBoard);
		cloneConfiguration.fromXPos = this.fromXPos;
		cloneConfiguration.fromYPos = this.fromYPos;
		cloneConfiguration.newXPos = this.newXPos;
		cloneConfiguration.newYPos = this.newYPos;
		cloneConfiguration.noOfAlicePieces = this.noOfAlicePieces;
		cloneConfiguration.noOfBillPieces = this.noOfBillPieces;
		cloneConfiguration.depth = this.depth;

		return cloneConfiguration;
	}

	public boolean isBillPiece(Character pieceValue){
		return ((pieceValue == null) ? false : (isBillNormalPiece(pieceValue) || isBillKingPiece(pieceValue)));
	}

	private boolean isBillNormalPiece(Character pieceValue) {
		return (pieceValue == null) ? false : pieceValue.equals(BoardPieces.BILL_PIECE.getValue());
	}

	public boolean isBillKingPiece(Character pieceValue){
		return (pieceValue == null) ? false : pieceValue.equals(BoardPieces.BILL_KING_PIECE.getValue());
	}

	public int calcUtilityValue(){
		int alicePieces = 0;
		int billPieces = 0;
		for(int row=0; row < ROWS; row++){
			for(int column=0; column < COLUMNS; column++){
				Character piece = board[row][column];
				if(!isEmpty(piece)){
					if(isAliceKingPiece(piece)){
						alicePieces = alicePieces + 2;
					}else if(isAlicePiece(piece)){
						alicePieces++;
					}else if(isBillKingPiece(piece)){
						billPieces = billPieces + 2;
					}else if(isBillNormalPiece(piece)){
						billPieces++;
					}
				}
			}
		}

		return alicePieces - billPieces;
	}

	public int getFromXPos() {
		return fromXPos;
	}

	public int getFromYPos() {
		return fromYPos;
	}

	public int getDepth(){
		return depth;
	}
}
