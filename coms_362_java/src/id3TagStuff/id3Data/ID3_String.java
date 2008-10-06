package id3TagStuff.id3Data;

import util.Util;
import id3TagStuff.ID3v2_XFrameData;

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
