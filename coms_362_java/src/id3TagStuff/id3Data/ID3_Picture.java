package id3TagStuff.id3Data;

import id3TagStuff.ID3v2_2FrameData;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ID3_Picture implements ID3v2_2FrameData {

	private String format, description;
	private byte type;
	private byte[] data;

	public ID3_Picture(byte[] dataP) throws IOException {
		format = new String(Arrays.copyOfRange(dataP, 0, 3));
		type = dataP[3];
		int descWidth = 0;
		for (int i = 4; i < dataP.length; i++) {
			if (dataP[i] == (byte) 00) {
				break;
			}
			descWidth++;
		}
		description = new String(Arrays.copyOfRange(dataP, 4, 4 + descWidth));
		this.data = Arrays.copyOfRange(dataP, 4 + descWidth, dataP.length);

		for (int i = 0; i < this.data.length; i++) {
			if (data[i] < 0) {
				data[i] = (byte) (data[i] & 0xff);
			}
		}
		// begin test code
		PrintStream out = new PrintStream(new File(".dataTest"));
		out.write(data);
		BufferedImage i = ImageIO.read(new File(".dataTest"));
		ImageIO.write(i, format, new File("imageTest.png"));
	}
}
