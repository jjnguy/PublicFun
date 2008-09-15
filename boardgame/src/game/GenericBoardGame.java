package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;

import piece.BlankPiece;
import piece.GenericGamePiece;
import util.Util;
import board.GenericBoard;

@SuppressWarnings("serial")
public abstract class GenericBoardGame extends JFrame implements BoardGame {

	protected GenericBoard board;

	public GenericBoardGame(String name, Color c1, Color c2, Dimension d) {
		super(name);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		board = new GenericBoard(this, c1, c2, d);
		add(board);
		pack();
		Util.moveToMiddle(this);
	}

	@Override
	public void play() {
		// TODO need to set turn order
		setVisible(true);
	}

	@Override
	public GenericBoard getBoard() {
		return board;
	}

	public void movePiece(Point originalPoint, Point newPoint) {
		addPiece(board.getPieceAtSquare(originalPoint), newPoint);
		addPiece(BlankPiece.BLANK(), originalPoint);
	}

	@Override
	public GenericGamePiece getPiece(Point coord) {
		return board.getPieceAtSquare(coord);
	}

	public void addPiece(GenericGamePiece piece, Point coord) {
		board.addPiece(coord.y, coord.x, piece);
	}

	public void addPiece(GenericGamePiece piece, Point coord, boolean causedByJump) {
		board.addPiece(coord.y, coord.x, piece);
		if (!causedByJump) {
			return;
		}
		// TODO keep track of pieces
	}

	@Override
	public Dimension getDimension() {
		return board.getDimenstion();
	}
}
