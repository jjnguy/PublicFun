package runnables;

import id3TagStuff.ID3v2_XTag;
import id3TagStuff.frames.ID3v2_XFrame;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CreateAndPrintTagFromFile {
	public static void main(String[] args) throws IOException {
		// suck my kiss is version 3...yay!
		ID3v2_XTag tg = new ID3v2_XTag(new File("TestFiles/12 Sex Rap.mp3"));
		System.out.println(tg);
		List<ID3v2_XFrame> frames = tg.getAllFrames();
		for (ID3v2_XFrame frame : frames) {
			System.out.println(frame);
		}
	}
}
