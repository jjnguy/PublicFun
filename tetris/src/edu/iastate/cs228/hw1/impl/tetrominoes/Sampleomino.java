package edu.iastate.cs228.hw1.impl.tetrominoes;

import java.awt.Color;
import java.awt.Point;

import edu.iastate.cs228.hw1.IPolyomino;

public class Sampleomino extends AbstractOmino {

	public Sampleomino(Point initial) {
		super(initial);
	}

	@Override
	public Object clone() {
		AbstractOmino ret = new Sampleomino(new Point(getPosition()));
		if (isFrozen()) ret.freeze();
		ret.cells = new Point[cells.length];
		for (int i = 0; i < cells.length; i++) {
			ret.cells[i] = new Point(cells[i]);
		}
		return ret;
	}

	@Override
	public Color getColorHint() {
		return Color.RED;
	}

	private static final Point[] cellPosition1 = { new Point(0, 0), new Point(0, 1) };
	private static final Point[] cellPosition2 = { new Point(0, 1), new Point(1, 1) };
	private static final Point[] cellPosition3 = { new Point(1, 0), new Point(1, 1) };
	private static final Point[] cellPosition4 = { new Point(0, 0), new Point(1, 0) };

	@Override
	public Point[] getCellPos1() {
		return cellPosition1;
	}

	@Override
	public Point[] getCellPos2() {
		return cellPosition2;
	}

	@Override
	public Point[] getCellPos3() {
		return cellPosition3;
	}

	@Override
	public Point[] getCellPos4() {
		return cellPosition4;
	}

}
