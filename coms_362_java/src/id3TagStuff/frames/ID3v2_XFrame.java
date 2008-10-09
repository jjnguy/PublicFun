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
			data = new ID3_Picture(header.getVersion(), Util.castByteArrToIntArr(frameBytes));
		} else {
			data = new ID3_String(Util.castByteArrToIntArr(frameBytes));
		}
	}

	public ID3v2_XFrame(ID3v2_XFrameData data, int vers) {
		this.data = data;
		header = new ID3v2_XFrameHeader(vers);
		header.setSize(data.getByteRepresentation(vers).length);
		if (data.getClass() == ID3_Picture.class) {
			header.setTagID(vers < 3 ? "PIC" : "APIC");
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
		String headerStr = header.getID();
		if (majorVersionNumber < 3 && headerStr.length() == 4) {
			headerStr = ID3v2_XFrameHeader.translate3ByteTagTo4ByteTagAndBack(headerStr);
		} else if (majorVersionNumber >= 3 && headerStr.length() == 3) {
			headerStr = ID3v2_XFrameHeader.translate3ByteTagTo4ByteTagAndBack(headerStr);
		}

		int[] dataBytes = data.getByteRepresentation(majorVersionNumber);
		int totalLength = dataBytes.length;
		int headerLength = (majorVersionNumber < 3 ? 6 : 10) + header.getSize();
		totalLength += headerLength;

		byte[] ret = new byte[totalLength];
		int offset = 0;
		System.arraycopy(headerStr.getBytes(), 0, ret, offset, headerStr.getBytes().length);
		offset += headerStr.getBytes().length;

		int size = header.getSize();
		// TODO translate size into three bytes
		byte b1 = (byte) ((size >> 16) & 0xff);
		byte b2 = (byte) ((size >> 8) & 0xff);
		byte b3 = (byte) (size & 0xff);
		ret[offset++] = b1;
		ret[offset++] = b2;
		ret[offset++] = b3;

		System.arraycopy(dataBytes, 0, ret, offset, dataBytes.length);
		return Util.castByteArrToIntArr(ret);
	}
}
