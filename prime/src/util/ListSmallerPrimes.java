package util;

public class ListSmallerPrimes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long runningTime;
		long startTime = System.currentTimeMillis();
		boolean isPrime = false;
		int i = 3;
		for (; i < 10000000; i += 2) {
			for (int j = 3; j < Math.sqrt(i) + 1; j += 2) {
				if (i % j == 0) {
					isPrime = false;
					break;
				}
				isPrime = true;
			}
			if (isPrime) {
				System.out.println(i);
				isPrime = false;
			}
		}
		runningTime = System.currentTimeMillis() - startTime;
		System.out.println("This took " + runningTime/1000 + " seconds.");

	}

}
