package edu.iastate.cs309.torrentManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import edu.iastate.cs309.communication.ServerProperties;
import edu.iastate.cs309.communication.TorrentInfo;
import edu.iastate.cs309.communication.TorrentProp;
import edu.iastate.cs309.server.Server;
import edu.iastate.cs309.torrentManager.containers.HandshakeInfo;
import edu.iastate.cs309.torrentManager.containers.InfoHash;
import edu.iastate.cs309.torrentManager.containers.PeerID;
import edu.iastate.cs309.torrentManager.exceptions.IncompleteFileException;
import edu.iastate.cs309.torrentManager.exceptions.PeerDeadException;
import edu.iastate.cs309.torrentManager.exceptions.TorrentNotFoundException;
import edu.iastate.cs309.torrentManager.interfaces.TorrentViewOfTorrentManager;
import edu.iastate.cs309.util.Util;

/**
 * 
 * 
 * @author sralmai
 * 
 */
public class TorrentManager implements Runnable, TorrentViewOfTorrentManager
{
	/** shutdown signal */
	boolean running = true;

	/** server settings */
	ServerProperties sProp = null;

	/** default Torrent settings */
	TorrentProp defTorrProp = new TorrentProp();

	/** counter for refID assignment */
	private int nextRefID = 0;

	/** executing thread */
	Thread thread = null;

	/** callback for notifying */
	Server server = null;

	/** blocking queue */
	private BlockingQueue<TorrentEgg> nest = new LinkedBlockingQueue<TorrentEgg>();

	/** list of Torrents to remove */
	private BlockingQueue<Torrent> killList = new LinkedBlockingQueue<Torrent>();

	/**
	 * storage class to hold new torrent info until TorrentManager thread can
	 * start it
	 */
	private class TorrentEgg
	{
		private int refID = 0;
		private File file = null;

		protected TorrentEgg(int refID, File file)
		{
			this.refID = refID;
			this.file = file;
		}

		protected int getRefID()
		{
			return refID;
		}

		protected File getFile()
		{
			return file;
		}
	}

	/** coordination lock for thread */
	Object lock = new Object();

	/** list of Torrents by refID */
	private Map<Integer, Torrent> torrents = Collections.synchronizedMap(new HashMap<Integer, Torrent>());

	/**
	 * used to place incoming peer connections (peers give a hashinfo in the
	 * handshake) (must be synced with torrents)
	 */
	private Map<InfoHash, Integer> toRefID = Collections.synchronizedMap(new HashMap<InfoHash, Integer>());

	/**
	 * constructor
	 */
	public TorrentManager(Server s)
	{
		server = s;
	}

	/**
	 * the number of torrents this TorrentManager is current managing
	 * 
	 * @return the current number of torrents
	 */
	public int torrentCount()
	{
		return torrents.size();
	}

	/**
	 * @return the port we are listening on for new connection
	 */
	public int getPort()
	{
		return sProp.getPeerPort();
	}

	/**
	 * inform the TorrentManager (and all Torrents) of new Server Properties (no
	 * variables are cached, so update is trivial)
	 * 
	 * @param sp
	 *            new server Properties
	 */
	public void newServerProp(ServerProperties sp)
	{
		sProp = sp;
	}

	/**
	 * Try to add a connect to a Torrent
	 * 
	 * @param s
	 *            peer-initiated connection
	 */
	public void addNewPeer(Socket s)
	{
		HandshakeInfo hInfo;
		try
		{
			hInfo = Peer.getHandshake(s);
		}
		catch (PeerDeadException e)
		{
			if (Util.DEBUG)
			{
				System.err.println("Peer-initiated connection died: " + e.toString());
			}
			return;
		}

		Integer refID = toRefID.get(hInfo.getInfoHash());

		Torrent t = torrents.get(refID);

		/** if we don't have the torrent, drop the connection */
		if (refID == null || t == null)
		{
			try
			{
				if (Util.DEBUG)
				{
					System.err.println("Someone thought we had a torrent we don't. He was dropkicked out the door.");
				}
				s.close();
			}
			catch (IOException e)
			{
				// don't care
			}
			return;
		}

		t.addConnection(s, hInfo.getPeerID());
	}

	/**
	 * @param file
	 *            a File (hopefully a torrent file) to begin working on
	 * @return The refID of the new torrent (or -1 on failure)
	 */
	public int addTorrent(File file)
	{
		nest.add(new TorrentEgg(nextRefID, file));

		/*
		 * if thread is null, the torrentManager is not asleep yet and it will
		 * still call birthTorrents()
		 */
		if (thread != null)
		{
			synchronized (thread)
			{
				thread.notify();
			}
		}

		return nextRefID++;
	}

	/**
	 * actually start the torrent
	 * 
	 * @param egg
	 * @return true on success
	 */
	private int birthTorrent(TorrentEgg egg)
	{
		File file = egg.getFile();
		int refID = egg.getRefID();

		TorrentFile tf;
		Torrent t;
		try
		{
			tf = new TorrentFile(file.toString());
		}
		catch (Exception e)
		{
			if (Util.DEBUG)
			{
				System.err.println("adding torrent failed: " + e.getMessage());
			}

			return -1;
		}

		try
		{
			t = new Torrent(tf, this, defTorrProp, file.toString());
		}
		catch (Exception e)
		{
			if (Util.DEBUG)
			{
				System.err.println("Torrent creation failed: ");
				e.printStackTrace();
			}
			return -1;
		}

		torrents.put(refID, t);
		toRefID.put(t.getInfoHash(), refID);

		/** are we the first torrent added? */
		if (torrents.size() == 1 && thread != null)
		{
			synchronized (thread)
			{
				/** let the thread know it has work to do */
				thread.notify();
			}
		}

		/** let the server know we have goods */
		if (server != null)
			server.torrentListUpdated();

		return refID;
	}

