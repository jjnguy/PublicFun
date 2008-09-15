package edu.iastate.cs309.torrentManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import edu.iastate.cs309.clientserverprotocol.NetUtils;
import edu.iastate.cs309.server.ServerLog;
import edu.iastate.cs309.torrentManager.containers.Bitfield;
import edu.iastate.cs309.torrentManager.containers.BlockRequest;
import edu.iastate.cs309.torrentManager.containers.HandshakeInfo;
import edu.iastate.cs309.torrentManager.containers.InfoHash;
import edu.iastate.cs309.torrentManager.containers.PeerID;
import edu.iastate.cs309.torrentManager.containers.SynchronizedOutputStream;
import edu.iastate.cs309.torrentManager.containers.TimeoutCheck;
import edu.iastate.cs309.torrentManager.exceptions.MalformedBitfieldException;
import edu.iastate.cs309.torrentManager.exceptions.PeerDeadException;
import edu.iastate.cs309.torrentManager.socketMeter.MeteredInputStream;
import edu.iastate.cs309.torrentManager.socketMeter.MeteredOutputStream;
import edu.iastate.cs309.torrentManager.socketMeter.ThrottledSocket;
import edu.iastate.cs309.util.Util;

/**
 * encapsulation of Peer connection
 * 
 * @author sralmai
 */
public class Peer implements Runnable
{

	/** Debug for just this file for cleanliness */
	private static final boolean DEBUG = false;

	/** Debug for block transfer messages only */
	private static final boolean DEBUG_MSG = true;

	/** test */
	private ThrottledSocket s = null;

	/** thread state */
	private boolean running = true;

	/** reference to the thread this peer runs on */
	private Thread thread = null;

	/** state table */
	private boolean peerChoking = true;
	private boolean peerInterested = false;
	private boolean amChoking = true;
	private boolean amInterested = false;

	/** block counts (used for choking to ensure fairness) */
	private int sent = 0;
	private int recv = 0;

	/** for keep-alive management */
	TimeoutCheck timeout = null;

	/** peer connection in */
	MeteredInputStream in = null;

	private MeteredOutputStream outMetered;

	/** peer connection out */
	/**
	 * this must be thread safe for "have" message propagation: there are public
	 * methods which write to the OutputStream.
	 */
	SynchronizedOutputStream out = null;

	/** outstanding block requests */
	List<BlockRequest> peerWantList = new ArrayList<BlockRequest>();

	/** our outstanding block requests */
	List<BlockRequest> sentList = new ArrayList<BlockRequest>();

	/** pieces we are currently requesting */
	List<Integer> workPieces = new ArrayList<Integer>();

	/** callback to parent Torrent */
	Torrent tm = null;

	/** peer's name */
	PeerID peerID = null;

	/** peer's pieces we want */
	Bitfield usefulPieces = null;

	/**
	 * run the thread
	 */
	public void run()
	{
		runOnce();

		while (running)
		{
			runEveryTime();
		}
	}

	/**
	 * @return The upload speed of this peer.
	 */
	public int uploadSpeed()
	{
		return outMetered.speed();
	}

	/**
	 * @return The download speed of this peer.
	 */
	public int downloadSpeed()
	{
		return in.speed();
	}

	private void runEveryTime()
	{
		try
		{
			/** choke peer if we've sent too much stuff */
			if ((sent - recv > tm.getFreebies()) && !tm.isComplete())
			{
				sendChoke(true);
			}
			/** toss them a piece */
			else if (peerWantList.size() > 0)
			{
				BlockRequest b = peerWantList.get(0);

				if (sendBlock(b))
					peerWantList.remove(0);
			}
			else
			{
				/** we will give them pieces if they want them */
				sendChoke(false);
			}

			processMessage();

			/** check for timeouts */
			if (timeout.timedOut())
				throw new PeerDeadException("Peer timed out!");
			else if (timeout.sendKeepAlive())
			{
				sendKeepAlive();
			}
		}
		catch (PeerDeadException e)
		{
			if (DEBUG)
			{
				ServerLog.log.log(Level.FINE, "PeerDeadException on in.available in Peer thread. Dying: ", e.getMessage());
			}
			running = false;
			if (s != null)
				tm.removePeer((InetSocketAddress) s.getLocalSocketAddress());
		}
	}

	/**
	 * Init code for the peer "thread" can probably be removed as soon as
	 * debugging is over.
	 */
	public void runOnce()
	{
		thread = Thread.currentThread();

		if (DEBUG)
			System.out.println("\tPeer thread started");
	}

