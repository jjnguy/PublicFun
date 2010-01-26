package edu.iastate.cs228.hw1;
/**
 * Status values for a Tetris-like game.  The meaning of each values is
 * as follows:
 * <dl>
 *   <dt>NEW_POLYOMINO
 *   <dd>There is a new current polyomino at the top of the grid 
 *   <dt>FALLING
 *   <dd>The current polyomino was successfully shifted down one cell.
 *   <dt>STOPPED
 *   <dd>The current polyomino could not be shifted down, but did
 *       not complete a horizontal row (it may be possible to move
 *       it horizontally)
 *   <dt>COLLAPSING
 *   <dd>There is at least one complete horizontal row, and the next
 *       invocation of step() will collapse it
 *   <dt>GAME_OVER
 *   <dd>A new polyomino cannot be placed at the top of the grid without
 *       colliding with occupied cells
 * </dl>
 */
public enum GameStatus
{
  NEW_POLYOMINO, FALLING, STOPPED, COLLAPSING, GAME_OVER
}
