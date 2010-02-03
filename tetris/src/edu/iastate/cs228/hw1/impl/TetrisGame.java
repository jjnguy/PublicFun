package edu.iastate.cs228.hw1.impl;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.iastate.cs228.hw1.GameStatus;
import edu.iastate.cs228.hw1.IGame;
import edu.iastate.cs228.hw1.IPolyomino;
import edu.iastate.cs228.hw1.IPolyominoGenerator;

/**
 * A basic concrete implementation of the IGame interface for Tetris-like games.
 */
public class TetrisGame implements IGame {
	/**
	 * Width of the game grid.
	 */
	private static final int WIDTH = 12;

	/**
	 * Height of the game grid.
	 */
	private static final int HEIGHT = 24;

	/**
	 * The polyomino that is subject to motion during the step() method or via
	 * invocations of the shiftX() or rotate() methods.
	 */
	private IPolyomino current;

	/**
	 * A WIDTH x HEIGHT grid of cells that may be occupied by either the current
	 * polyomino or by frozen polyominoes (that can no longer be moved).
	 * Unoccupied cells are null.
	 */
	private IPolyomino[][] grid;

	/**
	 * Status of the game after each invocation of step(), as described in the
	 * GameStatus documentation.
	 */
	private GameStatus gameStatus;

	/**
	 * Generator for new polyominoes. The BasicGenerator implementation will
	 * uniformly select one of the seven tetromino types.
	 */
	private IPolyominoGenerator generator;

	/**
	 * State variable indicating which rows need to be deleted when the status
	 * is COLLAPSING. The implementation maintains the invariant that
	 * rowsToCollapse.size() is nonzero if and only if gameStatus is COLLAPSING.
	 */
	private List<Integer> rowsToCollapse;

	/**
	 * Count of the total number of rows that have been completed and collapsed.
	 */
	private int totalRowsCollapsed;

