package id3TagStuff.id3Data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import util.Util;

public class ID3_Picture implements ID3v2_XFrameData {

	private String format, description;
	private byte type;
	private int[] data;

	public ID3_Picture(int[] dataP) throws IOException {
		System.out.println("We are creating a pic");
		format = new String(Util.castIntArrToByteArr(Arrays.copyOfRange(dataP, 1, 4)));
		type = (byte) dataP[4];
		int descWidth = 0;
		for (int i = 5; i < dataP.length; i++) {
			if (dataP[i] == (byte) 00) {
				break;
			}
			descWidth++;
		}
		description = new String(Util.castIntArrToByteArr(Arrays.copyOfRange(dataP, 4,
				4 + descWidth)));
		this.data = Arrays.copyOfRange(dataP, 4 + descWidth + 2, dataP.length);
	}
	
	public ID3_Picture(int[] dataP, File saveToLocation) throws IOException{
		this(dataP);
		PrintStream out = new PrintStream(saveToLocation);
		Util.writeIntArrToStream(out, data);
	}

	@Override
	public String toString() {
		return "ID3 Picture: " + description;
	}

	@Override
	public String getType() {
		return "Picture";
	}
}
