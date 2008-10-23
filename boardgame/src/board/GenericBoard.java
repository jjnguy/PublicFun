package board;

import game.BoardGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.NoSuchElementException;

import javax.swing.JPanel;

import piece.BlankPiece;
import piece.GenericGamePiece;

@SuppressWarnings("serial")
public class GenericBoard extends JPanel {

	public static int curID = Integer.MIN_VALUE;

	private BoardGame parent;

	private Color cOne, cTwo;
	private Dimension d;
	private BoardSquare[][] squares;
	private BoardSquare selected1, selected2;

	public GenericBoard(BoardGame parent, Color color1, Color color2, Dimension d) {
		cOne = color1;
		cTwo = color2;
		this.parent = parent;
		setLayout(new GridLayout(d.height, d.width));

		squares = new BoardSquare[d.height][d.width];
		selected1 = selected2 = null;

		this.d = d;
		setPreferredSize(new Dimension(d.height * BoardSquare.CELL_WIDTH, d.width
				* BoardSquare.CELL_WIDTH));
		fillSquares();
		addSquares();
	}

	private void fillSquares() {
		Color cur = cOne;
		for (int i = 0; i < d.height; i++) {
			for (int j = 0; j < d.width; j++) {
				squares[i][j] = new BoardSquare(cur, GenericBoard.curID++);
				cur = cur == cOne ? cTwo : cOne;
			}
			if (d.width % 2 == 0)
				cur = cur == cOne ? cTwo : cOne;
		}
	}

	private void addSquares() {
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares[i].length; j++) {
				add(squares[i][j]);
				squares[i][j].addPropertyChangeListener(cellChange);
			}
		}
	}

	/**
	 * Adds the <code>GenericGamePiece</code> to the given square.
	 * 
	 * @param row
	 * @param col
	 * @param p
	 */
	public void addPiece(int row, int col, GenericGamePiece p) {
		squares[row][col].setPiece(p);
	}

	public GenericGamePiece getPieceAtSquare(Point p) {
		return squares[p.y][p.x].getPiece();
	}

	public Dimension getDimenstion() {
		return d;
	}

	public void resetBoard() {
		for (int row = 0; row < squares.length; row++) {
			for (int col = 0; col < squares[row].length; col++) {
				squares[row][col].setPiece(BlankPiece.BLANK());
			}
		}
		for (int row = 0; row < squares.length; row++) {
			for (int col = 0; col < squares[row].length; col++) {
				squares[row][col].setStatus(BoardSquare.NONE);
			}
		}
	}

	public Point getPointFromSquare(BoardSquare square) {
		for (int row = 0; row < squares.length; row++) {
			for (int col = 0; col < squares[0].length; col++) {
				if (squares[row][col].getID() == square.getID())
					return new Point(col, row);
			}
		}
		throw new NoSuchElementException("The square doesn't exist in this board.");
	}

	private PropertyChangeListener cellChange = new PropertyChangeListener() {

		// @Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (!evt.getPropertyName().equals(BoardSquare.STATUS_CHANGE))
				return;
			int oldVal = (Integer) evt.getOldValue();
			int newVal = (Integer) evt.getNewValue();
			if (newVal == BoardSquare.HIGHLIGHTED && oldVal == BoardSquare.NONE)
				return;
			if (newVal == BoardSquare.NONE && oldVal == BoardSquare.HIGHLIGHTED)
				return;
			if (newVal == BoardSquare.SELECTED) {
				if (selected1 == null)
					selected1 = (BoardSquare) evt.getSource();
				else
					selected2 = (BoardSquare) evt.getSource();
				if (selected2 != null && selected1 != null) {
					parent.makeMove(getPointFromSquare(selected1),
							getPointFromSquare(selected2));
					selected1.setStatus(BoardSquare.NONE);
					selected2.setStatus(BoardSquare.NONE);
					selected1 = null;
					selected2 = null;
					return;
				}
			}
			if (newVal == BoardSquare.HIGHLIGHTED) {
				if (selected2 != null) {
					selected2 = null;
				} else {
					selected1 = null;
				}
				return;
			}
		}
	};

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cOne == null) ? 0 : cOne.hashCode());
		result = prime * result + ((cTwo == null) ? 0 : cTwo.hashCode());
		result = prime * result + ((d == null) ? 0 : d.hashCode());
		result = prime * result + ((selected1 == null) ? 0 : selected1.hashCode());
		result = prime * result + ((selected2 == null) ? 0 : selected2.hashCode());
		result = prime * result + Arrays.hashCode(squares);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericBoard other = (GenericBoard) obj;
		if (cOne == null) {
			if (other.cOne != null)
				return false;
		} else if (!cOne.equals(other.cOne))
			return false;
		if (cTwo == null) {
			if (other.cTwo != null)
				return false;
		} else if (!cTwo.equals(other.cTwo))
			return false;
		if (d == null) {
			if (other.d != null)
				return false;
		} else if (!d.equals(other.d))
			return false;
		if (selected1 == null) {
			if (other.selected1 != null)
				return false;
		} else if (!selected1.equals(other.selected1))
			return false;
		if (selected2 == null) {
			if (other.selected2 != null)
				return false;
		} else if (!selected2.equals(other.selected2))
			return false;
		if (!Arrays.equals(squares, other.squares))
			return false;
		return true;
	}
}
