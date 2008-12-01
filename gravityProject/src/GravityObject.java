import java.awt.Graphics2D;
import java.awt.Point;

public interface GravityObject {
	// gravity is sped up
	public static final double GRAVITATIONAL_CONSTANT = -15; // 9.81

	public double getPosition_Y();

	public double getPosition_X();

	public double getVelocity_Y();

	public double getVelocity_X();

	/**
	 * Falling will update the objects velocity and position based on it's acceleration due to
	 * gravity
	 * 
	 * @param seconds
	 *            the length of the fall in seconds
	 */
	public void fall(long miliseconds);

	public void draw(Graphics2D g, int gravPaneHeight);
	
}
