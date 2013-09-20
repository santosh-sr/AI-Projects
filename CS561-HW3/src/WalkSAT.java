import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WalkSAT {
	private List<Clause> clausesList;
	private List<Clause> unsatisifiedClausesList;
	private float probability;
	private int max_flips;
	private int max_flips_iteration;
	private int guests;
	private int tables;

	public WalkSAT(List<Clause> knowledgeBaseList, float probability, int max_flips, int guests, int tables){
		this.clausesList = new LinkedList<Clause>(knowledgeBaseList);
		this.unsatisifiedClausesList = new LinkedList<Clause>();
		this.max_flips = max_flips;
		this.probability = probability;
		this.guests = guests;
		this.tables = tables;
	}

	public boolean walkSAT(){
		Map<Literal.Symbol, Boolean> randomModel = generateRandomModel();
		Random r = new Random();
		for(int i=1; i<=max_flips; i++){
			max_flips_iteration++;
			if(satisfyKB(randomModel)){
				return true;
			}

			int size = unsatisifiedClausesList.size();
			if(size > 0){
				int random = r.nextInt(size);
				Clause unsatisfiedClause = unsatisifiedClausesList.get(random);
				
				float p = r.nextFloat();
				if(p <= probability){
					randomModel = minConflicts(unsatisfiedClause, randomModel);
				}else{
					randomModel = randomWalk(unsatisfiedClause, randomModel);
				}
			}
		}

		return false;
	}

	private Map<Literal.Symbol, Boolean> minConflicts(Clause unsatisfiedClause, Map<Literal.Symbol, Boolean> randomModel){
		Map<Literal.Symbol, Boolean> maxSatisifiabilityMap = null;
		Map<Literal.Symbol, Boolean> satisfiabilityMap = new HashMap<Literal.Symbol, Boolean>(randomModel);
		List<Clause> satisfiedClauses = new LinkedList<Clause>();
		int satisfiabilityCount = 0;
		for(Literal literal : unsatisfiedClause.getLiteralsList()){
			Literal.Symbol symbol = literal.getSymbol();
			boolean value = satisfiabilityMap.get(symbol);
			if(!literal.value(value)){
				satisfiabilityMap.put(symbol, !value);
			}

			satisfiedClauses = getSatisfiedClauses(satisfiabilityMap);
			int size = satisfiedClauses.size();
			if(size > satisfiabilityCount){
				satisfiabilityCount = size;
				maxSatisifiabilityMap = new HashMap<Literal.Symbol, Boolean>(satisfiabilityMap);
			}
			
			//Restore the original model
			satisfiabilityMap.put(symbol, value);
		}

		return maxSatisifiabilityMap;
	}

	private Map<Literal.Symbol, Boolean> randomWalk(Clause unsatisfiedClause, Map<Literal.Symbol, Boolean> randomModel){

		Random r = new Random();
		List<Literal> literalsList = unsatisfiedClause.getLiteralsList();
		
		int random_x = r.nextInt(literalsList.size());
		Literal literal = literalsList.get(random_x);
		Literal.Symbol symbol = literal.getSymbol();
		boolean value = randomModel.get(symbol);
		if(!literal.value(value)){
			boolean flip = (value) ? false : true;
			randomModel.put(symbol, flip);
		}

		return randomModel;
	}

	public int getMax_flips_iteration() {
		return max_flips_iteration;
	}

	private Map<Literal.Symbol, Boolean> generateRandomModel(){
		Map<Literal.Symbol, Boolean> valuesMap = new HashMap<Literal.Symbol, Boolean>();
		Random r = new Random();
		Literal literal = new Literal();
		for(int i=1; i<=guests; i++){
			for(int j=1; j<=tables; j++){
				float random_x = r.nextFloat();
				if(random_x <= 0.5){
					valuesMap.put(literal.new Symbol(i, j), true);
				}else{
					valuesMap.put(literal.new Symbol(i, j), false);
				}
			}
		}

		return valuesMap;
	}

	public boolean satisfyKB(Map<Literal.Symbol, Boolean> valuesMap){
		boolean satisfyKB = true;
		unsatisifiedClausesList.clear();
		for(Clause clause : clausesList){
			if(!clause.isSatisfiable(valuesMap)){
				unsatisifiedClausesList.add(clause);
				satisfyKB = false;
			}
		}

		return satisfyKB;
	}

	public List<Clause> getSatisfiedClauses(Map<Literal.Symbol, Boolean> valuesMap){
		List<Clause> satisfiedClauses = new LinkedList<Clause>();
		for(Clause clause : clausesList){
			if(clause.isSatisfiable(valuesMap)){
				satisfiedClauses.add(clause);
			}
		}

		return satisfiedClauses;
	}
}
