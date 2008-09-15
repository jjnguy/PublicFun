package piece;

import java.awt.Graphics;

@SuppressWarnings("serial")
public class BlankPiece extends GenericGamePiece {

	private BlankPiece() {
	}

	protected void paintComponent(Graphics g) {
		// intentionally blank
	}

	public static BlankPiece BLANK() {
		return new BlankPiece();
	}

	@Override
	public String toString() {
		return "Blank Piece";
	}
}
