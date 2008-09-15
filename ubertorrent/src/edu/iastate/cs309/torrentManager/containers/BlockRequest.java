package edu.iastate.cs309.torrentManager.containers;

import edu.iastate.cs309.torrentManager.Torrent;
import edu.iastate.cs309.torrentManager.exceptions.PeerDeadException;

/**
 * encapsulates a single request for a part of a piece
 * 
 * according to the bittorrent spec, (There is tons of problems) 32kB (2^15) is
 * the max request size, and 16kB (2^14) is the default. 2^17 seems to be the
 * "usage" standard, it is defined in Piece.maxSize. Requesting large sizes
 * drops the connection (throws a PeerDeadException).
 * 
 * @author sralmai
 * @author kc0dhb
 */
public class BlockRequest
{
	
	/** piece index of request */
	private int index = 0;
	
	/** offset within piece */
	private int offset = 0;
	
	/** length of request */
	private int length = 0;

	/**
	 * 
	 * @param torr
	 *            parent Torrent (used for validation)
	 * @param index
	 * @param offset
	 * @param length
	 */
	public BlockRequest(Torrent torr, int index, int offset, int length)
	{
		if (length > Piece.maxSize)
			throw new IndexOutOfBoundsException();

		if (index > torr.numOfPieces() - 1)
			throw new IndexOutOfBoundsException();

		if (offset + length > torr.pieceLength())
			throw new IndexOutOfBoundsException();

		this.index = index;
		this.offset = offset;
		this.length = length;
	}

	/**
	 * Auto-generated
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + length;
		result = prime * result + offset;
		return result;
	}

	/**
	 * Auto-generated
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BlockRequest other = (BlockRequest) obj;
		if (index != other.index)
			return false;
		if (length != other.length)
			return false;
		if (offset != other.offset)
			return false;
		return true;
	}

	/**
	 * print out the block request real nice
	 */
	@Override
	public String toString()
	{
		return "index: " + index + "\toffset: " + offset + "\tlength: " + length;
	}

	/**
	 * @return the index
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * @return the offset
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * @return the length
	 */
	public int getLength()
	{
		return length;
	}

}
