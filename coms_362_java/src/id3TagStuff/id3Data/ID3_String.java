package id3TagStuff.id3Data;

import util.Util;

public class ID3_String implements ID3v2_XFrameData {
	private String data;

	public ID3_String(int[] dataP) {
		data = new String(Util.castIntArrToByteArr(dataP));
	}

	@Override
	public String toString() {
		return data;
	}
}
