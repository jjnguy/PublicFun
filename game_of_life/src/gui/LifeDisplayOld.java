package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import logic.LifeBoard;

public class LifeDisplayOld extends LifeDisplay {
	private LifeSquare[][] squares;
	boolean mouseDown;

	public LifeDisplayOld(LifeBoard board) {
		setLayout(new GridLayout(width(), height()));
		mouseDown = false;
		this.board = board;
		squares = new LifeSquare[width()][height()];
		for (int i = 0; i < width(); i++) {
			for (int j = 0; j < height(); j++) {
				squares[i][j] = new LifeSquare(new Point(i, j));
				add(squares[i][j]);
			}
		}
	}

	@Override
	public void update(boolean repaint) {
		board.step();
		for (Point p : board) {
			if (p.x < 0 || p.x >= squares.length)
				continue;
			if (p.y < 0 || p.y >= squares[0].length)
				continue;
			if (repaint)
				squares[p.x][p.y].repaint();
		}
	}

	@Override
	public void update(){
		update(true);
	}
	
	@Override
	public void setGrids(boolean on) {
		Rectangle max = maxDimension();
		for (int i = 0; i < width(); i++) {
			for (int j = 0; j < height(); j++) {
				if (on)
					squares[i][j].setGridBorder();
				else
					squares[i][j].setNoBorder();
			}
		}
	}

	class LifeSquare extends JPanel {
		Point coord;

		public LifeSquare(Point p) {
			coord = p;
			setPreferredSize(new Dimension(sqWidth(), sqWidth()));
			setGridBorder();
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					board.toggle(coord);
					repaint();
				}

				@Override
				public void mousePressed(MouseEvent e) {
					mouseDown = true;
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					mouseDown = false;
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					if (mouseDown) {
						board.set(coord, true);
						repaint();
					}
				}
			});
		}

		public void setGridBorder() {
			setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}

		public void setNoBorder() {
			setBorder(null);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (board.get(coord))
				setBackground(Color.BLACK);
			else
				setBackground(Color.LIGHT_GRAY);
		}
	}

	@Override
	public void applyBoard(LifeBoard newB) {
		// TODO Auto-generated method stub

	}

	@Override
	public int liveCells() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int width() {
		Rectangle max = maxDimension();
		return max.width - max.x;
	}

	public int height() {
		Rectangle max = maxDimension();
		return max.height - max.y;
	}

	@Override
	public Rectangle maxDimension() {
		// TODO Auto-generated method stub
		return null;
	}
}
