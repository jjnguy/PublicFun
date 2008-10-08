package id3TagStuff.id3Data;

import id3TagStuff.frames.ID3v2_XFrame;

public interface ID3v2_XFrameData {
	
	public String getType();

	public ID3v2_XFrame createFrame(int majorVersion);
}
