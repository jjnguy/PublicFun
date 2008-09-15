package edu.iastate.cs309.clientserverprotocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import edu.iastate.cs309.clientserverprotocol.exceptions.CommunicationException;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedMessageException;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedRecvMessageException;
import edu.iastate.cs309.clientserverprotocol.exceptions.FailedSendMessageException;
import edu.iastate.cs309.clientserverprotocol.transferThreads.RecieveFileThread;
import edu.iastate.cs309.comminterfaces.Client;
import edu.iastate.cs309.communication.ServerProperties;
import edu.iastate.cs309.communication.TorrentInfo;
import edu.iastate.cs309.communication.TorrentProp;
import edu.iastate.cs309.torrentparser.ParseException;
import edu.iastate.cs309.util.Util;

/**
 * Helper thread for ServerConnection. This is the listening half of the
 * connection on the client side.
 * 
 * @author sralmai
 */
public class ServerConnectionCallbackThread implements Runnable
{
	/** callback to higher level program */
	Client client = null;

	/** input from socket connection */
	InputStream in = null;

	/** needed for file transfer authentication */
	UserToken user = null;

	/** reference to ServerConnection... used to coordinate file transfers */
	ServerConnection servConn = null;

	private boolean exiting;

	/**
	 * @param ut
	 *            user stuff for authentication
	 * @param i
	 *            pipe to read from
	 * @param c
	 *            callback to higher level program
	 * @param servConn
	 *            other half of low level communication
	 */
	ServerConnectionCallbackThread(UserToken ut, InputStream i, Client c, ServerConnection servConn)
	{
		user = ut;
		client = c;
		in = i;
		this.servConn = servConn;
	}

	/**
	 * @see java.lang.Runnable#run()
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
				case SRV_TORPROP:
					updateTorrProp(msg);
					break;
				case SRV_TORINFO:
					updateTorrInfo(msg);
					break;
				case SRV_CONFIG:
					updateServerProp(msg);
					break;
				case SRV_FILE:
					prepFileStreams(msg);
					break;
				case SRV_TORREMOVED:
					torrentRemoved(msg);
					break;
				case SRV_TORLIST:
					prepList(msg);
					break;
				case SRV_READY_FOR_TORRENT:
					sendTorrent(msg);
					break;

				default:
					throw new CommunicationException("nonsensicle mID=" + mID + " mc= " + mc.toString());
				}

				mID = NetUtils.readInt(in);
			}

			if (Util.DEBUG)
			{
				System.out.println("quiting ServerConnectionCallbackThread.. (closed connection)");
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
		catch (CommunicationException c)
		{
			System.err.println("Caught CommunicationException from " + "ServerConnectionCallbackThead: " + c.getMessage());
			if (Util.DEBUG)
			{
				c.printStackTrace();
			}
		}
		catch (FailedMessageException f)
		{
			System.err.println("Caught FailedRecvMessageException: " + f.getMessage());
			if (Util.DEBUG)
			{
				f.printStackTrace();
			}
		}

	}/* end of run() */

	/** ******************** message processing **************** */

	/**
	 * tell the server connection to spawn a thread to send a file
	 * 
	 * @throws FailedMessageException
	 */
	private void sendTorrent(byte[] msg) throws FailedMessageException
	{
		if (msg.length != 4)
			throw new FailedMessageException("sendTorrent() has bad length: " + msg.length + " (should be 2)");

		int port = NetUtils.bytesToInt(msg, 0);
		servConn.sendTorrent(port);
	}

	/**
	 * convert the byte array into the list of refID integers
	 * 
	 * @throws FailedSendMessageException
	 */
	private void prepList(byte[] msg) throws FailedSendMessageException
	{
		/** if no refIDs, send a blank list */
		if (msg == null)
		{
			client.torrentList(new ArrayList<Integer>());
			return;
		}
		if (msg.length < 4 || (msg.length % 4) != 0)
			System.err.println("got malformed TORLIST message");

		int num = msg.length / 4;

		List<Integer> list = new ArrayList<Integer>();

		for (int i = 0; i < num; ++i)
		{
			list.add(NetUtils.bytesToInt(msg, 4 * i));
		}

		client.torrentList(list);
	}

	/**
	 * format torrentRemoved message for Server interface
	 */
	private void torrentRemoved(byte[] message) throws FailedMessageException
	{
		if (message.length != 4)
			throw new FailedRecvMessageException("bad message length for torrentRemoved. Length should be 4, but was " + message.length);

		int value = NetUtils.bytesToInt(message, 0);

		client.torrentRemoved(value);

		return;
	}

	/**
	 * list of files to recieve
	 * 
	 * 
	 * @throws FailedMessageException
	 */
	private void prepFileStreams(byte[] msg) throws FailedMessageException
	{
		if (msg.length < 12 || (msg.length - 4) % 8 != 0)
			throw new FailedRecvMessageException("Ignored a malformed prefFileStreams() message");

		int refID = NetUtils.bytesToInt(msg, 0);
		int num = (msg.length - 4) / 8;

		for (int i = 0; i < num; ++i)
		{
			/** offset of index */
			int index = NetUtils.bytesToInt(msg, 4 + i * 8);
			int port = NetUtils.bytesToInt(msg, 8 + i * 8);

			if (Util.DEBUG)
			{
				System.out.println("started a thread for refID: " + refID + "\toffset: " + index);
			}

			RecieveFileThread rft = new RecieveFileThread(client, user, port);
			new Thread(rft).start();
		}

		return;
	}

	private void updateServerProp(byte[] message) throws FailedMessageException
	{
		ServerProperties s = new ServerProperties();

		try
		{
			s.fromBytes(message);
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.updateServerStatus(s);

		return;
	}

	/**
	 * format torrentRemoved message for Server interface
	 */
	private void updateTorrInfo(byte[] msg) throws FailedMessageException
	{
		TorrentInfo t = new TorrentInfo();

		int refID = NetUtils.bytesToInt(msg, 0);

		byte[] rawInfo = new byte[msg.length - 4];

		System.arraycopy(msg, 4, rawInfo, 0, rawInfo.length);
		try
		{
			t.fromBytes(rawInfo);
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		client.updateTorrentInformation(refID, t);

		return;
	}

	/**
	 * format update torrent properties message for Server interface
	 */
	private void updateTorrProp(byte[] msg) throws FailedMessageException
	{
		int refID = NetUtils.bytesToInt(msg, 0);

		TorrentProp t = new TorrentProp();

		/** this is dirty */
		byte[] rawTP = new byte[msg.length - 4];
		System.arraycopy(msg, 4, rawTP, 0, rawTP.length);
		try
		{
			t.fromBytes(rawTP);
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.updateTorrentProperties(refID, t);

		return;
	}

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
} /* end of ServerConnectionCallbackThread */
