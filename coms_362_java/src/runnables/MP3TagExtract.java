package runnables;

import id3TagStuff.ID3v2_XTag;
import id3TagStuff.frames.ID3v2_XFrame;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MP3TagExtract {
	public static int EXIT_OPTION;
	public static int VIEW_INFO_OPTION;
	public static int EXTRACT_PIC_OPTION;

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
			System.out.println("The file was not found; we could not create the tag.");
			System.exit(0);
		}
		List<ID3v2_XFrame> frames = theTag.getAllFrames();
		boolean hasPic = false;
		for (ID3v2_XFrame frame : frames) {
			if (frame.getFrameType().matches("APIC|PIC")) {
				hasPic = true;
				break;
			}
		}
		int optionNumber = 1;
		System.out.println("The tag was sucessfully created!");
		System.out.println("What would you like to do?");
		VIEW_INFO_OPTION = optionNumber;
		System.out.println(optionNumber++ + ". View Tag Data");
		if (hasPic) {
			EXTRACT_PIC_OPTION = optionNumber;
			System.out.println(optionNumber++ + ". Extract Picture");
		}
		EXIT_OPTION = optionNumber;
		System.out.println(optionNumber++ + ". Exit");
		System.out.print(">>");
		int option = Integer.MIN_VALUE;
		try {
			option = Integer.parseInt(stdin.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("You must enter a number silly.");
			System.exit(0);
		}
		if (option == EXIT_OPTION) {
			System.out.println("See Ya!");
			System.exit(0);
		} else if (option == VIEW_INFO_OPTION) {
			while (true) {
				System.out.println("Available Tags To View:");
				int count = 0;
				for (ID3v2_XFrame frame : frames) {
					System.out.println(count + "." + frame.getEnglishTagDescription());
					count++;
				}
				System.out.print(">>");
				try {
					option = Integer.parseInt(stdin.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Please enter a number next time.");
					System.exit(0);
				}
				System.out.println("Tag data:");
				System.out.println(frames.get(option).getData());
			}
		} else if (option == EXTRACT_PIC_OPTION) {
			// TODO extract stuff
		} else {
			System.out.println("NOT AN OPTION FUCKER!!!");
			System.exit(0);
		}
	}
}
