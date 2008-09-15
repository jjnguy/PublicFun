package edu.iastate.cs309.torrentManager.containers;

import edu.iastate.cs309.torrentparser.ParseException;

public class PeerID extends ByteID
{
	public PeerID(byte[] b)
	{
		super(b);
	}
	
	public PeerID()
	{
		super();
	}

}
