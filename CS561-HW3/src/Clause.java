import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;



public class Clause {
	private UUID clauseName;
	private List<Literal> literalsList;
	private Clause parentClause;
	private Clause otherParentClause;

	public Clause(){
		this.clauseName = UUID.randomUUID();
		literalsList = new LinkedList<Literal>();
	}

	public List<Literal> getLiteralsList(){
		return literalsList;
	}

	public void addLiteral(Literal literal){
		if(literal == null){
			throw new IllegalArgumentException();
		}

		literalsList.add(literal);
	}

	public boolean isSatisfiable(Map<Literal.Symbol, Boolean> valMap){
		for(Literal literal : literalsList){
			boolean model = valMap.get(literal.getSymbol());
			boolean literalValue = literal.value(model);

			if(literalValue){
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;

		Clause other = (Clause)obj;
		if(!this.clauseName.equals(other.clauseName)){
			return false;
		}

		if(this.literalsList.size() != other.getLiteralsList().size()){
			return false;
		}

		for(Literal literal : literalsList){
			if(!other.getLiteralsList().contains(literal)){
				return false;
			}
		}

		return true;
	}
	
	public boolean isSameLiterals(Clause clause){
		List<Literal> other_literalsList = clause.getLiteralsList();
		if(this.literalsList.size() != other_literalsList.size()){
			return false;
		}

		for(Literal literal : literalsList){
			if(!other_literalsList.contains(literal)){
				return false;
			}
		}
		
		return true;
	}
	
	public boolean containsComplimentLiteral(Clause clause){
		for(Literal literal : literalsList){
			for(Literal other : clause.getLiteralsList()){
				if(literal.isComplimentLiteral(other)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public int complimentLiteralsCount(Clause clause){
		int count = 0;
		for(Literal literal : literalsList){
			for(Literal other : clause.getLiteralsList()){
				if(literal.isComplimentLiteral(other)){
					++count;
				}
			}
		}
		
		return count;
	}
	
	public boolean containsMoreThanOneComplimentLiteral(Clause clause){
		int count = 0;
		for(Literal literal : literalsList){
			for(Literal other : clause.getLiteralsList()){
				if(literal.isComplimentLiteral(other)){
					count++;
				}
			}
		}
		
		return (count > 0 && count <= 1) ? false : true;
	}
	
	public boolean containsLiteral(Literal literal){
		for(Literal other : literalsList){
			if(literal.equals(other)){
				return true;
			}
		}
		
		return false;
	}

	public UUID getClauseName() {
		return clauseName;
	}

	public Clause getParentClause() {
		return parentClause;
	}

	public void setParentClause(Clause parentClause) {
		this.parentClause = parentClause;
	}

	public Clause getOtherParentClause() {
		return otherParentClause;
	}

	public void setOtherParentClause(Clause otherParentClause) {
		this.otherParentClause = otherParentClause;
	}

	@Override
	public String toString() {
		String cnfClause = "";
		boolean firstIteration = true;
		for(Literal literal : literalsList){
			if(firstIteration){
				cnfClause += literal.toString();
				firstIteration = false;
			}else{
				cnfClause += " or " + literal.toString();
			}
		}

		return "[" + cnfClause + "]";
	}

}
