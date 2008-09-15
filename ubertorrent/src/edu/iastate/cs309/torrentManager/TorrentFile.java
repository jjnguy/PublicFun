package edu.iastate.cs309.torrentManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import edu.iastate.cs309.torrentManager.containers.InfoHash;
import edu.iastate.cs309.torrentManager.containers.PieceHash;
import edu.iastate.cs309.torrentparser.BEncodedObject;
import edu.iastate.cs309.torrentparser.ByteString;
import edu.iastate.cs309.torrentparser.Dictionary;
import edu.iastate.cs309.torrentparser.ParseException;
import edu.iastate.cs309.torrentparser.TorrentParser;
import edu.iastate.cs309.util.Util;

/**
 * object representing all data in a torrent file There are two different types:
 * single file and multiple file mode TorrentManager objects use these.
 * 
 * @author sralmai
 */
public class TorrentFile
{
	private Dictionary torrent;
	private Dictionary info;
	private Dictionary[] files;

	/**
	 * number of pieces the torrent is broken into. each one has its own SHA1
	 * hash
	 */
	private int numOfPieces = 0;

	private boolean singleFile = false;
	private boolean privatePeers = false;

	/** wrapper for 20 byte hash */
	private InfoHash infoHash = null;

	/** length of last piece (its a pain to calculate every time) */
	private long lastPieceLength = 0;
	private File dotTorrentFile;

	/**
	 * TODO [david] (priority 4): completely validate the Dictionary
	 * 
	 * @param file
	 *            the torrent file to read in
	 * 
	 * @throws FileNotFoundException
	 * @throws ParseException --
	 *             thrown on broken torrent
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public TorrentFile(String file) throws FileNotFoundException, ParseException, IOException, NoSuchAlgorithmException
	{
		infoHash = TorrentParser.getInfoHash(file);

		torrent = TorrentParser.getDictionary(file);

		dotTorrentFile = new File(file);

		/** ensure basic necessary parts are there */
		if (!(torrent.hasValue("info") && torrent.hasValue("announce")))
			throw new ParseException("torrent doesnt have basic parts!");

		info = torrent.getDictionary("info");
		if (!info.hasValue("name"))
			throw new ParseException("torrent missing \"name\" key");

		/** grab number of pieces */
		if ((info.getBytes("pieces")).length % 20 != 0)
			throw new ParseException("SHA1 hashes corrupt");
		numOfPieces = (info.getBytes("pieces")).length / 20;

		/** single file mode */
		if (info.hasValue("length"))
		{
			singleFile = true;
		}
		else
		{
			if (!info.hasValue("files"))
				throw new ParseException("missing \"files\" key");

			initFiles(info);

			/** only allow peers from explicit announce */
			if (info.hasValue("private"))
			{
				privatePeers = (1 == info.getInt("private"));
			}
		}

