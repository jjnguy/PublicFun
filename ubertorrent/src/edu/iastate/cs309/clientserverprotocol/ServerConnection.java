package edu.iastate.cs309.clientserverprotocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import edu.iastate.cs309.clientserverprotocol.exceptions.FailedMessageException;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedSendMessageException;
import edu.iastate.cs309.clientserverprotocol.exceptions.ServerClientIOException;
import edu.iastate.cs309.clientserverprotocol.transferThreads.SendTorrentThread;
import edu.iastate.cs309.comminterfaces.Client;
import edu.iastate.cs309.comminterfaces.IServer;
import edu.iastate.cs309.communication.PasswordHash;
import edu.iastate.cs309.communication.ServerProperties;
import edu.iastate.cs309.communication.TorrentProp;
import edu.iastate.cs309.util.Util;

/**
 * Client communicates with server through ServerConnection. An object which
 * implements the Client interface must be passed. It spawns a thread to listen
 * on the connection which calls methods of the Client interface on the passed
 * clientCallback object. (This is the reference implementation. All network
 * protocol definitions are overridden by whatever this class does.)
 */
public class ServerConnection implements IServer
{

	private InputStream in;
	private OutputStream out;

	private UserToken userToken;

	private List<TorrentFile> queuedTorrents = new ArrayList<TorrentFile>();
	private ServerConnectionCallbackThread callback;

	/**
	 * initialize connection (connect to server and authenticate)
	 * 
	 * @param host
	 *            name of host with which to connect
	 * @param port
	 *            port of host
	 * @param p
	 *            login password hash
	 * @param client
	 *            callback
	 * @throws UnknownHostException
	 *             on DNS resolution error
	 * @throws ServerClientIOException
	 *             if basic connection failed
	 */
	public ServerConnection(String host, int port, PasswordHash p, Client client) throws UnknownHostException, ServerClientIOException
	{
		/** encapsulate basic info */
		userToken = new UserToken(p, host, (short) port);

		try
		{
			SocketFactory socketFactory = SSLSocketFactory.getDefault();
			SSLSocket s = (SSLSocket) socketFactory.createSocket(host, port);
			String[] ciphers = { "SSL_DH_anon_WITH_RC4_128_MD5" };
			s.setEnabledCipherSuites(ciphers);
			in = s.getInputStream();
			out = s.getOutputStream();
		}
		catch (IOException e)
		{
			throw new ServerClientIOException("Failed to connect to " + host + " on " + port);
		}

		/** authenticate */
		try
		{
			/** send hash */
			out.write(userToken.getPwHash().getBytes());
		}
		catch (IOException e)
		{
			throw new ServerClientIOException("Connection broken " + "during authentication." + e.getMessage());
		}

		callback = new ServerConnectionCallbackThread(userToken, in, client, this);
		new Thread(callback).start();
	}

