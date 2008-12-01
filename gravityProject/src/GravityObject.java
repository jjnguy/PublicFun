import java.awt.Graphics2D;

public interface GravityObject {
	public static final double GRAVITATIONAL_CONSTANT = -9.80665; // 9.81

	public double getPosition_Y();

	public double getPosition_X();

	public double getVelocity_Y();

	public double getVolicity_X();

	/**
	 * Falling will update the objects velocity and position based on it's acceleration due to
	 * gravity
	 * 
	 * @param seconds
	 *            the length of the fall in seconds
	 */
	public void fall(double seconds);

	public void draw(Graphics2D g);
}