	/**
	 * Start new connection to a peer.
	 * 
	 * @param isa
	 *            address of port
	 * @param torrMan
	 *            callback to parent Torrent
	 * 
	 * @throws PeerDeadException
	 */
	public Peer(InetSocketAddress isa, Torrent torrMan) throws PeerDeadException
	{
		tm = torrMan;

		/** send a keep-alive message 30 seconds before timeout */
		timeout = new TimeoutCheck(tm.getTimeout(), tm.getTimeout() - 30000);

		try
		{
			s = new ThrottledSocket(new Socket());

			/** give a five second timeout */
			s.connect(isa, 5000);
			in = new MeteredInputStream(s.getInputStream());
			outMetered = new MeteredOutputStream(s.getOutputStream());
			out = new SynchronizedOutputStream(outMetered);

			if (DEBUG)
				System.out.println("Connected to peer: " + s);
		}
		catch (IOException e)
		{
			throw new PeerDeadException("Couldn't open a connection to the peer: " + e.getMessage());
		}

		giveHandshake();
		peerID = recieveHandshake();

		if (DEBUG)
			System.out.println("added peer: " + peerID.toString());

		if (tm.getPiecesCount() > 0)
			/** tell them what pieces we have */
			sendBitfield();

		/** assume Peer has no pieces */

		usefulPieces = new Bitfield(tm.numOfPieces());
	}

	/**
	 * Receive a new connection from TorrentManager
	 * 
	 * @param s
	 *            connected socket
	 * @param p
	 *            PeerID from handshake
	 * @param t
	 *            Callback to parent torrent
	 * @throws PeerDeadException
	 */
	public Peer(Socket s, PeerID p, Torrent t) throws PeerDeadException
	{
		this.s = new ThrottledSocket(s);

		if (DEBUG)
			System.out.println("Socket: " + s);

		tm = t;

		if (DEBUG)
			System.out.print("Recieving conection from a new peer...");

		/** send a keep-alive message 30 seconds before timeout */
		timeout = new TimeoutCheck(tm.getTimeout(), tm.getTimeout() - 30000);

		peerID = p;

		try
		{
			in = new MeteredInputStream(s.getInputStream());
			outMetered = new MeteredOutputStream(s.getOutputStream());
			out = new SynchronizedOutputStream(outMetered);
		}
		catch (IOException e)
		{
			throw new PeerDeadException("Couldn't open a connection to the peer: " + e.getMessage());
		}

		giveHandshake();

		if (DEBUG)
			System.out.print("sent handshake...");

		if (tm.getPiecesCount() > 0)
		{
			if (DEBUG)
				System.out.print("giving current pieces...");
			/** tell them what pieces we have */
			sendBitfield();
		}

		/** assume they don't have any pieces */
		usefulPieces = new Bitfield(tm.numOfPieces());

		if (DEBUG)
			System.out.println("done.");
	}

	/**
	 * helper method for constructor send the first bits of Bittorrent down the
	 * wire
	 * 
	 * @throws PeerDeadException
	 */
	private void giveHandshake() throws PeerDeadException
	{
		byte[] handshake = new byte[68];
		byte[] name = new String("BitTorrent protocol").getBytes();

		handshake[0] = (byte) 19;

		System.arraycopy(name, 0, handshake, 1, 19);

		/** reserved 8 bytes */

		/** info_hash */
		System.arraycopy(tm.getInfoHash().getBytes(), 0, handshake, 28, 20);

		/** peer_id */
		System.arraycopy(tm.getPeerID().getBytes(), 0, handshake, 48, 20);

		try
		{
			out.write(handshake);
			timeout.sentMsg();
		}
		catch (IOException e)
		{
			throw new PeerDeadException("Couldn't send handshake: " + e.getMessage());
		}
	}

	/**
	 * helper method for the constructor
	 * 
	 * @return PeerID
	 * @throws PeerDeadException
	 */
	private PeerID recieveHandshake() throws PeerDeadException
	{
		HandshakeInfo hInfo = getHandshake(in);
		timeout.gotMsg();

		if (!hInfo.getInfoHash().equals(tm.getInfoHash()))
			throw new PeerDeadException("Peer gave incorrect info_hash!");

		/**
		 * NOTE: According to the BitTorrent spec, this peerID should be checked
		 * against the peerID given for the address by the tracker. But most
		 * trackers do not include the peerID, so this check is worthless.
		 */
		return hInfo.getPeerID();
	}

