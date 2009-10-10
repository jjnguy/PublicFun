import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GravityPane extends JPanel {
	private long INTERVAL = 10;
	private final Color BGROUND_COLOR = Color.WHITE;

	private List<GravityObject> objects;
	private List<Wall> walls;

	private PrintStream log;

	public GravityPane() throws FileNotFoundException {
		super();
		log = new PrintStream(new File("PhysLog"));
		setBackground(BGROUND_COLOR);
		setPreferredSize(new Dimension(500, 500));
		objects = new ArrayList<GravityObject>();
		walls = new ArrayList<Wall>();
		walls.add(new Wall(new Point(getHeight() / 2, 0), 0, false));
		addMouseListener(clickListener);
		addMouseMotionListener(mouseMoveListener);
	}

	public void advanceFrame() {
		for (GravityObject obj : objects) {
			obj.fall(INTERVAL);
			log.println(String.format("Y Pos: %8f  Y Vel: %8f", obj
					.getPosition_Y(), obj.getVelocity_Y()));
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		GravityObject ob1;
		GravityObject ob2;
		for (int i = 0; i < objects.size(); i++) {
			ob1 = objects.get(i);
			if (i == objects.size() - 1) {
				ob2 = ob1;
			} else {
				ob2 = objects.get(i + 1);
			}
			ob1.draw((Graphics2D) g, getHeight());
		}
		for (Wall w : walls) {
			w.draw((Graphics2D) g, getHeight(), getWidth());
		}
	}

	public void addObject(GravityObject obj) {
		if (obj == null)
			return;
		objects.add(obj);
	}

	private Dragable heldOnto;
	
	private MouseListener clickListener = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			Point xFormedPoint = new Point(e.getX(), e.getComponent()
					.getHeight()
					- e.getY());
			// going backwards here so that the objects on top will be the ones
			// that are
			// grabbed
			for (int i = objects.size() - 1; i >= 0; i--) {
				GravityObject obj = objects.get(i);
				if (obj instanceof Dragable
						&& ((Dragable) obj).containsPoint(xFormedPoint)) {
					Dragable objD = (Dragable) obj;
					objD.grabedOnto(e);
					heldOnto = objD;
					break;
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			heldOnto = null;
			for (GravityObject obj : objects) {
				if (obj instanceof Dragable) {
					((Dragable) obj).letGo();
				}
			}
		}
	};

	private MouseMotionListener mouseMoveListener = new MouseMotionListener() {

		@Override
		public void mouseDragged(MouseEvent e) {
			Point xFormedPoint = new Point(e.getX(), e.getComponent()
					.getHeight()
					- e.getY());
			for (GravityObject obj : objects) {
				if (obj instanceof Dragable
						&& ((Dragable) obj).containsPoint(xFormedPoint)
						&& ((Dragable) obj).isHeld()) {
					Dragable objD = (Dragable) obj;
					objD.grabedOnto(e);
				}
			}
			if (heldOnto != null)
				heldOnto.grabedOnto(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}
	};
}
