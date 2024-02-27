import java.awt.*;

public class Pawn {
	private Box position;
	private final Color color;

	public Pawn(Box position, Color color) {
		this.position = position;
		this.color = color;
	}

	public Box getPosition() {
		return position;
	}

	public Color getColor() {
		return color;
	}

	public void setPosition(Box position) {
		this.position = position;
	}
}
