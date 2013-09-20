

public class DirectCost {

	private String nodeName;
	
	private int directTime;
	
	private int directRisk;
	
	public DirectCost(String nodeName, int directTime, int directRisk){
		this.nodeName = nodeName;
		this.directTime = directTime;
		this.directRisk = directRisk;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public int getDirectTime() {
		return directTime;
	}

	public void setDirectTime(int directTime) {
		this.directTime = directTime;
	}

	public int getDirectRisk() {
		return directRisk;
	}

	public void setDirectRisk(int directRisk) {
		this.directRisk = directRisk;
	}
}
