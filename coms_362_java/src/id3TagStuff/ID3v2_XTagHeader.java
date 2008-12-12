package id3TagStuff;

import java.util.Arrays;

import util.Util;



/**
 * A ID3v2_XTagHeader represents the first few (10) bytes in an ID3 tag.
 * 
 * @author Justin Nelson
 * 
 */
public class ID3v2_XTagHeader {

	private String tagIdentifier;
	private int majorVersion, minorVersion, flags;
	private int tagSize;
	private int headerSize;

	/**
	 * Creates an {@link ID3v2_XTagHeader} given the byte array
	 * 
	 * @param bytes
	 *            the header bytes
	 * @throws IllegalArgumentException
	 *             if the length of the header bytes given is not ten bytes long, or the first
	 *             three bytes aren't id3
	 */
	public ID3v2_XTagHeader(int[] bytes) {
		headerSize = bytes.length;
		if (bytes.length != 10)
			throw new IllegalArgumentException(
					"The byte string must be exactly 10 bytes long.");
		/*String id3 = new String(Util.castIntArrToByteArr(Arrays.copyOfRange(bytes, 0, 2)));	//TODO::JUSTIN MAKE THIS WORK THANKS KBYE
		if (!id3.equalsIgnoreCase("id3"))
			throw new IllegalArgumentException("The file doesn't contain an ID3 tag.");*/
		tagIdentifier = new String(Util.castIntArrToByteArr(Arrays.copyOfRange(bytes, 0, 3)));
		majorVersion = bytes[3];
		minorVersion = bytes[4];
		flags = bytes[5];
		tagSize = (bytes[6] << 21) + (bytes[7] << 14) + (bytes[8] << 7) + bytes[9];
	}

	/**
	 * @return the size ing bytes of the tag
	 */
	public int getTagSize() {
		return tagSize;
	}

	/**
	 * @return the major version of the ID3 tag
	 */
	public int getMajorVersion() {
		return majorVersion;
	}

	@Override
	public String toString() {
		return String.format("ID: %s, Version: %d.%d, Flags: %d, Size: %d", tagIdentifier,
				majorVersion, minorVersion, flags, tagSize);

	}

	/**
	 * @return the lenght of the header. Better be ten bytes or something is completely wrong.
	 */
	public int getHeaderSize() {
		return headerSize;
	}

	/**
	 * Tells the tag header that the tag has grown due to padding addition or adding of a tag.
	 * 
	 * @param totalSize
	 *            the size to increment the tag by.
	 */
	public void incrementTagSize(int totalSize) {
		if (totalSize < 0)
			throw new IllegalArgumentException("A framse size cannot be negative.");
		tagSize += totalSize;
	}
}
