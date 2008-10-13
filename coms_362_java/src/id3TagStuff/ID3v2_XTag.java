package id3TagStuff;

import id3TagStuff.frames.ID3v2_XFrame;
import id3TagStuff.id3Data.ID3v2_XFrameData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import util.Util;

public class ID3v2_XTag {

	private File mp3File;
	private ID3v2_XTagHeader header;
	private List<ID3v2_XFrame> frames;
	private int paddingSize;

	/**
	 * Creates a new ID3v2.X tag.
	 * 
	 * @param mp3FileP
	 *            The mp3 file that contains the tag
	 * @throws IOException
	 */
	public ID3v2_XTag(File mp3FileP) throws IOException {

		mp3File = mp3FileP;

		// read the bytes in from the file for the header
		InputStream in = new FileInputStream(mp3File);
		byte[] headerBytes = new byte[10];
		in.read(headerBytes);

		// create the meat of the tag object
		header = new ID3v2_XTagHeader(Util.castByteArrToIntArr(headerBytes));
		frames = new ArrayList<ID3v2_XFrame>();

		// keep track of how many bytes we have left in the tag
		int bytesLeft = header.getTagSize();
		// the frame header is 6 bytes for 2.2 and 10 bytes for 2.3 or 2.4
		int frameHeadderLength = header.getMajorVersion() == 2 ? 6 : 10;

		// the array will be filled with header bytes every time, either 6 or 10 bytes
		byte[] frameHeadderBytes = new byte[frameHeadderLength];
		byte[] blankHeader = new byte[frameHeadderLength];
		// create new frames until we have no more bytes left in the tag.
		while (bytesLeft > 0) {
			in.read(frameHeadderBytes);
			if (new String(frameHeadderBytes).equals(new String(blankHeader))) {
				byte[] garbage = new byte[bytesLeft - frameHeadderLength];
				paddingSize = garbage.length;
				in.read(garbage);
				bytesLeft = 0;
				break;
			}
			ID3v2_XFrame frame = new ID3v2_XFrame(Util.castByteArrToIntArr(frameHeadderBytes),
					in);

			frames.add(frame);
			// we use <framelength> + <headerlength> bytes every time
			bytesLeft -= frameHeadderLength;
			bytesLeft -= frame.getFrameSize();
		}
		in.close();
	}

	/**
	 * Immediately adds the data onto the tag in the file
	 * 
	 * @param toAdd
	 * @throws IOException
	 * @throws UnsupportedOperationException
	 *             Throws the exception if the new frame will not fit within the paddin that we
	 *             have left
	 */
	public void addID3v2_XFrame(ID3v2_XFrame toAdd) throws IOException {
		int[] toWrite = toAdd.getFrameData(header.getMajorVersion());
		// if (toWrite.length > paddingSize)
		// throw new UnsupportedOperationException(
		// "We do not support adding frames without enough padding.");
		paddingSize -= toWrite.length;
		if (Util.DEBUG)
			System.out.println(paddingSize);
		frames.add(toAdd); // we need to update the current object as well as the file
		RandomAccessFile file = new RandomAccessFile(mp3File, "rw");
		file.seek(header.getHeaderSize());

		file.write(Util.castIntArrToByteArr(toWrite));
	}

	@Override
	public String toString() {
		return String.format("ID3v2.%d, file name: %s", mp3File.getName(), header
				.getMajorVersion());
	}

	public int getPaddingSize() {
		return paddingSize;
	}

	public int getVersion() {
		return header.getMajorVersion();
	}

	/**
	 * Allows access to the frames of the Tag
	 * 
	 * @return the tag frames
	 */
	public List<ID3v2_XFrame> getAllFrames() {
		return frames;
	}
}
