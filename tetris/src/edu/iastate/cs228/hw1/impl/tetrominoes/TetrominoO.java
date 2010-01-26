package edu.iastate.cs228.hw1.impl.tetrominoes;

import java.awt.Color;
import java.awt.Point;

public class TetrominoO extends AbstractOmino {

	public TetrominoO(Point initial) {
		super(initial);
	}

	@Override
	public Object clone() {
		AbstractOmino ret = new TetrominoO(new Point(getPosition()));
		if (isFrozen())
			ret.freeze();
		ret.cells = new Point[cells.length];
		for (int i = 0; i < cells.length; i++) {
			ret.cells[i] = new Point(cells[i]);
		}
		return ret;
	}

	private static final Point[] cellPos = { new Point(0, 0), new Point(1, 0), new Point(0, 1),
			new Point(1, 1) };

	@Override
	public Point[] getCellPos1() {
		return cellPos;
	}

	@Override
	public Point[] getCellPos2() {
		return cellPos;
	}

	@Override
	public Point[] getCellPos3() {
		return cellPos;
	}

	@Override
	public Point[] getCellPos4() {
		return cellPos;
	}

	@Override
	public Color getColorHint() {
		return Color.YELLOW;
	}

}
