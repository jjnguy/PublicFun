package id3TagStuff.frames;

import id3TagStuff.frames.v2.ID3v2_2Frame;
import id3TagStuff.frames.v3.ID3v2_3Frame;

import java.io.IOException;
import java.io.InputStream;

public class ID3v2_XFrameFactory {

	private ID3v2_XFrameFactory() {
	}

	public static ID3v2_XFrame getFrame(int[] headerBytes, InputStream frameStream)
			throws IOException {
		ID3v2_XFrame frame = null;
		if (headerBytes.length == 6) {
			frame = new ID3v2_2Frame(headerBytes, frameStream);
		} else if (headerBytes.length == 10) {
			frame = new ID3v2_3Frame(headerBytes, frameStream);
		} else {
			throw new IllegalArgumentException(
					"A frame header must be either 6 or 10 bytes long");
		}
		return frame;
	}
}
