package edu.iastate.cs309.torrentManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Date;

import edu.iastate.cs309.clientserverprotocol.NetUtils;
import edu.iastate.cs309.torrentManager.trackerConnection.BEncodingContentHandlerFactory;
import edu.iastate.cs309.torrentparser.BEncodedObject;
import edu.iastate.cs309.torrentparser.BList;
import edu.iastate.cs309.torrentparser.Dictionary;
import edu.iastate.cs309.torrentparser.ParseException;
import edu.iastate.cs309.util.Util;

/**
 * connection to tracker used by TorrentManager
 * 
 * Wishlist: clean up the logical flow of parsing tracker response. It is
 * absolutely awful.
 * 
 * @author sralmai
 * 
 */
public class Tracker
{
	static
	{
		/** use BEncodingContentHandler */
		URLConnection.setContentHandlerFactory(new BEncodingContentHandlerFactory());
	}

	/** message count */
	private int mesgCount = 0;

	/** stat from tracker */
	private int numLeechers = 0;

	/** stat from tracker */
	private int numSeeders = 0;

	/** interval in seconds to wait after hello before sending updates */
	private int firstWait = 0;

	/** minimal length in seconds between requests */
	private int wait = 0;

	/** time (in seconds) of last message */
	private long lastMsg = 0;

	/** Tracker interal for bookkeeping */
	String trackerID = null;

	/**
	 * Torrent who called this Tracker
	 * 
	 * This is tightly coupled with Torrent, because Torrent, Tracker, and Peer
	 * objects all need access to data in Torrent
	 */
	Torrent tm;

	/** tracker base address */
	private String tracker;

	/** torrent is completely downloaded */
	private boolean done = false;

