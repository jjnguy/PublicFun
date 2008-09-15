package edu.iastate.cs309.torrentManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iastate.cs309.communication.TorrentInfo;
import edu.iastate.cs309.communication.TorrentProp;
import edu.iastate.cs309.torrentManager.containers.BlockRequest;
import edu.iastate.cs309.torrentManager.containers.FileAccess;
import edu.iastate.cs309.torrentManager.containers.InfoHash;
import edu.iastate.cs309.torrentManager.containers.PeerID;
import edu.iastate.cs309.torrentManager.containers.Piece;
import edu.iastate.cs309.torrentManager.exceptions.IncompleteFileException;
import edu.iastate.cs309.torrentManager.exceptions.PeerDeadException;
import edu.iastate.cs309.torrentManager.exceptions.TorrentFailedException;
import edu.iastate.cs309.torrentManager.interfaces.TorrentViewOfTorrentManager;
import edu.iastate.cs309.util.Util;

/**
 * Torrent and classes used by it implement the BitTorrent protocol.
 * 
 * Each Torrent is a single active torrent. Runs for a bit when step() is
 * called.
 * 
 * This is the nexus of communication between the file system, tracker, peers,
 * and messages to/from the torrent manager
 * 
 * Wish list: FileAcces, Peer, Tracker, Piece, and TorrentManager should all
 * have an interface associated with them to mask out other calls
 * 
 * @author sralmai
 */
public class Torrent
{
	/** parent torrent manager (for callbacks ) */
	TorrentViewOfTorrentManager manager = null;

	/** internal data */
	private TorrentFile torrFile = null;

	/**
	 * "file system" name of .torrent file (for TorrentInfo) XXX: not used?!?!?
	 */
	private String name = null;

	/**
	 * @return raw bitfield of currently owned pieces
	 */

	/** tracker connection */
	private Tracker tracker = null;

	/** last piece index some Peer was working on */
	private int index = 0;

	/** date started */
	private long time = 0;

	/** are we deleting everything now? */
	boolean dying = false;

	/** file access */
	private FileAccess fileAccess = null;

	/** claimed Bitfield (set bits are claimed pieces a Peer is working on) */
	private Piece[] inProg = null;

	/** total number of bytes transferred since opening tracker connection */
	private long uploaded = 0;
	private long downloaded = 0;

	/** torrent properties */
	private TorrentProp torrProp = null;

	/** active Peers */
	private Map<InetSocketAddress, Peer> peers = Collections.synchronizedMap(new HashMap<InetSocketAddress, Peer>());

	/**
	 * TorrentManager creates Torrents on torrentManager.addTorrent(String
	 * filename)
	 * 
	 * @param tvtm
	 *            Torrent's interface for accessing TorrentManager (callback)
	 * @param tf
	 * @param tp
	 *            starting properties for this torrent
	 * @param dotTorrentFileName
	 *            file name of the torrent file this is based on
	 * @throws TorrentFailedException
	 * @throws IOException
	 * @throws SecurityException
	 */
	public Torrent(TorrentFile tf, TorrentViewOfTorrentManager tvtm, TorrentProp tp, String dotTorrentFileName) throws TorrentFailedException, SecurityException, IOException
	{
		/** current settings */
		torrProp = tp;

		/** file name */
		name = dotTorrentFileName;

		/** cache this */
		torrFile = tf;

		/** manager */
		manager = tvtm;

		/** mark time of birth */
		time = new Date().getTime();

		/** in progress pieces */
		inProg = new Piece[tf.getNumOfPieces()];

		if (Util.DEBUG)
		{
			System.err.print("Initializing files...");
		}

		/** initialize file system */
		try
		{
			fileAccess = new FileAccess(tvtm.getRootDir(), tf, null, true);
		}
		catch (NoSuchAlgorithmException e)
		{
			if (Util.DEBUG)
				e.printStackTrace();

			System.err.println("Throwing a new exception that is handled by everyone else!");
			throw new TorrentFailedException("no alg: " + e.getMessage());
		}

		if (Util.DEBUG)
		{
			System.err.println("done.");
			System.err.print("initializing tracker...");
		}

		/** create tracker */
		tracker = new Tracker(this);

		if (Util.DEBUG)
		{
			System.err.println("done.");
		}

	}

