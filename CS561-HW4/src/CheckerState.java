import java.util.ArrayList;
import java.util.List;


public class CheckerState {
	private BoardConfiguration boardConfig;
	
	private CheckerState parentState;
	
	private List<CheckerState> childStateList;
	
	private int utilityValue;
	
	private int alpha;
	
	private int beta;
	
	private Player player;
	
	private List<CheckerState> prunedStateList;
	
	public CheckerState(Player player, BoardConfiguration boardConfig, CheckerState parentState){
		this.player = player;
		this.boardConfig = boardConfig;
		this.parentState = parentState;
		this.childStateList = new ArrayList<CheckerState>();
		this.prunedStateList = new ArrayList<CheckerState>();
		utilityValue = 999;
	}

	public int getUtilityValue() {
		return utilityValue;
	}

	public void setUtilityValue(int utilityValue) {
		this.utilityValue = utilityValue;
	}

	public CheckerState getParentState() {
		return parentState;
	}

	public void setParentState(CheckerState parentState) {
		this.parentState = parentState;
	}

	public Player getPlayer() {
		return player;
	}

	public BoardConfiguration getBoardConfiguration() {
		return boardConfig;
	}

	public void setBoardConfiguration(BoardConfiguration boardConfig) {
		this.boardConfig = boardConfig;
	}
	
	public void addCheckerState(CheckerState checkerState){
		this.childStateList.add(checkerState);
	}
	
	public List<CheckerState> getChildStateList() {
		return childStateList;
	}

	public void setChildStateList(List<CheckerState> childStateList) {
		this.childStateList = childStateList;
	}

	public void addPrunedState(CheckerState checkerState){
		this.prunedStateList.add(checkerState);
	}
	
	public void setPrunedStateList(List<CheckerState> prunedStateList) {
		this.prunedStateList = prunedStateList;
	}
	
	public List<CheckerState> getPrunedStateList(){
		return prunedStateList;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public int getBeta() {
		return beta;
	}

	public void setBeta(int beta) {
		this.beta = beta;
	}
	
}
