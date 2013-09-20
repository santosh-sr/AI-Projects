import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class PLResolution {

	private List<Clause> knowledgeBaseList;
	private List<Clause> clausesList;
	private List<Clause> resolvedClauseList;
	private Map<Clause, List<Clause>> resolvedPairsMap;

	public PLResolution(List<Clause> knowledgeBaseList){
		this.knowledgeBaseList = knowledgeBaseList;
		this.clausesList = new LinkedList<Clause>(knowledgeBaseList);
		this.resolvedPairsMap = new HashMap<Clause, List<Clause>>();

		resolvedClauseList = new LinkedList<Clause>();
	}

	public Clause resolve(Clause clause, Clause other_clause){
		if(clause.containsComplimentLiteral(other_clause) && !clause.containsMoreThanOneComplimentLiteral(other_clause)){
			Clause new_clause = new Clause(); 
			Literal resolvedLiteral = null;
			List<Literal> literalsList = clause.getLiteralsList();
			List<Literal> other_literalsList = other_clause.getLiteralsList();

			for(Literal literal : literalsList){
				for(Literal other : other_literalsList){
					if(literal.isComplimentLiteral(other)){
						resolvedLiteral = other;
						break;
					}
				}
			}

			//Eliminate duplicate literals in the new resolved clause
			for(Literal literal : literalsList){
				if(!literal.isSamePrefix(resolvedLiteral) && !new_clause.containsLiteral(literal)){
					new_clause.addLiteral(literal);
				}
			}

			for(Literal literal : other_literalsList){
				if(!literal.isSamePrefix(resolvedLiteral) && !new_clause.containsLiteral(literal)){
					new_clause.addLiteral(literal);
				}
			}

			//Set the parent clauses for the new clause
			new_clause.setParentClause(clause);
			new_clause.setOtherParentClause(other_clause);

			return new_clause;
		}

		return null;
	}

	public boolean isSatisfiable(){
		
		while(true){
			resolvedClauseList.clear();
			for(int i=0; i<clausesList.size(); i++){
				for(int j=i+1; j<clausesList.size(); j++){
					Clause clause = clausesList.get(i);
					Clause otherClause = clausesList.get(j);

					if(!containsResolvedPairs(clause, otherClause)){
						//Add resolved pairs in map
						addResolvedPairs(clause, otherClause);
						
						Clause new_clause = resolve(clause, otherClause);
						if(new_clause != null){
							
							if(new_clause.getLiteralsList().isEmpty()){
								return false;
							}
							
							if(!containsNewClause(new_clause)){
								resolvedClauseList.add(new_clause);
							}
						}
					}
				}
			}

			if(resolvedClauseList.isEmpty()){
				return true;
			}
			clausesList.addAll(resolvedClauseList);
		}

	}

	private boolean containsNewClause(Clause new_clause){
		for(Clause clause : resolvedClauseList){
			if(clause.isSameLiterals(new_clause)){
				return true;
			}
		}
		
		for(Clause clause : clausesList){
			if(clause.isSameLiterals(new_clause)){
				return true;
			}
		}

		return false;
	}

	private boolean containsResolvedPairs(Clause clause, Clause otherClause){
		if(resolvedPairsMap.containsKey(clause)){
			List<Clause> pairClausesList = resolvedPairsMap.get(clause);
			if(pairClausesList.contains(otherClause)){
				return true;
			}
		}

		if(resolvedPairsMap.containsKey(otherClause)){
			List<Clause> pairClausesList = resolvedPairsMap.get(otherClause);
			if(pairClausesList.contains(clause)){
				return true;
			}
		}

		return false;
	}

	private void addResolvedPairs(Clause clause, Clause otherClause){

		//Add the resolved pairs as adjacency list. its bidirectional
		if(resolvedPairsMap.containsKey(clause)){
			List<Clause> pairClausesList = resolvedPairsMap.get(clause);
			pairClausesList.add(otherClause);
			resolvedPairsMap.put(clause, pairClausesList);
		}else{
			List<Clause> pairClausesList = new LinkedList<Clause>();
			pairClausesList.add(otherClause);
			resolvedPairsMap.put(clause, pairClausesList);
		}

		if(resolvedPairsMap.containsKey(otherClause)){
			List<Clause> pairClausesList = resolvedPairsMap.get(otherClause);
			pairClausesList.add(clause);
			resolvedPairsMap.put(otherClause, pairClausesList);
		}else{

			List<Clause> pairClausesList = new LinkedList<Clause>();
			pairClausesList.add(clause);
			resolvedPairsMap.put(otherClause, pairClausesList);

		}
	}

}