	/**
	 * Add a peer initiated connection to Peers
	 * 
	 * @param s
	 *            socket new peer is connected on
	 * @param p
	 *            PeerID of peer on socket
	 */
	protected void addConnection(Socket s, PeerID p)
	{
		try
		{
			InetSocketAddress sa = (InetSocketAddress) s.getLocalSocketAddress();
			peers.put(sa, new Peer(s, p, this));
			if (Util.DEBUG)
			{
				System.err.println("Adding peer-initiated connection");
				new Thread(peers.get(sa)).start();
			}
		}
		catch (PeerDeadException e)
		{
			if (Util.DEBUG)
			{
				System.err.println("Peer from addConnection(Socket s, PeerID p) died on instantiation! (We didn't want him anyways!)");
			}
		}
	}

	/**
	 * get a downloaded file from the torrent
	 * 
	 * @param index
	 *            of file to get
	 * @return in input stream connected to the requested file
	 * @throws IncompleteFileException
	 * 
	 * @returns InputStream connected to that file
	 */
	public InputStream getFile(int index) throws IncompleteFileException
	{
		return fileAccess.getFile(index);
	}

	/** ************ methods for Peers and Trackers ********** */

	/**
	 * @param i
	 *            number of bytes just uploaded
	 */
	protected void addUploaded(int i)
	{
		uploaded += i;
	}

	/**
	 * @return true if all files in torrent are present (and correct)
	 */
	public boolean isComplete()
	{
		return fileAccess.complete();
	}

	/**
	 * @param i
	 *            number of bytes just downloaded
	 */
	protected void addDownloaded(int i)
	{
		downloaded += i;
	}

	/**
	 * @return InfoHash of this torrent
	 */
	protected InfoHash getInfoHash()
	{
		return torrFile.getInfoHash();
	}

	/**
	 * @return String address of tracker
	 */
	protected String getAnnounce()
	{
		String retVal = torrFile.getAnnounce();
		if (Util.DEBUG)
		{
			System.err.println(retVal);
		}
		return retVal;
	}

	/**
	 * @return total uploaded bytes (of the files since this object initialized)
	 *         (protocol overhead NOT included)
	 */
	protected long getUploaded()
	{
		return uploaded;
	}

	/**
	 * @return total downloaded bytes (of the files since this object
	 *         initialized) (protocol overhead NOT included)
	 */
	protected long getDownloaded()
	{
		return downloaded;
	}

	/**
	 * The approximation of remaining bytes to download is used in Tracker
	 * communication
	 * 
	 * @return rough estimate of the number of bytes to download
	 */
	protected long getLeft()
	{
		return (torrFile.getNumOfPieces() - fileAccess.numOfCompletePieces()) * torrFile.pieceLength();
	}

	/**
	 * @return PeerID of the server
	 */
	protected PeerID getPeerID()
	{
		return manager.getPeerID();
	}

	/**
	 * @return number of currently owned pieces
	 */
	protected int getPiecesCount()
	{
		return fileAccess.numOfTotalPieces();
	}

	/**
	 * The number of "freebies" is the number of blocks we share freely with any
	 * peer before blocking (see TorrentProp for more info)
	 * 
	 * @return number of "freebie" blocks in this TorrentProp
	 */
	protected int getFreebies()
	{
		return torrProp.getFreebies();
	}

	/**
	 * (@see TorrentProp for details)
	 * 
	 * @return length of queue for block requests
	 */
	protected int getQueueSize()
	{
		return torrProp.getQueueSize();
	}

	/**
	 * @return raw bitfield of currently owned pieces
	 */
	public byte[] getPieces()
	{
		return fileAccess.getRawBitfield();
	}

	/**
	 * @param i
	 * @return whether or not piece index i is downloaded and verified
	 */
	public boolean havePiece(int i)
	{
		return fileAccess.havePiece(i);
	}

	/**
	 * @return the timeout length from ServerProp
	 * 
	 * If there is no input for longer than this time, Torrent will drop the
	 * peer connection
	 */
	protected long getTimeout()
	{
		return manager.getTimeout();
	}

	/**
	 * @return length of a single piece in bytes (all pieces are the same length
	 *         except possibly the last piece)
	 */
	public long pieceLength()
	{
		return torrFile.pieceLength();
	}

	/**
	 * @return length of the last piece in bytes
	 */
	public long lastPieceLength()
	{
		return torrFile.lastPieceLength();
	}

