package edu.iastate.cs228.hw3;

public class Test {
	public static void main(String[] args) {
		CS228LinkedList<String> list = new CS228LinkedList<String>();
		for(int i = 0; i < 16; i++){
			list.add("" + i);
		}
		System.out.println(list);
	}
}
