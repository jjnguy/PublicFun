package edu.iastate.cs228.hw1;
import java.awt.Point;

/**
 * Interface for polyominoes used by Tetris-like games.  Each polyomino
 * has a position and rotation. The position is an (x,y) pair representing
 * the upper left corner of the bounding square. The position is represented
 * by an instance of java.awt.Point.  The initial position is presumed to be 
 * given by a constructor; thereafter it can be modified by the shiftX()
 * methods.  Note that no bounds checking is done in implementations of 
 * this interface; therefore, the position can have negative coordinates.  After the 
 * freeze() method is called, the position is no longer valid, and
 * the shiftX() and rotate() methods, as well as the getCells() method,
 * should throw an IllegalStateException.
 */
public interface IPolyomino extends Cloneable
{
  /**
   * Returns the cells occupied by this polyomino as an array of java.awt.Point.
   * @return the cells occupied by this polyomino
   */
  Point[] getCells();
  
  /**
   * Returns a suggested color for rendering this polyomino in a GUI.
   * @return a suggested color for this polyomino.
   */
  java.awt.Color getColorHint();
  
  /**
   * Shifts the position of this polyomino down (increasing the y-coordinate) 
   * by one cell.  No bounds checking is done.
   * 
   * @throws IllegalStateException
   *   if this object is in the frozen state
   */
  void shiftDown();
  
  /**
   * Shifts the position of this polyomino left (decreasing the x-coordinate) 
   * by one cell.  No bounds checking is done.
   * 
   * @throws IllegalStateException
   *   if this object is in the frozen state
   */
  void shiftLeft();
  
  /**
   * Shifts the position of this polyomino right (increasing the x-coordinate) 
   * by one cell.  No bounds checking is done.
   * 
   * @throws IllegalStateException
   *   if this object is in the frozen state
   */  
  void shiftRight();
  
  /**
   * Rotates this polyomino 90 degrees clockwise without changing
   * its position.  No bounds checking is done.
   * 
   * @throws IllegalStateException
   *   if this object is in the frozen state
   */    
  void rotate();
  
  /**
   * Returns a deep copy of this object.
   * @return a deep copy of this object.
   */
  Object clone();
  
  /**
   * Sets the state of this polyomino to frozen.
   */
  void freeze();
}
