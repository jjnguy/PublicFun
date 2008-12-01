import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

public class Wall implements Collidable {

	private Point center;
	private boolean twoSided;
	private double angle;
	private double force;

	public Wall() {
		this(new Point(0, 0), 0, false);
	}

	public Wall(Point centerP, double angleP, boolean twoSidedP) {
		center = centerP;
		angle = angleP;
		twoSided = twoSidedP;
		force = 1;
	}

	public void draw(Graphics2D g, int gravPaneHeight, int gravPaneWidth) {
		Color originalColor = g.getColor();
		Stroke originalStroke = g.getStroke();

		g.setStroke(new BasicStroke(3));
		int y1 = (int) (center.y - (center.x * Math.tan(degreeToRadians(angle))));
		int x1 = 0; // temp, kinda
		int y2 = (int) (center.y + ((gravPaneWidth - center.x) * Math
				.tan(degreeToRadians(angle))));
		int x2 = gravPaneWidth; // temp, kinda

		g.drawLine(x1, gravPaneHeight - y1, x2, gravPaneHeight - y2);

		g.setColor(originalColor);
		g.setStroke(originalStroke);
	}

	private static double degreeToRadians(double deg) {
		return deg * Math.PI / 180.0;
	}

	@Override
	public double getVelocity_X() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getVelocity_Y() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateVelocity_X(double newVel_x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateVelocity_Y(double newVel_y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collide(Collidable other) {
		// TODO Auto-generated method stub
		
	}

}
