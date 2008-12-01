public interface Collidable {
	public void collide(Collidable other);

	public double getVelocity_X();

	public double getVelocity_Y();

	public void updateVelocity_X(double newVel_x);

	public void updateVelocity_Y(double newVel_y);
}