	/**
	 * wrapper for TorrentManager to find out if a torrent is being served
	 * 
	 * @param s
	 *            socket connection is on
	 * @return infoHash and PeerID encapsulated in HandshakeInfo
	 * @throws PeerDeadException
	 */
	public static HandshakeInfo getHandshake(Socket s) throws PeerDeadException
	{
		try
		{
			return getHandshake(s.getInputStream());
		}
		catch (IOException e)
		{
			throw new PeerDeadException(e.getMessage());
		}
	}

	/**
	 * helper method for constructor
	 * 
	 * @param in
	 * @return infoHash and PeerID encapsulated in HandshakeInfo
	 * @throws PeerDeadException
	 */
	protected static HandshakeInfo getHandshake(InputStream in) throws PeerDeadException
	{
		String protocol = "BitTorrent protocol";

		try
		{
			int sLen = in.read();
			if (sLen < 0)
				throw new PeerDeadException("Peer dumped us before we read input!");

			byte[] peerProtocol = new byte[sLen];

			NetUtils.readFully(in, peerProtocol, 0, sLen);
			if (!protocol.equals(new String(peerProtocol)))
				throw new PeerDeadException("Peer gave bad protocol in handshake!");

			byte[] reserved = new byte[8];
			byte[] infoHash = new byte[20];
			byte[] peerID = new byte[20];

			/** ignore reserved 8 bytes */
			NetUtils.readFully(in, reserved, 0, 8);

			NetUtils.readFully(in, infoHash, 0, 20);
			NetUtils.readFully(in, peerID, 0, 20);

			return new HandshakeInfo(new InfoHash(infoHash), new PeerID(peerID));
		}
		catch (IOException e)
		{
			if (DEBUG)
			{
				System.err.println("Message is " + e.getMessage());
				e.printStackTrace();
			}
			throw new PeerDeadException("Peer connection died: " + e.getMessage());
		}
	}

	/**
	 * frontend to processMessage( int length, int type)
	 * 
	 */
	private void processMessage() throws PeerDeadException
	{
		int len;
		int msgID;

		try
		{
			len = NetUtils.readInt(in);

			if (len == 0)
			{
				/** got a keep-alive */
				timeout.gotMsg();

				if (DEBUG)
					System.out.println("Got a keep alive message.");

				return;
			}

			msgID = in.read();

			if (msgID < 0)
			{
				if (DEBUG)
					System.out.println("Peer is closed");
				throw new PeerDeadException("Peer closed connection");
			}

			// len-1 because msgID already read
			processMessage(len - 1, msgID);
		}
		catch (IOException e)
		{
			if (DEBUG)
			{
				System.err.println("Message is " + e.getMessage() + " for address: " + s);
				e.printStackTrace();
			}
			throw new PeerDeadException("Couldn't read from peer: " + e.getMessage());
		}
	}

	/**
	 * message parsing happens here
	 * 
	 * @param len
	 *            length in bytes of the message waiting in the input stream
	 * @param msgID
	 * @throws PeerDeadException
	 */
	private void processMessage(int len, int msgID) throws PeerDeadException, IOException
	{
		/** reset the timer */
		timeout.gotMsg();

		/** allocate space for the message */
		byte[] msg = new byte[len];

		/** read the juicy details */
		NetUtils.readFully(in, msg, 0, len);

		if (DEBUG)
			System.out.print("Got message id: " + msgID + " with length: " + len + " from " + s);

		switch (msgID)
		{
		case 0:/** choke */
			if (DEBUG)
				System.out.println("\tChoke");
			choke();
			break;

		case 1:/** unchoke */
			if (DEBUG)
				System.out.println("\tUnchoke");
			unchoke();
			requestBlocks();
			break;

		case 2:/** interested */
			if (DEBUG)
				System.out.println("\tInterested");
			peerInterested = true;
			break;

		case 3:/** not interested */
			if (DEBUG)
				System.out.println("\tNot interested");
			peerInterested = false;
			break;

		case 4:/** have */
			if (DEBUG)
				System.out.println("\tHave");
			haveParse(msg);
			break;

		case 5:/** bitfield (only allowed as first message) */
			if (DEBUG)
				System.out.println("\tBitfield");
			bitfieldParse(msg);
			break;

		case 6:/** request */
			if (DEBUG)
				System.out.println("\tRequest");
			requestParse(msg);
			break;

		case 7:/** piece */
			if (DEBUG)
				System.out.println("\tPiece");
			pieceParse(msg);
			requestBlocks();
			break;

		case 8:/** cancel a piece request */
			if (DEBUG)
				System.out.println("\tCancel a Piece Request");
			cancelPieceRequest(msg);
			break;

		case 9:/** port (for DHT) */
			if (DEBUG)
				System.out.println("\tDHT");
			/** this extenstion not implemented */
			break;

		default:
			throw new PeerDeadException("Peer sent unknown msgID: " + msgID);
		}
	}

