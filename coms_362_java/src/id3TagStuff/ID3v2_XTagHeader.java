package id3TagStuff;

import java.util.Arrays;

import util.Util;

public class ID3v2_XTagHeader {
	
	private String tagIdentifier;
	private int majorVersion, minorVersion, flags;
	private int size;

	public ID3v2_XTagHeader(int[] bytes) {
		if (bytes.length != 10)
			throw new IllegalArgumentException(
					"The byte string must be exactly 10 bytes long.");
		tagIdentifier = new String(Util.castIntArrToByteArr(Arrays.copyOfRange(bytes, 0,
				3)));
		majorVersion = bytes[3];
		minorVersion = bytes[4];
		flags = bytes[5];
		size = (bytes[6] << 21) + (bytes[7] << 14) + (bytes[8] << 7) + bytes[9];
	}

	public int getSize() {
		return size;
	}

	public int getMajorVersion(){
		return majorVersion;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("ID: %s, Version: %d.%d, Flags: %d, Size: %d",
				tagIdentifier, majorVersion, minorVersion, flags, size);

	}
}
