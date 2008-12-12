package id3TagStuff.id3Data;

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
		// the substrings trim off null bytes
		return String.format("Language: %s, Comment: %s", language.substring(1, language
				.length()), comment.substring(0, comment.length() - 1));
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Comment";
	}

	@Override
	public int[] getByteRepresentation(int versionNumber) {
		// encoding + language + empty comment description + comment
		byte[] ret = new byte[1 + 3 + 1 + comment.length()];
		ret[0] = ret[4] = 0;
		System.arraycopy(language.getBytes(), 0, ret, 1, language.getBytes().length);
		System.arraycopy(comment.getBytes(), 0, ret, 5, comment.getBytes().length);
		return Util.castByteArrToIntArr(ret);
	}
}
