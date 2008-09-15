package edu.iastate.cs309.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.iastate.cs309.guiElements.guiUTil.PieceSize;
import edu.iastate.cs309.torrentparser.BInteger;
import edu.iastate.cs309.torrentparser.BList;
import edu.iastate.cs309.torrentparser.ByteString;
import edu.iastate.cs309.torrentparser.Dictionary;

/**
 * Creates .torrent files that are up to specification
 * 
 * @author Justin
 */
public class TorrentFileCreator
{
	private String[] trackerUrls;
	private int numBytesPerPiece;
	private File theFile;
	private Dictionary dic;
	private String comment;

	/**
	 * Creates a TorrentFileCreator object with the given properties
	 * 
	 * @param trackerUrlsP
	 *            an array of the tracker urls to use. The order does matter as
	 *            far as priority goes. (low index first)
	 * @param numBytesPerPeiceP
	 *            represents the number of bytes per piece
	 * @param theFileP
	 *            the file or directory that should be turned into a torrent
	 * @param commentP
	 *            a user specified comment to be placed in the torrent file
	 */
	public TorrentFileCreator(String[] trackerUrlsP, int numBytesPerPeiceP, File theFileP, String commentP)
	{
		trackerUrls = trackerUrlsP;
		numBytesPerPiece = numBytesPerPeiceP;
		theFile = theFileP;
		comment = commentP;
	}

	/**
	 * Writes the .torrent file using all of the information stored in this
	 * class
	 * 
	 * @param torrentFileToWriteTo
	 *            the file where the new torrent should be saved
	 * @throws IOException
	 *             due to file writing, duh
	 */
	public void writeFile(File torrentFileToWriteTo) throws IOException
	{
		FileOutputStream fOut = new FileOutputStream(torrentFileToWriteTo);
		buildMainDictionary();
		dic.bEncode(fOut);
	}

	private Dictionary buildInfoDictionary() throws IOException
	{
		Dictionary infoDic = new Dictionary();
		infoDic.put(new ByteString("piece length"), new BInteger(numBytesPerPiece));
		infoDic.put(new ByteString("name"), new ByteString(theFile.getName()));
		infoDic.put(new ByteString("private"), new BInteger(0));
		if (theFile.isDirectory())
		{
			// do multi file one
			infoDic.put(new ByteString("files"), buildListOfFiles(theFile));
		}
		else
		{
			// do single one
			infoDic.put(new ByteString("length"), new BInteger(theFile.length()));
		}
		infoDic.put(new ByteString("pieces"), buildPieceHash());
		return infoDic;
	}

	private Dictionary buildFileDictionary(BList file, long length)
	{
		Dictionary ret = new Dictionary();
		ret.put(new ByteString("path"), file);
		ret.put(new ByteString("length"), new BInteger(length));
		return ret;
	}

	private BList buildListOfFiles(File par)
	{
		BList root = new BList();

		BList ret = new BList();

		buildListOfFiles(root, par, ret);

		return ret;
	}

	private void buildListOfFiles(BList parent, File par, BList toAddTo)
	{
		File[] files = par.listFiles();
		if (files == null)
		{
			toAddTo.add(buildFileDictionary(parent, par.length()));
			return;
		}
		for (File file : files)
		{
			BList newFile = new BList(parent);
			newFile.add(new ByteString(file.getName()));
			if (!file.isDirectory())
			{
				toAddTo.add(buildFileDictionary(newFile, file.length()));
			}
			else
			{
				buildListOfFiles(newFile, file, toAddTo);
			}
		}
	}

