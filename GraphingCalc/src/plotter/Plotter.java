package plotter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Plotter extends JPanel {

	private static final Color B_GROUND_COLOR = Color.WHITE;
	private static final Color LINE_COLOR = Color.BLUE;
	private static final Color POINT_COLOR = Color.BLACK;
	private static final Color RULE_COLOR = Color.LIGHT_GRAY;

	public static final int VIEWPORT_MAX = 10;
	public static final int VIEWPORT_MIN = -10;
	public static final int Y_MAX = 10;
	public static final int Y_MIN = -10;
	public static final int X_MAX = VIEWPORT_MAX;
	public static final int X_MIN = VIEWPORT_MIN;
	private static final int RELATIVE_X_MAX = X_MAX * 2;
	private static final int RELATIVE_Y_MAX = Y_MAX * 2;

	private static final int POINT_DIAMETER = 4;
	private static final double SLOPE_TOLLERANCE = Double.MAX_VALUE;

	private List<Point2D.Double> points;
	private boolean connectedMode;
	private boolean onTop = true;

	private JFrame plotHolder;

	public Plotter() {
		this(true);
	}

	public Plotter(boolean connectedModeP) {
		points = new ArrayList<Point2D.Double>();
		setPreferredSize(new Dimension(400, 400));
		setBackground(B_GROUND_COLOR);
		connectedMode = connectedModeP;
	}

	public void plotPoint(Point2D.Double p) {
		points.add(p);
		repaint();
	}

	public void addPoint(final double x, final double y) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				plotPoint(new Point2D.Double(x, y));
			}
		};
		SwingUtilities.invokeLater(r);
	}

	public void clear() {
		points.clear();
		repaint();
	}

	public void startPlotter() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				createAndRun();
			}
		};
		SwingUtilities.invokeLater(r);
	}

	private void createAndRun() {
		plotHolder = new JFrame();
		plotHolder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		plotHolder.add(this);
		plotHolder.pack();
		plotHolder.setAlwaysOnTop(onTop);
		plotHolder.setVisible(true);
	};

	public void stopPlotter() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				points.clear();
				plotHolder.dispose();
			}
		};
		SwingUtilities.invokeLater(r);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		paintGrid(g2);
		paintPoints(g2);
	}

	private void paintPoints(Graphics2D g) {
		boolean paintHugeSlope = true;
		Color originalColor = g.getColor();
		Stroke originalStroke = g.getStroke();
		g.setColor(POINT_COLOR);
		g.setStroke(new BasicStroke(2));
		Point oldJavaPoint = null;
		Point2D.Double oldRegPoint = null;
		for (Point2D.Double point : points) {
			if (point.x > X_MAX || point.x < X_MIN)
				continue;
			if (point.y > Y_MAX || point.y < Y_MIN)
				continue;
			Point javaPoint = shiftPointToJavaCoord(point, getHeight(), getWidth());
			if (connectedMode && oldRegPoint != null) {
				double slope;
				if (oldRegPoint.x - point.x != 0) {
					slope = (oldRegPoint.y - point.y) / (double) (oldRegPoint.x - point.x);
					// System.out.println(slope);
				} else {
					slope = 0;
				}
				// For tan graphs we want to exclude the lines between points that shouldn't be
				// there
				if (paintHugeSlope || Math.abs(slope) < SLOPE_TOLLERANCE) {
					g.setColor(LINE_COLOR);
					g.drawLine(oldJavaPoint.x, oldJavaPoint.y, javaPoint.x, javaPoint.y);
					g.setColor(POINT_COLOR);
				}
			} else {
				g.fillOval(javaPoint.x - POINT_DIAMETER / 2, javaPoint.y - POINT_DIAMETER / 2,
						POINT_DIAMETER, POINT_DIAMETER);
			}
			oldJavaPoint = javaPoint;
			oldRegPoint = point;
		}

		g.setStroke(originalStroke);
		g.setColor(originalColor);
	}

	private static Point shiftPointToJavaCoord(Point2D.Double cartesianCoord,
			int componentHeight, int componentWidth) {
		double relativeX = cartesianCoord.x + 10;
		double relativeY = cartesianCoord.y + 10;

		int javaCoordX = (int) ((relativeX / RELATIVE_X_MAX) * componentWidth);
		int javaCoordY = componentHeight
				- (int) ((relativeY / RELATIVE_Y_MAX) * componentHeight);
		return new Point(javaCoordX, javaCoordY);
	}

	private void paintGrid(Graphics2D g) {
		// save some things before changing them
		Color originalColor = g.getColor();
		Stroke originalStroke = g.getStroke();

		g.setColor(RULE_COLOR);

		final int MINI_LINE_LENGTH = 20;

		// Main lines
		g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
		g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
		// Mini lines
		for (int i = 1; i < RELATIVE_X_MAX; i++) {
			// the ones on the x axis
			g.drawLine(i * getWidth() / RELATIVE_X_MAX,
					getHeight() / 2 - MINI_LINE_LENGTH / 2, i * getWidth() / RELATIVE_X_MAX,
					getHeight() / 2 + MINI_LINE_LENGTH / 2);
			// the ones on the y axis
			g.drawLine(getWidth() / 2 - MINI_LINE_LENGTH / 2,
					i * getHeight() / RELATIVE_Y_MAX, getWidth() / 2 + MINI_LINE_LENGTH / 2, i
							* getHeight() / RELATIVE_Y_MAX);
		}

		g.setStroke(originalStroke);
		g.setColor(originalColor);
	}
}
