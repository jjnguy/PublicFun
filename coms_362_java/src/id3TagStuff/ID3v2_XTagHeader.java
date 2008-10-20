package id3TagStuff;

import java.util.Arrays;

import util.Util;

public class ID3v2_XTagHeader {

	private String tagIdentifier;
	private int majorVersion, minorVersion, flags;
	private int tagSize;
	private int headerSize;

	public ID3v2_XTagHeader(int[] bytes) {
		headerSize = bytes.length;
		if (bytes.length != 10)
			throw new IllegalArgumentException(
					"The byte string must be exactly 10 bytes long.");
		tagIdentifier = new String(Util.castIntArrToByteArr(Arrays.copyOfRange(bytes, 0, 3)));
		majorVersion = bytes[3];
		minorVersion = bytes[4];
		flags = bytes[5];
		tagSize = (bytes[6] << 21) + (bytes[7] << 14) + (bytes[8] << 7) + bytes[9];
	}

	public int getTagSize() {
		return tagSize;
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	@Override
	public String toString() {
		return String.format("ID: %s, Version: %d.%d, Flags: %d, Size: %d", tagIdentifier,
				majorVersion, minorVersion, flags, tagSize);

	}

	public int getHeaderSize() {
		return headerSize;
	}

	public void incrementTagSize(int totalSize) {
		if (totalSize < 0)
			throw new IllegalArgumentException("A framse size cannot be negative.");
		tagSize += totalSize;
	}
}
