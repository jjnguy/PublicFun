import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Euler {

	public static void sum3And5Below1000() {
		int sum = 0;
		for (int i = 1; i < 1000; i++) {
			if (i % 3 == 0 || i % 5 == 0) {
				sum += i;
			}
		}
		System.out.println(sum);
	}

	public static int getMaxPalindrome() {
		PriorityQueue<Integer> q = new PriorityQueue<Integer>(50,
				new Comparator<Integer>() {

					@Override
					public int compare(Integer o1, Integer o2) {
						// TODO Auto-generated method stub
						return o2 - o1;
					}
				});
		for (int i = 999; i > 3; i--) {
			for (int j = 999; j > 3; j--) {
				if (palindrome(j * i)) {
					q.offer(j * i);
				}
			}
		}
		return q.poll();
	}

	public static boolean palindrome(int num) {
		return num == reverse(num);
	}

	public static int reverse(int num) {
		String numStr = num + "";
		String rev = "";
		for (int i = numStr.length() - 1; i >= 0; i--) {
			rev += numStr.charAt(i);
		}
		return Integer.parseInt(rev);
	}

	public static List<Integer> fibsBelow4Mil() {
		List<Integer> ret = new ArrayList<Integer>();
		ret.add(1);
		ret.add(2);
		for (int i = 0; i < 100000; i++) {
			int newNum = ret.get(i) + ret.get(i + 1);
			if (newNum > 4000000)
				break;
			ret.add(newNum);
		}
		return ret;
	}

	public static void sumAllEvenVals(List<Integer> ints) {
		int sum = 0;
		for (Integer integer : ints) {
			if (integer % 2 == 0)
				sum += integer;
		}
		System.out.println(sum);
	}

	public static boolean divisibleBy1to20(int num) {
		for (int i = 1; i < 21; i++) {
			if (num % i != 0)
				return false;
		}
		return true;
	}

	public static int minNumberDivisibleBy1to20() {
		for (int i = 1; true; i++) {
			if (divisibleBy1to20(i)) {
				return i;
			}
		}
	}

	public static int difOfSquareOfSumandSumOfSquare() {
		int sumOfSquares = 0;
		int sum = 0;
		for (int i = 1; i <= 100; i++) {
			sumOfSquares += i * i;
			sum += i;
		}
		return sum * sum - sumOfSquares;
	}

	public static boolean isPrime(long num) {
		if (num == 1)
			return false;
		if (num == 2 || num == 3)
			return true;
		if (num % 2 == 0)
			return false;
		double sqrt = Math.sqrt(num) + 3;
		for (long i = 3; i < sqrt; i += 2) {
			if (num % i == 0)
				return false;
		}
		return true;
	}

	public static void printListOfPrimes() {
		int numPrimes = 2; // 2 and 3
		System.out.println("1: 2");
		System.out.println("2: 3");
		for (int i = 4; numPrimes < 10002; i++) {
			if (isPrime(i)) {
				numPrimes++;
				System.out.println(numPrimes + ": " + i);
			}
		}
	}

	public static int findGreatestProduct() {
		String num = "7316717653133062491922511967442657474235534919493496983520312774506326239578318016984801869478851843858615607891129494954595017379583319528532088055111254069874715852386305071569329096329522744304355766896648950445244523161731856403098711121722383113622298934233803081353362766142828064444866452387493035890729629049156044077239071381051585930796086670172427121883998797908792274921901699720888093776657273330010533678812202354218097512545405947522435258490771167055601360483958644670632441572215539753697817977846174064955149290862569321978468622482839722413756570560574902614079729686524145351004748216637048440319989000889524345065854122758866688116427171479924442928230863465674813919123162824586178664583591245665294765456828489128831426076900422421902267105562632111110937054421750694165896040807198403850962455444362981230987879927244284909188845801561660979191338754992005240636899125607176060588611646710940507754100225698315520005593572972571636269561882670428252483600823257530420752963450";
		int maxProd = Integer.MIN_VALUE;
		for (int i = 0; i < num.length() - 4; i++) {
			int num1 = Integer.parseInt(num.charAt(i) + "");
			int num2 = Integer.parseInt(num.charAt(i + 1) + "");
			int num3 = Integer.parseInt(num.charAt(i + 2) + "");
			int num4 = Integer.parseInt(num.charAt(i + 3) + "");
			int num5 = Integer.parseInt(num.charAt(i + 4) + "");
			int prod = num1 * num2 * num3 * num4 * num5;
			if (prod > maxProd) {
				maxProd = prod;
			}
		}
		return maxProd;

	}

	public static boolean isTriplet(int o, int t, int th) {
		return o * o + t * t == th * th || o * o + th * th == t * t
				|| th * th + t * t == o * o;
	}

	public static void print1kTripplet() {
		for (int i = 1; i < 1000; i++) {
			for (int j = 1; j < 1000; j++) {
				for (int k = 1; k < 1000; k++) {
					if (i + j + k == 1000 && isTriplet(k, j, i))
						System.out.println(k + " " + i + " " + j);
				}
			}
		}
	}

	public static String sumPrimesBelow2mil() {
		BigInteger sum = new BigInteger("0");
		for (int i = 1; i < 2000000; i++) {
			if (isPrime(i)) {
				sum = sum.add(new BigInteger(i+""));
			}
		}
		return sum.toString();
	}

	public static void main(String[] args) {
		//System.out.println(isPrime(3));
		System.out.println(sumPrimesBelow2mil());
	}
}
