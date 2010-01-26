package edu.iastate.cs228.hw1;

/**
 * Abstraction of a generator for game pieces in a Tetris-like
 * video game.  
 */
public interface IPolyominoGenerator
{
  /**
   * Returns a new IPolyomino instance according to generator's 
   * strategy.
   * @return a new IPolyomino 
   */
  IPolyomino getNext();
}
