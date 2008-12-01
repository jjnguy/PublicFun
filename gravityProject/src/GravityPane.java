import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GravityPane extends JPanel {
	private long INTERVAL = 150;
	private final Color BGROUND_COLOR = Color.WHITE;

	private List<GravityObject> objects;

	public GravityPane() {
		super();
		setBackground(BGROUND_COLOR);
		setPreferredSize(new Dimension(500, 500));
		objects = new ArrayList<GravityObject>();
	}

	public void advanceFrame() {
		for (GravityObject obj : objects) {
			obj.fall(INTERVAL);
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (GravityObject obj : objects) {
			obj.draw((Graphics2D) g, getHeight());
		}
	}

	public void addObject(GravityObject obj) {
		if (obj == null)
			return;
		objects.add(obj);
	}

	private MouseListener clickListener = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	};
}
