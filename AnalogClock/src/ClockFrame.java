import java.awt.Color;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Method;

import javax.swing.JFrame;

public class ClockFrame extends JFrame {

	public ClockFrame(ClockPanel p) {
		setUndecorated(true);
		getContentPane().setBackground(Color.WHITE);
		setAlwaysOnTop(true);
		try {
			Class<?> awtUtilitiesClass = Class.forName("com.sun.awt.AWTUtilities");
			Method mSetWindowOpacity = awtUtilitiesClass.getMethod("setWindowOpacity", Window.class,
					float.class);
			mSetWindowOpacity.invoke(null, this, Float.valueOf(0.75f));
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		add(p);
		p.addMouseMotionListener(mml);
		p.addMouseListener(ml);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private Point clickedPoint;
	private Point clickedLocation;
	MouseListener ml = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			clickedLocation = getLocationOnScreen();
			clickedPoint = e.getLocationOnScreen();
		}
	};
	MouseMotionListener mml = new MouseMotionAdapter() {
		@Override
		public void mouseDragged(MouseEvent arg0) {
			int xDiff = clickedPoint.x - arg0.getXOnScreen();
			int yDiff = clickedPoint.y - arg0.getYOnScreen();
			Point newPoint = new Point(clickedLocation.x - xDiff, clickedLocation.y - yDiff);
			ClockFrame.this.setLocation(newPoint);
		}
	};
}
