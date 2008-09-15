package edu.iastate.cs309.torrentManager.trackerConnection;

import java.net.ContentHandler;
import java.net.ContentHandlerFactory;

import edu.iastate.cs309.util.Util;

/**
 * Return the BEncodingContentHandler always
 * @author sralmai
 *
 */
public class BEncodingContentHandlerFactory implements ContentHandlerFactory
{
	/**
	 * @param arg0 mimetype of the content (given by a URLStreamHandler
	 * @return a BEncodingContentHandler
	 */
	public ContentHandler createContentHandler(String arg0)
	{
		if(Util.DEBUG && (! arg0.matches("text/plain.*")))
		{
			System.err.println("Ignoring mimetype: "+arg0+" (should be \"text/plain\")");
		}
		return new BEncodingContentHandler();
	}
}
