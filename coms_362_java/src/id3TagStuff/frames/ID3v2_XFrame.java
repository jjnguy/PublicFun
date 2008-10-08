package id3TagStuff.frames;

import id3TagStuff.id3Data.ID3_Comment;
import id3TagStuff.id3Data.ID3_Picture;
import id3TagStuff.id3Data.ID3_String;
import id3TagStuff.id3Data.ID3v2_XFrameData;

import java.io.IOException;
import java.io.InputStream;

import util.Util;

public class ID3v2_XFrame {

	private ID3v2_XFrameHeader header;
	private ID3v2_XFrameData data;

	public ID3v2_XFrame(int[] headerBytes, InputStream tagFile) throws IOException {
		header = new ID3v2_XFrameHeader(headerBytes);
		byte[] frameBytes = new byte[header.getSize()];
		tagFile.read(frameBytes);
		if (header.getID().startsWith("T", 0)) {
			data = new ID3_String(Util.castByteArrToIntArr(frameBytes));
		} else if (header.getID().matches("COM|COMM")) {
			data = new ID3_Comment(Util.castByteArrToIntArr(frameBytes));
		} else if (header.getID().matches("PIC|APIC")) {
			data = new ID3_Picture(Util.castByteArrToIntArr(frameBytes));
		} else {
			data = new ID3_String(Util.castByteArrToIntArr(frameBytes));
		}
	}

	public int getFrameSize() {
		return header.getSize();
	}

	@Override
	public String toString() {
		return String.format("Type: %s, Data: %s", header.getID(), data.toString());
	}

	public String getFrameType() {
		return header.getID();
	}

	public String getEnglishTagDescription() {
		return ID3v2_XFrameHeader.translateFrameHeaderStringToEnglish(header.getID());
	}

	public ID3v2_XFrameData getData() {
		return data;
	}
}
