import java.util.Scanner;

public class Lock {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		final int cOne = 1;
		final int cTwo = 2;
		final int cThree = 3;
		int one = 0, two = 0, three = 0;

		while (three != cThree) {
			while (two != cTwo) {
				while (one != cOne) {
					one = in.nextInt();
				}
			}
		}
	}
}
