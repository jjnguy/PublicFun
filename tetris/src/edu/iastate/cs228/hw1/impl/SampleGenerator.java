package edu.iastate.cs228.hw1.impl;
import java.awt.Point;

import edu.iastate.cs228.hw1.IPolyomino;
import edu.iastate.cs228.hw1.IPolyominoGenerator;
import edu.iastate.cs228.hw1.impl.tetrominoes.TetrominoI;
import edu.iastate.cs228.hw1.impl.tetrominoes.TetrominoO;
import edu.iastate.cs228.hw1.impl.tetrominoes.TetrominoT;

/**
 * Implementation of IPolyominoGenerator that always returns
 * a Sampleomino.
 */
public class SampleGenerator implements IPolyominoGenerator
{
  @Override
  public IPolyomino getNext()
  {
    // initial position should be roughly centered left to right,
    // and extending just onto the first row (y = 0) of the grid.  Since
    // the Sampleomino is two cells high, its initial position 
    // has y = -1.
    return new TetrominoT(new Point(5, -1));
     
  }
}
