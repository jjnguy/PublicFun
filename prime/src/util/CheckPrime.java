package util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;

/**
 * @author Justin
 * 
 */
public class CheckPrime {
	
	/**
	 * private constructor disallows instantiation
	 */
	private CheckPrime() {
	}
	
	public static void main(String[] args) {
		System.out.println(new BigInteger(Integer.MAX_VALUE + "").multiply(new BigInteger(Integer.MAX_VALUE + "")));
	}
	
	/**
	 * Checks to see if an int is prime, uses brute force.
	 * 
	 * @param num,
	 *            the number to be checked if prime. The allowed range is <code>0 <= num <= Integer.MAX_VALUE</code>
	 * @return true if prime, or false if not prime. 1 is not prime.
	 */
	private static boolean isPrimeInt(int num) {
		if (num == 1)
			return false;
		if (num == 2 || num == 0)
			return true;
		if (num % 2 == 0)
			return false;
		
		int sqrtNum = (int) Math.sqrt(num) + 1;
		for (int i = 3; i < sqrtNum; i += 2) {
			if (num % i == 0)
				return false;
		}
		return true;
	}
	
	public static boolean isPrime(BigInteger num) {
		if (num.compareTo(new BigInteger("" + Integer.MAX_VALUE)) < 0)
			return isPrimeInt(num.intValue());
		if (num.mod(new BigInteger(2 + "")).equals(new BigInteger(0 + "")))
			return false;
		
		BigDecimal forSqrtCalc = new BigDecimal(num);
		
		int sqrtNum;
		
		return false;
	}
	
	/**
	 * Recursive algorithm to find the prime factors of a number
	 * 
	 * @param numToFactor,
	 *            the number to be factored. The allowed range is <code>0 <= num <= Integer.MAX_VALUE</code>
	 * @return LinkedList<Integer>, a list of prime factors of the input
	 */
	public static LinkedList<BigInteger> getListOfFactors(BigInteger numToFactor) {
		LinkedList<BigInteger> listOfFactors = new LinkedList<BigInteger>();
		if (numToFactor.compareTo(new BigInteger("" + 1)) < 0) {
			listOfFactors.add(new BigInteger("" + 1));
			return listOfFactors;
		}
		listOfFactors.add(getListOfFactorsR(numToFactor, listOfFactors));
		return listOfFactors;
	}
	
	/**
	 * @param numToFactor
	 * @param listOfFactors
	 * @return
	 */
	private static BigInteger getListOfFactorsR(BigInteger numToFactor, LinkedList<BigInteger> listOfFactors) {
		if (CheckPrime.isPrime(numToFactor))
			return numToFactor;
		
		BigInteger smallestFactor;
		
		if (numToFactor.mod(new BigInteger(2 + "")).equals(new BigInteger(0 + "")))
			smallestFactor = new BigInteger(2 + "");
		else
			for (smallestFactor = new BigInteger(3 + ""); smallestFactor.compareTo(numToFactor) < 0; smallestFactor = smallestFactor.add(new BigInteger(1 + "")))
				if (numToFactor.mod(smallestFactor).equals(new BigInteger(0 + "")))
					break;
		
		listOfFactors.add(smallestFactor);
		return getListOfFactorsR((numToFactor.divide(smallestFactor)), listOfFactors);
		
	}
	
	/**
	 * @return false
	 */
	@Override
	public boolean equals(Object o) {
		return false;
	}
}
