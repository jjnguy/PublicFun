public class PI {
	public static void main(String[] args) {
		double a = 1;
		double b = 1.0 / Math.sqrt(2);
		double t = 1 / 4.0;
		double x = 1;

		for (int i = 0; i < 1023; i++) {
			double y = a;
			a = (a + b) / 2.0;
			b = Math.sqrt(b * y);
			t = t - x * (y - a) * (y - a);
			x = 2 * x;
		}
		System.out.println(a + " " + b + " " + t + " " + x);
		System.out.println((a + b) * (a + b) / (4.0 * t));
	}
}
