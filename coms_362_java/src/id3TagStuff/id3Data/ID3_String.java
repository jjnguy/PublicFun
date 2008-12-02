package id3TagStuff.id3Data;

import controller.Util;

public class ID3_String implements ID3v2_XFrameData {

	private String data;

	/**
	 * Creates an ID3_String out of some bytes
	 * 
	 * @param dataP
	 */
	public ID3_String(int[] dataP) {
		data = new String(Util.castIntArrToByteArr(dataP));
	}

	@Override
	public String toString() {
		return data;
	}

	@Override
	public String getType() {
		return "String";
	}

	@Override
	public int[] getByteRepresentation(int versionNumber) {
		byte encoding = 0;
		byte[] ret = new byte[1 + data.getBytes().length];
		ret[0] = encoding;
		System.arraycopy(data.getBytes(), 0, ret, 1, data.getBytes().length);
		return Util.castByteArrToIntArr(ret);
	}
}
