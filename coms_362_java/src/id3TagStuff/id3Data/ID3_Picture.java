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

	public ID3_Picture(byte[] data) throws IOException {
		format = new String(Arrays.copyOfRange(data, 0, 3));
		type = data[3];
		description = new String(Arrays.copyOfRange(data, 4, data.length))	
				.split(new String(new byte[] { 0 }))[0];
		data = Arrays.copyOfRange(data, description.length(), data.length);
		for (int i = 0; i < data.length; i++) {
			if (data[i] < 0) {
				data[i] = (byte) (data[i] & 0xff);
			}
		}
		// begin test code
		PrintStream out = new PrintStream(new File(".dataTest"));
		out.write(data);
		//BufferedImage i = ImageIO.read(new File(".dataTest"));
		//ImageIO.write(i, format, new File("imageTest.png"));
	}
}
