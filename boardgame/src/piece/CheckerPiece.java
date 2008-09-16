package piece;

import java.awt.Color;

@SuppressWarnings("serial")
public class CheckerPiece extends GenericGamePiece {
	
	private CheckerPiece(Color c1, Color c2) {
		super(c1, c2);
	}
	
	private CheckerPiece(Color c1, Color c2, Color c3) {
		super(c1, c2, c3);
	}
	
	// TODO implement graphics for king checkers
	
	public static CheckerPiece RED() {
		return new CheckerPiece(new Color(128, 0, 0), Color.BLACK);
	}
	
	public static CheckerPiece RED_KING() {
		return new CheckerPiece(new Color(128, 0, 0), Color.BLACK, Color.YELLOW);
	}
	
	public static CheckerPiece BLACK() {
		return new CheckerPiece(Color.BLACK, new Color(128, 0, 0));
	}
	
	public static CheckerPiece BLACK_KING() {
		return new CheckerPiece(Color.BLACK, new Color(128, 0, 0), Color.YELLOW);
	}
	
	@Override
	public String toString() {
		if (innerColor().equals(Color.BLACK))
			return "Black Piece";
		else
			return "Red Piece";
	}
}
