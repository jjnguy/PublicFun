package edu.iastate.cs309.communication;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import edu.iastate.cs309.client.ClientLog;
import edu.iastate.cs309.clientserverprotocol.NetUtils;
import edu.iastate.cs309.server.ServerLog;
import edu.iastate.cs309.torrentparser.ParseException;

/**
 * Encapsulation of torrent information to pass to the client.
 * 
 * @author sralmai
 * 
 * TODO: add the file lengths to tobytes and fromBytes
 */
public class TorrentInfo
{
	/** ident */
	private int refID;

	/** *********** torrent stats ****************** */

	/** number of pieces in the torrent */
	private int numPieces;

	private int numSeeders;
	private int numLeechers;

	private int uploadSpeed;

	private int downloadSpeed;

	/**
	 * size of the torrent (in bytes)
	 */
	private long size;

	/** start time */
	private long dateStarted;

	private long bytesUploaded;
	private long bytesDownloaded;

	/** name of torrent */
	private String name;

	/** tracker */
	private String trackerUrl = null;

	private String dotTorrentFile = null;

	private List<String> individualFileNames = new ArrayList<String>();
	private List<Long> fileSizes = new ArrayList<Long>();
	/**
	 * bitfield indicating which pieces have been downloaded (MUST be non-null)
	 */
	private byte[] progress = new byte[4];

	private byte complete = 0;

	/**
	 * @return a copy of this torrentinfo as a byte string
	 */
	public byte[] toBytes()
	{
		int numFiles = 0;
		if (!(individualFileNames == null))
			numFiles = individualFileNames.size();

		byte[][] fileNames = new byte[numFiles][];

		/** total byte length of files chunk */
		int filesLen = 0;

		for (int i = 0; i < numFiles; ++i)
		{
			fileNames[i] = NetUtils.encodeForTrans(individualFileNames.get(i));
			filesLen += fileNames[i].length;
		}

		byte[] rawName = NetUtils.encodeForTrans(name);
		byte[] rawTrac = NetUtils.encodeForTrans(trackerUrl);
		byte[] rawTorrFile = NetUtils.encodeForTrans(dotTorrentFile);

		int filesSize = 0;
		if (fileSizes != null)
			filesSize = fileSizes.size();

		int totalSize = 6 * 4;//6 ints
		totalSize += 4 * 8;//4 longs
		totalSize += rawName.length;
		totalSize += rawTrac.length;
		totalSize += rawTorrFile.length;
		totalSize += 4;//individualFileNames Length
		totalSize += filesLen;
		totalSize += filesSize * 8;
		totalSize += 4;//progress size
		totalSize += progress.length * 1;
		totalSize += 1;//byte for completion.

		byte[] msg = new byte[totalSize];

		int offset = 0;

		// 6 ints
		NetUtils.intToBytes(refID, msg, offset);
		offset += 4;
		NetUtils.intToBytes(numPieces, msg, offset);
		offset += 4;
		NetUtils.intToBytes(numSeeders, msg, offset);
		offset += 4;
		NetUtils.intToBytes(numLeechers, msg, offset);
		offset += 4;
		NetUtils.intToBytes(uploadSpeed, msg, offset);
		offset += 4;
		NetUtils.intToBytes(downloadSpeed, msg, offset);
		offset += 4;

		//4 longs
		NetUtils.longToBytes(size, msg, offset);
		offset += 8;
		NetUtils.longToBytes(dateStarted, msg, offset);
		offset += 8;
		NetUtils.longToBytes(bytesUploaded, msg, offset);
		offset += 8;
		NetUtils.longToBytes(bytesDownloaded, msg, offset);
		offset += 8;

		//Name
		System.arraycopy(rawName, 0, msg, offset, rawName.length);
		offset += rawName.length;

		//trackerURL
		System.arraycopy(rawTrac, 0, msg, offset, rawTrac.length);
		offset += rawTrac.length;

		//dotTorrentFile
		System.arraycopy(rawTorrFile, 0, msg, offset, rawTorrFile.length);
		offset += rawTorrFile.length;

		//num files
		NetUtils.intToBytes(fileSizes.size(), msg, offset);
		offset += 4;

		//The file names
		for (int i = 0; i < fileNames.length; i++)
		{
			System.arraycopy(fileNames[i], 0, msg, offset, fileNames[i].length);
			offset += fileNames[i].length;
		}

		//The file sizes
		for (int i = 0; i < fileSizes.size(); i++)
		{
			NetUtils.longToBytes(fileSizes.get(i), msg, offset);
			offset += 8;
		}

		//progress
		NetUtils.intToBytes(progress.length, msg, offset);
		offset += 4;

		System.arraycopy(progress, 0, msg, offset, progress.length);
		offset += progress.length;

		// 1 for completion
		msg[offset] = complete;
		offset += 1;

		if (offset != totalSize)
			ServerLog.log.log(Level.WARNING, "Something went terribly wrong with the network code.");
		return msg;
	}