	private void choke()
	{
		peerChoking = true;
	}

	private void unchoke() throws PeerDeadException
	{
		peerChoking = false;

		/** send some block requests */
		requestBlocks();
	}

	private void haveParse(byte[] msg) throws PeerDeadException
	{
		int piece = NetUtils.bytesToInt(msg, 0);

		if (DEBUG)
			System.out.println("\tPeer " + s + " has piece: " + piece);
		if (!tm.havePiece(piece))
		{
			usefulPieces.set(piece);

			/** if we weren't already interested, we want the piece */
			if (!amInterested)
				sendInterested(true);
		}
	}

	private void bitfieldParse(byte[] msg) throws PeerDeadException
	{
		/**
		 * This should only be sent as the first message, but no other client
		 * really follows the spec, so it is accepted any time.
		 */

		/** set up usefulPieces */
		usefulPieces = findUsefulPieces(msg);

		/** let peer know we want pieces */
		if (usefulPieces.totalSet() > 0)
			sendInterested(true);
	}

	/**
	 * remove a block from the remote peer's want list
	 * 
	 * @param msg
	 * @throws PeerDeadException
	 */
	private void cancelPieceRequest(byte[] msg) throws PeerDeadException
	{
		int index;
		int offset;
		index = NetUtils.bytesToInt(msg, 0);
		offset = NetUtils.bytesToInt(msg, 4);
		int length = NetUtils.bytesToInt(msg, 8);

		peerWantList.remove(new BlockRequest(tm, index, offset, length));
	}

	private void requestParse(byte[] msg) throws PeerDeadException
	{
		int index;
		int offset;
		int length;
		/**
		 * ignore the message if choking or peer never said they want something
		 */
		if (!amChoking || !peerInterested)
		{
			index = NetUtils.bytesToInt(msg, 0);
			offset = NetUtils.bytesToInt(msg, 4);
			length = NetUtils.bytesToInt(msg, 8);

			peerWantList.add(new BlockRequest(tm, index, offset, length));
		}
	}

	private void pieceParse(byte[] msg) throws PeerDeadException
	{
		int index;
		int offset;
		index = NetUtils.bytesToInt(msg, 0);
		offset = NetUtils.bytesToInt(msg, 4);

		BlockRequest b = new BlockRequest(tm, index, offset, msg.length - 8);

		if (!sentList.remove(b))
		{
			if (DEBUG)
				System.out.println("we got a block we didn't request... using it anyways");
		}

		/** this is dirty... we shouldn't have to arraycopy */
		byte[] data = new byte[msg.length - 8];
		System.arraycopy(msg, 8, data, 0, data.length);

		/** remove block request from our queue */
		sentList.remove(new BlockRequest(tm, index, offset, data.length));

		/** return true when piece is complete */
		if (tm.recieveBlock(index, offset, data))
			/** needs to be boxed in object to disambiguate */
			workPieces.remove(new Integer(index));

		if (DEBUG_MSG)
			System.out.println("Recieved block: " + b);

		/** piece + 1 */
		++recv;

		/** send more block requests */
		requestBlocks();

	}

	/**
	 * update usefulPieces and request several (up to TorrentProp.queueSize)
	 * wanted blocks
	 * 
	 */
	private void requestBlocks() throws PeerDeadException
	{
		/** Peer will throw requestBlocks away if choking */
		if (peerChoking)
			return;

		if (DEBUG)
			System.out.println("Requesting Blocks... peer has " + usefulPieces.totalSet() + " we want");

		/** make sure we have good pieces */
		if (workPieces.size() < tm.getQueueSize() && usefulPieces.totalSet() > 0)
			managePieces();

		if (DEBUG)
			System.out.println("We have " + workPieces.size() + " pieces in the \"queue\" to get for peer " + s + " .");

		for (Integer p : workPieces)
		{
			/** returns null on bad request */
			BlockRequest br = tm.requestBlock(p);

			/**
			 * piece is already finished.. will be cleaned next acquirePieces()
			 * call
			 */
			if (br == null)
				continue;

			/** if we haven't already requested this piece, do it */
			if (!sentList.contains(br))
				sendBlockRequest(br);
		}
	}

