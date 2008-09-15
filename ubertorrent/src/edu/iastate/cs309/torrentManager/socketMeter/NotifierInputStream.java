/**
 * 
 */
package edu.iastate.cs309.torrentManager.socketMeter;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;



/**
 * 
 * 
 * @author Michael Seibert
 */
public class NotifierInputStream extends FilterInputStream
{

	private final ThrottledSocket throttledSocket;

	/**
	 * @see java.io.FilterInputStream#read()
	 */
	@Override
	public int read() throws IOException
	{
		int result = super.read();
		throttledSocket.updateDownloadSpeed();
		return result;
	}

	/**
	 * @see java.io.FilterInputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		int result = super.read(b, off, len);
		throttledSocket.updateDownloadSpeed();
		return result;
	}

	/**
	 * @see java.io.FilterInputStream#read(byte[])
	 */
	@Override
	public int read(byte[] b) throws IOException
	{
		int result = super.read(b);
		throttledSocket.updateDownloadSpeed();
		return result;
	}

	/**
	 * @param in
	 * @param throttledSocket
	 */
	protected NotifierInputStream(InputStream in, ThrottledSocket throttledSocket)
	{
		super(in);
		this.throttledSocket = throttledSocket;
	}

}
