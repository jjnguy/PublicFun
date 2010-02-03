package edu.iastate.cs228.hw1.impl.tetrominoes;

import java.awt.Color;
import java.awt.Point;

public class TetrominoT extends AbstractOmino {

	public TetrominoT(Point initial) {
		super(initial);
	}

	@Override
	public Point[] getCellPos1() {
		return new Point[] { new Point(0, 2), new Point(1, 1), new Point(1, 2), new Point(2, 2) };
	}

	@Override
	public Point[] getCellPos2() {
		return new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) };
	}

	@Override
	public Point[] getCellPos3() {
		return new Point[] { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 0) };
	}

	@Override
	public Point[] getCellPos4() {

		return new Point[] { new Point(2, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2) };
	}

	@Override
	public Color getColorHint() {
		return Color.MAGENTA;
	}
}
