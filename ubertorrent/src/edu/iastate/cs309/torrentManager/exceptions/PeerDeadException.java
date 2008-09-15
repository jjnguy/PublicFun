package edu.iastate.cs309.torrentManager.exceptions;

/**
 * thrown whenever a peer connection dies breaks protocol (and should be
 * dropped)
 */
public class PeerDeadException extends Exception
{
	public PeerDeadException(String s)
	{
		super(s);
	}

	public PeerDeadException()
	{
		// left blank
	}
}
