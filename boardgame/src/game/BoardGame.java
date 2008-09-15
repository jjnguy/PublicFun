package game;

import java.awt.Dimension;
import java.awt.Point;

import piece.GenericGamePiece;

import board.GenericBoard;

public interface BoardGame {
	public void play();

	public void resetGame();

	public GenericBoard getBoard();

	public boolean makeMove(Point p1, Point p2);

	public String getSide(int rowNumber);

	public Dimension getDimension();

	GenericGamePiece getPiece(Point coord);
}
