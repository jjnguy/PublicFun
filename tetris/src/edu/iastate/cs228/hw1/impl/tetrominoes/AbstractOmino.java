package edu.iastate.cs228.hw1.impl.tetrominoes;

import java.awt.Color;
import java.awt.Point;

import edu.iastate.cs228.hw1.IPolyomino;

public abstract class AbstractOmino implements IPolyomino {

	private Point position;
	protected Point[] cells;
	private int curPos = 0;

	private boolean frozen;

	protected AbstractOmino(Point initial) {
		frozen = false;
		position = initial;
		applyRotate();
	}

	@Override
	public void freeze() {
		frozen = true;
	}

	protected boolean isFrozen(){
		return frozen;
	}
	
	@Override
	public Point[] getCells() {
		return cells;
	}
	
	protected Point getPosition(){
		return new Point(position);
	}

	@Override
	public abstract Color getColorHint();

	@Override
	public void rotate() {
		curPos++;
		if (curPos > 3)
			curPos = 0;
		applyRotate();
	}

	protected void transformCells(Point[] cellPos, Point current) {
		cells = new Point[cellPos.length];
		for (int i = 0; i < cells.length; i++) {
			cells[i] = new Point(current.x + cellPos[i].x, current.y - cellPos[i].y);
		}
	}

	private void applyRotate() {
		switch (curPos) {
		case 0: {
			transformCells(getCellPos1(), position);
			break;
		}
		case 1: {
			transformCells(getCellPos2(), position);
			break;
		}
		case 2: {
			transformCells(getCellPos3(), position);
			break;
		}
		case 3: {
			transformCells(getCellPos4(), position);
			break;
		}
		}
	}

	public abstract Point[] getCellPos1();

	public abstract Point[] getCellPos2();

	public abstract Point[] getCellPos3();

	public abstract Point[] getCellPos4();

	private void frozenCheck() {
		if (frozen)
			throw new IllegalArgumentException();
	}

	@Override
	public void shiftDown() {
		frozenCheck();
		position.y += 1;
		for (Point p : cells) {
			p.y += 1;
		}
	}

	@Override
	public void shiftLeft() {
		frozenCheck();
		position.x -= 1;
		for (Point p : cells) {
			p.x -= 1;
		}
	}

	@Override
	public void shiftRight() {
		frozenCheck();
		position.x += 1;
		for (Point p : cells) {
			p.x += 1;
		}
	}

	@Override
	public abstract Object clone();
}
