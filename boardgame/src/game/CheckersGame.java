package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import piece.CheckerPiece;
import verifiers.CheckersMoveVerifier;

@SuppressWarnings("serial")
public class CheckersGame extends GenericBoardGame {

	public static String RED_SIDE = "redside";
	public static String BLACK_SIDE = "blackside";

	public CheckersGame() {
		super("Java - Checkers", Color.RED, Color.BLACK, new Dimension(8, 8));
		initializeGame();
	}

	private void initializeGame() {
		for (int row = 0; row < 3; row++) {
			for (int col = (row % 2 == 0) ? 0 : 1; col < board.getDimenstion().width; col += 2) {
				addPiece(CheckerPiece.RED(), new Point(col, row));
			}
		}
		for (int row = board.getDimenstion().height - 1; row > board.getDimenstion().height - 4; row--) {
			for (int col = (row % 2 == 0) ? 0 : 1; col < board.getDimenstion().width; col += 2) {
				addPiece(CheckerPiece.BLACK(), new Point(col, row));
			}
		}
	}

	public static void main(String[] args) {
		new CheckersGame().play();
	}

	@Override
	public boolean makeMove(Point p1, Point p2) {
		CheckersMoveVerifier verify = new CheckersMoveVerifier();
		if (!verify.legalMove(this, p1, p2)) {
			System.out.println("Illegal move");
			return false;
		}
		movePiece(p1, p2);
		return true;
	}

	@Override
	public void resetGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSide(int rowNumber) {
		if (rowNumber < 4)
			return RED_SIDE;
		else
			return BLACK_SIDE;
	}
}