	/**
	 * Initialize a Tracker object.
	 * 
	 * @param t
	 *            callback to the parent Tracker object
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public Tracker(Torrent t) throws MalformedURLException, IOException
	{
		tm = t;
		tracker = tm.getAnnounce();

		/** used for deciding status messages */
		done = tm.isComplete();
	}

	/**
	 * send the tracker an update
	 * 
	 * handles waiting periods and sending the correct "event"
	 * 
	 * @param num
	 *            number of wanted peers
	 * @return array of PeerAddresses with length less than or equal to num, or
	 *         null if num==0 (some elements may be null)
	 */
	public InetSocketAddress[] update(int num)
	{
		if (mesgCount == 0)
			return sendMessage(1, num);

		/** "completed" message takes precedence over wait times */
		if (!done && tm.isComplete())
		{
			done = true;
			return sendMessage(3, num);
		}

		/** return null if not past wait time */
		if (mesgCount == 1)
		{
			if ((new Date().getTime() / 1000) - lastMsg < firstWait)
			{
				try
				{
					Thread.sleep(6000);
				}
				catch (InterruptedException e)
				{
					// don't care
				}
				return null;
			}
		}
		if ((new Date().getTime() / 1000) - lastMsg < wait)
		{
			try
			{
				Thread.sleep(6000);
			}
			catch (InterruptedException e)
			{
				// don't care
			}

			return null;
		}

		return sendMessage(0, num);
	}

	/**
	 * send the tracker a message according to the Bittorrent spec
	 * 
	 * @param i
	 *            current state of the torrent 0 -- do not send state info 1 --
	 *            "started" torrent (MUST be in first message) 2 -- "stopped"
	 *            quitting gracefully 3 -- "completed" finished downloading
	 *            torrent
	 * 
	 * @param numwant
	 *            number of peers wanted (-1 means default)
	 */
	private InetSocketAddress[] sendMessage(int i, int num)
	{
		String state;
		String numwant;
		if (num < 0)
		{
			/** if input is bad, give them nothing */
			numwant = "0";
		}
		else
		{
			numwant = Integer.toString(num);
		}

		switch (i)
		{
		case 1:
			state = "started";
			break;
		case 2:
			state = "stopped";
			break;
		case 3:
			state = "completed";
			break;
		default:
			state = null;
		}

		String name = null;
		try
		{
			/** in two steps so name can be viewed when everything dies */
			name = genCGIreq(state, numwant);

			/** mark time and count */
			++mesgCount;
			lastMsg = (new Date()).getTime() / 1000;

			return parse(new URL(name).getContent());
		}
		catch (MalformedURLException e)
		{
			// should have caught this earlier
			if (Util.DEBUG)
			{
				System.err.println(name + " did not work: " + e.getMessage());
			}
			return null;
		}
		catch (UnsupportedEncodingException e)
		{
			// never happens
			if (Util.DEBUG)
			{
				System.err.println("sendMessage() died: " + e.getMessage());
				System.err.println("Message is " + e.getMessage());
				e.printStackTrace();
			}
			return null;
		}
		catch (IOException e)
		{
			if (Util.DEBUG)
			{
				System.err.println("couldn't send message: " + e.getMessage());
				System.err.println("Message is " + e.getMessage());
				e.printStackTrace();
			}
			return null;
		}
		catch (ParseException e)
		{
			if (Util.DEBUG)
			{
				System.err.println("could not parse tracker response");
				System.err.println("Message is " + e.getMessage());
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * Translate the raw response of the tracker into something useful
	 * 
	 * @param response
	 *            the raw getContent()
	 * @return array of PeerAddress[] found in the response
	 */
	private InetSocketAddress[] parse(Object response) throws ParseException
	{
		if (Util.DEBUG)
		{
			System.out.println("Parsing tracker response...");
		}
		if (!(response instanceof Dictionary))
		{
			if (Util.DEBUG)
			{
				System.err.println("Tracker sent us garbage (not a Dictionary)!");
			}
			return null;
		}
		Dictionary dict = (Dictionary) response;
		if (dict.hasValue("failure reason"))
		{
			System.err.println("Request failed: " + dict.getString("failure reason"));
			return null;
		}

		if (dict.hasValue("warning message"))
		{
			System.err.println("Warning: " + dict.getString("warning message"));
		}

		/** ensure the rest of the pieces are in place */
		return readmsg(dict);
	}

	/**
	 * Ensure a dictionary has all the necessary parts and update and necessary
	 * parts
	 * 
	 * @param dict
	 *            Dictionary to verify
	 * @throws ParseException
	 */
	private InetSocketAddress[] readmsg(Dictionary dict) throws ParseException
	{
		wait = (int) dict.getInt("interval");

		if (Util.DEBUG)
			System.out.println("wait:" + wait);

		if (dict.hasValue("min interval"))
		{
			firstWait = (int) dict.getInt("min interval");

			if (Util.DEBUG)
				System.out.println("firstWait:" + firstWait);
		}
		if (dict.hasValue("tracker id"))
		{
			trackerID = dict.getString("tracker id");

			if (Util.DEBUG)
				System.out.println("trackerID:" + trackerID);
		}

		numSeeders = (int) dict.getInt("complete");

		if (Util.DEBUG)
			System.out.println("numSeeders:" + numSeeders);

		numLeechers = (int) dict.getInt("incomplete");

		if (Util.DEBUG)
			System.out.println("numLeechers:" + numLeechers);

		if (!dict.hasValue("peers"))
			throw new ParseException("missing \"incomplete\"");

		if (dict.get("peers") instanceof BList)
		{
			if (Util.DEBUG)
			{
				System.err.println("Tracker gave us long format of peers (which we told it not to do).");
			}
			parseLongFormat(dict.getList("peers"));
		}

		return parseShortFormat(dict.getBytes("peers"));
	}

	private InetSocketAddress[] parseShortFormat(byte[] addresses) throws ParseException
	{
		if (Util.DEBUG)
		{
			System.out.println("have raw addresses of length: " + addresses.length);
		}
		if (addresses.length % 6 != 0)
			throw new ParseException("bad peers list");

		/** helper piece for making InetAddresses */
		byte[] addr = new byte[4];

		InetSocketAddress[] retVal = new InetSocketAddress[addresses.length / 6];
		for (int i = 0; i < retVal.length; ++i)
		{
			System.arraycopy(addresses, i * 6, addr, 0, 4);
			int port = NetUtils.bytesToShort(addresses, i * 6 + 4);
			try
			{
				/** java doesn't have unsigned bytes... this may be funky */
				InetSocketAddress inet = new InetSocketAddress(InetAddress.getByAddress(addr), port);

				if (Util.DEBUG && inet != null)
				{
					System.out.println("Added " + inet.toString());
				}
				retVal[i] = inet;
			}
			catch (UnknownHostException e)
			{
				// never happens (we used 4 bytes.. not doing a lookup)
				e.printStackTrace();
			}
		}
		return retVal;
	}

	private InetSocketAddress[] parseLongFormat(BEncodedObject[] peers) throws ParseException
	{
		InetSocketAddress[] retVal = new InetSocketAddress[peers.length];
		Dictionary tmp = null;
		for (int i = 0; i < peers.length; ++i)
		{
			if (!(peers[i] instanceof Dictionary))
			{
				if (Util.DEBUG)
				{
					System.err.println("Tracker gave broken long peers list!");
				}
				return null;
			}
			tmp = (Dictionary) peers[i];
			int port = (int) tmp.getInt("port");
			String ip = tmp.getString("ip");

			try
			{
				retVal[i] = new InetSocketAddress(InetAddress.getByName(ip), port);
			}
			catch (UnknownHostException e)
			{
				/** skip the bad one */
				if (Util.DEBUG)
				{
					System.err.println("Couldn't resolve host (in crappy long format).. skipping..");
				}
				retVal[i] = null;
			}
		}
		return retVal;
	}

	/**
	 * be polite about stopping
	 * 
	 */
	public void close()
	{
		/** tell the tracker we are stopping */
		sendMessage(2, 0);
	}

	/**
	 * 
	 * @param state
	 *            an optional event of: "started", "stopped", or "completed"
	 * @param numWant
	 *            number of peers to request
	 * @return exact URL for tracker as a string
	 * @throws UnsupportedEncodingException
	 */
	private String genCGIreq(String state, String numWant) throws UnsupportedEncodingException
	{
		String infoHash = tm.getInfoHash().toURLEncodedString();
		String peerID = tm.getPeerID().toURLEncodedString();
		String port = Integer.toString(tm.getPort());
		String left = Long.toString(tm.getLeft());
		String uploaded = Long.toString(tm.getUploaded());
		String downloaded = Long.toString(tm.getDownloaded());

		StringBuilder sb = new StringBuilder();
		sb.append(tracker + "?");
		sb.append("info_hash=" + infoHash);
		sb.append("&peer_id=" + peerID);
		sb.append("&port=" + port);
		sb.append("&uploaded=" + uploaded);
		sb.append("&downloaded=" + downloaded);
		sb.append("&compact=1");
		sb.append("&left=" + left);
		sb.append("&numwant=" + numWant);

		if (state != null)
			sb.append("&state=" + state);

		if (trackerID != null)
			sb.append("&trackerid=" + trackerID);

		return sb.toString();
	}

	/**
	 * @return the numLeechers
	 */
	protected int getNumLeechers()
	{
		return numLeechers;
	}

	/**
	 * @return the numSeeders
	 */
	protected int getNumSeeders()
	{
		return numSeeders;
	}

}
