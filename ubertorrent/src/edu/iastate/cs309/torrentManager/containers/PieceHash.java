package edu.iastate.cs309.torrentManager.containers;


/**
 * Container for SHA1 hash of a piece
 * @author sralmai
 *
 */
public class PieceHash extends ByteID
{
	public PieceHash(byte[] b)
	{
		super(b);
	}
}
