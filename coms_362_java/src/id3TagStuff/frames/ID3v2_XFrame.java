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
			data = new ID3_Picture(header.getVersion(), Util
					.castByteArrToIntArr(frameBytes));
		} else {
			data = new ID3_String(Util.castByteArrToIntArr(frameBytes));
		}
	}

	public int getFrameSize() {
		return header.getSize();
	}

	public int getTotalSize() {
		return getFrameSize() + header.getVersion() < 3 ? 6 : 10;
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

	/**
	 * Gets the raw bytes that can be directly written to a ID3 tag
	 * 
	 * @return
	 */
	public int[] getFrameData(int majorVersionNumber) {
		// TODO needs to combine the frame header bytes and the data bytes to make the
		// whole frame
		int[] ret = new int[(majorVersionNumber < 3 ? 6 : 10) + header.getSize()];
		String headerStr = header.getID();
		if (majorVersionNumber < 3 && headerStr.length() == 10) {
			headerStr = ID3v2_XFrameHeader.translate3ByteTagTo4ByteTagAndBack(headerStr);
		}

		return ret;
	}
}
