package edu.iastate.cs309.torrentManager.containers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.iastate.cs309.torrentManager.TorrentFile;
import edu.iastate.cs309.torrentManager.exceptions.IncompleteFileException;
import edu.iastate.cs309.util.Util;

/**
 * FileAccess encapsulates all file access for a single torrent, and abstracts
 * all file access as a single byte array.
 * 
 * @author sralmai
 * 
 */
public class FileAccess
{
	/** total size in bytes of all files */
	long totalLength = 0;

	/** do we do all validation up front, or just as pieces are requested? */
	boolean strictChecking = false;

	/** array of filenames in this torrent */
	File[] fileNames = null;

	/** array of open files in this torrent */
	FileChannel[] files = null;

	/** index of first byte of each file (corresponding to files array) */
	long[] offsets = null;

	/** length in bytes of each file (corresponding to files array) */
	long[] lengths = null;

	Object writeLock = new Object();

	/**
	 * the TorrentFile this FileAccess class describes (saved for verifyPiece()
	 * SHA1 values)
	 */
	TorrentFile torrFile = null;

	/**
	 * internal record of good pieces
	 */
	private Bitfield pieces = null;

	/**
	 * create all files and the directory structure (if they don't exist)
	 * 
	 * @param baseDir
	 *            base directory in the filesystem
	 * @param t
	 *            torrentfile we are usuing
	 * @param b
	 *            saved bitfield of valid pieces (blindly accept these)
	 * @param strictChecking
	 *            true to perform all piece validation at initialization (slow),
	 *            or false to perform it on an ad-hoc basis
	 * @throws SecurityException
	 *             when passed directory is broken
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public FileAccess(String baseDir, TorrentFile t, Bitfield b, boolean strictChecking) throws SecurityException, IOException, NoSuchAlgorithmException
	{
		this.strictChecking = strictChecking;
		File test = new File(baseDir);
		if (!test.isDirectory())
			throw new SecurityException(baseDir + " is not a directory (or doesn't exist)!");

		torrFile = t;

		/** make a clean bitfield */
		pieces = new Bitfield(t.getNumOfPieces());

		/** set up names and lengths */
		initializeFileNames(baseDir);

