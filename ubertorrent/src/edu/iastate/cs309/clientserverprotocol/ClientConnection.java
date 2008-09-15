package edu.iastate.cs309.clientserverprotocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;

import javax.net.ssl.SSLSocket;

import edu.iastate.cs309.clientserverprotocol.exceptions.FailedMessageException;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedSendMessageException;
import edu.iastate.cs309.clientserverprotocol.transferThreads.RecieveTorrentThread;
import edu.iastate.cs309.clientserverprotocol.transferThreads.SendFileThread;
import edu.iastate.cs309.comminterfaces.Client;
import edu.iastate.cs309.comminterfaces.IServer;
import edu.iastate.cs309.communication.PasswordHash;
import edu.iastate.cs309.communication.ServerProperties;
import edu.iastate.cs309.communication.TorrentInfo;
import edu.iastate.cs309.communication.TorrentProp;
import edu.iastate.cs309.server.ServerLog;
import edu.iastate.cs309.util.Util;

/**
 * Server's view of client. The server should pass each new connected socket
 * into a new ClientConnection object
 * 
 * @author sralmai
 * 
 */
public class ClientConnection implements Client
{
	private static int idCounter = 0;
	private int ID = 0;

	/** ouput stream from the socket */
	OutputStream out = null;

	/** server callback */
	IServer server = null;

	/** for authentification */
	PasswordHash pw = null;

	/** reference to the "in" half of low level communication */
	ClientConnectionCallbackThread ccct = null;

	/**
	 * @param s
	 *            newly connected socket from (hopefully) a client
	 * @param callback
	 *            object which will be called asynchonously as messages come in
	 * @param pass
	 *            password hash to authenticate with
	 * @throws IOException
	 */
	public ClientConnection(SSLSocket s, IServer callback, PasswordHash pass) throws IOException
	{
		if (pass == null)
			throw new NullPointerException("Seibert LIED TO ME!");
		/** unique id for this client connection */
		ID = claimID();

		pw = pass;
		server = callback;

		out = s.getOutputStream();
		InputStream in = s.getInputStream();

		/** authenticate */
		byte[] clientPwHash = new byte[20];
		NetUtils.readFully(in, clientPwHash, 0, 20);

		if (!(new PasswordHash(clientPwHash).equals(pw)))
			throw new SecurityException("Incorrect password recieved");

		ccct = new ClientConnectionCallbackThread(in, server, this, ID);

		/** start up the callback thread */
		new Thread(ccct).start();
	}

	/**
	 * @return this Client connection's unique id
	 */
	public int getID()
	{
		return ID;
	}

