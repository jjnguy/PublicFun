package hw4;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ShortAudio {

	private static final int CHUNK_SIZE = 16;
	private static final short FORMAT = 1;
	private static final short CHANNELS = 1;
	private static final short TWO = 2;

	private short[] data;
	private int sampleRate;

	public ShortAudio(String fileLoc) throws IOException {
		LEDataInputStream rdr = null;
		try {
			rdr = new LEDataInputStream(fileLoc);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("The supplied file did not exist, or some IO went worng");
		}
		byte[] fourByteBuffer = new byte[4];
		rdr.read(fourByteBuffer);
		String riff = new String(fourByteBuffer);
		int sampleCount = rdr.readInt() / 2 - 36;
		rdr.read(fourByteBuffer);
		String wave = new String(fourByteBuffer);
		rdr.read(fourByteBuffer);
		String fmt = new String(fourByteBuffer);
		int sizeOfChunk = rdr.readInt();
		if (sizeOfChunk != 16)
			throw new IllegalArgumentException("Expected chunk size of 16, instead got: " + sizeOfChunk);
		short format = rdr.readShort();
		if (format != 1)
			throw new IllegalArgumentException("Expected format of 1, instead got: " + format);
		int channels = rdr.readShort();
		if (channels != 1)
			throw new IllegalArgumentException("Expected channels of 1, instead got: " + channels);
		int sampleRate = rdr.readInt();
		int sampleRateDbl = rdr.readInt();
		if (sampleRate * 2 != sampleRateDbl)
			throw new IllegalArgumentException("Sample rate was not double the double sample rate");
		this.sampleRate = sampleRate;
		short two = rdr.readShort();
		if (two != 2)
			throw new IllegalArgumentException("WTF");
		short bitsPerSample = rdr.readShort();
		if (bitsPerSample != 16)
			throw new IllegalArgumentException("Fuck this");
		rdr.read(fourByteBuffer);
		String data = new String(fourByteBuffer);
		int numSamplesAgain = rdr.readInt() / 2;
		this.data = new short[numSamplesAgain];
		for (int i = 0; i < numSamplesAgain; i++) {
			this.data[i] = rdr.readShort();
		}
		rdr.close();
	}

	public void append(ShortAudio other) {
		short[] dataNew = new short[this.data.length + other.data.length];
		System.arraycopy(this.data, 0, dataNew, 0, this.data.length);
		System.arraycopy(other.data, 0, dataNew, this.data.length, other.data.length);
		this.data = dataNew;
	}

	public void writeTo(String saveLoc) throws IOException {
		LEDataOutputStream out = new LEDataOutputStream(saveLoc);
		out.writeBytes("RIFF");
		out.writeInt(36 + data.length * 2);
		out.writeBytes("WAVE");
		out.writeBytes("fmt ");
		out.writeInt(CHUNK_SIZE);
		out.writeShort(FORMAT);
		out.writeShort(CHANNELS);
		out.writeInt(sampleRate);
		out.writeInt(sampleRate * 2);
		out.writeShort(TWO);
		out.writeShort((short)16);
		out.writeBytes("data");
		out.writeInt(data.length * 2);
		out.write(data);
		out.close();
	}

}
