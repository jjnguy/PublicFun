import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class Cell extends JPanel {

	public static final String FLAG_CHANGE = "flag_change";
	public static final String UNCOVERED_PROP = "uncoverd";

	public static final int CELL_SIZE = 20;

	public static final int COVERED = -1;
	public static final int UNCOVERED = -3;
	public static final int FLAGGED = -4;
	public static final int IMPRESSED = -5;

	private boolean pressed;
	private boolean stillInCell;

	private int status;
	private boolean mine;
	private int surroundedBy;

	public Cell(boolean mine) {
		this(COVERED, mine);
	}

	public Cell(int status, boolean mine) {
		super();
		this.surroundedBy = Integer.MAX_VALUE;
		this.status = status;
		this.mine = mine;
		initGUI();
	}

	public boolean mine() {
		return mine;
	}

	private void initGUI() {
		setPreferredSize(new Dimension(Cell.CELL_SIZE, Cell.CELL_SIZE));
		setSize(getPreferredSize());
		addMouseListener(mouseListener);
	}

	public int getstatus() {
		return status;
	}

	public int getSurroundedBy() {
		return surroundedBy;
	}

	public void setSurroundedBy(int number) {
		surroundedBy = number;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (status == COVERED) {
			setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			return;
		} else if (status == FLAGGED) {
			g.setColor(Color.GREEN);
			g.fillRect(0, 0, CELL_SIZE, CELL_SIZE);
			return;
		} else if (status == IMPRESSED) {
			setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			return;
		} else if (status == UNCOVERED && mine) {
			setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			g.setColor(Color.RED);
			g.fillRect(0, 0, CELL_SIZE, CELL_SIZE);
			return;
		} else if (status == UNCOVERED && !mine) {
			if (surroundedBy == Integer.MAX_VALUE)
				throw new RuntimeException("Surrounded by not set");
			setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			if (surroundedBy != 0) {
				if (surroundedBy == 1)
					g.setColor(Color.BLUE);
				if (surroundedBy == 2)
					g.setColor(Color.GREEN);
				if (surroundedBy == 3)
					g.setColor(Color.RED);
				if (surroundedBy == 4)
					g.setColor(new Color(128,0,0));
				if (surroundedBy == 5)
					g.setColor(new Color(117,7,120));
				g.setFont(new Font("Ariel", Font.BOLD, 16));
				g.drawString(surroundedBy + "", 5, CELL_SIZE - 4);
				g.setColor(Color.BLACK);
				return;
			}
		}
	}

	private MouseListener mouseListener = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			if (getstatus() == Cell.UNCOVERED)
				return;
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (status == FLAGGED)
					return;
				pressed = true;
				stillInCell = true;
				status = Cell.IMPRESSED;
				repaint();
			} else {
				if (status == FLAGGED) {
					status = COVERED;
					firePropertyChange(Cell.FLAG_CHANGE, 1, 0);
				} else {
					status = FLAGGED;
					firePropertyChange(Cell.FLAG_CHANGE, 0, 1);
				}
				repaint();
				return;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (!pressed)
				return;
			if (stillInCell) {
				Cell.this.uncover();
			} else {
				status = Cell.COVERED;
				repaint();
			}
			pressed = false;
			stillInCell = false;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (pressed) {
				stillInCell = true;
				status = IMPRESSED;
				repaint();
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (pressed) {
				stillInCell = false;
				status = COVERED;
				repaint();
			}
		}
	};

	public boolean uncover() {
		int oldStatus = status;
		status = UNCOVERED;
		repaint();
		firePropertyChange(Cell.UNCOVERED_PROP, false, true);
		return oldStatus != status;
	}

}
