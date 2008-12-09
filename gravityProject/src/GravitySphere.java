import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

public class GravitySphere implements Dragable, Collidable {

	private int DIAMETER = 30;
	private double BOUNCE_FACTOR = .6;
	private Color color = Color.RED;

	private boolean held;

	private double pos_x, pos_y;
	private double vel_x, vel_y;

	public GravitySphere(double xpos, double ypos) {
		pos_x = xpos;
		pos_y = ypos;
		vel_x = 0;
		vel_y = 0;
		held = false;
	}

	public GravitySphere(double xpos, double ypos, double yvel) {
		this(xpos, ypos);
		vel_y = yvel;
	}

	public GravitySphere(double xpos, double ypos, double yvel, double xvel) {
		this(xpos, ypos, yvel);
		vel_x = xvel;
	}

	public GravitySphere(double xpos, double ypos, double yvel, double xvel,
			double bounceFactor, int diameter, Color color) {
		this(xpos, ypos, yvel, xvel);
		DIAMETER = diameter;
		BOUNCE_FACTOR = bounceFactor;
		this.color = color;
	}

	/**
	 * The shorter fall time the more accurate the calculation
	 */
	@Override
	public void fall(long miliseconds) {
		if (held)
			return;
		double seconds = miliseconds / 1000.0;
		pos_y = pos_y + vel_y * seconds + .5 * (GravityObject.GRAVITATIONAL_CONSTANT)
				* seconds * seconds;

		pos_x = pos_x + vel_x * seconds;

		vel_y = vel_y + GRAVITATIONAL_CONSTANT * seconds;
		checkForBounce();
	}

	private void checkForBounce() {
		if (pos_y < DIAMETER / 2 && vel_y < 0) {
			pos_y = DIAMETER / 2;
			vel_y = -vel_y * BOUNCE_FACTOR;
		}
	}

	@Override
	public double getVelocity_Y() {
		return vel_y;
	}

	@Override
	public double getVelocity_X() {
		return vel_x;
	}

	@Override
	public double getPosition_X() {
		return pos_x;
	}

	@Override
	public double getPosition_Y() {
		return pos_y;
	}

	public void clickedOn() {
		held = true;
	}

	@Override
	public void draw(Graphics2D g, int gravPaneHeight) {
		Color originalColor = g.getColor();
		Stroke originalStroke = g.getStroke();

		g.setColor(color);
		g.fillOval((int) pos_x - (DIAMETER / 2), (int) (gravPaneHeight - pos_y)
				- (DIAMETER / 2), DIAMETER, DIAMETER);
		g.setStroke(new BasicStroke(2));
		g.setColor(Color.BLACK);
		g.drawOval((int) pos_x - (DIAMETER / 2), (int) (gravPaneHeight - pos_y)
				- (DIAMETER / 2), DIAMETER, DIAMETER);

		g.setStroke(originalStroke);
		g.setColor(originalColor);
	}

	@Override
	public void grabedOnto(MouseEvent e) {
		held = true;
		pos_x = e.getX();
		pos_y = e.getComponent().getHeight() - e.getY();
		vel_x = vel_y = 0;
	}

	@Override
	public void letGo() {
		held = false;
	}

	@Override
	public boolean containsPoint(Point p) {
		// basically, if the point is farther than the
		// radius away, it is not contained
		double distance = Math.sqrt((p.x - pos_x) * (p.x - pos_x) + (p.y - pos_y)
				* (p.y - pos_y));
		return distance <= DIAMETER / 2;
	}

	@Override
	public boolean isHeld() {
		return held;
	}

	@Override
	public void collide(Collidable other) {
		// TODO
	}

	@Override
	public void updateVelocity_X(double newVel_x) {
		vel_x = newVel_x;
	}

	@Override
	public void updateVelocity_Y(double newVel_y) {
		vel_y = newVel_y;
	}

}
