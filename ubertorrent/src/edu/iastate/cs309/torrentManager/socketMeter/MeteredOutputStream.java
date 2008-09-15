/**
 * 
 */
package edu.iastate.cs309.torrentManager.socketMeter;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Class to monitor the amount of data being read out of an InputStream.
 * 
 * Note that this class does NOT support a mark/reset operation very well (the
 * data will get counted as being read twice)
 * 
 * @author Michael Seibert
 */
public class MeteredOutputStream extends FilterOutputStream
{
	public static final double NANOS_PER_SECOND = 1000000000;

	public static final double SMOOTHNESS = .9;

	private long bytesWritten = 0;

	private long lastUpdateTime = System.nanoTime();

	private int prevSpeed = -1;

	/**
	 * @param in
	 */
	public MeteredOutputStream(OutputStream in)
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
			prevSpeed = (int) (bytesWritten / (delta / NANOS_PER_SECOND));
		else
			prevSpeed = (int) (bytesWritten / (delta / NANOS_PER_SECOND) * (1 - SMOOTHNESS) + prevSpeed * SMOOTHNESS);

		bytesWritten = 0;
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
	 * @see java.io.FilterOutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		bytesWritten += len;
		out.write(b, off, len);
	}

	/**
	 * @see java.io.FilterOutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException
	{
		bytesWritten += b.length;
		out.write(b);
	}

	/**
	 * @see java.io.FilterOutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException
	{
		bytesWritten++;
		out.write(b);
	}
}
