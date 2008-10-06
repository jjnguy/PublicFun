package id3TagStuff.id3Data;

import id3TagStuff.ID3v2_XFrameData;

import java.util.Arrays;

import util.Util;

public class ID3_Comment implements ID3v2_XFrameData {
	private String language, comment;

	public ID3_Comment(int[] dataP) {
		language = new String(Util.castIntArrToByteArr(Arrays.copyOfRange(dataP, 0, 3)));
		comment = new String(Util.castIntArrToByteArr(Arrays.copyOfRange(dataP, 3,
				dataP.length)));
	}

	@Override
	public String toString() {
		return String.format("Language: %s, Comment: %s", language, comment);
	}
}
