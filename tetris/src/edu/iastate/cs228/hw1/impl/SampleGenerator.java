package edu.iastate.cs228.hw1.impl;

import java.awt.Point;

import edu.iastate.cs228.hw1.IPolyomino;
import edu.iastate.cs228.hw1.IPolyominoGenerator;
import edu.iastate.cs228.hw1.impl.tetrominoes.TetrominoI;
import edu.iastate.cs228.hw1.impl.tetrominoes.TetrominoO;
import edu.iastate.cs228.hw1.impl.tetrominoes.TetrominoT;

/**
 * Implementation of IPolyominoGenerator that always returns a Sampleomino.
 */
public class SampleGenerator implements IPolyominoGenerator {
	@Override
	public IPolyomino getNext() {

		return new TetrominoT(new Point(5, -1));

	}
}
