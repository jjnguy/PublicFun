package id3TagStuff;

import java.util.Arrays;

public class ID3v2_2FrameHeader {
	private String tagID;
	private int size;

	public ID3v2_2FrameHeader(byte[] headerBytes) {
		if (headerBytes.length != 6)
			throw new IllegalArgumentException(
					"The frame header for version 2.2 needs to be 6 bytes long.");
		tagID = new String(Arrays.copyOfRange(headerBytes, 0, 3));
		size = (headerBytes[3] << 16) + (headerBytes[4] << 8) + headerBytes[5];
		if (size < 0){
			size = size & 0xff;
		}
	}
	
	public int getSize(){
		return size;
	}

	public String getID() {
		// TODO Auto-generated method stub
		return tagID;
	}

}
