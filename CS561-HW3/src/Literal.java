
public class Literal {
	private Symbol symbol;
	private boolean isNegation;

	public Literal(int guest, int table, boolean isNegation){
		this.symbol = new Symbol(guest, table);
		this.isNegation = isNegation;
	}

	public Literal(int guest, int table){
		this(guest, table, false);
	}

	public Literal(){
		//No arg constructor
	}

	public boolean isComplimentLiteral(Literal literal){
		if(this.isSamePrefix(literal) && (this.isNegation() == !literal.isNegation())){
			return true;
		}

		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;

		Literal other = (Literal)obj;
		if(this.symbol.equals(other.getSymbol()) && this.isNegation == other.isNegation){
			return true;
		}

		return false;
	}

	public boolean isSamePrefix(Object obj) {
		if(obj == null)
			return false;

		Literal other = (Literal)obj;
		if(this.symbol.equals(other.getSymbol())){
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		String literal = symbol.toString();
		if(isNegation){
			literal = "!" + literal;
		}
		return literal;
	}

	public boolean value(boolean val){
		if(isNegation){
			if(val){
				return false;
			}else{
				return true;
			}
		}

		return val; 
	}



	public boolean isNegation() {
		return isNegation;
	}

	public void setNegation(boolean isNegation) {
		this.isNegation = isNegation;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public class Symbol{
		private int guest;
		private int table;

		public Symbol(int guest, int table){
			this.guest = guest;
			this.table = table;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj == null)
				return false;

			Symbol other = (Symbol)obj;
			if(this.guest == other.getGuest() && this.table == other.getTable()){
				return true;
			}

			return false;
		}

		@Override
		public String toString() {
			String symbol = "X" + guest + "," + table;

			return symbol;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ guest;
			result = prime * result + table;
			return result;
		}

		public void setGuest(int guest) {
			this.guest = guest;
		}

		public int getTable() {
			return table;
		}

		public void setTable(int table) {
			this.table = table;
		}

		public int getGuest() {
			return guest;
		}
	}
}
