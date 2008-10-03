package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class ListPrimes implements Runnable {

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public void run() {
		PrintStream out = null;
		try {
			out = new PrintStream(new File("out1.txt"));
		} catch (Exception e) {

		}
		boolean isPrime = false;
		int i = 3;
		long runningTime;
		long startTime = System.currentTimeMillis();
		for (; i < 250000000; i += 2) {
			for (int j = 3; j < Math.sqrt(i) + 1; j += 2) {
				if (i % j == 0) {
					isPrime = false;
					break;
				}
				isPrime = true;
			}
			if (isPrime) {
				out.println(i);
				isPrime = false;
			}
		}
		runningTime = System.currentTimeMillis() - startTime;
		System.out.println("This quarter took " + runningTime / 1000 + "seconds.");
		out.close();
		try {
			out = new PrintStream(new File("out2.txt"));
		} catch (Exception e) {

		}

		startTime = System.currentTimeMillis();
		for (; i < 500000000; i += 2) {
			for (int j = 3; j < Math.sqrt(i) + 1; j += 2) {
				if (i % j == 0) {
					isPrime = false;
					break;
				}
				isPrime = true;
			}
			if (isPrime) {
				out.println(i);
				isPrime = false;
			}
		}
		runningTime += (System.currentTimeMillis() - startTime);
		out.close();
		System.out.println("This quarter took " + (System.currentTimeMillis() - startTime) / 1000
				+ "seconds.");
		try {
			out = new PrintStream(new File("out3.txt"));
		} catch (Exception e) {

		}

		startTime = System.currentTimeMillis();
		for (; i < 750000000; i += 2) {
			for (int j = 3; j < Math.sqrt(i) + 1; j += 2) {
				if (i % j == 0) {
					isPrime = false;
					break;
				}
				isPrime = true;
			}
			if (isPrime) {
				out.println(i);
				isPrime = false;
			}
		}
		runningTime += (System.currentTimeMillis() - startTime);
		out.close();
		System.out.println("This quarter took " + (System.currentTimeMillis() - startTime) / 1000
				+ "seconds.");
		try {
			out = new PrintStream(new File("out4.txt"));
		} catch (Exception e) {

		}
		startTime = System.currentTimeMillis();
		for (; i < 1000000000; i += 2) {
			for (int j = 3; j < Math.sqrt(i) + 1; j += 2) {
				if (i % j == 0) {
					isPrime = false;
					break;
				}
				isPrime = true;
			}
			if (isPrime) {
				out.println(i);
				isPrime = false;
			}
		}
		out.close();
		System.out.println("This quarter took " + (System.currentTimeMillis() - startTime) / 1000
				+ "seconds.");
		runningTime += (System.currentTimeMillis() - startTime);
		System.out.println("The total running time was about " + ((runningTime / 1000) / 60) + " mins.");

	}

}
