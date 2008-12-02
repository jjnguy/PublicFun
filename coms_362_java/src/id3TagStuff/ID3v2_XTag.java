package id3TagStuff;

import id3TagStuff.frames.ID3v2_XFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import controller.Util;


/**
 * An ID3v2_XTag represents an entire ID3 tag. It can be artifically created from a fake mp3
 * file.
 * 
 * It holds no information about the actual music data, save a copy of the file it was
 * generated from.
 * 
 * @author Justin Nelson
 * 
 */
public class ID3v2_XTag {

	// For now we add a kilobyte of extra padding
	private static int EXTRA_PADDING_TO_ADD = 1024;

	private File mp3File;
	private ID3v2_XTagHeader header;
	private List<ID3v2_XFrame> frames;
	private int paddingSize;

	/**
	 * Creates a new ID3v2.X tag from a given mp3 file.
	 * 
	 * The actual song data is not checked, nor is the file extension. No error is created if
	 * the song data is corrupt but the tag is correct. Various bad things may happen if the
	 * ID3 tag is missing or corrupt.
	 * 
	 * @param mp3FileP
	 *            The mp3 file that contains the tag to be created
	 * @throws IOException
	 */
	public ID3v2_XTag(File mp3FileP) throws IOException {

		mp3File = mp3FileP;

		// read the bytes in from the file for the header
		InputStream in = new FileInputStream(mp3File);
		byte[] headerBytes = new byte[10];
		int bytesRead = in.read(headerBytes);
		if (bytesRead != headerBytes.length) {
			throw new IOException("WTF Mate?");
		}
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
				// The padding is what's left plus the empty bytes we read into the header
				paddingSize = garbage.length + frameHeadderLength;
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
	 * @deprecated
	 */
	public void addID3v2_XFrame(ID3v2_XFrame toAdd) throws IOException {
		int[] toWrite = toAdd.getFrameData(header.getMajorVersion());
		// if (toWrite.length > paddingSize)
		// throw new UnsupportedOperationException(
		// "We do not support adding frames without enough padding.");
		paddingSize -= toAdd.getTotalSize();
		header.incrementTagSize(toAdd.getTotalSize());
		if (Util.DEBUG)
			System.out.println(paddingSize);
		if (paddingSize < 0) {
			addPaddingToFile(toAdd.getTotalSize() + EXTRA_PADDING_TO_ADD);
			paddingSize = EXTRA_PADDING_TO_ADD;
		}
		frames.add(toAdd); // we need to update the current object as well as the file
		RandomAccessFile file = new RandomAccessFile(mp3File, "rw");
		file.seek(header.getHeaderSize());

		file.write(Util.castIntArrToByteArr(toWrite));
	}

	/**
	 * 
	 * @param ammountToAdd
	 * @throws IOException
	 * @deprecated
	 */
	public void addPaddingToFile(int ammountToAdd) throws IOException {
		// TODO Now I need to figure out how to grow the padding...
		// TODO error might be in the util methods
		// First we save the tag
		InputStream in = new FileInputStream(mp3File);
		int[] tagBytes = Util.getBytesFromStream(in, header.getHeaderSize()
				+ header.getTagSize());
		// skip the current padding
		Util.skipFully(in, paddingSize);
		// Now we read in and save the song data
		int[] songBytes = Util.getBytesFromStream(in);
		// Now its time to write over our current file (better have a backup)
		OutputStream out = new FileOutputStream(mp3File);
		Util.writeIntArrToStream(out, tagBytes);
		int[] paddingToAdd = new int[ammountToAdd];
		Util.writeIntArrToStream(out, paddingToAdd);
		Util.writeIntArrToStream(out, songBytes);
		// Idealy this should put the file back where it was with some extra tag bytes in the
		// middle
	}

	@Override
	public String toString() {
		return String.format("ID3v2.%d, file name: %s", header.getMajorVersion(), mp3File
				.getName());
	}

	/**
	 * @return the size of the padding after the tag and before the song data.
	 */
	public int getPaddingSize() {
		return paddingSize;
	}

	/**
	 * @return the version of the ID3 tag.
	 */
	public int getVersion() {
		return header.getMajorVersion();
	}

	/**
	 * Allows access to the frames of the Tag
	 * 
	 * @return a List containing all of the {@link ID3v2_XFrame}s in the tag.
	 */
	public List<ID3v2_XFrame> getAllFrames() {
		return frames;
	}
}
