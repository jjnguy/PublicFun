package edu.iastate.cs309.clientserverprotocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import edu.iastate.cs309.clientserverprotocol.exceptions.CommunicationException;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedMessageException;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedRecvMessageException;
import edu.iastate.cs309.comminterfaces.IServer;
import edu.iastate.cs309.communication.ServerProperties;
import edu.iastate.cs309.communication.TorrentProp;
import edu.iastate.cs309.torrentparser.ParseException;
import edu.iastate.cs309.util.Util;

/**
 * Listener thread on server (created by clientconnection)
 * 
 * @author sralmai
 * 
 */
public class ClientConnectionCallbackThread implements Runnable
{
	private IServer callback = null;
	private InputStream in = null;

	/** needed for sending files from client to server */
	private ClientConnection cCon = null;

	/** unique client id */
	private int ID = 0;
	private boolean exiting;

	/**
	 * 
	 * @param in
	 * @param callback
	 * @param cCon
	 *            the ClientConnection which spawned this thread
	 * @param ID
	 */
	protected ClientConnectionCallbackThread(InputStream in, IServer callback, ClientConnection cCon, int ID)
	{
		this.callback = callback;
		this.in = in;
		this.cCon = cCon;
		this.ID = ID;
	}

	/**
	 * read in from connection to client and call appropriate messages
	 */
	public void run()
	{
		try
		{
			int len = 0;
			int mID = NetUtils.readInt(in);
			byte[] msg = null;
			MessageCode mc;

			while (mID > -1)
			{
				len = NetUtils.readInt(in);

				if (len > 0)
				{
					msg = new byte[len];
					NetUtils.readFully(in, msg, 0, len);
				}
				else
				{
					msg = null;
				}

				/* ensure valid code */
				if (mID > MessageCode.values().length - 1)
				{
					/* this will be thrown later */
					mc = MessageCode.BAD_MID;
				}
				else
				{
					mc = MessageCode.values()[mID];
				}

				/*
				 * this is not nearly as pretty as a function array, but it uses
				 * less memory
				 */
				switch (mc)
				{
				case MSG_ADDTOR:
					addTorrent();
					break;
				case MSG_TORLIST:
					callback.getAllTorrents(ID);
					break;
				case MSG_GETTORINFO:
					requestTorrentInfo(msg);
					break;
				case MSG_GETTORPROP:
					requestTorrentProp(msg);
					break;
				case MSG_SETTORPROP:
					setTorrentProp(msg);
					break;
				case MSG_GETSERVERCONFIG:
					callback.getServerProp(ID);
					break;
				case MSG_SETSERVERCONFIG:
					setServerConfig(msg);
					break;
				case MSG_REMOVETOR:
					removeTorrent(msg);
					break;
				case MSG_GETTOR:
					getTorrent(msg);
					break;
				case MSG_SHUTDOWN:
					callback.shutdown();
					break;

				default:
					throw new CommunicationException("nonsensicle mID=" + mID + " mc= " + mc.toString());
				}

				/** get next message id */
				mID = NetUtils.readInt(in);
			}

			if (Util.DEBUG)
			{
				System.out.println("quiting ClientConnectionCallbackThread.. (closed connection)");
			}

		}
		catch (IOException e)
		{
			if (exiting)
				return;

			System.err.println("Reading failed on socket connection. Bailing out!");
			if (Util.DEBUG)
			{
				e.printStackTrace();
			}
			return;
		}
		catch (CommunicationException e)
		{
			System.err.println("Caught CommunicationException from " + "ServerConnectionCallbackThead: " + e.getMessage());
			if (Util.DEBUG)
			{
				e.printStackTrace();
			}
		}
		catch (FailedMessageException e)
		{
			System.err.println("Caught FailedRecvMessageException: " + e.getMessage());
			if (Util.DEBUG)
			{
				e.printStackTrace();
			}
		}
	}

	private void getTorrent(byte[] msg) throws FailedMessageException
	{
		if (msg == null)
			throw new FailedRecvMessageException("got empty payload on getTorrent");

		if (msg.length < 4 || msg.length % 4 != 0)
			throw new FailedRecvMessageException("getTorrent() message has wrong length");

		int refID = NetUtils.bytesToInt(msg, 0);

		int len = (msg.length - 4) / 4;
		int offset = 4;

		List<Integer> indices = new ArrayList<Integer>();

		for (int i = 0; i < len; ++i)
		{
			indices.add(NetUtils.bytesToInt(msg, offset + 4 * i));
		}

		if (Util.TDEBUG)
			System.out.println("ClientConnectionCallback got request:\n\trefID: " + refID + "\n\tindices: " + indices);
		callback.transferTorrent(ID, refID, indices);
	}

	private void removeTorrent(byte[] msg) throws FailedMessageException
	{
		if (msg.length != 4)
			throw new FailedRecvMessageException("bad removeTorrent() length!");

		int refID = NetUtils.bytesToInt(msg, 0);

		callback.removeTorrent(refID);
	}

	/**
	 * grab the ServerProperties from the byte array
	 * 
	 * @param msg
	 * @throws FailedMessageException
	 */
	private void setServerConfig(byte[] msg) throws FailedMessageException
	{
		ServerProperties s = new ServerProperties();

		try
		{
			s.fromBytes(msg);
		}
		catch (UnsupportedEncodingException e)
		{
			if (Util.DEBUG)
			{
				e.printStackTrace();
			}
			return;
		}
		catch (ParseException e)
		{
			if (Util.DEBUG)
			{
				e.printStackTrace();
			}
			return;
		}

		callback.changeServerProp(s);
	}

	private void setTorrentProp(byte[] msg) throws FailedMessageException
	{
		/** first four bytes is refID */

		int refID = NetUtils.bytesToInt(msg, 0);

		/** this is wasteful */
		byte[] rawTP = new byte[msg.length - 4];
		System.arraycopy(msg, 4, rawTP, 0, rawTP.length);

		TorrentProp t = new TorrentProp();

		try
		{
			t.fromBytes(rawTP);
		}
		catch (ParseException e)
		{
			if (Util.DEBUG)
			{
				e.printStackTrace();
			}
			return;
		}
		callback.changeTorrentProp(refID, t);
	}

	private void requestTorrentProp(byte[] msg) throws FailedMessageException
	{
		if (msg.length != 4)
			throw new FailedRecvMessageException("bad reqTorrentProp length!");

		callback.getTorrentProp(ID, NetUtils.bytesToInt(msg, 0));

	}

	private void requestTorrentInfo(byte[] msg) throws FailedMessageException
	{
		if (msg.length != 4)
			throw new FailedRecvMessageException("bad reqTorrentInfo length!");

		callback.getTorrentInfo(ID, NetUtils.bytesToInt(msg, 0));

	}

	private void addTorrent() throws FailedMessageException
	{
		/**
		 * tell the ClientConnection to start up a listen thread
		 */
		cCon.getTorrent();
	}

	/**
	 * 
	 */
	public void disconnect()
	{
		exiting = true;
		try
		{
			if (in != null)
				in.close();
		}
		catch (IOException e)
		{
		}
	}
}
