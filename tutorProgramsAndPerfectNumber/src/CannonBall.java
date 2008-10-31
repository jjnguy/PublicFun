public class CannonBall {

	public static final double G = 9.81;

	public static void computeVelovityAndPos(double initialVelocity) {
		final double deltaT = .01;
		final double INITIAL_POS = 0;
		double pos = INITIAL_POS;
		double velocity = initialVelocity;
		int loops = 0;

		while (pos >= 0) {
			pos = pos + velocity * deltaT;
			velocity = velocity - G * deltaT;
			loops++;
			if (loops % 100 == 0)
				System.out.println("Position is: " + pos);
		}
	}

	public static void main(String[] args) {
		computeVelovityAndPos(100);
	}
}
