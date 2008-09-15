package edu.iastate.cs309.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import edu.iastate.cs309.communication.TorrentProp;
import edu.iastate.cs309.test.davidsTests.fakeServer.FakeServer;
import edu.iastate.cs309.test.davidsTests.fakeTracker.FakeTracker;
import edu.iastate.cs309.torrentManager.Torrent;
import edu.iastate.cs309.torrentManager.TorrentFile;
import edu.iastate.cs309.torrentManager.containers.PeerID;
import edu.iastate.cs309.torrentManager.exceptions.TorrentFailedException;
import edu.iastate.cs309.torrentManager.interfaces.TorrentViewOfTorrentManager;
import edu.iastate.cs309.torrentparser.ParseException;

/** run the party */
public class TorrentDriver implements TorrentViewOfTorrentManager
{
	/** port open to the outside world */
	static private int port = 9919;

	/** base download */
	private static String downloadDir = null;

	/** peer id */
	PeerID pid = null;

	/** 2 minute timeout */
	long timeout = 120000;

	/** torrent properties */
	static TorrentProp tp = new TorrentProp();

	/** fake.torrent */
	static String torr = null;

	/**
	 * Download a torrent
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	public TorrentDriver() throws UnsupportedEncodingException, ParseException
	{
		pid = new PeerID(new String("-BD0300-594835987634").getBytes("UTF-8"));
	}

	/**
	 * 
	 * @param args
	 *            <torrent to download> <directory to download>
	 */
	public static void main(String[] args)
	{
		/** fake tracker used in test mode */
		FakeTracker f = null;

		/** fake server used to pass in peer-initiated connections */
		FakeServer s = null;

		/** only use 5 peers */
		tp.setNumPeers(40);

		if (args.length != 2)
		{
			System.out.printf("Usage: <torrentfile> <downloaddir>");
			return;
		}

		/** are we in test mode? */
		if (args[0].equals("test"))
		{
			/** use fake.torrent in svn */
			torr = (new File(System.getProperty("user.dir"), "edu/iastate/cs309/test/fakeTracker/fake.torrent")).toString();
			try
			{
				f = FakeTracker.startDaemon();
			}
			catch (IOException e)
			{
				// don't care
			}
		}
		else
		{
			torr = args[0];
		}

		downloadDir = args[1];
		try
		{
			TorrentViewOfTorrentManager callback = new TorrentDriver();
			TorrentFile tf = new TorrentFile(torr);

			System.out.println("Infohash: " + tf.getInfoHash());

			System.out.println("Bitfield should have " + (int) (Math.ceil(tf.getNumOfPieces() / 8.0)) + "byes");

			/** only ask for one thing at a time */
			tp.setQueueSize(1);
			/** null is String name of .torrent file */
			Torrent t = new Torrent(tf, callback, tp, null);
			System.out.println("Has: " + t.getPieces().length);

			/** start up fake server to handle inbound connections */
			s = new FakeServer(port, t);
			new Thread(s).start();

			/** run forever */
			while (true)
			{
				t.step();
				System.out.println(t.getInformation());
			}

		}
		catch (UnsupportedEncodingException e)
		{
			// lies!
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			// lies!
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			System.err.println("This system cannot support BitTorrent client");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (TorrentFailedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public PeerID getPeerID()
	{
		return pid;
	}

	public int getPort()
	{
		return port;
	}

	public long getTimeout()
	{
		return timeout;
	}

	public String getRootDir()
	{
		return downloadDir;
	}

}
