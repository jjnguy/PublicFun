package id3TagStuff.id3Data;

import id3TagStuff.ID3v2_2FrameData;

import java.util.Arrays;

public class ID3_Comment implements ID3v2_2FrameData {
	private String language, comment;

	public ID3_Comment(byte[] dataP) {
		language = new String(Arrays.copyOfRange(dataP, 0, 3));
		comment = new String(Arrays.copyOfRange(dataP, 3, dataP.length));
	}

	@Override
	public String toString() {
		return String.format("Language: %s, Comment: %s", language, comment);
	}
}