	/**
	 * change all settings to those described by a byte array
	 * 
	 * @param msg
	 *            the msg to be sent with the info
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	public void fromBytes(byte[] msg) throws UnsupportedEncodingException, ParseException
	{
		int offset = 0;
		// ints
		refID = NetUtils.bytesToInt(msg, offset);
		offset += 4;
		numPieces = NetUtils.bytesToInt(msg, offset);
		offset += 4;
		numSeeders = NetUtils.bytesToInt(msg, offset);
		offset += 4;
		numLeechers = NetUtils.bytesToInt(msg, offset);
		offset += 4;
		uploadSpeed = NetUtils.bytesToInt(msg, offset);
		offset += 4;
		downloadSpeed = NetUtils.bytesToInt(msg, offset);
		offset += 4;

		// longs
		size = NetUtils.bytesToLong(msg, offset);
		offset += 8;
		dateStarted = NetUtils.bytesToLong(msg, offset);
		offset += 8;
		bytesUploaded = NetUtils.bytesToLong(msg, offset);
		offset += 8;
		bytesDownloaded = NetUtils.bytesToLong(msg, offset);
		offset += 8;

		// strings
		int len = NetUtils.bytesToInt(msg, offset);
		name = NetUtils.decode(msg, offset);
		offset += len + 4;

		len = NetUtils.bytesToInt(msg, offset);
		trackerUrl = NetUtils.decode(msg, offset);
		offset += len + 4;

		len = NetUtils.bytesToInt(msg, offset);
		dotTorrentFile = NetUtils.decode(msg, offset);
		offset += len + 4;

		// num files
		int numFiles = NetUtils.bytesToInt(msg, offset);
		offset += 4;

		individualFileNames = new ArrayList<String>(numFiles);

		// Load each filename
		for (int i = 0; i < numFiles; i++)
		{
			len = NetUtils.bytesToInt(msg, offset);
			individualFileNames.add(NetUtils.decode(msg, offset));
			offset += len + 4;
		}

		fileSizes = new ArrayList<Long>(numFiles);

		//The file sizes
		for (int i = 0; i < numFiles; i++)
		{
			fileSizes.add(NetUtils.bytesToLong(msg, offset));
			offset += 8;
		}

		// Get progress bitfield
		len = NetUtils.bytesToInt(msg, offset);
		offset += 4;
		progress = new byte[len];
		System.arraycopy(msg, offset, progress, 0, len);
		offset += len;

		//completion
		complete = msg[offset];
		offset += 1;

		if (offset != msg.length)
			ClientLog.log.log(Level.WARNING, "Something went terribly wrong with the network code.");

		/** whew! that was horrible */
	}

	/**
	 * helper for dumping name into the byte array
	 * 
	 * @throws ParseException
	 */
	@SuppressWarnings("unused")
	private int dumpName(byte[] rawString, byte[] msg, int offset) throws ParseException
	{
		/** write header */
		NetUtils.intToBytes(offset, msg, 0);

		return dumpString(rawString, msg, offset);
	}

	/**
	 * helper for dumping trackerUrl into the byte array
	 */
	private int dumpUrl(byte[] rawString, byte[] msg, int offset) throws ParseException
	{
		/** write header */
		NetUtils.intToBytes(offset, msg, 4);

		return dumpString(rawString, msg, offset);
	}

	/**
	 * helper for dumping dotTorrentFile
	 */
	private int dumpTorr(byte[] raw, byte[] msg, int offset) throws ParseException
	{

		/** write header */
		NetUtils.intToBytes(offset, msg, 8);

		return dumpString(raw, msg, offset);
	}

	/**
	 * generic encoded string dump helper
	 * 
	 * @throws ParseException
	 */
	private int dumpString(byte[] rawString, byte[] msg, int offset) throws ParseException
	{
		if (NetUtils.bytesToInt(rawString, 0) != rawString.length - 4)
			throw new ParseException("dumpString got bad info()");

		System.arraycopy(rawString, 0, msg, offset, rawString.length);

		return offset + rawString.length;
	}

	/**
	 * helper for writing individualFileNames
	 * 
	 * @throws ParseException
	 */
	private int dumpFileNames(byte[][] rawNames, byte[] msg, int offset) throws ParseException
	{
		if (rawNames == null)
			throw new NullPointerException();

		/** find total length */
		int len = 0;
		for (int i = 0; i < rawNames.length; ++i)
		{
			len += rawNames[i].length;
		}

		/** write offset and length */
		NetUtils.intToBytes(offset, msg, 12);
		NetUtils.intToBytes(len, msg, offset);
		offset += 4;

		/** write all files */
		for (int i = 0; i < rawNames.length; ++i)
		{
			offset = dumpString(rawNames[i], msg, offset);

		}

		return offset;
	}

	/**
	 * helper for writing fileSizes length
	 */
	private int dumpFileSizes(List<Long> list, byte[] msg, int offset)
	{
		int num = 0;

		if (list != null)
			num = list.size();

		/** write header */
		NetUtils.intToBytes(offset, msg, 16);

		NetUtils.intToBytes(num, msg, offset);
		offset += 4;

		/** write everything in */
		for (int i = 0; i < num; ++i)
		{
			NetUtils.longToBytes(list.get(i), msg, offset);
			offset += 8;
		}

		return offset;
	}

	/***************************************************************************
	 * ****************** getters and setters
	 */

	/**
	 * @return the total bytes downloaded so far
	 */
	public long getBytesDownloaded()
	{
		return bytesDownloaded;
	}

	/**
	 * @param bytesDownloaded
	 *            the number of bytes that have been downloaded so far
	 */
	public void setBytesDownloaded(long bytesDownloaded)
	{
		this.bytesDownloaded = bytesDownloaded;
	}

	/**
	 * @return the total bytes uploaded so far
	 */
	public long getBytesUploaded()
	{
		return bytesUploaded;
	}

	/**
	 * @param bytesUploaded
	 *            the total bytes uploaded so far
	 */
	public void setBytesUploaded(long bytesUploaded)
	{
		this.bytesUploaded = bytesUploaded;
	}

	/**
	 * @return The uploadSpeed.
	 */
	public int getUploadSpeed()
	{
		return uploadSpeed;
	}

	/**
	 * @param uploadSpeed
	 *            The new uploadSpeed.
	 */
	public void setUploadSpeed(int uploadSpeed)
	{
		this.uploadSpeed = uploadSpeed;
	}

	/**
	 * @return The downloadSpeed.
	 */
	public int getDownloadSpeed()
	{
		return downloadSpeed;
	}

	/**
	 * @param downloadSpeed
	 *            The new downloadSpeed.
	 */
	public void setDownloadSpeed(int downloadSpeed)
	{
		this.downloadSpeed = downloadSpeed;
	}

	/**
	 * @return the date started in miliseconds since January 1, 1970
	 */
	public long getDateStarted()
	{
		return dateStarted;
	}

	/**
	 * @param dateStarted
	 *            the date started in miliseconds since January 1, 1970
	 */
	public void setDateStarted(long dateStarted)
	{
		this.dateStarted = dateStarted;
	}

	@Deprecated
	public long getFileSize()
	{
		return getSize();
	}

	@Deprecated
	public void setFileSize(long fileSize)
	{
		setSize(fileSize);
	}

	/**
	 * @return a list of all filenames in the torrent file
	 */
	public List<String> getIndividualFileNames()
	{
		return individualFileNames;
	}

	/**
	 * @param individualFileNames
	 *            filenames of the torrent
	 */
	public void setIndividualFileNames(List<String> individualFileNames)
	{
		this.individualFileNames = individualFileNames;
	}

	/**
	 * @return a list of all of the file sizes of every file in the torrent
	 */
	public List<Long> getFilesizes()
	{
		return fileSizes;
	}

	/**
	 * @param fileSizesP
	 *            the filesizes of every file in the torrent
	 */
	public void setFilesizes(List<Long> fileSizesP)
	{
		fileSizes = fileSizesP;
	}

	/**
	 * @return the name of the torrent
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name of the torrent
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the current number of leechers of this torrent
	 */
	public int getNumLeechers()
	{
		return numLeechers;
	}

	/**
	 * @param numLeechers
	 *            current number of leechers of this torrent
	 */
	public void setNumLeechers(int numLeechers)
	{
		this.numLeechers = numLeechers;
	}

	/**
	 * @return the number of pieces in the torrent
	 */
	public int getNumPieces()
	{
		return numPieces;
	}

	/**
	 * @param numPieces
	 *            the number of pieces in the torrent
	 */
	public void setNumPieces(int numPieces)
	{
		this.numPieces = numPieces;
	}

	/**
	 * @return the number of seeders of the torrent
	 */
	public int getNumSeeders()
	{
		return numSeeders;
	}

	/**
	 * @param numSeeders
	 *            the number of seeders of the torrent
	 */
	public void setNumSeeders(int numSeeders)
	{
		this.numSeeders = numSeeders;
	}

	/**
	 * @return a byte array designating the progress of a torrent file each
	 *         element of the array represents one piece. A piece is completed
	 *         if it is a 1 and 0 otherwise.
	 */
	public byte[] getProgress()
	{
		return progress;
	}

	/**
	 * @return if a torrent is complete or not
	 */
	public boolean getComplete()
	{
		return (complete == 1);
	}

	/**
	 * @param done
	 *            whether or not the torrent is complete
	 */
	public void setComplete(boolean done)
	{
		complete = (byte) (done ? 1 : 0);
	}

	/**
	 * @param progress
	 *            a byte array designating the progress of a torrent file each
	 *            element of the array represents one piece. A piece is
	 *            completed if it is a 1 and 0 otherwise.
	 */
	public void setProgress(byte[] progress)
	{
		if (progress == null)
			throw new NullPointerException("The progress of the torrent cannot be null");
		this.progress = progress;
	}

	/**
	 * @return the refID
	 */
	public int getRefID()
	{
		return refID;
	}

	/**
	 * @param refID
	 */
	public void setRefID(int refID)
	{
		this.refID = refID;
	}

	/**
	 * @return the size of the torrent file
	 */
	public long getSize()
	{
		return size;
	}

	/**
	 * @param size
	 *            the size of the torrent file
	 */
	public void setSize(long size)
	{
		this.size = size;
	}

	/**
	 * @return the tracker URL
	 */
	public String getTrackerUrl()
	{
		return trackerUrl;
	}

	/**
	 * @param trackerUrl
	 *            the tracker URL
	 */
	public void setTrackerUrl(String trackerUrl)
	{
		this.trackerUrl = trackerUrl;
	}

	/**
	 * @return the filename of the torrent file
	 */
	public String getDotTorrentFile()
	{
		return dotTorrentFile;
	}

	/**
	 * @param dotTorrentFile
	 *            the filename of the torrent file
	 */
	public void setDotTorrentFile(String dotTorrentFile)
	{
		this.dotTorrentFile = dotTorrentFile;
	}

	/**
	 * Override
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (bytesDownloaded ^ (bytesDownloaded >>> 32));
		result = prime * result + (int) (bytesUploaded ^ (bytesUploaded >>> 32));
		result = prime * result + (int) (dateStarted ^ (dateStarted >>> 32));
		result = prime * result + ((dotTorrentFile == null) ? 0 : dotTorrentFile.hashCode());
		result = prime * result + downloadSpeed;
		result = prime * result + ((fileSizes == null) ? 0 : fileSizes.hashCode());
		result = prime * result + ((individualFileNames == null) ? 0 : individualFileNames.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + numLeechers;
		result = prime * result + numPieces;
		result = prime * result + numSeeders;
		result = prime * result + Arrays.hashCode(progress);
		result = prime * result + refID;
		result = prime * result + (int) (size ^ (size >>> 32));
		result = prime * result + ((trackerUrl == null) ? 0 : trackerUrl.hashCode());
		result = prime * result + uploadSpeed;
		return result;
	}

	/**
	 * Override
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TorrentInfo other = (TorrentInfo) obj;
		if (bytesDownloaded != other.bytesDownloaded)
			return false;
		if (bytesUploaded != other.bytesUploaded)
			return false;
		if (dateStarted != other.dateStarted)
			return false;
		if (dotTorrentFile == null)
		{
			if (other.dotTorrentFile != null)
				return false;
		}
		else if (!dotTorrentFile.equals(other.dotTorrentFile))
			return false;
		if (downloadSpeed != other.downloadSpeed)
			return false;
		if (fileSizes == null)
		{
			if (other.fileSizes != null)
				return false;
		}
		else if (!fileSizes.equals(other.fileSizes))
			return false;
		if (individualFileNames == null)
		{
			if (other.individualFileNames != null)
				return false;
		}
		else if (!individualFileNames.equals(other.individualFileNames))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (numLeechers != other.numLeechers)
			return false;
		if (numPieces != other.numPieces)
			return false;
		if (numSeeders != other.numSeeders)
			return false;
		if (!Arrays.equals(progress, other.progress))
			return false;
		if (refID != other.refID)
			return false;
		if (size != other.size)
			return false;
		if (trackerUrl == null)
		{
			if (other.trackerUrl != null)
				return false;
		}
		else if (!trackerUrl.equals(other.trackerUrl))
			return false;
		if (uploadSpeed != other.uploadSpeed)
			return false;
		return true;
	}

	/**
	 * Override
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("refID: " + refID + "\n");

		sb.append("numPieces: " + numPieces + "\n");

		sb.append("numSeeders: " + numSeeders + "\n");
		sb.append("numLeechers: " + numLeechers + "\n");

		sb.append("uploadSpeed: " + uploadSpeed + "\n");
		sb.append("downloadSpeed: " + downloadSpeed + "\n");

		sb.append("size: " + size + "\n");

		/** start time */
		sb.append("dateStarted: " + dateStarted + "\n");
		sb.append("bytesUploaded: " + bytesUploaded + "\n");
		sb.append("bytesDownloaded: " + bytesDownloaded + "\n");

		/** strings */
		sb.append("name: " + name + "\n");
		sb.append("trackerUrl: " + trackerUrl + "\n");

		sb.append("IndividiualFileNames\n");
		for (int i = 0; i < individualFileNames.size(); i++)
			sb.append("\t" + individualFileNames.get(i) + " of size: " + fileSizes.get(i) + "\n");

		sb.append("Complete: " + getComplete());
		return sb.toString();
	}
}