		/**
		 * create missing files and verify existing ones
		 */
		initializeFilesystem(strictChecking);
	}

	/**
	 * initialize all file channels
	 * 
	 * @throws IOException
	 */
	private void initializeFileChannels() throws IOException
	{
		files = new FileChannel[fileNames.length];
		for (int i = 0; i < files.length; ++i)
		{
			files[i] = (new RandomAccessFile(fileNames[i], "rw")).getChannel();
		}
	}

	/**
	 * helper method for the constructor
	 * 
	 * ensures all files in the torrent exist, are of the correct length, and
	 * checks if any pieces are valid
	 * 
	 * @param fullCheck
	 *            verify all possibly good files right away (takes a long time)
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	private void initializeFilesystem(boolean fullCheck) throws IOException, NoSuchAlgorithmException
	{
		boolean[] toCheck = new boolean[lengths.length];

		for (int i = 0; i < lengths.length; ++i)
		{
			if (fileNames[i].exists() && fileNames[i].length() == lengths[i])
			{
				/** this file may be correct */
				toCheck[i] = true;
			}
			else
			{
				/** erase all bad files and make new ones */
				// TODO (FindBugs) test for error condition and alert the user.
				fileNames[i].delete();

				/** ensure entire directory structure preset */
				// TODO (FindBugs) test for error condition and alert the user.
				fileNames[i].getParentFile().mkdirs();

				/** make this file the correct size */
				fill(i);
			}
		}

		/** now that all files exist */
		initializeFileChannels();

		if (fullCheck)
		{
			if (Util.DEBUG)
				System.out.println("strict checking is on... verifying files");
			/** check potentially complete files */
			for (int i = 0; i < lengths.length; ++i)
			{
				if (toCheck[i])
					verifyFile(i);
			}
		}
	}

	/**
	 * wrapper for verify piece which checks all pieces used by a file
	 * 
	 * @param i
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	private void verifyFile(int i) throws NoSuchAlgorithmException, IOException
	{
		int start = (int) (offsets[i] / torrFile.pieceLength());
		int end = (int) ((offsets[i] + lengths[i]) / torrFile.pieceLength());

		if (Util.DEBUG)
			System.out.print("verifying " + fileNames[i]);
		for (int j = start; j < (end + 1); ++j)
		{
			verifyPiece(j);
		}

		if (Util.DEBUG)
			System.out.println("done.");

	}

	/**
	 * fill in the file with zeros to the correct size
	 * 
	 * @param the
	 *            file index to fill
	 * @throws IOException
	 */
	private void fill(int i) throws IOException
	{
		FileChannel file = new FileOutputStream(fileNames[i]).getChannel();

		/** only need to touch the last byte in each file */
		byte[] buff = new byte[1];

		file.write(ByteBuffer.wrap(buff), lengths[i] - 1);
	}

	/**
	 * return a copy of the raw bitfield (used by the sendBitfield message in
	 * the bittorrent spec)
	 * 
	 * @return a copy of the raw bitfield representation of the current pieces
	 */
	public byte[] getRawBitfield()
	{
		return pieces.getBytes();
	}

	/**
	 * @param piece
	 *            index of the torrent
	 * @return true if the piece is correct
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public boolean verifyPiece(int piece) throws NoSuchAlgorithmException, IOException
	{
		if (pieces.isSet(piece))
			return true;

		boolean retVal = false;

		/** total piece length */
		long pieceLength = getPieceLength(piece);

		/** offset in the total torrent */
		long pieceOffset = piece * torrFile.pieceLength();

		PieceHash hash = getHash(pieceOffset, pieceLength);

		retVal = hash.equals(torrFile.getHash(piece));

		/** update bitfield */
		if (retVal)
		{
			pieces.set(piece);
		}

		if (Util.DEBUG)
		{
			if (retVal)
				System.out.println("Piece " + piece + " verified! ;)");
			else
				System.out.println("Piece " + piece + " failed verification : (");
		}

		return retVal;
	}

	/**
	 * generate a SHA1 sum from a chunk
	 * 
	 * @param offset
	 * @param length
	 * @return SHA1 encapsulated in PieceHash of passed chunk
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	private PieceHash getHash(long offset, long length) throws NoSuchAlgorithmException, IOException
	{
		if (offset + length > totalLength)
			throw new IndexOutOfBoundsException();
		/** number of bytes read */
		long read = 0;

		/** XXX: magic numbers! */
		/** buffer for transferring into MessageDigest */
		byte[] buff = null;
		if (length < 1 << 20)
			buff = new byte[(int) length];
		else
			buff = new byte[1 << 20];

		MessageDigest md = MessageDigest.getInstance("SHA1");

		/** read in all chunks */
		while (length > read)
		{
			int toRead = 0;
			if (length - read < buff.length)
				toRead = (int) (length - read);
			else
				toRead = buff.length;

			read += getBytes(offset + read, toRead, buff);

			md.update(buff, 0, toRead);
		}

		return new PieceHash(md.digest());
	}

	/**
	 * treats the files as a large array, and fills a byte array starting at the
	 * passed index (does NO SHA1 validation or anything)
	 * 
	 * @param offset
	 *            index to start reading
	 * @param length
	 *            the number of bytes to try to read
	 * @param toFill
	 *            the byte array to attempt to fill
	 * @return the number of bytes read
	 * @throws IOException
	 */
	private int getBytes(long offset, int length, byte[] toFill) throws IOException
	{
		if (length > toFill.length || length < 1 || offset + length > totalLength)
			throw new IndexOutOfBoundsException();

		/** file offset is contained in */
		int firstFile = getIndex(offset);

		/** number of files to read from */
		int filesInChunk = getIndex(offset + length - 1) - firstFile + 1;

		/** byte array index */
		int read = 0;

		/** read in all files except the last one */
		while (filesInChunk > 1)
		{
			read += slurpFile(toFill, read, offset + read, firstFile, -1);

			++firstFile;
			--filesInChunk;
		}

		/** read the last file */
		while (read < length)
		{
			read += slurpFile(toFill, read, offset + read, firstFile, length - read);
		}

		return read;
	}

	/**
	 * treats the files as a large array, and writes from a byte array starting
	 * at the passed index
	 * 
	 * @param offset
	 *            offset at which to begin writing
	 * 
	 * @param data
	 *            the byte array to read from
	 * 
	 * @param length
	 *            number of bytes to read from data
	 * @return the number of bytes written
	 * @throws IOException
	 */
	public int writeBytes(long offset, byte[] data, int length) throws IOException
	{
		if (length > data.length)
			throw new IndexOutOfBoundsException();

		/** file offset is contained in */
		int firstFile = getIndex(offset);

		/** number of files to read from */
		int filesInChunk = getNumFiles(offset, length);

		/** invalid offset or length */
		if (firstFile < 0 || filesInChunk < 0)
			throw new ArrayIndexOutOfBoundsException();

		/** count of written bytes */
		int written = 0;

		/** write to all files except the last one */
		while (filesInChunk > 1)
		{
			written += write(firstFile, (offset + written) - offsets[firstFile], data, (int) (offsets[firstFile + 1] - (offset + written)));

			++firstFile;
			--filesInChunk;
		}

		/** write the last file */
		written += write(firstFile, (offset + written) - offsets[firstFile], data, length - written);

		if (Util.DEBUG)
		{
			System.err.println("writeBytes() wrote: " + written + " bytes,  " + length + " should have been.");
		}
		return written;
	}

	/**
	 * Write bytes to a single file.
	 * 
	 * @param index
	 *            of file in files[]
	 * @param l
	 *            offset in file
	 * @param data
	 *            byte array to copy from
	 * @param m
	 *            number of bytes to read from data (reading starts at 0 index)
	 * @return the number of bytes written
	 */
	private int write(int fileIndex, long l, byte[] data, int m) throws IOException
	{
		files[fileIndex].position(l);

		return files[fileIndex].write(ByteBuffer.wrap(data, 0, m), l);
	}

	/**
	 * return the number of good pieces
	 * 
	 * @return number of pieces currently owned
	 */
	public int numOfCompletePieces()
	{
		return pieces.totalSet();
	}

	/**
	 * return the number of good pieces
	 * 
	 * @return number of pieces currently owned
	 */
	public int numOfTotalPieces()
	{
		return pieces.getSize();
	}

	/**
	 * @return true if the entire torrent has been downloaded and verified
	 */
	public boolean complete()
	{
		return pieces.totalSet() == pieces.getSize();
	}

	/**
	 * helper method for the constructor
	 * 
	 * initializes files[], lengths[], and totalLength
	 * 
	 */
	private void initializeFileNames(String baseDir)
	{
		lengths = new long[torrFile.getNumOfFiles()];
		fileNames = new File[torrFile.getNumOfFiles()];
		offsets = new long[torrFile.getNumOfFiles()];

		if (torrFile.isSingleFile())
		{
			fileNames[0] = new File(baseDir, torrFile.getBaseName());
			lengths[0] = torrFile.fileLength();
			offsets[0] = 0;
			totalLength = lengths[0];
		}
		else
		{
			File fullBaseDir = new File(baseDir, torrFile.getBaseName());
			for (int i = 0; i < lengths.length; ++i)
			{
				fileNames[i] = new File(fullBaseDir, torrFile.filePath(i));
				offsets[i] = totalLength;
				lengths[i] = torrFile.fileLength(i);
				totalLength += lengths[i];
			}
		}
	}

	/**
	 * Check if this piece has been verified by this torrent
	 * 
	 * @param i
	 *            the index of the piece
	 * @return true if this piece is verified
	 */
	public boolean havePiece(int i)
	{
		return pieces.isSet(i);
	}

	/**
	 * This is a helper class for getBytes() dumps a file, starting at long
	 * offset, into the byte[], starting at index. (This is used for calculating
	 * SHA1 sums only.)
	 * 
	 * @param file
	 *            index of file to read
	 * @param offset
	 *            to begin reading
	 * @param read
	 *            index to start filling toFill
	 * @param toFill
	 *            byte array to fill
	 * @param length
	 *            number of bytes to read (negative to read to the end of the
	 *            file)
	 * 
	 * @return the number of bytes read
	 * @throws IOException
	 */
	private int slurpFile(byte[] buff, int buffIndex, long realOffset, int fileToSlurp, int length) throws IOException
	{
		/** find the total length on "read until the end of the file" */
		if (length < 0)
		{
			if (fileToSlurp < fileNames.length - 1)
			{
				length = (int) (offsets[fileToSlurp + 1] - realOffset);
			}
			else
			{
				length = (int) (totalLength - realOffset);
			}
		}

		/** sanity check */
		if (realOffset + length > totalLength)
			throw new IndexOutOfBoundsException();

		/** sanity check */
		if (buff.length < buffIndex + length)
			throw new IndexOutOfBoundsException();
		return files[fileToSlurp].read(ByteBuffer.wrap(buff, buffIndex, length), realOffset - offsets[fileToSlurp]);
	}

	/**
	 * return the file index the passed offset is contained in
	 * 
	 * @param offset
	 * @return integer file index the passed offset is contained in
	 */
	private int getIndex(long offset)
	{
		/** offset doesn't exist */
		if (offset < 0 || offset > (totalLength - 1))
			throw new IndexOutOfBoundsException();

		int fileIndex = 0;

		/** check last file */
		if (offset >= offsets[offsets.length - 1])
		{
			fileIndex = offsets.length - 1;
		}
		else
		{
			while (offset > offsets[fileIndex + 1])
			{
				++fileIndex;
			}
		}
		return fileIndex;
	}

	/**
	 * find the number of files covered by an offset and length
	 * 
	 * @param offset
	 * @param length
	 * 
	 * @return number of files
	 */
	private int getNumFiles(long offset, long length)
	{
		if (offset < 0 || length < 0 || offset + length > totalLength)
			throw new IndexOutOfBoundsException();

		int start = getIndex(offset);
		int end = getIndex(offset + length - 1);

		/** need to include the first file in the count */
		return (end - start) + 1;
	}

	/**
	 * get the length of a piece (constant except for last piece)
	 * 
	 * @param piece
	 *            index
	 * @return long length in bytes of the piece
	 */
	private long getPieceLength(int piece)
	{
		if (piece < 0 || piece > torrFile.getNumOfPieces())
			throw new IndexOutOfBoundsException();

		long pieceLength = 0;

		if (piece == torrFile.getNumOfPieces() - 1)
		{
			pieceLength = torrFile.lastPieceLength();
		}
		else
		{
			pieceLength = torrFile.pieceLength();
		}

		return pieceLength;
	}

	/**
	 * return a file input stream to the passed file index
	 * 
	 * @param index
	 * @return InputStream of file
	 * 
	 * @throws IncompleteFileException
	 */
	public InputStream getFile(int index) throws IncompleteFileException
	{
		if (!fileComplete(index))
			throw new IncompleteFileException();
		try
		{
			return new FileInputStream(fileNames[index]);
		}
		catch (FileNotFoundException e)
		{
			if (Util.DEBUG)
			{
				e.printStackTrace();
				System.out.println("That should never have happened!");
			}
			throw new IncompleteFileException("Couldn't even find the file!");
		}
	}

	/**
	 * return true if all parts of a file have been verified
	 * 
	 * @param index
	 * @return true if file is complete
	 */
	private boolean fileComplete(int index)
	{
		int start = indexToPiece(offsets[index]);
		int end = indexToPiece(offsets[index] + lengths[index] - 1);

		for (int i = start; i < end + 1; ++i)
		{
			if (strictChecking)
			{
				if (!pieces.isSet(i))
					return false;
			}
			else
			{
				/** check and set the piece */
				try
				{
					if (!verifyPiece(i))
						return false;
				}
				catch (NoSuchAlgorithmException e)
				{
					if (Util.DEBUG)
						e.printStackTrace();
					return false;
				}
				catch (IOException e)
				{
					if (Util.DEBUG)
						e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * return the piece that the passed offset is contained in
	 * 
	 * @param l
	 * @return piece number that the passed number is countained in
	 */
	private int indexToPiece(long l)
	{
		if (l < 0 || l >= totalLength)
			throw new IndexOutOfBoundsException();

		return (int) (l / torrFile.pieceLength());
	}

	/**
	 * read fill a Block request
	 * 
	 * @param b
	 *            BlockRequest to fill
	 * @return byte array of requested data or null on failure
	 * @throws IOException
	 */
	public byte[] getBlock(BlockRequest b) throws IOException
	{
		/** make sure we have this piece */
		if (!pieces.isSet(b.getIndex()))
			throw new IOException("Requesting incomplete data!");

		int len = b.getLength();

		byte[] data = new byte[len];

		long offset = b.getIndex() * torrFile.pieceLength() + b.getOffset();

		if (Util.DEBUG)
		{
			System.out.println("Getting bytes for block request: " + b);
			System.out.println("getBytes(" + offset + "," + len + ", (arraylength): " + data.length);
		}

		if (len != getBytes(offset, len, data))
			throw new IOException("Couldn't read all data!");

		return data;
	}

	/**
	 * delete all files (this must be called AFTER all peers are dead)
	 */
	public void delete()
	{
		/** remove all downloaded files */
		for (int i = 0; i < files.length; ++i)
		{
			try
			{
				files[i].close();
			}
			catch (IOException e)
			{
				// don't care
				if (Util.DEBUG)
					e.printStackTrace();
			}

			fileNames[i].delete();
		}

		torrFile.getDotTorrentFile().delete();
	}
}
