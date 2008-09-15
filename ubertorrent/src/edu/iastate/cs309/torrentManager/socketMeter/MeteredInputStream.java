/**
 * 
 */
package edu.iastate.cs309.torrentManager.socketMeter;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class to monitor the amount of data being read out of an InputStream.
 * 
 * Note that this class does NOT support a mark/reset operation very well (the
 * data will get counted as being read twice)
 * 
 * @author Michael Seibert
 */
public class MeteredInputStream extends FilterInputStream
{
	public static final double NANOS_PER_SECOND = 1000000000;

	public static final double SMOOTHNESS = .9;

	private long bytesRead = 0;

	private long lastUpdateTime = System.nanoTime();

	private int prevSpeed = -1;

	/**
	 * @param in
	 */
	public MeteredInputStream(InputStream in)
	{
		super(in);
	}

	/**
	 * This method does rudimentary smoothing, but isn't very smart about it.
	 * 
	 * @return The speed in bytes/second since the last call to this method.
	 */
	public int speed()
	{
		long delta = System.nanoTime() - lastUpdateTime;
		lastUpdateTime = System.nanoTime();

		if (prevSpeed == -1)
			prevSpeed = (int) (bytesRead / (delta / NANOS_PER_SECOND));
		else
			prevSpeed = (int) (bytesRead / (delta / NANOS_PER_SECOND) * (1 - SMOOTHNESS) + prevSpeed * SMOOTHNESS);

		bytesRead = 0;
		return prevSpeed;
	}

	/**
	 * @return The number of nanoseconds since the last update to the speed.
	 */
	public long timeSinceLastUpdate()
	{
		return System.nanoTime() - lastUpdateTime;
	}

	/**
	 * @see java.io.FilterInputStream#read()
	 */
	@Override
	public int read() throws IOException
	{
		bytesRead++;
		return in.read();
	}

	/**
	 * @see java.io.FilterInputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		int lenRead = in.read(b, off, len);
		bytesRead += lenRead;
		return lenRead;
	}

	/**
	 * @see java.io.FilterInputStream#read(byte[])
	 */
	@Override
	public int read(byte[] b) throws IOException
	{
		int lenRead = in.read(b);
		bytesRead += lenRead;
		return lenRead;
	}

	/**
	 * @see java.io.FilterInputStream#skip(long)
	 */
	@Override
	public long skip(long n) throws IOException
	{
		long lenSkipped = in.skip(n);
		bytesRead += lenSkipped;
		return lenSkipped;
	}
}
