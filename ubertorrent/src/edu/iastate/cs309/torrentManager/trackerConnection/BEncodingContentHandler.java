package edu.iastate.cs309.torrentManager.trackerConnection;

import java.io.IOException;
import java.io.InputStream;
import java.net.ContentHandler;
import java.net.URLConnection;

import edu.iastate.cs309.torrentparser.BEncodedObject;
import edu.iastate.cs309.torrentparser.ParseException;
import edu.iastate.cs309.util.Util;

/**
 * Content handler for trackers
 * 
 * Trackers give all responses as BEncoded objects (specifically, Dictionaries
 * @author sralmai
 *
 */
public class BEncodingContentHandler extends ContentHandler
{

	/**
	 * read in a bencoded object from a website (a tracker),
	 * @param arg0 
	 * 
	 * @return BEncodedObject on success, or null if URLConnection data could
	 *         not be parsed as a BEncodedObject
	 * @throws IOException
	 *             on read error
	 */
	public BEncodedObject getContent(URLConnection arg0) throws IOException
	{
		InputStream in = arg0.getInputStream();
		try
		{
			return BEncodedObject.readObject(in);
		}
		catch (ParseException e)
		{
			if (Util.DEBUG)
			{
				System.err.println("URLConnection gave junk information!");
			}
			return null;
		}
	}
}
