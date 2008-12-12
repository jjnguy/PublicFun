package turtledisplay;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;


public class Turtle {
	
	private static int RAD = 3;
	
	public static void draw(Graphics2D g, Point p){
		Color orig = g.getColor();
		g.setColor(Color.GREEN);
		g.fillOval(p.x - RAD, p.y - RAD, 2 * RAD, 2 * RAD);
		g.setColor(orig);
	}
	
}