	/**
	 * @return the total number of pieces in the torrent
	 */
	public int numOfPieces()
	{
		return torrFile.getNumOfPieces();
	}

	/**
	 * open a connection to the tracker, grab some peers, and start rocking
	 * 
	 * this method should NOT be public
	 */
	public void start()
	{
		step();
	}

	/**
	 * gently kill all connections
	 */
	public void stop()
	{
		tracker.close();
		synchronized (peers)
		{
			for (Peer peer : peers.values())
			{
				peer.close();
			}
		}
	}

	/**
	 * Update the torrent settings. Since all things which use settings access
	 * the Torrent's stored TorrentProp, updating is trivial.
	 * 
	 * @param prop
	 *            new torrent properties to use
	 */
	public void changeProperties(TorrentProp prop)
	{
		torrProp = prop;
		updateRates();
	}

	/**
	 * Compile information about the torrent into a TorrentInfo object (used to
	 * transfer info to client)
	 * 
	 * @return aggregation of current torrent info
	 */
	public TorrentInfo getInformation()
	{
		TorrentInfo result = new TorrentInfo();
		result.setBytesDownloaded(getDownloaded());
		result.setBytesUploaded(getUploaded());
		result.setDateStarted(time);
		String baseName = torrFile.getBaseName();

		if (torrFile.isSingleFile())
		{
			result.setSize(torrFile.fileLength());
			result.setName(baseName);
		}
		else
		{
			// This works even when the baseName doesn't contain File.separator
			result.setName(baseName.substring(baseName.indexOf(File.separator) + 1));
			List<Long> sizes = new ArrayList<Long>(torrFile.getNumOfFiles());
			List<String> names = new ArrayList<String>(torrFile.getNumOfFiles());
			for (int i = 0; i < torrFile.getNumOfFiles(); i++)
			{
				sizes.add(torrFile.fileLength(i));
				names.add(torrFile.filePath(i));
			}
			result.setFilesizes(sizes);
			result.setIndividualFileNames(names);
		}
		result.setNumLeechers(tracker.getNumLeechers());
		result.setNumPieces(getPiecesCount());
		result.setNumSeeders(tracker.getNumSeeders());
		result.setUploadSpeed(getUploadSpeed());
		result.setDownloadSpeed(getDownloadSpeed());
		result.setDotTorrentFile(torrFile.getDotTorrentFile().getAbsolutePath());
		result.setProgress(fileAccess.getRawBitfield());
		result.setTrackerUrl(getAnnounce());

		result.setComplete(isComplete());

		return result;
	}

	/**
	 * @return current upload speed (in bytes/sec)
	 */
	private int getUploadSpeed()
	{
		int totalSpeed = 0;
		synchronized (peers)
		{
			for (Peer p : peers.values())
				totalSpeed += p.uploadSpeed();
		}
		return totalSpeed;
	}

	/**
	 * @return current download speed (in bytes/sec)
	 */
	private int getDownloadSpeed()
	{
		int totalSpeed = 0;
		synchronized (peers)
		{
			for (Peer p : peers.values())
				totalSpeed += p.downloadSpeed();
		}
		return totalSpeed;
	}

	/**
	 * @return current torrent properties
	 * 
	 * XXX: !DANGER! this should return a copy of TorrentProp (but for now, the
	 * only time this call is used is to push TorrentProp across the network)
	 */
	public TorrentProp getProperties()
	{
		return torrProp;
	}

	/**
	 * 
	 * @param fileIndex
	 *            index of file to transfer
	 * @return InputStream of the bits of the requested file
	 * @throws IncompleteFileException
	 *             if File is not complete
	 */
	public InputStream transferFile(int fileIndex) throws IncompleteFileException
	{
		return fileAccess.getFile(fileIndex);
	}

	/**
	 * Run the torrent for a single "click". Upload/download rates are
	 * recalculated, the tracker is updated if necessary, and new peers are
	 * possibly added
	 */
	public void step()
	{
		updateRates();
		int numPeersWanted = regulatePeers();

		/** should timing be moved out of tracker into Torrent? */
		InetSocketAddress[] newPeers = tracker.update(numPeersWanted);

		/** if we got new peers, add them */
		if (newPeers != null)
		{
			if (Util.DEBUG)
			{
				System.out.println("Got " + newPeers.length + " new peers!");
			}

			int numToAdd = torrProp.getNumPeers() - peers.size();
			int actualAdded = 0;
			for (int i = 0; i < newPeers.length && actualAdded < numToAdd; ++i)
			{
				if (newPeers[i] != null)
				{
					/** count successfully started peerss */
					if (addPeer(newPeers[i]))
					{
						++actualAdded;
					}
				}
			}
		}

		if (Util.DEBUG)
		{
			System.out.println("Should be: " + peers.size() + " other threads running");
		}
	}

