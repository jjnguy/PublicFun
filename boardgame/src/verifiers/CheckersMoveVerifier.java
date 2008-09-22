package verifiers;

import game.CheckersGame;

import java.awt.Point;

import piece.BlankPiece;
import piece.CheckerPiece;
import piece.GenericGamePiece;

public class CheckersMoveVerifier implements MoveVerifyer<CheckersGame> {

	/**
	 * Sorry about the length of the method!
	 * 
	 * @Override
	 */
	public boolean legalMove(CheckersGame game, GenericGamePiece turn,
			Point originalSpot, Point newSpot) {
		// If it isn't your turn, don't move
		if (!game.getPiece(originalSpot).turnEquals(turn))
			return false;
		/*
		 * first we verify that the move points are legal
		 */
		// if the first point is out of bounds
		// return false
		if (originalSpot.x >= game.getDimension().width
				|| originalSpot.y >= game.getDimension().height)
			return false;
		if (originalSpot.x < 0 || originalSpot.y < 0)
			return false;
		// if the second point is out of bounds
		// return false
		if (newSpot.x >= game.getDimension().width
				|| newSpot.y >= game.getDimension().height)
			return false;
		if (newSpot.x < 0 || newSpot.y < 0)
			return false;

		// if the first point is the same as the second point
		// return false
		if (originalSpot.equals(newSpot))
			return false;


		// the first piece must be a CheckerPiece..otherwise
		// the move cannot be valid
		CheckerPiece pieceAtFirstPoint = null;
		try {
			pieceAtFirstPoint = (CheckerPiece) game.getPiece(originalSpot);
		} catch (ClassCastException e) {
			return false;
		}

		/*
		 * Now we verify that the piece is making a move in the right direction
		 */
		if (pieceAtFirstPoint.equals(CheckerPiece.RED())) {
			// ie from bottom to top, or top to bottom
			if (game.getSide(0).equals(CheckersGame.RED_SIDE)) {
				// The row in the new point better be higher than the row in
				// the second point
				if (originalSpot.y > newSpot.y) {
					return false;
				}
			} else {
				// the row in the new point better be less than
				// the row in the old point
				if (originalSpot.y < newSpot.y) {
					return false;
				}
			}
		} else if (pieceAtFirstPoint.equals(CheckerPiece.BLACK())) {
			// ie from bottom to top, or top to bottom
			if (game.getSide(0).equals(CheckersGame.BLACK_SIDE)) {
				// The row in the new point better be higher than the row in
				// the second point
				if (originalSpot.y > newSpot.y) {
					return false;
				}
			} else {
				// the row in the new point better be less than
				// the row in the old point
				if (originalSpot.y < newSpot.y) {
					return false;
				}
			}
		} else {
			// if its a king then it can go either way
		}

		/*
		 * The second piece better be a BlankPiece.BLANK
		 */
		@SuppressWarnings("unused")
		BlankPiece pieceAtSecondPoint = null;
		try {
			pieceAtSecondPoint = (BlankPiece) game.getPiece(newSpot);
		} catch (ClassCastException e) {
			return false;
		}

		/*
		 * Make sure the piece is moving diagonally
		 */
		if (originalSpot.x == newSpot.x || originalSpot.y == newSpot.y)
			return false;
		// diagonal implies same dx and dy
		if (Math.abs(newSpot.x - originalSpot.x) != Math.abs(newSpot.y - originalSpot.y))
			return false;

		/*
		 * Check for jump case if the y distance is greater than 1 if there is no jump,
		 * then the move should be valid
		 */
		if ((Math.abs(originalSpot.y - newSpot.y) > 1)
				|| Math.abs(originalSpot.x - newSpot.x) > 1) {
			// if the y or x dist is too far..then we have an illegal move
			if (Math.abs(originalSpot.y - newSpot.y) > 2
					|| (Math.abs(originalSpot.x - newSpot.x) > 2)) {
				return false;
			}
			Point jumpedPoint = new Point(originalSpot.x
					- ((originalSpot.x - newSpot.x) / 2), originalSpot.y
					- ((originalSpot.y - newSpot.y) / 2));
			GenericGamePiece jumpedPiece = game.getPiece(jumpedPoint);
			// We cannot jump ourselves or blank spots
			if (jumpedPiece.equals(BlankPiece.BLANK())) {
				return false;
			}
			if (jumpedPiece.equals(pieceAtFirstPoint)) {
				return false;
			}
			// this essentially removes the jumped piece
			game.addPiece(BlankPiece.BLANK(), jumpedPoint, true);
		}

		return true;
	}
}
