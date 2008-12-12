package runnables;

import id3TagStuff.ID3v2_XTag;
import id3TagStuff.frames.ID3v2_XFrame;
import id3TagStuff.id3Data.ID3_Picture;

import java.io.File;
import java.io.IOException;

import util.Util;


public class AddNewFrameTest {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ID3v2_XTag tag = new ID3v2_XTag(new File("TestFiles/03 Sonny.mp3"));
		int[] b = Util.getBytesFromFile(new File("TestFiles/compiler.PNG"));
		ID3_Picture idPic = new ID3_Picture("A pic that I added", (byte) 0, (byte) 0, "png", b);
		ID3v2_XFrame toAdd = new ID3v2_XFrame(idPic, tag.getVersion());
		tag.addID3v2_XFrame(toAdd);
	}
}