	/**
	 * Recalculate upload and download rates
	 */
	private void updateRates()
	{
		if (peers.size() == 0)
			return;
		int peerDownloadSpeed = torrProp.getDownloadCap() / peers.size();
		int peerUploadSpeed = torrProp.getUploadCap() / peers.size();
		for (Peer p : peers.values())
		{
			p.setTargetDownload(peerDownloadSpeed);
			p.setTargetDownload(peerUploadSpeed);
		}
	}

	/**
	 * Initiate a new connection for our Peer pool
	 * 
	 * @param address
	 *            of peer
	 * @return true if peer successfully added
	 */
	private boolean addPeer(InetSocketAddress address)
	{
		Peer p = null;

		if (peers.size() > torrProp.getMaxPeers())
		{
			if (Util.DEBUG)
				System.err.println("Bad logic... calling addPeer() with max number of peers");
			return false;
		}
		try
		{
			p = new Peer(address, this);
		}
		catch (PeerDeadException e)
		{
			if (Util.DEBUG)
			{
				System.err.println("addPeer(" + address + ") failed: " + e.getMessage());
			}
			return false;
		}

		/** put it in our list */
		peers.put(address, p);
		new Thread(p).start();
		return true;
	}

	/**
	 * ensure number of peers is below the max bounds described by TorrentProp
	 * 
	 * if below the lower bound, return a positive number
	 * 
	 * @return the number of peers we want added
	 */
	private int regulatePeers()
	{
		int numPeers = peers.size();
		if (numPeers > torrProp.getMaxPeers())
		{
			if (Util.DEBUG)
				System.err.println("Too many peers: " + (numPeers - torrProp.getMaxPeers()) + " over the limit");

			return 0;
		}
		else if (numPeers > torrProp.getNumPeers())
			return 0;
		else
			return torrProp.getNumPeers() - numPeers;
	}

	/**
	 * 
	 * @return the port we are currently listening on for peer initiated
	 *         connections
	 */

	public int getPort()
	{
		return manager.getPort();
	}

	/**
	 * Abstracts torrent files to a continuous byte array
	 * 
	 * updates the torrent with data starting at offset
	 * 
	 * @param offset
	 *            to being writing
	 * @param data
	 *            byte array to completely write
	 * @return number of bytes written
	 */
	public int writeBytes(long offset, byte[] data)
	{
		try
		{
			return fileAccess.writeBytes(offset, data, data.length);
		}
		catch (IOException e)
		{
			if (Util.DEBUG)
				System.err.println("Problem writing bytes: " + e.getMessage());
			return 0;
		}
	}

