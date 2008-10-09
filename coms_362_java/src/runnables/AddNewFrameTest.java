package runnables;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import util.Util;

import id3TagStuff.ID3v2_XTag;
import id3TagStuff.frames.ID3v2_XFrame;
import id3TagStuff.id3Data.ID3_Picture;

public class AddNewFrameTest {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ID3v2_XTag tag = new ID3v2_XTag(new File("TestFiles/07 Suck My Kiss.mp3"));
		InputStream in = new FileInputStream(new File("TestFiles/compiler.PNG"));
		byte[] b = new byte[in.available()];
		in.read(b);
		ID3v2_XFrame toAdd = new ID3v2_XFrame(new ID3_Picture(tag.getVersion(), Util
				.castByteArrToIntArr(b)), tag.getVersion());
		tag.addID3v2_XFrame(toAdd);
	}

}
