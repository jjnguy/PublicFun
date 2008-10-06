package runnables;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import id3TagStuff.ID3v2_XTag;
import id3TagStuff.frames.ID3v2_XFrame;

public class CreateAndPrintTagFromFile {
	public static void main(String[] args) throws IOException {
		// suck my kiss is version 3...yay!
		ID3v2_XTag tg = new ID3v2_XTag(new File("07 Suck My Kiss.mp3"));
		System.out.println(tg);
		List<ID3v2_XFrame> frames = tg.getAllFrames();
		for (ID3v2_XFrame frame : frames) {
			System.out.println("Printing a new frame");
			//System.out.println(frame);
		}
	}
}
