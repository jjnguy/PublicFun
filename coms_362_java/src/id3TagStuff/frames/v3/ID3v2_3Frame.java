package id3TagStuff.frames.v3;

import id3TagStuff.frames.ID3v2_XFrame;
import id3TagStuff.id3Data.ID3_Comment;
import id3TagStuff.id3Data.ID3_Picture;
import id3TagStuff.id3Data.ID3_String;
import id3TagStuff.id3Data.ID3v2_XFrameData;

import java.io.IOException;
import java.io.InputStream;

import util.Util;

public class ID3v2_3Frame implements ID3v2_XFrame {
	
	private ID3v2_3FrameHeader header;
	private ID3v2_XFrameData data;

	public ID3v2_3Frame(int[] headerBytes, InputStream tagFile) throws IOException {
		header = new ID3v2_3FrameHeader(headerBytes);
		byte[] frameBytes = new byte[header.getSize()];
		tagFile.read(frameBytes);
		if (header.getID().startsWith("T", 0)) {
			data = new ID3_String(Util.castByteArrToIntArr(frameBytes));
		} else if (header.getID().equals("COM")) {
			data = new ID3_Comment(Util.castByteArrToIntArr(frameBytes));
		} else if (header.getID().equals("PIC")) {
			data = new ID3_Picture(Util.castByteArrToIntArr(frameBytes));
		}
	}

	public int getSize() {
		return header.getSize();
	}

	@Override
	public String toString() {
		return String.format("Type: %s, Data: %s", header.getID(), data.toString());
	}

	@Override
	public String getFrameType() {
		// TODO Auto-generated method stub
		return null;
	}
}
