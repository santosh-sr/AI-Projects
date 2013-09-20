
public class Symbol {

	private int guest;
	private int table;

	public Symbol(int guest, int table){
		this.guest = guest;
		this.table = table;
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
