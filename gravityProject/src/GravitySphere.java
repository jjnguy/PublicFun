import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

public class GravitySphere implements GravityObject {
	private final int DIAMETER = 50;
	private final Color color = Color.RED;

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

	/**
	 * The shorter fall time the more accurate the calculation
	 */
	@Override
	public void fall(long miliseconds) {
		if (!held) {
			double seconds = miliseconds / 1000.0;
			pos_y = pos_y + vel_y * seconds + .5 * (GravityObject.GRAVITATIONAL_CONSTANT)
					* seconds * seconds;

			pos_x = pos_x + vel_x * seconds;

			vel_y = vel_y + GRAVITATIONAL_CONSTANT * seconds;
			checkForNeg();
		}
	}

	private void checkForNeg() {
		pos_x = Math.max(pos_x, 0);
		pos_y = Math.max(pos_y, 0);
	}

	@Override
	public double getVelocity_Y() {
		return vel_y;
	}

	@Override
	public double getVolicity_X() {
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

		g.setColor(originalColor);
	}

}
