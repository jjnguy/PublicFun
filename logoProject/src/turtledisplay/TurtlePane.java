package turtledisplay;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import executables.Executable;

import TurtleInterpreter.TurtleOrientation;

public class TurtlePane extends JPanel {

	private List<TurtleOrientation> points;

	public TurtlePane(Dimension size) {
		TurtleOrientation o = new TurtleOrientation();
		o.angle = 0;
		o.x = size.width / 2;
		o.y = size.height / 2;
		points = new ArrayList<TurtleOrientation>();
		points.add(o);
		setBackground(Color.WHITE);
		setPreferredSize(size);
	}

	public void setExecutable(Executable ex){
		points.clear();
		TurtleOrientation o = new TurtleOrientation();
		o.angle = 0;
		o.x = getWidth() / 2;
		o.y = getHeight() / 2;
		points.add(o);
		while(ex.hasNextStatement()){
			o = ex.execute(o).copy();
			points.add(o);
		}
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (points.isEmpty())
			return;
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		// g2.setStroke(new BasicStroke(2));
		Point p1 = new Point(points.get(0).x, points.get(0).y);
		for (int i = 0; i < points.size(); i++) {
			Point p = new Point(points.get(i).x, points.get(i).y);
			g2.drawLine(p1.x, p1.y, p.x, p.y);
			p1 = p;
		}
		Turtle.draw(g2, p1);
	}
}
