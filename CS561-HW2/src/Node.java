

public class Node {
	/**
	 * Profile Name
	 */
	private String userName;
	
	/**
	 * Time taken to send the message
	 */
	private int timeTaken;
	
	/**
	 * Risk factor to send the message
	 */
	private int riskFactor;
	
	/**
	 * Heuristic time cost to reach the goal
	 */
	private int directTimeCost;
	
	/**
	 * Heuristic risk factor to reach the goal
	 */
	private int directRiskCost;

	/**
	 * Constructor 
	 * 
	 * @param user_name
	 * @param time_taken
	 * @param risk_factor
	 */
	public Node(String user_name, int time_taken, int risk_factor){
		this.userName = user_name;
		this.timeTaken = time_taken;
		this.riskFactor = risk_factor;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String user_name) {
		this.userName = user_name;
	}

	public int getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(int time_taken) {
		this.timeTaken = time_taken;
	}

	public int getRiskFactor() {
		return riskFactor;
	}

	public void setRiskFactor(int risk_factor) {
		this.riskFactor = risk_factor;
	}
	
	public int getDirectTimeCost() {
		return directTimeCost;
	}

	public void setDirectTimeCost(int directTimeCost) {
		this.directTimeCost = directTimeCost;
	}

	public int getDirectRiskCost() {
		return directRiskCost;
	}

	public void setDirectRiskCost(int directRiskCost) {
		this.directRiskCost = directRiskCost;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + directRiskCost;
		result = prime * result + directTimeCost;
		result = prime * result + riskFactor;
		result = prime * result + timeTaken;
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (directRiskCost != other.directRiskCost)
			return false;
		if (directTimeCost != other.directTimeCost)
			return false;
		if (riskFactor != other.riskFactor)
			return false;
		if (timeTaken != other.timeTaken)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Node [userName=" + userName + ", timeTaken=" + timeTaken
				+ ", riskFactor=" + riskFactor + ", directTimeCost="
				+ directTimeCost + ", directRiskCost=" + directRiskCost + "]";
	}

}
