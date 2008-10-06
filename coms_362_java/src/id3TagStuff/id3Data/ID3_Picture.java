package id3TagStuff.id3Data;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import util.Util;

public class ID3_Picture implements ID3v2_XFrameData {

	private String format, description;
	private byte type;
	private int[] data;

	public ID3_Picture(int[] dataP) throws IOException {
		System.out.println("We are creating a pic");
		format = new String(Util.castIntArrToByteArr(Arrays.copyOfRange(dataP, 1, 4)));
		type = (byte)dataP[4];
		int descWidth = 0;
		for (int i = 5; i < dataP.length; i++) {
			if (dataP[i] == (byte) 00) {
				break;
			}
			descWidth++;
		}
		description = new String(Util.castIntArrToByteArr(Arrays.copyOfRange(dataP, 4, 4 + descWidth)));
		this.data = Arrays.copyOfRange(dataP, 4 + descWidth, dataP.length);

		System.out.println(type);
		// begin test code
		PrintStream out = new PrintStream(new File(".dataTest"));
		Util.writeIntArrToStream(out, data);
		BufferedImage i = ImageIO.read(new File(".dataTest"));
		ImageIO.write(i, format, new File("imageTest." + format));
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
