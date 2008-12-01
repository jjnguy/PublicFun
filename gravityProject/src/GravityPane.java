import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GravityPane extends JPanel {

	private final Color BGROUND_COLOR = Color.WHITE;

	private List<GravityObject> objects;

	public GravityPane() {
		super();
		setBackground(BGROUND_COLOR);
		setPreferredSize(new Dimension(500, 500));
		objects = new ArrayList<GravityObject>();
		objects.add(new GravitySphere(50, 100));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (GravityObject obj : objects) {
			obj.draw((Graphics2D) g);
		}
	}

	public GravityPane run() {
		GravityThread t = new GravityThread(objects);
		t.start();
		return this;
	}

	public void addObject(GravityObject obj) {
		if (obj == null)
			return;
		if (obj.getPosition_Y() < 0 || obj.getPosition_X() < 0) {

		}
		objects.add(obj);
	}
	
	class GravityThread extends Thread {
		private double INTERVAL = .001;
		private List<GravityObject> objects;
		private boolean run;

		public GravityThread(List<GravityObject> objects) {
			this.objects = objects;
			run = false;
		}

		@Override
		public void run() {
			run = true;
			while (run) {
				for (GravityObject obj : objects) {
					obj.fall(INTERVAL);
				}
				try {
					sleep((long)(INTERVAL*10000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
