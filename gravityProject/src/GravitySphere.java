import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class GravitySphere implements GravityObject {
	private final int DIAMETER = 10;
	private final Color color = Color.RED;
	
	private double pos_x, pos_y;
	private double vel_x, vel_y;

	public GravitySphere(double xpos, double ypos) {
		pos_x = xpos;
		pos_y = ypos;
		vel_x = 0;
		vel_y = 0;
	}

	/**
	 * The shorter fall time the more accurate the calculation
	 */
	@Override
	public void fall(double seconds) {
		pos_x = pos_x + vel_x * seconds + .5 * (GravityObject.GRAVITATIONAL_CONSTANT)
				* seconds * seconds;
		pos_y = pos_y + vel_y * seconds + .5 * (GravityObject.GRAVITATIONAL_CONSTANT)
				* seconds * seconds;

		vel_x = vel_x + GRAVITATIONAL_CONSTANT * seconds;
		vel_y = vel_y + GRAVITATIONAL_CONSTANT * seconds;
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

	@Override
	public void draw(Graphics2D g) {
		Color originalColor = g.getColor();
		Stroke originalStroke = g.getStroke();

		g.setColor(color);
		g.fillOval((int) pos_x, (int) pos_y, DIAMETER, DIAMETER);
		
		g.setColor(originalColor);
	}

}
