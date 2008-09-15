package verifiers;

import game.GenericBoardGame;

import java.awt.Point;

import piece.GenericGamePiece;

public interface MoveVerifyer<E extends GenericBoardGame> {

	public boolean legalMove(E game, GenericGamePiece turn, Point originalSpot, Point newSpot);

}
