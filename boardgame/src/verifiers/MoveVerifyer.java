package verifiers;

import game.GenericBoardGame;

import java.awt.Point;

public interface MoveVerifyer<E extends GenericBoardGame> {

	public boolean legalMove(E game, Point originalSpot, Point newSpot);

}
