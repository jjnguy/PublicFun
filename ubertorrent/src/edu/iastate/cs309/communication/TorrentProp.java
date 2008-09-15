package edu.iastate.cs309.communication;

import edu.iastate.cs309.clientserverprotocol.NetUtils;
import edu.iastate.cs309.torrentparser.ParseException;
import edu.iastate.cs309.util.Util;

/**
 * A TorrentProp object represents the mutable information about a torrent.
 * 
 * @author sralmai
 * 
 */
public class TorrentProp
{
	/**
	 * number of "freebies" to allow on this torrent
	 * 
	 * This is the number of blocks (not pieces) this Torrent will send over
	 * recieving before choking the peer
	 * 
	 * (should choke) = ( sent - recv > freebies )
	 * 
	 * average block size is 16KB (thats kibobytes Michael),
	 * 
	 * 
	 * 30 is a sane default value (by sane default value I mean arbitrary number
	 * I thought up... feel free to change if it you have a good reason)
	 */
	private int freebies = 30;

	/**
	 * block request queue size
	 * 
	 * This is the maximum number of blocks to have outstanding to on a single
	 * Peer object
	 * 
	 * In general, pick a higher number for faster (higher bandwidth, lower
	 * latency) connections
	 * 
	 * 5 is the default in many implementations
	 */
	private int queueSize = 5;

	/**
	 * is this torrent running uploading and downloading?
	 */
	private boolean active = false;

	/**
	 * Will this endlessly serve getBlock requests?
	 */
	private boolean seeder = false;

	/**
	 * number of peers this torrent should try to aquire
	 * 
	 * 30 is the recommended
	 */
	private int numPeers = 30;

	/**
	 * max number of peers to allow (above this, drop peers)
	 * 
	 * 50 is recommended max (performance drops as you go up)
	 */
	private int maxPeers = 50;

	/**
	 * refID
	 */
	private int refID = 0;

	/**
	 * Maximum download rate for this torrent
	 */
	private int downloadCap = Integer.MAX_VALUE;

	/**
	 * Maximum upload rate for this torrent
	 */
	private int uploadCap = Integer.MAX_VALUE;

	/**
	 * Encode this torrentprop as a byte array suitable for transmission over a
	 * network or storage in a file.
	 * 
	 * @return the encoded array
	 */
	public byte[] toBytes()
	{
		byte[] ret = new byte[30];
		NetUtils.intToBytes(freebies, ret, 0);
		NetUtils.intToBytes(queueSize, ret, 4);
		ret[8] = (byte) (active ? 0 : 1);
		ret[9] = (byte) (seeder ? 0 : 1);
		NetUtils.intToBytes(numPeers, ret, 10);
		NetUtils.intToBytes(maxPeers, ret, 14);
		NetUtils.intToBytes(refID, ret, 18);
		NetUtils.intToBytes(downloadCap, ret, 22);
		NetUtils.intToBytes(uploadCap, ret, 26);

		return ret;
	}

	/**
	 * Load settings from a byte array encoded with the toBytes method.
	 * 
	 * @param msg
	 *            The encoded information to be loaded
	 * @throws ParseException
	 *             If the array is corrupted.
	 */
	public void fromBytes(byte[] msg) throws ParseException
	{
		if (msg.length != 30)
			throw new ParseException("bad frombytes() message (size is " + msg.length + ", should be 22)");

		freebies = NetUtils.bytesToInt(msg, 0);
		queueSize = NetUtils.bytesToInt(msg, 4);
		active = msg[8] == 1;
		seeder = msg[9] == 1;
		numPeers = NetUtils.bytesToInt(msg, 10);
		maxPeers = NetUtils.bytesToInt(msg, 14);
		refID = NetUtils.bytesToInt(msg, 18);
		downloadCap = NetUtils.bytesToInt(msg, 22);
		uploadCap = NetUtils.bytesToInt(msg, 26);
	}

	/** ************* getters and setters ******************** */
	/**
	 * @return The number of blocks that will be sent to a peer before we choke
	 *         them
	 */
	public int getFreebies()
	{
		return freebies;
	}

