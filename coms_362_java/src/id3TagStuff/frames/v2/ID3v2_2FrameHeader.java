package id3TagStuff.frames.v2;

import id3TagStuff.frames.ID3v2_XFrameHeader;

import java.util.Arrays;

import util.Util;

public class ID3v2_2FrameHeader implements ID3v2_XFrameHeader {
	private String tagID;
	private int size;

	public ID3v2_2FrameHeader(int[] headerBytes) {
		if (headerBytes.length != 6)
			throw new IllegalArgumentException(
					"The frame header for version 2.2 needs to be 6 bytes long.");
		tagID = new String(Util
				.castIntArrToByteArr(Arrays.copyOfRange(headerBytes, 0, 3)));
		size = (headerBytes[3] << 16) + (headerBytes[4] << 8) + headerBytes[5];
		if (size < 0) {
			size = size & 0xffff;
		}
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public String getID() {
		return tagID;
	}

}
