import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HW3 {

	private static final String PATH_SEPARATOR = System.getProperty("file.separator");
	private ClauseGenerator cg;
	private List<int [][]> instanceList;

	public HW3(){
		cg = new ClauseGenerator();
		instanceList = new LinkedList<int [][]>();
	}

	public static void main(String[] args) throws NumberFormatException, IOException {

		if(args.length <= 0){
			System.err.println("Specify the experiment number...");
			return;
		}

		String arg = args[0];

		//Create the Main instance
		HW3 hw3 = new HW3();
		if(arg.equals("-exp1")){
			int guests = 16;
			int tables = 2;

			hw3.experiment1(guests, tables);
		}else if(arg.equals("-exp2")){
			int guests = 16;
			int tables = 2;

			hw3.experiment2(guests, tables);
		}else if(arg.equals("-exp3")){
			hw3.experiment3();
		}

	}

	private static boolean isEnemy(int val) {
		return val == RelationshipGenerator.ENEMY;
	}

	private static boolean isFriend(int val) {
		return val == RelationshipGenerator.FRIEND;
	}

	/**
	 * Experiment #1
	 * 
	 * @param guests
	 * @param tables
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private void experiment1(int guests, int tables) throws NumberFormatException, IOException{
		int walksat_probability;
		int maxflips;
		int f_percent = 0;
		int e_percent;

		walksat_probability = 50;
		maxflips = 100;

		RelationshipGenerator relGenerator = new RelationshipGenerator();
		int relationship = (guests * (guests - 1))/2;
		int plSatisifiabilityCount;
		int walkSatSatisfiabilityCount;
		float p = (float)walksat_probability/100;

		File file = new File(getFilePath("exp1.csv"));
		if(!file.exists()){
			file.createNewFile();
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		writer.write("Guests, Tables, f-percent, e-percent, max flips, pl-resolution satisfiability, walksat satisfiability");
		writer.write("\n");

		System.out.println("Guests: " + guests);
		System.out.println("Tables: " + tables);
		System.out.println("f-percent: " + f_percent);
		System.out.println("Walk SAT Probability: " + walksat_probability);
		System.out.println("\n");

		//e-percent [2%-20%]
		for(e_percent = 2; e_percent <= 20; e_percent = e_percent+2){
			plSatisifiabilityCount = 0;
			walkSatSatisfiabilityCount = 0;

			//Calculate the number of friends and enemies
			int friends = (int)Math.ceil((((float)f_percent/100) * relationship));
			int enemies = (int)Math.ceil((((float)e_percent/100) * relationship));

			//Run for 100 instances
			int instanceCount = 0;
			while(instanceCount < 100){
				int[][] relationshipMatrix = relGenerator.generateRelationshipMatrix(guests, friends, enemies);

				//Check the instance matrix is already being resolved with the algorithm
				if(!containsInstanceMatrix(relationshipMatrix, guests)){
					instanceList.add(relationshipMatrix);
					instanceCount++;

					List<Clause> knowledgeBaseList = generateKB(guests, tables,
							relationshipMatrix);

					//PL Resolution
					PLResolution pl = new PLResolution(knowledgeBaseList);
					boolean pl_satisfiable = pl.isSatisfiable();
					if(pl_satisfiable){
						//Increment the satisfiability count
						plSatisifiabilityCount++;
					}

					//Walk SAT Algorithm
					WalkSAT ws = new WalkSAT(knowledgeBaseList, (float)p, maxflips, guests, tables);
					boolean walkSAT = ws.walkSAT();
					if(walkSAT){
						//Increment the walk sat satisfiability count
						walkSatSatisfiabilityCount++;
					}
				}
			}

			System.out.println("[");
			System.out.println("e-percent: " + e_percent);
			System.out.println("PL Resolution Satisfiability Count: " + plSatisifiabilityCount);
			System.out.println("Walk SAT Satisfiability Count: " + walkSatSatisfiabilityCount);
			System.out.println("]");
			System.out.println("\n");
			writer.write(guests + "," + tables + "," + f_percent + "," + e_percent + "," + maxflips + "," + plSatisifiabilityCount 
					+ "," + walkSatSatisfiabilityCount);
			writer.write("\n");

			instanceList.clear();
		}

		instanceList.clear();
		writer.close();
	}

	/**
	 * EXperiment #2
	 * 
	 * @param guests
	 * @param tables
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private void experiment2(int guests, int tables) throws NumberFormatException, IOException{
		int walksat_probability;
		int f_percent;
		int e_percent = 5;

		walksat_probability = 50;
		List<Integer> maxFlipsList = new LinkedList<Integer>();
		maxFlipsList.add(100);
		maxFlipsList.add(500);
		maxFlipsList.add(1000);

		System.out.println("Guests: " + guests);
		System.out.println("Tables: " + tables);
		System.out.println("walksat_probability:  " + walksat_probability);
		System.out.println("\n");

		RelationshipGenerator relGenerator = new RelationshipGenerator();
		int relationship = (guests * (guests - 1))/2;
		int _100_walkSatSatisfiabilityCount;
		int _500_walkSatSatisfiabilityCount;
		int _1000_walkSatSatisfiabilityCount;

		float p = (float)walksat_probability/100;

		File file = new File(getFilePath("exp2.csv"));
		file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		writer.write("Guests, Tables, f-percent, e-percent, walksat probability, max flips, satisfiability count");
		writer.write("\n");
		for(f_percent = 2; f_percent <= 20; f_percent = f_percent+2){

			//Calculate the number of friends and enemies
			int friends = (int)Math.ceil((((float)f_percent/100) * relationship));
			int enemies = (int)Math.ceil((((float)e_percent/100) * relationship));

			_100_walkSatSatisfiabilityCount = 0;
			_500_walkSatSatisfiabilityCount = 0;
			_1000_walkSatSatisfiabilityCount = 0;

			int instanceCount = 0;
			while(instanceCount < 100){
				int[][] relationshipMatrix = relGenerator.generateRelationshipMatrix(guests, friends, enemies);

				if(!containsInstanceMatrix(relationshipMatrix, guests)){
					instanceList.add(relationshipMatrix);
					instanceCount++;

					List<Clause> knowledgeBaseList = generateKB(guests, tables,
							relationshipMatrix);

					for(Integer maxflips : maxFlipsList){
						WalkSAT ws = new WalkSAT(knowledgeBaseList, (float)p, maxflips, guests, tables);
						boolean walkSAT = ws.walkSAT();
						if(walkSAT){
							//Increment the count based on max flips
							if(maxflips == 100){
								_100_walkSatSatisfiabilityCount++;
							}else if(maxflips == 500){
								_500_walkSatSatisfiabilityCount++;
							}else if(maxflips == 1000){
								_1000_walkSatSatisfiabilityCount++;
							}
						}
					}
				}
			}

			System.out.println("[");
			System.out.println("f-percent: " + f_percent);
			System.out.println("e-percent: " + e_percent);
			System.out.println("max flips: " + 100);
			System.out.println("satisfiability count: " + _100_walkSatSatisfiabilityCount);
			System.out.println("max flips: " + 500);
			System.out.println("satisfiability count: " + _500_walkSatSatisfiabilityCount);
			System.out.println("max flips: " + 1000);
			System.out.println("satisfiability count: " + _1000_walkSatSatisfiabilityCount);
			System.out.println("]");

			writer.write(guests + "," + tables + "," + f_percent + "," + e_percent + "," + walksat_probability + "," + 100 
					+ "," + _100_walkSatSatisfiabilityCount);
			writer.write("\n");
			writer.write(guests + "," + tables + "," + f_percent + "," + e_percent + "," + walksat_probability + "," + 500 
					+ "," + _500_walkSatSatisfiabilityCount);
			writer.write("\n");
			writer.write(guests + "," + tables + "," + f_percent + "," + e_percent + "," + walksat_probability + "," + 1000 
					+ "," + _1000_walkSatSatisfiabilityCount);
			writer.write("\n");
			System.out.println("\n");
			instanceList.clear();
			//writer.write("\n");
		}

		instanceList.clear();
		writer.close();
	}

	/**
	 * Experiment #3
	 * 
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private void experiment3() throws NumberFormatException, IOException{
		int f_percent = 2;
		int e_percent = 2;

		int walksat_probability = 50;
		int max_flips_iteration;
		int maxflips = 1000;
		int tables;
		int friends;
		int enemies;
		int walkSatSatisfiabilityCount;

		//Execute the walk sat algorithm for different wedding size
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(16, 2);
		map.put(24, 3);
		map.put(32, 4);
		map.put(40, 5);
		map.put(48, 6);

		float p = (float)walksat_probability/100;

		//Write in the file
		File file = new File(getFilePath("exp3.csv"));
		file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		writer.write("Guests, Tables, f-percent, e-percent, # friends, # enemies, satisfiable instance, no of iterations, no of clauses, no of symbols");
		writer.write("\n");
		RelationshipGenerator relGenerator = new RelationshipGenerator();
		for(Integer guests : map.keySet()){
			tables = map.get(guests);

			System.out.println("Guests: " + guests);
			System.out.println("Tables: " + tables);
			System.out.println("Walk SAT Probability:  " + walksat_probability);
			System.out.println("\n");

			//Calculate the number of friends and enemies
			friends = Math.round(((float)f_percent/100) * 100);
			enemies = Math.round(((float)e_percent/100) * 100);

			int f_pairs;
			int e_pairs;
			walkSatSatisfiabilityCount = 0;
			while(walkSatSatisfiabilityCount < 100){
				f_pairs = e_pairs = 0;
				//Generate the random instance of relationship matrix
				int[][] relationshipMatrix = relGenerator.generateRelationshipMatrix(guests, friends, enemies);

				//Calculate the number of friends and enemies
				for(int i=0; i<guests; i++){
					for(int j=i; j<guests; j++){
						if(i != j){
							int val = relationshipMatrix[i][j];
							if(val == RelationshipGenerator.FRIEND){
								f_pairs++;
							}else if(val == RelationshipGenerator.ENEMY){
								e_pairs++;
							}
						}
					}
				}
				
				//Check the generated instance is not part of the list
				if(!containsInstanceMatrix(relationshipMatrix, guests)){
					instanceList.add(relationshipMatrix);

					//Generate KB with the constraints
					List<Clause> knowledgeBaseList = generateKB(guests, tables,
							relationshipMatrix);

					//Run the Walk SAT algorithm
					WalkSAT ws = new WalkSAT(knowledgeBaseList, (float)p, maxflips, guests, tables);
					boolean walkSAT = ws.walkSAT();

					//Check Walk SAT is satisfied for the instance
					if(walkSAT){
						max_flips_iteration = ws.getMax_flips_iteration();
						walkSatSatisfiabilityCount++;
						int symbols = getSymbols(knowledgeBaseList);

						//Console Print
						System.out.println("Instance: " + walkSatSatisfiabilityCount + " Iterations: " + max_flips_iteration);
						System.out.println("No of clauses: " + knowledgeBaseList.size());
						System.out.println("No of symbols: " + symbols);
						System.out.println("\n");

						//Write to the file
						writer.write(guests + "," + tables + "," + f_percent + "," + e_percent + "," + f_pairs + "," + e_pairs + "," + walkSatSatisfiabilityCount + "," + max_flips_iteration + "," + knowledgeBaseList.size() 
								+ "," + symbols);
						writer.write("\n");
					}
				}
			}

			System.out.println("\n");
			//Clear the instance list
			instanceList.clear();
		}

		//Clear the instance list
		instanceList.clear();
		writer.close();
	}

	/**
	 * Get the number of symbols in the clauses knowledge base list
	 * 
	 * @param knowledgeBaseList
	 * @return
	 */
	private int getSymbols(List<Clause> knowledgeBaseList){
		List<Literal.Symbol> symbolList = new LinkedList<Literal.Symbol>();
		for(Clause clause : knowledgeBaseList){
			List<Literal> literalsList = clause.getLiteralsList();
			for(Literal literal : literalsList){
				Literal.Symbol symbol = literal.getSymbol();
				if(!symbolList.contains(symbol)){
					symbolList.add(symbol);
				}
			}
		}

		return symbolList.size();
	}

	/**
	 * Generate KB for the wedding size with all the four constraints such as Atleast One, Atmost One, Friends and Enemy Constraint
	 * 
	 * @param guests
	 * @param tables
	 * @param relationshipMatrix
	 * @return
	 */
	private List<Clause> generateKB(int guests, int tables,
			int[][] relationshipMatrix) {
		//Atleast one table constraint
		List<Clause> atleastOne = cg.atleastOneTableConstraint(guests, tables);
		//Atmost one table constraint
		List<Clause> atmostOne = cg.atmostOneTableConstraint(guests, tables);
		List<Clause> friend = new LinkedList<Clause>();
		List<Clause> enemy = new LinkedList<Clause>();
		for(int i=0; i<guests; i++){
			for(int j=i; j<guests; j++){
				int val = relationshipMatrix[i][j];
				if(val <= RelationshipGenerator.NAN_VALUE){

					if(isFriend(val)){
						//Friend Constraint
						friend.addAll(cg.friendConstraint(i+1, j+1, tables));
					}else if(isEnemy(val)){
						//Enemy Constraint
						enemy.addAll(cg.enemyConstraint(i+1, j+1, tables));
					}
				}
			}
		}

		List<Clause> knowledgeBaseList = new LinkedList<Clause>();
		knowledgeBaseList.addAll(atleastOne);
		knowledgeBaseList.addAll(atmostOne);
		knowledgeBaseList.addAll(friend);
		knowledgeBaseList.addAll(enemy);
		return knowledgeBaseList;
	}

	private boolean containsInstanceMatrix(int[][] newMatrix, int guests){
		for(int [][] instanceMatrix : instanceList){
			if(isSameMatrix(newMatrix, guests,
					instanceMatrix)){
				return true;
			}
		}

		return false;
	}

	private boolean isSameMatrix(int[][] newMatrix, int guests
			, int[][] instanceMatrix) {
		for(int i=0; i<guests; i++){
			for(int j=0; j<guests; j++){
				if(instanceMatrix[i][j] != newMatrix[i][j]){
					return false;
				}
			}
		}
		return true;
	}

	private String getFilePath(String fileName){
		String path = ClassLoader.getSystemResource(".").getPath() + PATH_SEPARATOR + fileName;
		//Remove the %20 character with space to create new file. If the path
		//contains %20 character, it will throw exception
		if(path.contains("%20")){
			path = path.replaceAll("%20", " ");
		}
		return new File(path).getAbsolutePath();
	}
}
