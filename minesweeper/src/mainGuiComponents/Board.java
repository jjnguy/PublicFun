package mainGuiComponents;

import genericComponents.Difficulty;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Board extends JPanel {
	
	public static final String NONE = "none";
	public static final String LOSE = "lose";
	public static final String WIN = "win";
	public static final String FLAGCHANGE = "board_flag_change";
	public static final String WINLOSE_PROP = "WIN/LOSE";
	public static final String HAS_UNCOVERED_CELLS = "uncoverdCellProp";
	private boolean hasUncoveredCells;
	private Cell[][] cells;
	private int mineNumber;
	private boolean clickable;
	private int flagCount;
	
	public Board(int width, int height, int mineNumber) {
		
		if (mineNumber > width * height - 3) { throw new IllegalArgumentException("You must leave atleast three mine-free cells."); }
		
		clickable = true;
		
		flagCount = 0;
		
		hasUncoveredCells = false;
		
		fillAllCells(height, width, mineNumber);
		
		layoutAllCells();
		
		populateSurroundedByNumbers();
	}
	
	private void fillAllCells(int height, int width, int mineNumber) {
		cells = new Cell[height][width];
		this.mineNumber = mineNumber;
		
		int placedMines = 0;
		Random r = new Random();
		while (placedMines < mineNumber) {
			int row = r.nextInt(height);
			int col = r.nextInt(width);
			if (cells[row][col] == null) {
				cells[row][col] = new Cell(true);
				placedMines++;
			}
		}
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (cells[i][j] == null) {
					cells[i][j] = new Cell(false);
				}
			}
		}
		
	}
	
	private void layoutAllCells() {
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = gc.gridy = 0;
		
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				add(cells[i][j], gc);
				cells[i][j].addMouseListener(ml);
				cells[i][j].addPropertyChangeListener(flagChangeListener);
				cells[i][j].addPropertyChangeListener(uncoverListener);
				gc.gridx++;
			}
			gc.gridx = 0;
			gc.gridy++;
		}
	}
	
	public void populateSurroundedByNumbers() {
		// populate surrounded by lists
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				if (cells[row][col].mine()) continue;
				// loop through surrounding cells
				int count = 0;
				// move to the upper left
				for (int i = row - 1; i <= row + 1; i++) {
					for (int j = col - 1; j <= col + 1; j++) {
						if (i < 0 || i >= cells.length) continue;
						if (j < 0 || j >= cells[0].length) continue;
						if (cells[i][j].mine()) count++;
					}
				}
				cells[row][col].setSurroundedBy(count);
			}
		}
		
	}
	
	public void uncoverAll() {
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				cells[row][col].uncover();
			}
		}
	}
	
	public void reset(Difficulty d) {
		removeAll();
		fillAllCells(d.height(), d.width(), d.mines());
		
		layoutAllCells();
		
		populateSurroundedByNumbers();
		
		hasUncoveredCells = false;
		
		repaint();
	}
	
	public void fireUncoverEasyCells() {
		// iterate over each cell and find if more need
		// to be uncovered around it
		boolean easyCellsLeft = true;
		while (easyCellsLeft) {
			int uncovered = 0;
			for (int row = 0; row < cells.length; row++) {
				for (int col = 0; col < cells[row].length; col++) {
					if (!cells[row][col].mine() && cells[row][col].getstatus() == Cell.UNCOVERED
							&& cells[row][col].getSurroundedBy() == 0) {
						for (int i = row - 1; i <= row + 1; i++) {
							for (int j = col - 1; j <= col + 1; j++) {
								if (i < 0 || i >= cells.length) continue;
								if (j < 0 || j >= cells[0].length) continue;
								if (cells[i][j].getstatus() == Cell.FLAGGED) continue;
								if (cells[i][j].uncover()) uncovered++;
							}
						}
					}
				}
			}
			if (uncovered == 0) easyCellsLeft = false;
		}
	}
	
	private MouseListener ml = new MouseAdapter() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if (!clickable) return;
			if (e.getButton() == MouseEvent.BUTTON1) {
				Board.this.fireUncoverEasyCells();
				repaint();
				if (Board.this.lose()) firePropertyChange(Board.WINLOSE_PROP, Board.NONE, Board.LOSE);
				if (Board.this.win()) firePropertyChange(Board.WINLOSE_PROP, Board.NONE, Board.WIN);
			}
		}
	};
	
	private PropertyChangeListener flagChangeListener = new PropertyChangeListener() {
		
		// @Override
		public void propertyChange(PropertyChangeEvent evt) {
			String propName = evt.getPropertyName();
			if (propName.equals(Cell.FLAG_CHANGE)) {
				int old = flagCount;
				flagCount += (Integer) evt.getNewValue() - (Integer) evt.getOldValue();
				firePropertyChange(Board.FLAGCHANGE, old, flagCount);
			}
		}
	};
	
	private PropertyChangeListener uncoverListener = new PropertyChangeListener() {
		
		public void propertyChange(PropertyChangeEvent evt) {
			String propName = evt.getPropertyName();
			if (propName.equals(Cell.UNCOVERED_PROP)) {
				firePropertyChange(Board.HAS_UNCOVERED_CELLS, hasUncoveredCells, true);
				hasUncoveredCells = true;
			}
		}
	};
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		clickable = enabled;
	}
	
	public int getFlagCOunt() {
		return flagCount;
	}
	
	public boolean win() {
		int cellsLeft = cells.length * cells[0].length - mineNumber;
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				if (cells[row][col].mine() && cells[row][col].getstatus() == Cell.UNCOVERED) return false;
				if (!cells[row][col].mine() && cells[row][col].getstatus() == Cell.UNCOVERED) cellsLeft--;
			}
		}
		return cellsLeft == 0;
	}
	
	public boolean lose() {
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				if (cells[row][col].mine() && cells[row][col].getstatus() == Cell.UNCOVERED) return true;
			}
		}
		return false;
	}
}