	/**
	 * bring the torrent down safely
	 * 
	 * close tracker communication, then signal all peers
	 * 
	 */
	public void close()
	{
		tracker.close();
		synchronized (peers)
		{
			for (Peer p : peers.values())
			{
				p.close();
				try
				{
					Thread t = p.getThread();
					if (t == null)
					{
						if (Util.DEBUG)
							System.out.println("somehow a dead thread is still in the Peers pool!");
					}
					else
					{
						t.join();
					}
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Called to test if a piece is done
	 * 
	 * @param index
	 *            of piece in questions
	 */
	private boolean pieceDone(int index)
	{

		try
		{
			if (fileAccess.verifyPiece(index))
			{
				if (Util.DEBUG)
				{
					System.out.println("Have piece number " + index);
				}

				/** propagate have piece message */
				synchronized (peers)
				{
					for (Peer p : peers.values())
					{
						p.sendHave(index);
					}
				}
			}

			return true;
		}
		catch (NoSuchAlgorithmException e)
		{
			// don't care
			if (Util.DEBUG)
				e.printStackTrace();
		}
		catch (IOException e)
		{
			// don't care
			if (Util.DEBUG)
				e.printStackTrace();
		}

		return false;
	}

	/**
	 * add a peer initiated connection to this torrent
	 * 
	 * @param s
	 *            socket to add
	 * @param p
	 *            peer id
	 */
	public void addPeer(Socket s, PeerID p)
	{
		Peer peer = null;
		try
		{
			if (peers.size() > torrProp.getMaxPeers())
			{
				if (Util.DEBUG)
					System.err.println("Dropping connection. (over maxPeers() )");
				s.close();
				return;
			}
			peer = new Peer(s, p, this);
		}
		catch (PeerDeadException e)
		{
			if (Util.DEBUG)
			{
				System.err.println("Threw up taking new connection: " + e.getMessage());
			}
			return;
		}
		catch (IOException e)
		{
			// ignore dumped connection
			return;
		}
		new Thread(peer).start();
		peers.put((InetSocketAddress) s.getRemoteSocketAddress(), peer);
	}

	/**
	 * called by dying Peers to remove themselves from the peers group
	 * 
	 * (Since each Peer object runs on its own thread, only it knows when
	 * exactly it finishes executing.)
	 * 
	 * @param i
	 *            InetSocketAddress of dying Peer
	 * 
	 */
	protected void removePeer(InetSocketAddress i)
	{
		peers.remove(i);
	}

	/**
	 * called by a Peer object to collect data to send to its remote peer
	 * 
	 * @param b
	 *            BlockRequest
	 * @return byte array of data
	 * @throws IOException
	 */
	protected byte[] getBlock(BlockRequest b) throws IOException
	{
		return fileAccess.getBlock(b);
	}

	/**
	 * put a block of data into our files
	 * 
	 * @param index
	 * @param offset
	 *            offset in piece
	 * @param data
	 * @return true if the piece is now finished
	 */
	protected boolean recieveBlock(int index, int offset, byte[] data)
	{
		if (fileAccess.havePiece(index))
		{
			if (Util.DEBUG)
				System.out.println("given a block for a piece we already have!");
			return true;
		}

		/** tell everyone to cancel outstanding requests */
		synchronized (peers)
		{
			for (Peer p : peers.values())
			{
				p.sendCancelRequest(new BlockRequest(this, index, offset, data.length));
			}
		}

		/** if a piece manager doesn't exist, make one */
		if (inProg[index] == null)
			inProg[index] = new Piece(index, this);

		if (inProg[index].addData(offset, data))
		{
			/** do final piece checking */
			pieceDone(index);

			/**
			 * Even if checking failed, say we are done, so a new Piece manager
			 * will be created later (because this one is broken)
			 */
			inProg[index] = null;
			return true;
		}
		return false;
	}

	/**
	 * request a block from an incomplete piece
	 * 
	 * @param piece
	 *            index
	 * @return BlockRequest we want
	 */
	public BlockRequest requestBlock(int piece)
	{
		if (fileAccess.havePiece(piece))
		{
			if (Util.DEBUG)
				System.out.println("requesting blocks for a piece we already have!");
			return null;
		}

		/** start a piece manager */
		if (inProg[piece] == null)
		{
			inProg[piece] = new Piece(piece, this);
			if (Util.DEBUG)
				System.out.println("Starting piece manager for piece #" + piece);
		}
		return inProg[piece].getBlockRequest();

	}

	/**
	 * return a piece index for a peer to begin requesting from (All Peer
	 * objects call this method to determine what piece index they will begin
	 * searching for needed pieces. In a well-seeded torrent, this simply means
	 * each peer will sequentially request the next piece.
	 * 
	 * @return the piece index to begin searching for needed pieces
	 */
	synchronized protected int getIndex()
	{
		int retVal = index;
		index = (index + 1) % numOfPieces();
		return retVal;
	}

	/**
	 * called by Peer objects to inform Torrent of data transfers
	 * 
	 * @param uploaded
	 *            number of bytes in the block a Peer just uploaded to the
	 *            remote host
	 */
	public synchronized void addUploaded(long uploaded)
	{
		this.uploaded += uploaded;
	}

	/**
	 * called by Peer objects to inform Torrent of data transfers
	 * 
	 * @param downloaded
	 *            number of bytes in the block a Peer just recieved from the
	 *            remote host
	 */
	public synchronized void addDownloaded(long downloaded)
	{
		this.downloaded += downloaded;
	}

	/**
	 * Delete all torrent files and close down
	 */
	public void delete()
	{
		/** close all connections */
		close();

		/** remove the files on disk */
		fileAccess.delete();
	}

}
