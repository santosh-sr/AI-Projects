import java.util.Random;


public class RelationshipGenerator {
	public static final int NAN_VALUE = 1000;
	public static final int FRIEND = 1;
	public static final int ENEMY = -1;
	public static final int INDIFFERENT = 0;

	public int[][] generateRelationshipMatrix(int guests, int friends, int enemies) {
		int[][] relationshipMatrix = new int[guests][guests];
		int random_x;

		Random r = new Random();

		for(int i=0; i<guests; i++){
			for(int j=i; j<guests; j++){
				if(i != j){
					random_x = r.nextInt(100)+1;
					if(random_x <= friends){
						relationshipMatrix[i][j] = FRIEND;
						relationshipMatrix[j][i] = FRIEND;
					}else if(random_x > friends && random_x <= (friends + enemies)){
						relationshipMatrix[i][j] = ENEMY;
						relationshipMatrix[j][i] = ENEMY;
					}else{
						relationshipMatrix[i][j] = INDIFFERENT;
						relationshipMatrix[j][i] = INDIFFERENT;
					}
				}else{
					relationshipMatrix[i][j] = NAN_VALUE;
				}
			}
		}

		return relationshipMatrix;
	}

}
