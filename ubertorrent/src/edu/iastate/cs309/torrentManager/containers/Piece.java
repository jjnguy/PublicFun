package edu.iastate.cs309.torrentManager.containers;

import edu.iastate.cs309.torrentManager.Torrent;
import edu.iastate.cs309.util.Util;

/**
 * Simple representation of an incomplete Piece
 * 
 * Repeated calls to getBlockRequest() return sequential needed blocks. If only
 * one block is needed, repeated calls will return the same single remaining *
 * Limitations (and possible extensions): request block() only gives the next
 * 16kb of data (could give different needed blocks for multiple requests) block
 * (potential source of cancelBlockRequest() calls in a Peer object).
 * 
 * @author sralmai
 * 
 */
public class Piece
{
	/** piece index (in entire torrent) */
	private int index = 0;

	/** block counter (used to keep track of requests) */
	private int block = 0;

	/** total piece length */
	private int finalLength = 0;

	/** list of blocks */
	Bitfield blocks = null;

	/** torrent callback */
	private Torrent torr = null;

	/** Max request piece size */
	public final static int maxSize = 1 << 16;

	/**
	 * 
	 * @param index
	 *            piece to manage
	 * @param t
	 *            callback to the owner torrent
	 */
	public Piece(int index, Torrent t)
	{
		/** figure out piece length */
		if (index == t.numOfPieces() - 1)
		{
			finalLength = (int) t.lastPieceLength();
		}
		else
		{
			finalLength = (int) t.pieceLength();
		}

		this.index = index;
		torr = t;

		/** $#*$ing integer division */
		blocks = new Bitfield((int) Math.ceil((finalLength * 1.0) / maxSize));
	}

	/**
	 * 
	 * @return BlockRequest of the next chunk this Piece needs
	 * 
	 * 16kb size until last piece
	 * 
	 */
	synchronized public BlockRequest getBlockRequest()
	{
		int reqLength = 0;
		int thisBlock = block;

		if (block == blocks.getSize() - 1)
		{
			reqLength = (finalLength % maxSize);
			if (reqLength == 0)
				reqLength = maxSize;
		}
		else
		{
			reqLength = maxSize;
		}

		/** loop requests */
		block = (block + 1) % blocks.getSize();

		while (blocks.isSet(block) && block != thisBlock)
			block = (block + 1) % blocks.getSize();

		return new BlockRequest(torr, index, thisBlock * maxSize, reqLength);
	}

	/**
	 * Add information to this piece.
	 * 
	 * XXX: this is a stupid implementation
	 * 
	 * @param data
	 *            information to add
	 * @param offset
	 *            offset in piece
	 * @return true if Piece is complete after this add
	 */
	synchronized public boolean addData(int offset, byte[] data)
	{
		/** if this isn't exactly where we want it, ignore it */
		if (offset % maxSize != 0 || offset + data.length > finalLength)
		{
			if (Util.DEBUG)
				System.out.println("Ignoring bad addData()");
			return false;
		}

		/** XXX: should check length */

		if (!blocks.isSet(offset / maxSize))
		{
			/** got useful stuff */
			torr.addDownloaded(data.length);

			torr.writeBytes(index * torr.pieceLength() + offset, data);
			blocks.set(offset / maxSize);
		}
		else
		{
			if (Util.DEBUG)
				System.out.println("Ignoring block we already have");
		}

		if (Util.DEBUG)
			System.out.println("got block #" + (1 + offset / maxSize) + " of " + blocks.getSize() + " for piece " + index + "\n\t" + (blocks.getSize() - blocks.totalSet()) + " left.");

		return (blocks.totalSet() == blocks.getSize());
	}

	/**
	 * return the index this Piece represents
	 * 
	 * @return integer piece
	 */
	public int getIndex()
	{
		return index;
	}
}
