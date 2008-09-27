import java.util.Scanner;

public class CountDots {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int numRows = in.nextInt();
		printDiamond(numRows);
	}

	public static void printDiamond(int height) {
		int numSpace = height / 2;
		int numStars = 1;

		for (int i = 0; i <= height / 2; i++) {
			printChar(numSpace, ' ');
			printChar(numStars, '*');
			System.out.println();
			numStars += 2;
			numSpace--;
		}

		numSpace += 2;
		numStars -= 4;

		for (int i = height / 2; i > 0; i--) {
			printChar(numSpace, ' ');
			printChar(numStars, '*');
			System.out.println();
			numStars -= 2;
			numSpace++;
		}
	}

	private static void printChar(int num, char c) {
		for (int j = 1; j <= num; j++) {
			System.out.print(c);
		}
	}
}
