package id3TagStuff.id3Data;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import util.Util;

public class ID3_Picture implements ID3v2_XFrameData {

	private String format_MIME, description;
	private byte type, textEncoding;
	private int[] data;

	// TODO this won't work with v2.3
	public ID3_Picture(int majorVersion, int[] dataP) throws IOException {
		if (Util.DEBUG)
			System.out.println("We are creating a pic");
		format_MIME = new String(Util
				.castIntArrToByteArr(Arrays.copyOfRange(dataP, 1, 4)));
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

	public ID3_Picture(int majorVersion, int[] dataP, File saveToLocation)
			throws IOException {
		this(majorVersion, dataP);
		PrintStream out = new PrintStream(saveToLocation);
		Util.writeIntArrToStream(out, data);
	}

	public ID3_Picture(String descriptionP, byte textEncodingP, byte imageType,
			String formatOrMIME, int[] picData) {
		this.description = descriptionP;
		textEncoding = textEncodingP;
		type = imageType;
		format_MIME = formatOrMIME;
		data = picData;
	}

	@Override
	public String toString() {
		return "ID3 Picture: " + description;
	}

	@Override
	public String getType() {
		return "Picture";
	}

	public void saveAs(File loc) throws IOException {
		String name = loc.getName();
		if (!name.endsWith("." + format_MIME.toLowerCase())
				&& !name.endsWith("." + format_MIME.toUpperCase())) {
			name += "." + format_MIME;
		}
		PrintStream out = new PrintStream(new File(name));
		Util.writeIntArrToStream(out, data);
	}

	@Override
	public int[] getByteRepresentation(int majorVersion) {
		byte[] ret = new byte[1 + format_MIME.getBytes().length + 1
				+ description.getBytes().length + 1 + data.length];
		int offset = 0;
		ret[offset++] = 0;
		System
				.arraycopy(format_MIME.getBytes(), 0, ret, 1,
						format_MIME.getBytes().length);
		offset += format_MIME.getBytes().length;
		ret[offset++] = 0;
		System.arraycopy(description.getBytes(), 0, ret, offset,
				description.getBytes().length);
		offset += description.getBytes().length;
		ret[offset++] = 0;
		System.arraycopy(data, 0, ret, offset, data.length);
		return Util.castByteArrToIntArr(ret);
	}
}