	/**
	 * create torrent
	 */

	/**
	 * list of refIDs
	 * 
	 * @return list refIDs of all running Torrents
	 */
	public List<Integer> getTorrents()
	{
		return new ArrayList<Integer>(torrents.keySet());
	}

	/**
	 * Run the TorrentManager.
	 * 
	 * Depending on configuration (specifically, ServerProp.numThreads() ),
	 * simply loop around step(), or spawn threads to call Torrent.step() on
	 * this TorrentManager's Torrents.
	 */
	public void run()
	{
		/** grab the thread for signalling */
		thread = Thread.currentThread();

		while (running)
		{

			/** remove any torrents marked for deletion */

			killTorrents();

			birthTorrents();

			if (torrents.size() == 0)
			{
				try
				{
					synchronized (thread)
					{
						thread.wait();
					}
				}
				catch (InterruptedException e)
				{
					if (!running)
						return;
					// don't care
					if (Util.DEBUG)
					{
						e.printStackTrace();
					}
				}
			}
			try
			{
				for (Torrent t : torrents.values())
				{
					t.step();
				}
			}
			catch (ConcurrentModificationException e)
			{
				// Hacky fix, but I just don't care anymore
			}
		}
	}

	/**
	 * 
	 */
	public void stop()
	{
		running = false;
		synchronized (thread)
		{
			thread.notify();

			while (true)
			{
				try
				{
					thread.join();
					break;
				}
				catch (InterruptedException e)
				{
				}
			}
		}
		for (Torrent t : torrents.values())
		{
			t.close();
		}
	}

	/**
	 * 
	 * @return this program's PeerID
	 */
	public PeerID getPeerID()
	{
		return sProp.getPeerID();
	}

	/**
	 * 
	 * @return timeout in milliseconds for Peer connections
	 */
	public long getTimeout()
	{
		return sProp.getTimeout();
	}

	/**
	 * 
	 * @param refID
	 *            reference to Torrent to update
	 * @param prop
	 *            new properties
	 */
	public void changeProperties(int refID, TorrentProp prop)
	{
		Torrent t = torrents.get(refID);

		if (t == null && Util.DEBUG)
		{
			System.err.println("Server called changeProperties on nonexistant refID");
			return;
		}

		t.changeProperties(prop);
	}

	/**
	 * 
	 * @param refID
	 *            reference to desired torrent
	 * @return desired torrent's information
	 * @throws TorrentNotFoundException
	 */
	public TorrentInfo getInfo(int refID) throws TorrentNotFoundException
	{
		Torrent t = torrents.get(refID);
		if (t == null)
			throw new TorrentNotFoundException();

		TorrentInfo ti = t.getInformation();
		ti.setRefID(refID);

		return ti;
	}

	/**
	 * Get a torrent's properties.
	 * 
	 * @param refID
	 *            reference to desired torrent
	 * @return desired torrent's properties
	 * @throws TorrentNotFoundException
	 */
	public TorrentProp getProp(int refID) throws TorrentNotFoundException
	{
		Torrent t = torrents.get(refID);
		if (t == null)
			throw new TorrentNotFoundException();

		TorrentProp tp = t.getProperties();
		tp.setRefID(refID);

		return tp;
	}

	/**
	 * Get a file from a torrent
	 * 
	 * @param refID
	 *            Torrent
	 * @param index
	 *            which file from the torrent to send
	 * @return a stream which is the passed file
	 * @throws FileNotFoundException
	 * @throws TorrentNotFoundException
	 * @throws IncompleteFileException
	 */
	public InputStream getFile(int refID, int index) throws FileNotFoundException, TorrentNotFoundException, IncompleteFileException
	{
		Torrent t = torrents.get(refID);
		if (t == null)
			throw new TorrentNotFoundException();

		return t.getFile(index);
	}

	/**
	 * @return the root directory of torrent downloads
	 */
	public String getRootDir()
	{
		return sProp.getRootDir();
	}

	/**
	 * mark a Torrent for removal
	 * 
	 * @param refID
	 * @throws TorrentNotFoundException
	 */
	public void removeTorrent(int refID) throws TorrentNotFoundException
	{
		Torrent t = torrents.get(refID);
		if (t == null)
			return;

		killList.add(t);

		/** remove from both reference lists */
		torrents.remove(refID);
		toRefID.remove(t.getInfoHash());
	}

	/**
	 * remove marked torrents
	 * 
	 * @param refID
	 */
	private void killTorrents()
	{
		while (killList.peek() != null)
		{
			try
			{
				killList.take().delete();
			}
			catch (InterruptedException e)
			{
				// ignore
				if (Util.DEBUG)
					e.printStackTrace();
			}
		}
	}

	/**
	 * start up added torrents
	 * 
	 */
	private void birthTorrents()
	{
		while (nest.peek() != null)
		{
			try
			{
				birthTorrent(nest.take());
			}
			catch (InterruptedException e)
			{
				// ignore
				if (Util.DEBUG)
					e.printStackTrace();
			}
		}
	}
}