	/**
	 * remove dead pieces and add new pieces
	 * 
	 */
	private void managePieces()
	{
		List<Integer> removeList = new ArrayList<Integer>();

		/** remove dead pieces */
		for (Integer p : workPieces)
		{
			if (tm.requestBlock(p) == null)
				removeList.add(p);
		}

		workPieces.removeAll(removeList);

		/** add new pieces */
		int index = usefulPieces.firstSet(tm.getIndex());

		while (index > -1 && workPieces.size() < tm.getQueueSize())
		{
			if (!workPieces.contains(index))
				workPieces.add(index);

			if (index == tm.numOfPieces() - 1)
				index = -1;
			else
				index = usefulPieces.firstSet(index + 1);
		}
	}

	/**
	 * creates a Bitfield with bits set for pieces we want from the peer
	 * 
	 * @param msg
	 * @return Bitfield with bit the peer has that we need set
	 */
	private Bitfield findUsefulPieces(byte[] msg) throws PeerDeadException
	{
		try
		{
			if (msg.length != tm.getPieces().length)
				throw new MalformedBitfieldException("Incorrent length");

			byte[] have = tm.getPieces();
			/**
			 * bitwise add the have bitfield and this one, then xor them
			 */
			for (int i = 0; i < msg.length; ++i)
				msg[i] ^= msg[i] & have[i];

			if (DEBUG)
			{
				System.out.println("Peer has some pieces we want: " + new String(msg));
			}

			return new Bitfield(msg, tm.numOfPieces());
		}
		catch (MalformedBitfieldException e)
		{
			throw new PeerDeadException("Bad bitfield: " + e.getMessage());
		}
	}

	/** ********************** send messages ****************************** */

	/**
	 * send keep-alive
	 * 
	 * @throws PeerDeadException
	 */
	private void sendKeepAlive() throws PeerDeadException
	{
		byte[] msg = new byte[4];
		NetUtils.intToBytes(0, msg, 0);

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			throw new PeerDeadException("IOException on sendKeepAlive(): " + e.getMessage());
		}

