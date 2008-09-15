package edu.iastate.cs309.torrentManager.containers;

import java.util.Arrays;

/**
 * framework for the several 20 byte data hunks in the BitTorrent spec
 * 
 * @author sralmai
 * 
 */
public abstract class ByteID
{
	/** internal representation */
	private byte[] data;

	/**
	 * wrapper object for 20 byte things, which are a big deal in the bittorrent
	 * protocol
	 * 
	 * @param b
	 *            a byte array with length of 20
	 */
	protected ByteID(final byte[] b)
	{
		if (b.length != 20)
			throw new IllegalArgumentException("ByteID subclasses only take byte arrays of length 20!");

		data = new byte[20];
		System.arraycopy(b, 0, data, 0, 20);
	}

	/**
	 * create a zeroed 20 byte id
	 * 
	 */
	protected ByteID()
	{
		data = new byte[20];
	}

	/**
	 * Override
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
	 * Override
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
		final ByteID other = (ByteID) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}

	/**
	 * 
	 * @return a copy of the internal representation
	 */
	public byte[] getBytes()
	{
		byte[] copy = new byte[data.length];
		System.arraycopy(data, 0, copy, 0, data.length);
		return copy;
	}

	/**
	 * TODO David, tell us why this is here... and why you are doing this!!
	 * 
	 * @return a URLEncoded string of this type
	 * 
	 * (thank you java for having UNSIGNED TYPES and LOGICAL ENCODING)
	 */
	public String toURLEncodedString()
	{
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < data.length; ++i)
		{
			int curr = (0xff & data[i]);

			// UTF8 upper case alphas and lower case alphas
			if ((curr > 0x40 && curr < 0x5b) || (curr > 0x60 && curr < 0x7b))
			{
				s.append((char) (curr));
			}
			// the other 4 unescaped characters ( ~, _, ., - )
			else if (curr == 0x7e || curr == 0x5f || curr == 0x2e || curr == 0x2d)
			{
				s.append((char) (curr));
			}
			else if (curr > 15)
			{
				s.append("%" + Integer.toHexString(curr).toUpperCase());
			}
			else
			{
				/** these zeroes matter (WireShark + Azereus = all knowledge) */
				s.append("%0" + Integer.toHexString(curr).toUpperCase());
			}
		}

		return s.toString();
	}

	/**
	 * print out the string as hex
	 * 
	 * @return returned value
	 */
	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();

		for (int i = 0; i < data.length; ++i)
		{
			s.append(Integer.toHexString(0xff & (data[i])));
		}

		return s.toString();
	}
}
