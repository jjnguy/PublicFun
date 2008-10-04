package id3TagStuff;

import id3TagStuff.id3Data.ID3_Comment;
import id3TagStuff.id3Data.ID3_Picture;
import id3TagStuff.id3Data.ID3_String;

import java.io.IOException;
import java.io.InputStream;

public class ID3v2_2Frame implements ID3v2_XFrame {
	private ID3v2_2FrameHeader header;
	private String tagID;
	private ID3v2_2FrameData data;
	public ID3v2_2Frame(byte[] headerBytes, InputStream tagFile) throws IOException {
		header = new ID3v2_2FrameHeader(headerBytes);
		byte[] frameBytes = new byte[header.getSize()];
		tagFile.read(frameBytes);
		tagID = header.getID();
		if (tagID.startsWith("T", 0)){
			data = new ID3_String(frameBytes);
		}else if(tagID.equals("COM")){
			data = new ID3_Comment(frameBytes);
		}else if (tagID.equals("PIC")){
			data = new ID3_Picture(frameBytes);
		}
	}
	public int getSize() {
		// TODO Auto-generated method stub
		return header.getSize();
	}
}
