package util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

public class UseListOfPrimes {

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(new File("out1.txt"));
		ArrayList<BigInteger> arr = new ArrayList<BigInteger>();
		for (int i = 0; i < 33200; i++){
			arr.add(new BigInteger(in.next()));
		}
		/*in = new Scanner(new File("out2.txt"));
		while (in.hasNext()) {
			arr.add(new BigInteger(in.next()));
		}
		in = new Scanner(new File("out3.txt"));
		while (in.hasNext()) {
			arr.add(new BigInteger(in.next()));
		}
		in = new Scanner(new File("out4.txt"));
		while (in.hasNext()) {
			arr.add(new BigInteger(in.next()));
		}*/
		System.out.println("Done loading list");
		ListIterator<BigInteger> iter = arr.listIterator();
		boolean isPrime = false;
		PrintStream out2 = new PrintStream(new File("out5.txt"));
		BigInteger temp = new BigInteger("-1");
		BigInteger limmit = new BigInteger("1100000001");
		for (BigInteger i = new BigInteger("1000000001"); i.compareTo(limmit) < 1; i.add(new BigInteger("2"))) {
			temp = new BigInteger("-1");
			iter = arr.listIterator();
			while (iter.hasNext() && temp.compareTo(i)  <= 1) {
				temp = iter.next();
				if (i.mod(temp).equals(new BigInteger("0"))) {
					isPrime = false;
					break;
				}
				isPrime = true;
			}
			if (isPrime) {
				out2.println(i);
				isPrime = false;
			}
		}
		System.out.print("Hi");
		out2.close();
	}
}
