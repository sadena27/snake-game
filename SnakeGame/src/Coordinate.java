public class Coordinate {
	
	private int row, col, direction;

	public Coordinate(int row, int col, int direction) {
		this.row = row;
		this.col = col;
		this.direction = direction;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public int getDirection() {
		return direction;
	}
	
	// Two coordinates are equal if they have the same row and column
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Coordinate)) {
			return false;
		}
		
		Coordinate c = (Coordinate)obj;
		return c.getRow() == this.row && c.getCol() == this.col;
	}
	
}