		timeout.sentMsg();
	}

	/**
	 * send choke message
	 * 
	 * @param choke
	 *            true for choke, false for unchoke
	 * @throws PeerDeadException
	 */
	private void sendChoke(boolean choke) throws PeerDeadException
	{
		/** peer already knows */
		if (amChoking == choke)
			return;

		amChoking = choke;

		byte[] msg = new byte[5];
		NetUtils.intToBytes(1, msg, 0);

		/* 0 if choking */
		msg[4] = (byte) (choke ? 0 : 1);

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			throw new PeerDeadException("IOException on sendChoke(): " + e.getMessage());
		}

		timeout.sentMsg();
	}

	/**
	 * send bitfield message
	 * 
	 * @throws PeerDeadException
	 */
	private void sendBitfield() throws PeerDeadException
	{
		/** total length of message */
		int length = 1 + tm.getPieces().length;

		/** include 4 bytes length prefix */
		byte[] msg = new byte[length + 4];

		NetUtils.intToBytes(length, msg, 0);

		/** bitfield message id */
		msg[4] = 5;

		System.arraycopy(tm.getPieces(), 0, msg, 5, length - 1);

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			throw new PeerDeadException("couldn't send bitfield message: " + e.getMessage());
		}

		timeout.sentMsg();
	}

	/**
	 * send interested message
	 * 
	 * @param interested
	 *            true for interested, false for not interested
	 */
	private void sendInterested(boolean interested) throws PeerDeadException
	{
		/** peer already knows */
		if (interested == amInterested)
			return;

		amInterested = interested;

		byte[] msg = new byte[5];
		NetUtils.intToBytes(1, msg, 0);

		/* 2 if interested, 3 if not */
		msg[4] = (byte) (interested ? 2 : 3);

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			throw new PeerDeadException("IOException on sendInterested(): " + e.getMessage());
		}

		timeout.sentMsg();
		return;
	}

	/**
	 * Torrent calls this on all Peers when it verifies a piece. send have
	 * message
	 * 
	 * No checking is done on this index. The peer will probably drop the
	 * connection on a bogus index.
	 * 
	 * @param index
	 *            of just found piece
	 * @throws IOException
	 */
	protected void sendHave(int index)
	{
		usefulPieces.unset(index);
		byte[] msg = new byte[9];
		NetUtils.intToBytes(5, msg, 0);

		msg[4] = 4;

		NetUtils.intToBytes(index, msg, 5);

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			if (DEBUG)
				System.out.println("Caught badness during sendHave() propagation. Will be handled later.");
		}

		timeout.sentMsg();
	}

	/**
	 * send block request message and add the BlockRequest to wantList
	 * 
	 * @param b
	 *            BlockRequest to request
	 * @throws PeerDeadException
	 */
	private void sendBlockRequest(BlockRequest b) throws PeerDeadException
	{
		byte[] msg = new byte[17];
		NetUtils.intToBytes(13, msg, 0);

		msg[4] = (byte) 6;
		NetUtils.intToBytes(b.getIndex(), msg, 5);
		NetUtils.intToBytes(b.getOffset(), msg, 9);
		NetUtils.intToBytes(b.getLength(), msg, 13);

		try
		{
			out.write(msg);
			if (DEBUG_MSG)
				System.out.println("Sending block request: " + b);

			sentList.add(b);
		}
		catch (IOException e)
		{
			throw new PeerDeadException("IOException on sendBlockRequest(): " + e.getMessage());
		}

		timeout.sentMsg();
	}

	/**
	 * Send a block to a peer
	 * 
	 * @param BlockRequest
	 *            to send to peer
	 * @return true on success
	 */
	private boolean sendBlock(BlockRequest b) throws PeerDeadException
	{
		if (!tm.havePiece(b.getIndex()))
			throw new PeerDeadException("Peer requested piece we don't have!");

		byte[] data = null;
		try
		{
			data = tm.getBlock(b);
		}
		catch (IOException e1)
		{
			if (Util.DEBUG)
				e1.printStackTrace();
			return false;
		}
		/** block size is validated by BlockRequest constructor */
		byte[] msg = new byte[13 + data.length];

		NetUtils.intToBytes(9 + data.length, msg, 0);
		msg[4] = (byte) 7;
		NetUtils.intToBytes(b.getIndex(), msg, 5);
		NetUtils.intToBytes(b.getOffset(), msg, 9);

		System.arraycopy(data, 0, msg, 13, data.length);

		if (DEBUG_MSG)
			System.out.println("Sending block: " + b);
		try
		{
			out.write(msg);

			/** sent some bytes */
			tm.addUploaded(b.getLength());
		}
		catch (IOException e)
		{
			throw new PeerDeadException("IOException on sendBlock(): " + e.getMessage());
		}

		++sent;
		return true;
	}

	/**
	 * cancel a block request message and remove the BlockRequest from wantList **
	 * 
	 * @param b
	 *            BlockRequest to cancel
	 */
	protected void sendCancelRequest(BlockRequest b)
	{
		/** if we didn't send a request, don't worry about it */
		if (!sentList.contains(b))
			return;

		sentList.remove(b);

		byte[] msg = new byte[17];
		NetUtils.intToBytes(13, msg, 0);

		msg[4] = (byte) 6;
		NetUtils.intToBytes(b.getIndex(), msg, 5);
		NetUtils.intToBytes(b.getOffset(), msg, 9);
		NetUtils.intToBytes(b.getLength(), msg, 13);

		try
		{
			out.write(msg);
		}
		catch (IOException e)
		{
			if (DEBUG)
				System.out.println("Caught badness during a sendCancelRequest(). Will be handled later.");
		}

		timeout.sentMsg();
	}

	/**
	 * inform this Peer that the party is over and close nicely
	 * 
	 * TODO: clean this up
	 * 
	 * No other calls should be made after close()
	 * 
	 */
	protected void close()
	{
		/** tell the thread to quit */
		running = false;

		try
		{
			in.close();
		}
		catch (IOException e)
		{
			// don't care
		}
		out.close();
	}

	/**
	 * @param targetDownload
	 * @see edu.iastate.cs309.torrentManager.socketMeter.ThrottledSocket#setTargetDownload(int)
	 */
	public void setTargetDownload(int targetDownload)
	{
		s.setTargetDownload(targetDownload);
	}

	/**
	 * @param targetUpload
	 * @see edu.iastate.cs309.torrentManager.socketMeter.ThrottledSocket#setTargetUpload(int)
	 */
	public void setTargetUpload(int targetUpload)
	{
		s.setTargetUpload(targetUpload);
	}

	/**
	 * get the the thread this peer is running on (to join() )
	 * 
	 * @return thread the Peer is running on
	 */
	public Thread getThread()
	{
		return thread;
	}
}
/** end of Peer.java */
