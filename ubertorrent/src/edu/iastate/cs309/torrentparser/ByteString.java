/**
 * 
 */
package edu.iastate.cs309.torrentparser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @author Michael Seibert
 */
public class ByteString extends BEncodedObject
{
	private byte[] data;

	/**
	 * Create a new {@link ByteString} from the given data.
	 * 
	 * @param data
	 *            Data to put in this ByteString
	 */
	public ByteString(byte[] data)
	{
		this.data = data;
	}

	/**
	 * Convenience method for {@link String}->{@link ByteString}
	 * 
	 * @param data
	 *            Data to put in this ByteString
	 */
	public ByteString(String data)
	{
		try
		{
			this.data = data.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
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
		final ByteString other = (ByteString) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new String(data);
	}

	/**
	 * Get the data from this byte string
	 * 
	 * @return The data from this byte string
	 */
	public byte[] get()
	{
		return data;
	}

	/**
	 * @see edu.iastate.cs309.torrentparser.BEncodedObject#bEncode(java.io.OutputStream)
	 */
	@Override
	public void bEncode(OutputStream target) throws IOException
	{
		target.write(Integer.toString(data.length).getBytes("UTF-8"));
		target.write(':');
		target.write(data);
	}
}
