package runnables;

import id3TagStuff.ID3v2_XTag;

import java.io.File;
import java.io.IOException;

public class AddPaddingToFile {
	public static void main(String[] args) throws IOException {
		ID3v2_XTag tag = new ID3v2_XTag(new File("TestFiles/03 Sonny.mp3"));
		tag.addPaddingToFile(10000);
	}
}
