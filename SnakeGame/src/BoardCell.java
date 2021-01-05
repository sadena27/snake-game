import java.awt.Color;

public enum BoardCell {
	
	RED(Color.RED), GREEN(Color.GREEN), DARK_GREEN(Color.GREEN.darker()), BLACK(Color.BLACK);
	
	private final Color color;
	
	private BoardCell(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
}
