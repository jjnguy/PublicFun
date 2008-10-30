package id3TagStuff.id3Data;

import java.util.Arrays;

import util.Util;

public class ID3_String implements ID3v2_XFrameData {

	private String data;

	public ID3_String(int[] dataP) {
		data = new String(Util.castIntArrToByteArr(dataP));
		// this strips the null char from the end
		data = data.substring(0, data.length() - 1);
	}

	@Override
	public String toString() {
		return data;
	}

	// @Override
	public String getType() {
		// TODO Auto-generated method stub
		return "String";
	}

	// @Override
	public int[] getByteRepresentation(int versionNumber) {
		byte encoding = 0;
		// enc + data + null-terminator
		byte[] ret = new byte[1 + data.getBytes().length + 1];
		ret[0] = encoding;
		System.arraycopy(data.getBytes(), 0, ret, 1, data.getBytes().length);
		ret[ret.length - 1] = 0;
		return Util.castByteArrToIntArr(ret);
	}
}
