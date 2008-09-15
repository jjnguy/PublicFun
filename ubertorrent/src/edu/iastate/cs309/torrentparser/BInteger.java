/**
 * 
 */
package edu.iastate.cs309.torrentparser;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Michael Seibert
 */
public class BInteger extends BEncodedObject
{
	private final long theInt;

	/**
	 * Create a {@link BInteger}
	 * 
	 * @param arg
	 *            The integer within this {@link BInteger}
	 */
	public BInteger(long arg)
	{
		theInt = arg;
	}

	/**
	 * @return The integer within this {@link BInteger}
	 */
	public long get()
	{
		return theInt;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (theInt ^ (theInt >>> 32));
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BInteger other = (BInteger) obj;
		if (theInt != other.theInt)
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return Long.toString(theInt);
	}

	/**
	 * @see edu.iastate.cs309.torrentparser.BEncodedObject#bEncode(java.io.OutputStream)
	 */
	@Override
	public void bEncode(OutputStream target) throws IOException
	{
		target.write('i');
		target.write(Long.toString(theInt).getBytes("ASCII"));
		target.write('e');
	}
}
