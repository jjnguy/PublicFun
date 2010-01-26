package edu.iastate.cs228.hw1;

/**
 * An IPlayLevel is an abstraction of various attributes of
 * a level of difficulty for a Tetris-like video game.
 */
public interface IPlayLevel
{
  /**
   * Time in milliseconds between frames or steps of the game.
   * @param rowCount total number of rows completed
   * @return time in milliseconds
   */
  int speed(int rowCount);
  
  /**
   * Time in milliseconds between frames or steps of the game
   * when the dropping of a piece is accelerated.
   * @param rowCount total number of rows completed
   * @return time in milliseconds
   */
  int fastDropSpeed(int rowCount);
  
}
