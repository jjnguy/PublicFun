package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import piece.CheckerPiece;
import piece.GenericGamePiece;
import verifiers.CheckersMoveVerifier;

@SuppressWarnings("serial")
public class CheckersGame extends GenericBoardGame {

	public static String RED_SIDE = "redside";
	public static String BLACK_SIDE = "blackside";

	private int reds, blacks;
	private CheckerPiece turn;

	public CheckersGame() {
		super("Java - Checkers", Color.RED, Color.BLACK, new Dimension(8, 8));
		initializeGame();
	}

	private void initializeGame() {
		turn = CheckerPiece.RED();

		reds = blacks = 0;

		addPieces();
	}

	private void addPieces() {
		for (int row = 0; row < 3; row++) {
			for (int col = (row % 2 == 0) ? 0 : 1; col < board.getDimenstion().width; col += 2) {
				addPiece(CheckerPiece.RED(), new Point(col, row));
				reds++;
			}
		}
		for (int row = board.getDimenstion().height - 1; row > board.getDimenstion().height - 4; row--) {
			for (int col = (row % 2 == 0) ? 0 : 1; col < board.getDimenstion().width; col += 2) {
				addPiece(CheckerPiece.BLACK(), new Point(col, row));
				blacks++;
			}
		}
	}

	@Override
	public void play() {
		setVisible(true);
	}

	public static void main(String[] args) {
		CheckersGame c = new CheckersGame();
		c.play();
		// c.resetGame();
	}

	// @Override
	public boolean makeMove(Point p1, Point p2) {
		CheckersMoveVerifier verify = new CheckersMoveVerifier();
		if (!verify.legalMove(this, turn, p1, p2)) {
			System.out.println("Illegal move");
			return false;
		}
		// TODO don't change turn if there is a double jump chance
		movePiece(p1, p2);

		if (turn.turnEquals(CheckerPiece.RED())
				&& p2.y == board.getDimenstion().height - 1) {
			addPiece(CheckerPiece.RED_KING(), p2);
		} else if (turn.turnEquals(CheckerPiece.BLACK()) && p2.y == 0) {
			addPiece(CheckerPiece.BLACK_KING(), p2);
		}

		turn = turn.equals(CheckerPiece.RED()) ? CheckerPiece.BLACK() : CheckerPiece
				.RED();
		return true;
	}

	public void addPiece(GenericGamePiece piece, Point coord, boolean causedByJump) {
		CheckerPiece p = (CheckerPiece) getPiece(coord);
		board.addPiece(coord.y, coord.x, piece);
		if (!causedByJump) {
			return;
		}
		if (p.equals(CheckerPiece.BLACK()))
			blacks--;
		else
			reds--;
		if (reds == 0 || blacks == 0) {
			// TODO win stuff
			resetGame();
		}
		System.out.println("Red: " + reds + "   Black: " + blacks);
	}

	// @Override
	public void resetGame() {
		// TODO fix this shit
		board.resetBoard();
		reds = blacks = 0;
		addPieces();
		turn = CheckerPiece.RED();
	}

	// @Override
	public String getSide(int rowNumber) {
		if (rowNumber < 4)
			return RED_SIDE;
		else
			return BLACK_SIDE;
	}
}
