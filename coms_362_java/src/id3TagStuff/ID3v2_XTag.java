package id3TagStuff;

import id3TagStuff.frames.ID3v2_XFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import util.Util;

public class ID3v2_XTag {

	private File mp3File;
	private ID3v2_XTagHeader header;
	private List<ID3v2_XFrame> frames;

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
				in.read(garbage);
				bytesLeft = 0;
				break;
			}
			ID3v2_XFrame frame = new ID3v2_XFrame(Util
					.castByteArrToIntArr(frameHeadderBytes), in);

			frames.add(frame);
			// we use <framelength> + <headerlength> bytes every time
			bytesLeft -= frameHeadderLength;
			bytesLeft -= frame.getFrameSize();

			if (Util.DEBUG) {
				System.out.println(bytesLeft);
			}
		}
	}

	@Override
	public String toString() {
		return String.format("File name: %s, Header data: %s", mp3File.getName(), header
				.toString());
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