		setLastPieceLength();
	}

	/**
	 * calculate the length of the last piece
	 * 
	 */
	private void setLastPieceLength()
	{
		long totalLength = 0;
		if (isSingleFile())
		{
			totalLength = fileLength();
		}
		else
		{
			int top = getNumOfFiles();
			for (int i = 0; i < top; ++i)
			{
				totalLength += fileLength(i);
			}
		}

		lastPieceLength = totalLength - ((getNumOfPieces() - 1) * pieceLength());
	}

	/**
	 * (constructor helper) initialize and validate files Dictionary list
	 * 
	 * @param info
	 *            dictionary to read file information from
	 */
	private void initFiles(Dictionary info) throws ParseException
	{
		/** get number of files */
		BEncodedObject[] fileObjs = info.getList("files");

		int numOfFiles = fileObjs.length;

		files = new Dictionary[numOfFiles];

		for (int i = 0; i < numOfFiles; ++i)
		{
			if (!(fileObjs[i] instanceof Dictionary))
				throw new ParseException("bad files Dictionary");

			files[i] = (Dictionary) fileObjs[i];
		}
	}

	/**
	 * 
	 * @return the address of the tracker for this torrent
	 */
	public String getAnnounce()
	{
		try
		{
			return torrent.getString("announce");
		}
		catch (ParseException e)
		{
			System.err.println("Uncaught announce error in torrent file");
			throw new RuntimeException();
		}
	}

	/**
	 * @return the length in bytes of each piece (except for perhaps the last
	 *         piece)
	 */
	public long pieceLength()
	{
		try
		{
			return info.getInt("piece length");
		}
		catch (ParseException e)
		{
			System.err.println("Uncaught piece length error in torrent file");
			throw new RuntimeException();
		}
	}

	/**
	 * @return boolean whether Peers can be obtained from sources other than
	 *         named tracker
	 */
	public boolean onlyPrivatePeers()
	{
		return privatePeers;
	}

	/**
	 * @return number of files in the torrent
	 */
	public int getNumOfFiles()
	{
		if (singleFile)
			return 1;
		return files.length;
	}

	/**
	 * 
	 * @return whether or not torrent describes a single file
	 */
	public boolean isSingleFile()
	{
		return singleFile;
	}

	/**
	 * 
	 * @return the "base name" as specified in the BitTorrent spec (this is the
	 *         filename in single file downloads)
	 */
	public String getBaseName()
	{
		try
		{
			return info.getString("name");

		}
		catch (ParseException e)
		{
			System.err.println("Uncaught name error in torrent file");
			throw new RuntimeException();
		}
	}

	/**
	 * @return file length of the file (in single file mode)
	 */
	public long fileLength()
	{
		if (!isSingleFile())
		{
			System.err.println("Not a single-file download");
			throw new RuntimeException();
		}
		try
		{
			return info.getInt("length");
		}
		catch (ParseException e)
		{
			System.err.println("Uncaught error in torrent file");
			throw new RuntimeException();
		}
	}

	/**
	 * @param i
	 *            index of file whose length is requested
	 * @return the length of the file at index i
	 */
	public long fileLength(int i)
	{
		if (isSingleFile())
		{
			System.err.println("This is a single-file download");
			throw new RuntimeException();
		}
		if (i > files.length - 1)
		{
			System.err.println("Out of bounds file index!");
			throw new RuntimeException();
		}

		try
		{
			return files[i].getInt("length");
		}
		catch (ParseException e)
		{
			System.err.println("Uncaught error in torrent file");
			throw new RuntimeException();
		}
	}

	/** only for multi-file downloads */

	/**
	 * this is returns the filepath (on top of the "name" directory)
	 * 
	 * @return the filepath for the file specified at index
	 */
	public String filePath(int index)
	{
		if (isSingleFile())
		{
			if (Util.DEBUG)
			{
				System.err.println("This is a single-file download... filePath(int) should NOT be called");
			}
			throw new RuntimeException();
		}
		if (index > files.length - 1)
			throw new ArrayIndexOutOfBoundsException("not that many files");

		try
		{
			BEncodedObject[] pathObj = files[index].getList("path");

			StringBuffer path = new StringBuffer();

			for (int i = 0; i < pathObj.length - 1; ++i)
			{
				if (!(pathObj[i] instanceof ByteString))
					throw new ParseException();

				path.append(((ByteString) pathObj[i]).toString());
				path.append(File.separator);
			}

			if (!(pathObj[pathObj.length - 1] instanceof ByteString))
				throw new ParseException();
			path.append(((ByteString) pathObj[pathObj.length - 1]).toString());

			return path.toString();

		}
		catch (ParseException e)
		{
			System.err.println("Uncaught error in torrent file");
			throw new RuntimeException();
		}
	}

	/**
	 * 
	 * @return the number of pieces in the torrent
	 */
	public int getNumOfPieces()
	{
		return numOfPieces;
	}

	/**
	 * @param index
	 * @return SHA1 hash of piece with index
	 */
	public PieceHash getHash(int index)
	{
		if (index >= numOfPieces)
			throw new ArrayIndexOutOfBoundsException("Not that many pieces");

		byte[] hash = new byte[20];

		PieceHash pHash = null;

		try
		{
			byte[] list = info.getBytes("pieces");
			System.arraycopy(list, index * 20, hash, 0, 20);
			pHash = new PieceHash(hash);
		}
		catch (ParseException e)
		{
			System.err.println("Uncaught error in torrent file");
			throw new RuntimeException();
		}

		return pHash;
	}

	/**
	 * 
	 * @return copy of infoHash
	 */
	public InfoHash getInfoHash()
	{
		return new InfoHash(infoHash.getBytes());
	}

	/**
	 * 
	 * @return the length of the last piece
	 */
	public long lastPieceLength()
	{
		return lastPieceLength;
	}

	public File getDotTorrentFile()
	{
		return dotTorrentFile;
	}

}
