package edu.iastate.cs309.communication;

import java.io.UnsupportedEncodingException;

import edu.iastate.cs309.clientserverprotocol.NetUtils;
import edu.iastate.cs309.torrentManager.containers.PeerID;
import edu.iastate.cs309.torrentparser.ParseException;

/**
 * encapsulates all data needed by a Torrent Manager from a server
 * 
 * @author sralmai
 * 
 */
public class ServerProperties
{
	/**
	 * 20 byte random string (encapsulated in an object) - check the bittorrent
	 * doc for formatting
	 */
	private PeerID peerID = new PeerID();

	/** port server is listening on for new client connections */
	private int port = 30908;

	/** port server is listening on for new bittorrent peer connections */
	private int peerPort = 666;

	/** root directory for this user */
	private String rootDir;

	/** timeout for Peer connection in milliseconds (standard is 2 minutes) */
	private long timeout;

	/** password that the client uses to connect to the server */
	private PasswordHash pass = new PasswordHash();

	/**
	 * Get the peer ID used for communication with peers.
	 * 
	 * @return The peer ID used for communication with peers
	 */
	public PeerID getPeerID()
	{
		return peerID;
	}

	/**
	 * Get client listening port.
	 * 
	 * @return The port that the server listens for clients on
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * Get the root directory to store torrents in.
	 * 
	 * @return Root directory to store torrents in
	 */
	public String getRootDir()
	{
		return rootDir;
	}

	/**
	 * Get the timeout used in peer communication.
	 * 
	 * @return The timeout used in peer communication
	 */
	public long getTimeout()
	{
		return timeout;
	}

	/**
	 * Get the password hash used for client authentication.
	 * 
	 * @return The password hash used for client authentication
	 */
	public PasswordHash getPass()
	{
		return pass;
	}

	/**
	 * Set the peer ID used for communication with peers.
	 * 
	 * @param peerID
	 *            The new peerID
	 * 
	 */
	public void setPeerID(PeerID peerID)
	{
		if (peerID == null)
			throw new NullPointerException();
		this.peerID = peerID;
	}

	/**
	 * Set client listening port.
	 * 
	 * @param port
	 *            The new port that the server should listen for clients on
	 */
	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * Set the root directory to store torrents in.
	 * 
	 * @param rootDir
	 *            New root directory to store torrents in
	 */
	public void setRootDir(String rootDir)
	{
		this.rootDir = rootDir;
	}

	/**
	 * Set the timeout used in peer communication.
	 * 
	 * @param timeout
	 *            The new timeout to use in peer communication
	 */
	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}

	/**
	 * Set the password hash used for client authentication.
	 * 
	 * @param pass
	 *            The new password hash to use for client authentication
	 */
	public void setPass(PasswordHash pass)
	{
		if (pass == null)
			throw new NullPointerException();
		this.pass = pass;
	}

	/**
	 * Convert this object to a byte array
	 * 
	 * @return A copy of this object of a byte array
	 * @throws UnsupportedEncodingException
	 *             If a required encoding is not supported by the java library
	 * @see #fromBytes
	 */
	public byte[] toBytes() throws UnsupportedEncodingException
	{
		byte[] string = NetUtils.encodeForTrans(rootDir);
		int strlen = string.length;

		/** box it up! */
		byte[] retVal = new byte[strlen + 54];

		System.arraycopy(peerID.getBytes(), 0, retVal, 0, 20);
		NetUtils.shortToBytes(port, retVal, 20);
		NetUtils.intToBytes(strlen, retVal, 22);
		System.arraycopy(string, 0, retVal, 26, strlen);
		NetUtils.longToBytes(timeout, retVal, 26 + strlen);
		System.arraycopy(pass.getBytes(), 0, retVal, 34 + strlen, 20);

		return retVal;
	}

	/**
	 * Load server properties from a byte array.
	 * 
	 * @param msg
	 *            Data to convert from
	 * @throws ParseException
	 *             If the data in msg is not formatted correctly
	 * @throws UnsupportedEncodingException
	 *             If a required encoding is not supported by the java library
	 * 
	 * @see #toBytes()
	 */
	public void fromBytes(byte[] msg) throws ParseException, UnsupportedEncodingException
	{
		/** sanity check */
		if (msg.length < 26 || (NetUtils.bytesToInt(msg, 22) + 54 != msg.length))
			throw new ParseException("bad length for serverprop fromBytes()");
		/** array for 20 byte ids */
		byte[] buff = new byte[20];

		System.arraycopy(msg, 0, buff, 0, 20);
		peerID = new PeerID(buff);

		port = (short) NetUtils.bytesToShort(msg, 20);
		int strlen = NetUtils.bytesToInt(msg, 22);

		rootDir = NetUtils.decode(msg, 22);

		timeout = NetUtils.bytesToLong(msg, 26 + strlen);

		System.arraycopy(msg, 34, buff, 0, 20);
		pass = new PasswordHash(buff);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pass == null) ? 0 : pass.hashCode());
		result = prime * result + ((peerID == null) ? 0 : peerID.hashCode());
		result = prime * result + peerPort;
		result = prime * result + port;
		result = prime * result + ((rootDir == null) ? 0 : rootDir.hashCode());
		result = prime * result + (int) (timeout ^ (timeout >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ServerProperties other = (ServerProperties) obj;
		if (pass == null)
		{
			if (other.pass != null)
				return false;
		}
		else if (!pass.equals(other.pass))
			return false;
		if (peerID == null)
		{
			if (other.peerID != null)
				return false;
		}
		else if (!peerID.equals(other.peerID))
			return false;
		if (peerPort != other.peerPort)
			return false;
		if (port != other.port)
			return false;
		if (rootDir == null)
		{
			if (other.rootDir != null)
				return false;
		}
		else if (!rootDir.equals(other.rootDir))
			return false;
		if (timeout != other.timeout)
			return false;
		return true;
	}

	/**
	 * Get peer listening port.
	 * 
	 * @return The port that the server listens for peers on
	 */
	public int getPeerPort()
	{
		return peerPort;
	}

	/**
	 * Set peer listening port.
	 * 
	 * @param peerPort
	 *            The new port that the server should listens for peers on
	 */
	public void setPeerPort(int peerPort)
	{
		this.peerPort = peerPort;
	}
}