	/**
	 * send a list refIDs down the pipe
	 * 
	 * @param allTorrents
	 *            list of torrents to sendd
	 * @throws FailedSendMessageException
	 */
	public void torrentList(List<Integer> allTorrents) throws FailedSendMessageException
	{
		int size = allTorrents.size();

		byte[] msg = new byte[size * 4 + 8];
		NetUtils.intToBytes(MessageCode.SRV_TORLIST.ordinal(), msg, 0);
		NetUtils.intToBytes(size * 4, msg, 4);

		int offset = 8;
		for (int i = 0; i < size; ++i)
		{
			NetUtils.intToBytes(allTorrents.get(i), msg, offset);
			offset += 4;
		}

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			ServerLog.log.log(Level.INFO, "Couldn't send torrentList()", e);
		}

	}

	/**
	 * tell the client that files are ready for pick up
	 * 
	 * spawns fileIndices.size() threads listening on the same number of ports
	 * 
	 * XXX: this method is too long
	 * 
	 * @param refID
	 *            torrent to request from
	 * @param fileIndices
	 *            list of file indices to transfer
	 * @param data
	 *            list of input streams
	 * @throws FailedMessageException
	 *             on failure to send message
	 */
	public void transferFiles(int refID, List<Integer> fileIndices, List<InputStream> data) throws FailedMessageException
	{
		if (fileIndices.size() != data.size() || fileIndices.size() < 1)
			throw new FailedSendMessageException("passed bad data on transferFiles");

		if (Util.TDEBUG)
			System.out.println("ClientConnect got request:\n\trefID: " + refID + "\n\tfileIndex" + fileIndices);

		int num = fileIndices.size();

		/** hopeful byte array */
		byte[] temp = new byte[12 + num * 8];

		/** set message id */
		NetUtils.intToBytes(MessageCode.SRV_FILE.ordinal(), temp, 0);

		/** set refID */
		NetUtils.intToBytes(refID, temp, 8);

		/** hopeful holder */
		Thread[] all = new Thread[num];

		int offset = 12;
		int count = 0;

		for (int i = 0; i < num; ++i)
		{
			SendFileThread ftt = new SendFileThread(refID, fileIndices.get(i), data.get(i), pw);
			int port = ftt.bind();

			/** if we got one, include this piece */
			if (port > 0)
			{
				NetUtils.intToBytes(fileIndices.get(i), temp, offset);
				NetUtils.intToBytes(port, temp, offset + 4);
				all[count] = new Thread(ftt);

				if (Util.DEBUG)
					System.out.println("Spawning SendFileThread");
				all[count].start();

				++count;
				offset += 8;
			}
		}

		/** we didn't get any threads */
		if (offset == 12)
			throw new FailedMessageException("Couldn't spawn any SendFileThreads!");

		/** set actual length */
		byte[] msg = new byte[offset];
		System.arraycopy(temp, 0, msg, 0, offset);

		/** set size */
		NetUtils.intToBytes(offset - 8, msg, 4);

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			/** if we couldn't send the message, kill of the waiting threads */
			for (int i = 0; i < all.length; ++i)
			{
				if (all[i] != null)
				{
					all[i].interrupt();
				}
			}
		}
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.Client#updateServerStatus(edu.iastate.cs309.communication.ServerProperties)
	 */
	public void updateServerStatus(ServerProperties settings) throws FailedMessageException
	{
		try
		{
			byte[] raw = settings.toBytes();

			byte[] msg = new byte[8 + raw.length];
			NetUtils.intToBytes(MessageCode.SRV_CONFIG.ordinal(), msg, 0);
			NetUtils.intToBytes(raw.length, msg, 4);

			System.arraycopy(raw, 0, msg, 8, raw.length);
			out.write(msg);
		}
		catch (Exception e)
		{
			ServerLog.log.log(Level.INFO, "Couldn't send updateServerStatus()", e);
		}
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.Client#updateTorrentInformation(int,
	 *      edu.iastate.cs309.communication.TorrentInfo)
	 */
	public void updateTorrentInformation(int refID, TorrentInfo info) throws FailedMessageException
	{
		byte[] raw;
		try
		{
			raw = info.toBytes();
			byte[] msg = new byte[12 + raw.length];
			NetUtils.intToBytes(MessageCode.SRV_TORINFO.ordinal(), msg, 0);
			NetUtils.intToBytes(raw.length + 4, msg, 4);
			NetUtils.intToBytes(refID, msg, 8);

			System.arraycopy(raw, 0, msg, 12, raw.length);
			out.write(msg);
		}
		catch (UnsupportedEncodingException e)
		{
			if (Util.DEBUG)
			{
				e.printStackTrace();
			}
		}
		catch (IOException e)
		{
			if (Util.DEBUG)
			{
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			ServerLog.log.log(Level.INFO, "Couldn't send updateTorrentInformation()", e);
		}
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.Client#updateTorrentProperties(int,
	 *      edu.iastate.cs309.communication.TorrentProp)
	 */
	public void updateTorrentProperties(int refID, TorrentProp info) throws FailedMessageException
	{
		try
		{
			byte[] raw = info.toBytes();

			byte[] msg = new byte[12 + raw.length];
			NetUtils.intToBytes(MessageCode.SRV_TORPROP.ordinal(), msg, 0);
			NetUtils.intToBytes(raw.length + 4, msg, 4);
			NetUtils.intToBytes(refID, msg, 8);

			System.arraycopy(raw, 0, msg, 12, raw.length);
			out.write(msg);
		}
		catch (Exception e)
		{
			ServerLog.log.log(Level.INFO, "Couldn't send updatePropInformation()", e);
		}
	}

	/**
	 * @see edu.iastate.cs309.comminterfaces.Client#torrentRemoved(int)
	 */
	public void torrentRemoved(int refID) throws FailedMessageException
	{
		byte[] msg = new byte[12];

		NetUtils.intToBytes(MessageCode.SRV_TORREMOVED.ordinal(), msg, 0);
		NetUtils.intToBytes(4, msg, 4);
		NetUtils.intToBytes(refID, msg, 8);

		try
		{
			out.write(msg);
		}
		catch (Exception e)
		{
			ServerLog.log.log(Level.INFO, "Couldn't send torrentRemoved()", e);
		}
	}

	/**
	 * 
	 * @return a unique ID for this client connection
	 */
	private synchronized int claimID()
	{
		int retVal = idCounter;
		++idCounter;
		return retVal;
	}

	/**
	 * spawn a thread to read in a torrent file
	 * 
	 * (called by ClientConnectionCallbackThread)
	 */
	protected void getTorrent()
	{
		byte[] msg = new byte[12];

		NetUtils.intToBytes(MessageCode.SRV_READY_FOR_TORRENT.ordinal(), msg, 0);
		NetUtils.intToBytes(4, msg, 4);

		/** create a thread to listen */
		RecieveTorrentThread tfct = new RecieveTorrentThread(server, pw);

		int port = 0;
		if ((port = tfct.bind()) < 1)
		{
			if (Util.DEBUG)
			{
				System.err.println("getTorrent() couldn't grab a port!");
			}
			return;
		}

		NetUtils.intToBytes(port, msg, 8);

		new Thread(tfct).start();

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			ServerLog.log.log(Level.INFO, "Couldn't send getTorrents()", e);
		}
	}

	/**
	 * Close the connection to the client and end the listening thread.
	 */
	public void disconnect()
	{
		ccct.disconnect();
	}
}