	/**
	 * @return Whether Torrent is currently active (has open connections)
	 */
	public boolean isActive()
	{
		return active;
	}

	/**
	 * @param active
	 *            The new activity level of this torrent
	 */
	public void setActive(boolean active)
	{
		this.active = active;
	}

	/**
	 * @param freebies
	 *            The new number of blocks that will be sent to a peer before we
	 *            choke them
	 */
	public void setFreebies(int freebies)
	{
		this.freebies = freebies;
	}

	/**
	 * The queue size is the maximum number of pieces that we will allow to all
	 * be downloading at once. Increasing this may marginally increase
	 * performace at the expense of extra overhead.
	 * 
	 * @return the current queue size of this torrent.
	 */
	public int getQueueSize()
	{
		return queueSize;
	}

	/**
	 * The queue size is the maximum number of pieces that we will allow to all
	 * be downloading at once. Increasing this may marginally increase
	 * performace at the expense of extra overhead.
	 * 
	 * @param queueSize
	 *            The new size of the queue
	 */
	public void setQueueSize(int queueSize)
	{
		/** don't change to a bad value */
		if (queueSize < 1)
		{
			if (Util.DEBUG)
			{
				System.err.println("Tried to setQueueSize < 1! Ignoring...");
			}

			return;
		}
		this.queueSize = queueSize;
	}

	/**
	 * The maximum number of peers that we will allow to be connected at any
	 * given time. A good default is 50. Increasing this may marginally increase
	 * performance at the expense of extra overhead.
	 * 
	 * @return Maximum number of peers
	 */
	public int getMaxPeers()
	{
		return maxPeers;
	}

	/**
	 * @return Current number of peers connected on this torrent
	 */
	public int getNumPeers()
	{
		return numPeers;
	}

	/**
	 * The maximum number of peers that we will allow to be connected at any
	 * given time. A good default is 50. Increasing this may marginally increase
	 * performance at the expense of extra overhead.
	 * 
	 * @param maxPeers
	 *            The new maximum number of peers for this torrent
	 */
	public void setMaxPeers(int maxPeers)
	{
		this.maxPeers = maxPeers;
	}

	/**
	 * Set the number of peers connected to this torrent.
	 * 
	 * @param numPeers
	 *            New number of peers
	 */
	public void setNumPeers(int numPeers)
	{
		this.numPeers = numPeers;
	}

	/**
	 * The refID of a torrent is an integer that uniquely identifies the
	 * torrent. refIDs will usually be sequential, but this is not guaranteed.
	 * RefIDs will be valid for the entire session until the server is closed.
	 * RefIDs are NOT persistent across server restarts. A removed torrent will
	 * not have it's refID reused for a new torrent (this is why refIDs are not
	 * guaranteed sequential).
	 * 
	 * @return the refID of this torrent.
	 */
	public int getRefID()
	{
		return refID;
	}

	/**
	 * @return Whether this torrent is seeding (we are seeding if we are active
	 *         and have all pieces downloaded).
	 */
	public boolean isSeeder()
	{
		return seeder;
	}

	/**
	 * Set the refID. It is expected that this will be set ONCE, at creation,
	 * and not changed after that.
	 * 
	 * @param refID
	 *            The refID of this torrent
	 */
	public void setRefID(int refID)
	{
		this.refID = refID;
	}

	/**
	 * Set whether this torrent is a seeder.
	 * 
	 * @param seeder
	 *            Whether this torrent is a seeder
	 */
	public void setSeeder(boolean seeder)
	{
		this.seeder = seeder;
	}

	/**
	 * @return The maximum download speed of this torrent
	 */
	public int getDownloadCap()
	{
		return downloadCap;
	}

	/**
	 * @param downloadCap
	 *            The new maximum download speed of this torrent
	 */
	public void setDownloadCap(int downloadCap)
	{
		this.downloadCap = downloadCap;
	}

	/**
	 * @return The maximum upload speed of this torrent
	 */
	public int getUploadCap()
	{
		return uploadCap;
	}

	/**
	 * @param uploadCap
	 *            The new maximum upload speed of this torrent
	 */
	public void setUploadCap(int uploadCap)
	{
		this.uploadCap = uploadCap;
	}
}
