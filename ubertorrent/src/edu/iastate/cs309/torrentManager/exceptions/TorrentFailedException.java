package edu.iastate.cs309.torrentManager.exceptions;

/**
 * thrown by a TorrentManager when it fails
 * 
 * @author sralmai
 */
public class TorrentFailedException extends Exception
{

	public TorrentFailedException(String string)
	{
		super(string);
	}

	public TorrentFailedException()
	{
		// left blank
	}

}
