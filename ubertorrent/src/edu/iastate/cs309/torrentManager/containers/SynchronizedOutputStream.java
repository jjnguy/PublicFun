package edu.iastate.cs309.torrentManager.containers;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Reentrant wrapper for OutputStream
 * @author sralmai
 */
public class SynchronizedOutputStream
{
	/** actual output stream */
	OutputStream out = null;
	
	/**
	 * 
	 * @param out OutputStream to box
	 */
	public SynchronizedOutputStream( OutputStream out)
	{
		this.out = out;
	}
	
	/**
	 * A synchronized pass-through call to OutputStream.write(byte[])
	 * @param msg
	 * @throws IOException
	 */
	public synchronized void write(byte[] msg) throws IOException
	{
			out.write(msg);
	}
	
	/**
	 * Close the underlying connection.
	 *
	 */
	public void close()
	{
		try
		{
			out.close();
		}
		catch (IOException e)
		{
			// don't care
		}
	}
}