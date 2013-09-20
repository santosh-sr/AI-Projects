import java.util.LinkedList;
import java.util.List;

public class ClauseGenerator {
	public List<Clause> atleastOneTableConstraint(int guests, int tables){
		List<Clause> atleastList = new LinkedList<Clause>();
		for(int i=1; i<=guests; i++){
			Clause clause = new Clause();
			for(int j=1; j<= tables; j++){
				Literal literal = new Literal(i, j);
				clause.addLiteral(literal);
			}
			atleastList.add(clause);
		}

		return atleastList;
	}

	public List<Clause> atmostOneTableConstraint(int guests, int tables){
		List<Clause> atmostList = new LinkedList<Clause>();
		for(int i=1; i<=guests; i++){
			for(int j=1; j<=tables; j++){
				for(int k=j+1; k<=tables; k++){
					Clause clause = new Clause();
					//Create the negation literal for each guest with each table
					Literal literal_left = new Literal(i, j, true);
					clause.addLiteral(literal_left);
					//Create the negation literal for other tables
					Literal literal_right = new Literal(i, k, true);
					clause.addLiteral(literal_right);

					atmostList.add(clause);
				}
			}
		}

		return atmostList;
	}

	public List<Clause> friendConstraint(int friend_1, int friend_2, int table){
		List<Clause> friendsList = new LinkedList<Clause>();
		for(int j=1; j<=table; j++){
			Clause clause_left = new Clause();
			{
				//Create the negative literal for one friend with each table
				Literal literal_left = new Literal(friend_1, j, true);
				clause_left.addLiteral(literal_left);
				//Create the negation literal for other tables
				Literal literal_right = new Literal(friend_2, j, false);
				clause_left.addLiteral(literal_right);
			}

			Clause clause_right = new Clause();
			{
				//Create the negative literal for one friend with each table
				Literal literal_left = new Literal(friend_2, j, true);
				clause_right.addLiteral(literal_left);
				//Create the negation literal for other tables
				Literal literal_right = new Literal(friend_1, j, false);
				clause_right.addLiteral(literal_right);
			}

			//Add the clause in the and operator
			friendsList.add(clause_left);
			friendsList.add(clause_right);
		}

		return friendsList;
	}

	public List<Clause> enemyConstraint(int enemy_1, int enemy_2, int table){
		List<Clause> enemyConstraintList = new LinkedList<Clause>();

		for(int j=1; j<=table; j++){
			Clause clause = new Clause();
			{
				//Create the negative literal for one enemy with each table
				Literal literal_left = new Literal(enemy_1, j, true);
				clause.addLiteral(literal_left);
				//Create the negation literal for other tables
				Literal literal_right = new Literal(enemy_2, j, true);
				clause.addLiteral(literal_right);
			}

			//Add the clause in the and operator
			enemyConstraintList.add(clause);
		}
		
		return enemyConstraintList;
	}
}
