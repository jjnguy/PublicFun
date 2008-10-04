package id3TagStuff.id3Data;

import id3TagStuff.ID3v2_2FrameData;

public class ID3_String implements ID3v2_2FrameData {
	private String data;
	public ID3_String(byte[] dataP){
		data = new String(dataP);
	}
}
