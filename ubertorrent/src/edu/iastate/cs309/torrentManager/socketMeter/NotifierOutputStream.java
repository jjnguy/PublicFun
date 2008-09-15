/**
 * 
 */
package edu.iastate.cs309.torrentManager.socketMeter;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;



/**
 * 
 * 
 * @author Michael Seibert
 */
public class NotifierOutputStream extends FilterOutputStream
{

	private final ThrottledSocket throttledSocket;

	/**
	 * @see java.io.FilterInputStream#read()
	 */
	@Override
	public void write(int b) throws IOException
	{
		super.write(b);
		throttledSocket.updateUploadSpeed();
	}

	/**
	 * @see java.io.FilterInputStream#read(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		super.write(b, off, len);
		throttledSocket.updateUploadSpeed();
	}

	/**
	 * @see java.io.FilterInputStream#read(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException
	{
		super.write(b);
		throttledSocket.updateUploadSpeed();
	}

	/**
	 * @param in
	 * @param throttledSocket
	 */
	protected NotifierOutputStream(OutputStream in, ThrottledSocket throttledSocket)
	{
		super(in);
		this.throttledSocket = throttledSocket;
	}

}