	private ByteString buildPieceHash() throws IOException
	{
		long totalLength = Util.getTotalLength(theFile);
		int numPieces = (int) Math.ceil(totalLength / (double) numBytesPerPiece);

		byte[][] hashConcat;
		hashConcat = new byte[numPieces][20];

		List<File> theFiles = TorrentFileCreator.getListOfFiles(theFile);
		Iterator<File> fIter = theFiles.iterator();
		InputStream fRead = new FileInputStream(fIter.next());
		byte[] bTmp = null;

		for (int i = 0; i < numPieces; i++)
		{ //deal with last case

			if (i == (numPieces - 1))
				bTmp = new byte[(int) (totalLength - numBytesPerPiece * (numPieces - 1))];
			//deal with normal cases
			else
				bTmp = new byte[numBytesPerPiece];

			fRead = fillArrayWithDataFromFiles(bTmp, fRead, fIter);

			hashConcat[i] = Util.getHashedBytes(bTmp);
		}

		return getByteStringFromArrayOfHashedValues(hashConcat);
	}

	private static InputStream fillArrayWithDataFromFiles(byte[] arrToFill, InputStream currentInput, Iterator<File> theRestOfTheFiles) throws IOException
	{
		int curPosInArray = 0;
		while (curPosInArray < arrToFill.length)
		{
			// if the input stream has enough bytes to fill another piece
			if (currentInput.available() >= arrToFill.length - curPosInArray)
			{
				currentInput.read(arrToFill, curPosInArray, arrToFill.length - curPosInArray);
				curPosInArray += arrToFill.length - curPosInArray;
				continue;
			}

			int ammntLeftInCurFile = currentInput.available();
			currentInput.read(arrToFill, curPosInArray, ammntLeftInCurFile);
			curPosInArray += ammntLeftInCurFile;
			currentInput.close();

			currentInput = new FileInputStream(theRestOfTheFiles.next());
		}
		return currentInput;
	}

	private ByteString getByteStringFromArrayOfHashedValues(byte[][] thePieces)
	{
		byte[] finalByteArr = new byte[thePieces.length * 20];
		int posToAdd = 0;
		for (int i = 0; i < thePieces.length; i++)
		{
			for (int j = 0; j < 20; j++)
			{
				finalByteArr[posToAdd] = thePieces[i][j];
				posToAdd++;
			}
		}

		return new ByteString(finalByteArr);
	}

	/**
	 * @param theFile
	 * @return a List of all of the files in a directory
	 */
	public static List<File> getListOfFiles(File theFile)
	{
		ArrayList<File> ret = new ArrayList<File>();
		if (!theFile.isDirectory())
		{
			ret.add(theFile);
			return ret;
		}
		File[] files = theFile.listFiles();
		for (File file : files)
		{
			if (!file.isDirectory())
				ret.add(file);
			else
				ret.addAll(getListOfFiles(file));
		}
		return ret;
	}

	private void buildMainDictionary() throws IOException
	{
		dic = new Dictionary();
		// put the announce part
		dic.put(new ByteString("announce"), new ByteString(trackerUrls[0]));
		BList trackerList1 = new BList();
		BList trackerList = new BList();
		for (String tracker : trackerUrls)
		{
			trackerList1.add(new ByteString(tracker));
			trackerList.add(trackerList1);
		}
		int milisPerSecond = 1000;
		dic.put(new ByteString("announce-list"), trackerList);
		dic.put(new ByteString("creation date"), new BInteger(System.currentTimeMillis() / milisPerSecond));
		if (comment != null)
			dic.put(new ByteString("comment"), new ByteString(comment));
		dic.put(new ByteString("created by"), new ByteString(Util.getUber() + " v" + Util.getVersionInfo()));

		Dictionary infoDic = buildInfoDictionary();
		dic.put(new ByteString("info"), infoDic);
	}

	/**
	 * TODO: Move to test code.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		String justinsPath = "L:/Documents and Settings/Justin/Desktop/risks";
		TorrentFileCreator f = new TorrentFileCreator(new String[] { "www.hi.com", "www.bye.com" }, PieceSize.autoCalcPieceSize(Util.getTotalLength(new File(justinsPath))).getNumBytes(), new File(justinsPath), "enjoy this comment please");
		f.writeFile(new File("Happy.torrent"));
	}
}
