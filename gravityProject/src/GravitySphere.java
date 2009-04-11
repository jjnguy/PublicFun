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
		pos_y = pos_y + vel_y * seconds + .5
				* (GravityObject.GRAVITATIONAL_CONSTANT) * seconds * seconds;

		pos_x = pos_x + vel_x * seconds;

		vel_y = vel_y + GRAVITATIONAL_CONSTANT * seconds;
		checkForBounce();
	}

	private void checkForBounce() {
		if (pos_y < DIAMETER / 2 && vel_y < 0) {
			pos_y = DIAMETER / 2;
			vel_y = -vel_y * BOUNCE_FACTOR;
		}
		if (pos_x < DIAMETER / 2) {
			pos_x = DIAMETER / 2;
			vel_x = -vel_x * BOUNCE_FACTOR;
		}
		// rough hack because I know the pane it is in is 500 wide
		if (pos_x > 500 - DIAMETER / 2) {
			pos_x = 500 - DIAMETER / 2;
			vel_x = -vel_x * BOUNCE_FACTOR;
		}
		// rough hack because I know the pane it is in is 500 wide
		if (pos_y > 500 - DIAMETER / 2){
			pos_y = 500 - DIAMETER / 2;
			vel_y = - vel_y * BOUNCE_FACTOR;
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

	// Dragable stuff

	private double last_xPos;
	private double last_yPos;
	private long lastGrabbed_time;

	@Override
	public void grabedOnto(MouseEvent e) {
		lastGrabbed_time = System.nanoTime();
		held = true;
		last_xPos = pos_x;
		pos_x = e.getX();
		last_yPos = pos_y;
		pos_y = e.getComponent().getHeight() - e.getY();
		vel_x = vel_y = 0;
	}

	@Override
	public void letGo() {
		int FACTOR = 1000000;
		if (held) {
			held = false;
			long timeDif = System.nanoTime() - lastGrabbed_time;
			System.out.println("Time dif: " + timeDif);
			if (timeDif <= 0)
				return;
			vel_x = FACTOR * (pos_x - last_xPos) / (double) (timeDif);
			vel_y = FACTOR * (pos_y - last_yPos) / (double) (timeDif);
			System.out.println("Vel x: " + vel_x);
			System.out.println("Vel y: " + vel_y);
		}
	}

	@Override
	public boolean containsPoint(Point p) {
		// basically, if the point is farther than the
		// radius away, it is not contained
		double distance = Math.sqrt((p.x - pos_x) * (p.x - pos_x)
				+ (p.y - pos_y) * (p.y - pos_y));
		return distance <= DIAMETER / 2;
	}

	@Override
	public boolean isHeld() {
		return held;
	}

	// End Dragable stuff

	@Override
	public void collide(Collidable other) {
		double oldVel_x = other.getVelocity_X();
		double oldVel_y = other.getVelocity_Y();
		vel_x = (1 - 2 * Math.PI - Wall.degreeToRadians(90) / Math.PI / 2)
				* oldVel_x;
		vel_y = (2 * Math.PI - Wall.degreeToRadians(90) / Math.PI / 2 - 1)
				* oldVel_y;
	}

}