	/**
	 * Constructs a new TetrisGame instance.
	 */
	public TetrisGame() {
		grid = new IPolyomino[getHeight()][getWidth()];
		generator = new SampleGenerator();
		current = generator.getNext();
		gameStatus = GameStatus.NEW_POLYOMINO;
		rowsToCollapse = new ArrayList<Integer>();
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

	@Override
	public IPolyomino getCell(int row, int col) {
		return grid[row][col];
	}

	@Override
	public IPolyomino getCurrent() {
		if (gameStatus == GameStatus.COLLAPSING
				|| gameStatus == GameStatus.GAME_OVER) {
			throw new IllegalStateException();
		}
		return current;
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getNextRowToCollapse() {
		if (rowsToCollapse.size() == 0) {
			throw new IllegalStateException();
		}
		return rowsToCollapse.get(rowsToCollapse.size() - 1);
	}

	@Override
	public boolean rotate() {
		IPolyomino clone = (IPolyomino) current.clone();
		clone.rotate();
		if (collides(clone))
			return false;
		current.rotate();
		return true;
	}

	@Override
	public boolean shiftLeft() {
		IPolyomino clone = (IPolyomino) current.clone();
		clone.shiftLeft();
		if (collides(clone))
			return false;
		current.shiftLeft();
		return true;
	}

	@Override
	public boolean shiftRight() {
		IPolyomino clone = (IPolyomino) current.clone();
		clone.shiftRight();
		if (collides(clone))
			return false;
		current.shiftRight();
		return true;
	}

	@Override
	public int getTotalCompletedRows() {
		return totalRowsCollapsed;
	}

	@Override
	public boolean gameOver() {
		return gameStatus == GameStatus.GAME_OVER;
	}

	@Override
	public GameStatus step() {
		switch (gameStatus) {
		case GAME_OVER:
			// do nothing
			break;
		case NEW_POLYOMINO:
		case FALLING:
			if (canShiftDown()) {
				current.shiftDown();
			} else {
				// Add current polyomino to the grid, maybe temporarily,
				// in order to check whether
				// it has filled in a row that can be collapsed
				for (Point p : current.getCells()) {
					if (p.y >= 0 && p.y < HEIGHT && p.x >= 0 && p.x < WIDTH) {
						grid[p.y][p.x] = current;
					}
				}
				determineRowsToCollapse();
				if (rowsToCollapse.size() != 0) {
					// current polyomino completes at least one row, so freeze
					// the
					// polyomino and prepare to collapse rows
					current.freeze();
					gameStatus = GameStatus.COLLAPSING;
				} else {
					// current polyomino is stopped, but has not completed a
					// row,
					// so it might be moved sideways; remove it from the grid
					// and
					// don't freeze it yet
					for (Point p : current.getCells()) {
						if (p.y >= 0 && p.y < HEIGHT && p.x >= 0 && p.x < WIDTH) {
							grid[p.y][p.x] = null;
						}
					}
					gameStatus = GameStatus.STOPPED;
				}
			}
			break;
		case STOPPED:
			// If the polyomino was previously stopped, it still may be possible
			// to shift it downwards since it could have been moved to the side
			// during the last step
			if (canShiftDown()) {
				current.shiftDown();
				gameStatus = GameStatus.FALLING;
			} else {
				// we only get in the stopped state when the polyomino doesn't
				// fill
				// in a row, so we don't have to check for filled rows again;
				// freeze
				// the current polyomino in the grid and try to start a new
				// polyomino at the top
				for (Point p : current.getCells()) {
					if (p.y >= 0 && p.y < HEIGHT && p.x >= 0 && p.x < WIDTH) {
						grid[p.y][p.x] = current;
					}
				}
				current.freeze();
				current = generator.getNext();
				if (collides(current)) {
					gameStatus = GameStatus.GAME_OVER;
				} else {
					gameStatus = GameStatus.NEW_POLYOMINO;
				}
			}
			break;
		case COLLAPSING:
			int last = rowsToCollapse.size() - 1;
			int row = rowsToCollapse.get(last);
			rowsToCollapse.remove(last);
			collapseRow(row);
			++totalRowsCollapsed;
			if (rowsToCollapse.size() > 0) {
				// update indices in the rowsToCollapse list to adjust for
				// the row just collapsed
				for (int i = 0; i < rowsToCollapse.size(); ++i) {
					int rowIndex = rowsToCollapse.get(i);
					rowsToCollapse.set(i, rowIndex + 1);
				}
			} else {
				// done collapsing, try to start a new polyomino
				current = generator.getNext();
				if (collides(current)) {
					gameStatus = GameStatus.GAME_OVER;
				} else {
					gameStatus = GameStatus.NEW_POLYOMINO;
				}
			}
			break;
		}
		return gameStatus;
	}

	/**
	 * Determines whether the current polyomino can be shifted down. Does not
	 * modify the game state.
	 * 
	 * @return true if the current polyomino can be shifted down, false
	 *         otherwise
	 */
	private boolean canShiftDown() {
		IPolyomino t = (IPolyomino) current.clone();
		t.shiftDown();
		return !collides(t);
	}

	/**
	 * Determines whether the given polyomino overlaps with the occupied cells
	 * of the grid, or extends beyond the sides or bottom of the grid. (A
	 * polyomino in its initial position MAY extend above the grid.)
	 * 
	 * @param t
	 *            a non-frozen polyomino
	 * @return true if the cells of the given polyomino extend beyond the sides
	 *         or bottom of the grid or overlap with any occupied cells of the
	 *         grid
	 */
	private boolean collides(IPolyomino t) {
		for (Point p : t.getCells()) {
			if (p.x < 0 || p.x > WIDTH - 1 || p.y > HEIGHT - 1) {
				return true;
			}

			// row, column
			if (p.y >= 0 && grid[p.y][p.x] != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Updates the rowsToCollapse state variable with the indices of all
	 * horizontal rows of the grid that are fully occupied. In the resulting
	 * list, the indices must be unique and must be in increasing order. If
	 * there are no fully occupied rows, the list is left empty.
	 */
	private void determineRowsToCollapse() {
		rowsToCollapse.clear();
		for (Point p : current.getCells()) {
			if (!rowsToCollapse.contains(p.y) && isRowComplete(p.y)) {
				rowsToCollapse.add(p.y);
			}
		}
		if (rowsToCollapse.size() != 0) {
			Collections.sort(rowsToCollapse);
		}
	}

	/**
	 * Determines whether all cells in the given row are occupied.
	 * 
	 * @param row
	 *            index of the row to check
	 * @return true if all cells in the row are occupied, false otherwise
	 */
	private boolean isRowComplete(int row) {
		for (IPolyomino p : grid[row]) {
			if (p == null)
				return false;
		}
		return true;
	}

	/**
	 * Deletes all references in the given row of the grid and shifts all rows
	 * above it down by one cell. Since all polyominoes associated with grid
	 * cells are frozen, their positions are not updated. All cells in the top
	 * row are null after this method returns.
	 * 
	 * @param row
	 *            the index of the row to be collapsed
	 */
	private void collapseRow(int row) {
		for (int i = row; i > 0; i--){
			for (int j = 0; j < WIDTH; j++){
				grid[i][j] = grid[i - 1][j];
			}
		}
		Arrays.fill(grid[0], null);
	}
}
