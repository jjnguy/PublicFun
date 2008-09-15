package piece;

import java.awt.Color;

@SuppressWarnings("serial")
public class CheckerPiece extends GenericGamePiece {

	private CheckerPiece(Color c1, Color c2) {
		super(c1, c2);
	}

	// TODO implement graphics for king checkers
	
	public static CheckerPiece RED(){
		return new CheckerPiece(new Color(128, 0, 0), Color.BLACK);
	}

	public static CheckerPiece BLACK(){
		return new CheckerPiece(Color.BLACK, new Color(128, 0, 0));
	}
	
	@Override
	public String toString() {
		if (innerColor().equals(Color.BLACK)) return "Black Piece";
		else return "Red Piece";
	}
}
