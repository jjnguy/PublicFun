package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Scanner;

public class Seive {
	
	public static void analyzePrimeRunningTime() {
		long runningTime;
		long startTime;
		long[] runningTimes = new long[10];
		int higestPrime = 1000000000;
		
		System.out.println("Running method " + runningTimes.length + " times.");
		for (int i = 0; i < runningTimes.length; i++) {
			startTime = System.currentTimeMillis();
			printPrimesBits(higestPrime, false, null);
			runningTime = System.currentTimeMillis() - startTime;
			runningTimes[i] = runningTime;
		}
		
		long sumTimes = 0;
		long minTime = Long.MAX_VALUE;
		long maxTime = Long.MIN_VALUE;
		
		for (int i = 0; i < runningTimes.length; i++) {
			if (runningTimes[i] < minTime)
				minTime = runningTimes[i];
			if (runningTimes[i] > maxTime)
				maxTime = runningTimes[i];
			sumTimes += runningTimes[i];
		}
		
		System.out.println("The avg. running time to calculate all of the primes from one to " + higestPrime + " was " + sumTimes / (double) runningTimes.length
				/ 1000.0 + " seconds.");
		System.out.println("The min time was " + minTime / 1000.0 + " seconds, and the max time was " + maxTime / 1000.0 + " seconds.");
	}
	
	/**
	 * 
	 * @param print
	 *            a boolean describing weather to calculate the numbers and print, or just to calculate them.
	 * @param out,
	 *            if out is null the numbers will get printed to the console, otherwise they will get printed to the file specified in the
	 *            PrintStream.
	 */
	public static void printPrimesIntArray(boolean print, PrintStream out) {
		
		// populate array with number range to be calculated
		int[] arr = new int[15000000];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = i;
		}
		
		// start with the first prime number, 2
		int skip = 2;
		// the first position that a prime number was in
		int lastSkipPos = 1;
		
		// this is how big of a number that we need to divide by is in order to reach our upper
		// limmit
		int limmitOfCheck = (int) Math.sqrt(arr[arr.length - 1]);
		while (lastSkipPos < limmitOfCheck) {
			
			// start j at the next multiple of the prime number that we are about to count by
			// then remove every multiple of that number
			for (int j = 2 * skip; j < arr.length; j += skip) {
				arr[j] = 0;
			}
			
			// go to the next non-0 in the array and use that prime to count by
			for (int i = lastSkipPos + 1; i < arr.length; i++) {
				if (arr[i] != 0) {
					skip = arr[i];
					lastSkipPos = i;
					break;
				}
			}
		}
		
		if (out == null && print) {
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] != 0 && print)
					System.out.println(arr[i]);
			}
		} else if (print) {
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] != 0 && print)
					out.println(arr[i]);
			}
		}
		
	}
	
	public static void printPrimesByteArray(int max, boolean print, PrintStream out) {
		
		// populate array with number range to be calculated
		System.out.print("Populating array....");
		byte[] arr = new byte[max];
		// for (int i = 0; i < arr.length; i++) {
		// arr[i] = 1;
		// }
		byte b = 1;
		Arrays.fill(arr, b);
		System.out.println("Done");
		// start with the first prime number, 2
		int skip = 2;
		// the first position that a prime number was in
		int lastSkipPos = 1;
		
		// this is how big of a number that we need to divide by is in order to reach our upper
		// limmit
		int limmitOfCheck = (int) Math.sqrt(arr.length - 1);
		System.out.print("Calculating....");
		while (lastSkipPos < limmitOfCheck) {
			
			// start j at the next multiple of the prime number that we are about to count by
			// then remove every multiple of that number
			for (int j = 2 * skip; j < arr.length; j += skip) {
				arr[j] = 0;
			}
			
			// go to the next non-0 in the array and use that prime to count by
			for (int i = lastSkipPos + 1; i < arr.length; i++) {
				if (arr[i] != 0) {
					skip = i;
					lastSkipPos = i;
					break;
				}
			}
		}
		System.out.println("Done");
		if (print)
			System.out.print("Printing....");
		if (out == null && print) {
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] != 0)
					System.out.println(i);
			}
		} else if (print) {
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] != 0)
					out.println(i);
			}
		}
		if (print)
			System.out.println("Done");
		
	}
	
	public static void printPrimesBooleanArray(boolean print, PrintStream out) {
		
		// populate array with number range to be calculated
		// System.out.println("Populating array");
		boolean[] arr = new boolean[60000000];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = true;
		}
		// System.out.println("Done");
		// start with the first prime number, 2
		int skip = 2;
		// the first position that a prime number was in
		int lastSkipPos = 1;
		
		// this is how big of a number that we need to divide by is in order to reach our upper
		// limmit
		int limmitOfCheck = (int) Math.sqrt(arr.length - 1);
		// System.out.print("Calculating....");
		while (lastSkipPos < limmitOfCheck) {
			
			// start j at the next multiple of the prime number that we are about to count by
			// then remove every multiple of that number
			for (int j = 2 * skip; j < arr.length; j += skip) {
				arr[j] = false;
			}
			
			// go to the next non-0 in the array and use that prime to count by
			for (int i = lastSkipPos + 1; i < arr.length; i++) {
				if (arr[i]) {
					skip = i;
					lastSkipPos = i;
					break;
				}
			}
		}
		// System.out.println("Done");
		// if (print) System.out.print("Printing....");
		if (out == null && print) {
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] && print)
					System.out.println(i);
			}
		} else if (print) {
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] && print)
					out.println(i);
			}
		}
		// if (print) System.out.println("Done");
		
	}
	
	public static void printPrimesBits(int max, boolean print, PrintStream out) {
		
		// populate array with number range to be calculated
		System.out.print("Populating bitset...");
		BitSet arr = new BitSet(max);
		arr.flip(0, arr.size());
		System.out.println("Done");
		
		// start with the first prime number, 2
		int skip = 2;
		// the first position that a prime number was in
		int lastSkipPos = 1;
		
		// this is how big of a number that we need to divide
		// by is in order to reach our upper limit
		int limmitOfCheck = (int) Math.sqrt(max);
		
		System.out.print("Calculating...");
		// loop until we have jumped by our limit
		while (lastSkipPos < limmitOfCheck) {
			
			// start j at the next multiple of the prime number that we are about to count by
			// then remove every multiple of that number
			for (int j = 2 * skip; j < arr.size(); j += skip) {
				arr.clear(j);
			}
			
			// go to the next non-0 in the array and use that prime to count by
			for (int i = lastSkipPos + 1; i < arr.size(); i++) {
				if (arr.get(i)) {
					skip = i;
					lastSkipPos = i;
					break;
				}
			}
		}
		
		//Now its a simple matter or printing out the results
		if (out == null && print) {
			for (int i = 0; i < arr.size(); i++) {
				if (arr.get(i) && print)
					System.out.println(i);
			}
		} else if (print) {
			for (int i = 0; i < arr.size(); i++) {
				if (arr.get(i) && print)
					out.println(i);
			}
		}
		System.out.println("Done");
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		printPrimesBits(100000000, true, new PrintStream(new File("A bunch of primes")));
	}
	
	public static boolean testSeiveOut(String fileName) throws FileNotFoundException {
		
		Scanner fileIn = new Scanner(new File(fileName));
		// LinkedList<Integer> listPrimes = new LinkedList<Integer>();
		
		Integer toTest;
		while (fileIn.hasNext()) {
			toTest = fileIn.nextInt();
			if (!CheckPrime.isPrime(new BigInteger("" + toTest))) {
				return false;
			} else
				System.out.println(toTest + ": Prime");
		}
		return true;
	}
}