	/**
	 * add a torrent to the server
	 * 
	 * send a request, recieve a port to listen on
	 * 
	 * @param torrentFile
	 * @param name
	 *            of torrent file
	 * @throws FailedSendMessageException
	 */
	public void addTorrent(String name, InputStream torrentFile) throws FailedSendMessageException
	{
		if (name == null || torrentFile == null)
			throw new FailedSendMessageException("addTorrent must have non-null arguments");

		byte[] msg = new byte[8];
		NetUtils.intToBytes(MessageCode.MSG_ADDTOR.ordinal(), msg, 0);
		NetUtils.intToBytes(0, msg, 4);

		queuedTorrents.add(new TorrentFile(name, torrentFile));

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			throw new FailedSendMessageException("couldn't send addTorrent(): " + e.getMessage());
		}
	}

	/**
	 * request pieces
	 * 
	 * @param clientID
	 *            (bogus integer)
	 * @param refID
	 *            torrent to request files from
	 * @param fileIndex
	 *            list of file indices to request
	 * @throws FailedSendMessageException
	 * 
	 */
	public void transferTorrent(int clientID, int refID, List<Integer> fileIndex) throws FailedSendMessageException
	{
		int size = fileIndex.size();
		byte[] msg = new byte[12 + 4 * size];

		NetUtils.intToBytes(MessageCode.MSG_GETTOR.ordinal(), msg, 0);
		NetUtils.intToBytes(4 + size * 4, msg, 4);
		NetUtils.intToBytes(refID, msg, 8);

		int offset = 12;

		for (int i = 0; i < size; ++i)
		{
			NetUtils.intToBytes(i, msg, offset);
			offset += 4;
		}

		try
		{
			out.write(msg);
			if (Util.DEBUG)
				System.out.println("Server Connection says: I just wrote a msg array of size " + msg.length);

			if (Util.TDEBUG)
				System.out.println("ServerConnection requested: \n\tlclientID: " + clientID + "\n\trefID: " + refID + "\n\tfileIndex" + fileIndex.toString());
		}
		catch (IOException e)
		{
			throw new FailedSendMessageException("Network error (on transferTorrent():" + e.getMessage());
		}
		return;
	}

	/**
	 * change server settings
	 * 
	 * @param setting
	 *            new settings configuration
	 * @throws FailedSendMessageException
	 */
	public void changeServerProp(ServerProperties setting) throws FailedSendMessageException
	{
		try
		{
			byte[] unboxed = setting.toBytes();
			byte[] msg = new byte[8 + unboxed.length];

			NetUtils.intToBytes(MessageCode.MSG_SETSERVERCONFIG.ordinal(), msg, 0);
			NetUtils.intToBytes(unboxed.length, msg, 4);
			System.arraycopy(unboxed, 0, msg, 8, unboxed.length);

			out.write(msg);
		}
		catch (Exception e)
		{
			throw new FailedSendMessageException("couldn't send changerServerPropmessage" + e.getMessage());
		}
	}

	/**
	 * change a torrent's properties
	 * 
	 * @param refID
	 *            id of torrent to change
	 * @param prop
	 *            new properties to set
	 * @throws FailedSendMessageException
	 */
	public void changeTorrentProp(int refID, TorrentProp prop) throws FailedSendMessageException
	{
		try
		{
			byte[] unboxed = prop.toBytes();
			byte[] msg = new byte[12 + unboxed.length];

			NetUtils.intToBytes(MessageCode.MSG_SETTORPROP.ordinal(), msg, 0);
			NetUtils.intToBytes(unboxed.length + 4, msg, 4);
			NetUtils.intToBytes(refID, msg, 8);
			System.arraycopy(unboxed, 0, msg, 12, unboxed.length);

			out.write(msg);
		}
		catch (Exception e)
		{
			throw new FailedSendMessageException("couldn't send changerServerPropmessage" + e.getMessage());
		}
	}

	/**
	 * request a list of all Torrents
	 * 
	 * @param clientID
	 *            garbage integer used by ClientConnection's implementation
	 * @throws FailedSendMessageException
	 */
	public void getAllTorrents(int clientID) throws FailedSendMessageException
	{
		byte[] msg = new byte[8];
		NetUtils.intToBytes(MessageCode.MSG_TORLIST.ordinal(), msg, 0);
		NetUtils.intToBytes(0, msg, 4);

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			throw new FailedSendMessageException("Couldn't send getAllTorrents() message: " + e.getMessage());
		}
	}

	/**
	 * request ServerProperties
	 * 
	 * @param clientID
	 *            garbage integer
	 * @throws FailedSendMessageException
	 */
	public void getServerProp(int clientID) throws FailedSendMessageException
	{
		byte[] msg = new byte[8];
		NetUtils.intToBytes(MessageCode.MSG_GETSERVERCONFIG.ordinal(), msg, 0);
		NetUtils.intToBytes(0, msg, 4);

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			throw new FailedSendMessageException("Couldn't send getServerProp() message: " + e.getMessage());
		}
	}

	/**
	 * request TorrentInfo
	 * 
	 * @param clientID
	 *            garbage integer
	 * @param refID
	 *            of torrent to get information from
	 * @throws FailedSendMessageException
	 */
	public void getTorrentInfo(int clientID, int refID) throws FailedSendMessageException
	{
		byte[] msg = new byte[12];
		NetUtils.intToBytes(MessageCode.MSG_GETTORINFO.ordinal(), msg, 0);
		NetUtils.intToBytes(4, msg, 4);
		NetUtils.intToBytes(refID, msg, 8);

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			throw new FailedSendMessageException("Couldn't send getTorrentInfo() message: " + e.getMessage());
		}
	}

	/**
	 * request TorrentProperties
	 * 
	 * @param clientID
	 *            garbage integer
	 * @param refID
	 *            of torrent to get properties from
	 * @throws FailedSendMessageException
	 */
	public void getTorrentProp(int clientID, int refID) throws FailedSendMessageException
	{
		byte[] msg = new byte[12];
		NetUtils.intToBytes(MessageCode.MSG_GETTORPROP.ordinal(), msg, 0);
		NetUtils.intToBytes(4, msg, 4);
		NetUtils.intToBytes(refID, msg, 8);

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			throw new FailedSendMessageException("Couldn't send getTorrentProp() message: " + e.getMessage());
		}
	}

	/**
	 * Container class for torrent files (while they wait in queue)
	 * 
	 * @author sralmai
	 * 
	 */
	private class TorrentFile
	{
		/** the torrent file */
		InputStream in = null;

		/** its name */
		String name = null;

		/**
		 * 
		 * @param name
		 * @param in
		 */
		TorrentFile(String name, InputStream in)
		{
			this.name = name;
			this.in = in;
		}
	}

	/**
	 * shut down the server
	 * 
	 * @throws FailedMessageException
	 *             if the message was not sent
	 */
	public void shutdown() throws FailedMessageException
	{

		byte[] msg = new byte[8];
		NetUtils.intToBytes(MessageCode.MSG_SHUTDOWN.ordinal(), msg, 0);
		NetUtils.intToBytes(0, msg, 4);

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			throw new FailedSendMessageException("Couldn't send getTorrentProp() message: " + e.getMessage());
		}
	}

	/**
	 * send remove torrent message
	 * 
	 * @param refID
	 *            reference ID of torrent to remove
	 * @throws FailedMessageException
	 *             if it could not be sent
	 */
	public void removeTorrent(int refID) throws FailedMessageException
	{
		byte[] msg = new byte[12];
		NetUtils.intToBytes(MessageCode.MSG_REMOVETOR.ordinal(), msg, 0);
		NetUtils.intToBytes(4, msg, 4);
		NetUtils.intToBytes(refID, msg, 8);

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			throw new FailedSendMessageException("Couldn't send getTorrentProp() message: " + e.getMessage());
		}
	}

	/**
	 * start a thread to send a torrent file to the server
	 * 
	 * @param port
	 *            the server is waiting on
	 */
	protected void sendTorrent(int port)
	{
		/** do we have something to send? */
		if (queuedTorrents.size() == 0)
			return;

		TorrentFile toSend = queuedTorrents.remove(0);

		/** start the send thread */
		new Thread(new SendTorrentThread(toSend.name, toSend.in, userToken, port)).start();
	}

	public void disconnect()
	{
		callback.disconnect();
	}

}
