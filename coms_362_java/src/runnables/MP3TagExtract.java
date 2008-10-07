package runnables;

import id3TagStuff.ID3v2_XTag;
import id3TagStuff.frames.ID3v2_XFrame;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MP3TagExtract {
	public static void main(String[] args) {
		Scanner stdin = new Scanner(System.in);
		File file = null;
		if (args.length == 0) {
			System.out.print("Enter the filename of the mp3 file: ");
			file = new File(stdin.nextLine());
		} else {
			file = new File(args[0]);
		}
		ID3v2_XTag theTag = null;
		try {
			theTag = new ID3v2_XTag(file);
		} catch (IOException e) {
			System.out.println("The file was not found, so we could not create the tag.");
			System.exit(0);
		}
		System.out.println("The tag was sucessfully created!");
		System.out.println("What would you like to do?");
		System.out.println("1. View Tag Data");
		System.out.println("2. Exit");
		int option = Integer.MIN_VALUE;
		try {
			option = Integer.parseInt(stdin.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("You must enter a number silly.");
			System.exit(0);
		}
		if (option == 2) {
			System.out.println("See Ya!");
			System.exit(0);
		} else if (option != 1) {
			System.out.println("That wasn't a choice.");
			System.exit(0);
		}
		List<ID3v2_XFrame> frames = theTag.getAllFrames();
		
		System.out.println("Available Tags To View:");

	}
}
