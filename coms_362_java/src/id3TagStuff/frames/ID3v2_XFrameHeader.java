package id3TagStuff.frames;

import java.util.Arrays;

import util.Util;

public class ID3v2_XFrameHeader {

	private String tagID;
	private int size;
	private int flags;

	public ID3v2_XFrameHeader(int[] headerBytes) {
		if (headerBytes.length == 6) {
			tagID = new String(Util.castIntArrToByteArr(Arrays.copyOfRange(headerBytes,
					0, 3)));
			size = (headerBytes[3] << 16) + (headerBytes[4] << 8) + headerBytes[5];
			if (size < 0) {
				size = size & 0xffff;
			}
			flags = 0;
		} else if (headerBytes.length == 10) {
			tagID = new String(Util.castIntArrToByteArr(Arrays.copyOfRange(headerBytes,
					0, 4)));
			size = (headerBytes[4] << 24) + (headerBytes[5] << 16)
					+ (headerBytes[6] << 8) + headerBytes[7];
			if (size < 0) {
				size = size & 0xffff;
			}
			flags = (headerBytes[8] << 8) + headerBytes[9];
		} else {
			throw new IllegalArgumentException("Header bytes must 6 or 10");
		}
	}

	public int getSize() {
		return size;
	}

	public String getID() {
		return tagID;
	}

	public int getFlags() {
		return flags;
	}

	public static String translateFrameHeaderStringToEnglish(String headerType) {
		if (headerType.matches("PIC|APIC")){
			return "Picture";
		}
		if(headerType.matches("COMM?")){
			return "Comment";
		}
		
		return null;
	}

}
