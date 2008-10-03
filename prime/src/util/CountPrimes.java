package util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;


public class CountPrimes {
	public static void main(String[] args) throws FileNotFoundException{
		PrintStream out = new PrintStream(new File("numOfPrimes.txt"));
		Scanner in = new Scanner(new File("out1.txt"));
		int num = 0;
		while(in.hasNext()){
			num++;
			in.next();
		}
		out.println("File 1: " + num);
		System.out.print(num + "\n");
		in = new Scanner(new File("out2.txt"));
		while(in.hasNext()){
			num++;
			in.next();
		}
		out.print("File 2: " + num);
		System.out.println(num + "\n");
		in = new Scanner(new File("out3.txt"));
		while(in.hasNext()){
			num++;
			in.next();
		}
		out.print("File 3: " + num);
		System.out.println(num + "\n");
		in = new Scanner(new File("out4.txt"));
		while(in.hasNext()){
			num++;
			in.next();
		}
		out.println("Final: " + num);
		System.out.print(num + "\n");
		System.out.println("hi");
		out.close();
	}
}
