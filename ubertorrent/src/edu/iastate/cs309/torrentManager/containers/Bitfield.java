package edu.iastate.cs309.torrentManager.containers;

import edu.iastate.cs309.torrentManager.exceptions.MalformedBitfieldException;
import edu.iastate.cs309.util.Util;

/**
 * wrapper for Bitfields used in Bittorrent protocol
 * 
 * 
 * 
 * @author sralmai
 * 
 */
public class Bitfield
{
	/** internal data */
	byte[] data = null;

	/** number pieces we actually count */
	int size = 0;

	/** number of 1 bits */
	int count = 0;

	/**
	 * Create a blank Bitfield
	 * 
	 * @param size
	 *            integer between 1 and MAX_INTEGER
	 */
	public Bitfield(int size)
	{
		if (size < 1)
			throw new IllegalArgumentException("Bitfield must have a positive size (passed " + size + ")");
		this.size = size;

		/** default value for bytes is 0, so array is zeroed */
		data = new byte[(int) Math.ceil(size / 8.0)];
	}

	/**
	 * 
	 * @param data
	 *            byte array to use as bitfield
	 * @param size
	 *            number of bits in the byte array that matter
	 * @throws MalformedBitfieldException
	 */
	public Bitfield(byte[] data, int size) throws MalformedBitfieldException
	{
		if (size > data.length * 8 || size < 1)
			throw new MalformedBitfieldException("byte array too small or size too small");

		this.data = new byte[data.length];
		System.arraycopy(data, 0, this.data, 0, data.length);

		this.size = size;

		/**
		 * Make sure that every bit after size is zero a.k.a. check for a dirty
		 * pad
		 */
		for (int i = size; i < data.length * 8; i++)
		{
			if ((data[i >> 3] >> (7 - (i % 8)) & 0x01) != 0)
			{
				if (Util.DEBUG)
				{
					System.out.println("size is " + size + " and i is " + i + " and i % 8 is " + (i % 8) + " and data[i >> 3] is " + Integer.toBinaryString(data[i >> 3]));
					throw new MalformedBitfieldException("Dirty pad in new Bitfield!");
				}
			}
		}

		/** count number of flipped bits */
		for (int i = 0; i < size; ++i)
		{
			if (isSet(i))
				++count;
		}
	}

	/**
	 * toggle a bit to '1'
	 * 
	 * repeated calls do nothing
	 * 
	 * @param piece
	 * @return true on success
	 */
	public boolean set(int piece)
	{

		if (piece > (size - 1))
			throw new IndexOutOfBoundsException();

		/** are we changing a bit? */
		if (!isSet(piece))
		{
			++count;
			data[piece >> 3] |= 0x1 << (7 - (piece % 8));
		}
		return true;
	}

	/**
	 * toggle a bit to '0'
	 * 
	 * repeated calls do nothing
	 * 
	 * @param piece
	 */
	public void unset(int piece)
	{

		if (piece > (size - 1))
			throw new IndexOutOfBoundsException();

		if (isSet(piece))
		{
			--count;
			data[piece >> 3] &= ~(0x1 << (7 - (piece % 8)));
		}
	}

	/**
	 * return true if the bit is '1'
	 * 
	 * @param piece
	 * @return true if piece is set
	 */
	public boolean isSet(int piece)
	{
		if (piece > (size - 1))
			throw new IndexOutOfBoundsException();

		return (data[piece >> 3] & (0x1 << (7 - (piece % 8)))) > 0;
	}

	/**
	 * 
	 * @return a copy of the internal byte array
	 */
	public byte[] getBytes()
	{
		byte[] retVal = new byte[data.length];
		System.arraycopy(data, 0, retVal, 0, data.length);

		return retVal;
	}

	/**
	 * 
	 * @return the size of this Bitfield
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 * ** this method assumes spare bits are cleared (it counts *ALL* bits) **
	 * 
	 * @return the number of bits that are '1'
	 */
	public int totalSet()
	{
		return count;
	}

	/**
	 * return the index of the first set bit equal to or to the right of the
	 * passed bit
	 * 
	 * @param start
	 *            bit to start searching on
	 * @return first bit index found equal to or to the right of start or -1 if
	 *         not found
	 */
	public int firstSet(int start)
	{
		if (start > (size - 1))
			throw new IndexOutOfBoundsException();

		if (count == 0)
			return -1;

		int val = start >> 3;

		int bitOffset = start % 8;

		if ((byte) (data[val] << bitOffset) == 0)
		{
			++val;
			bitOffset = 0;
			while (val < data.length && data[val] == 0)
				++val;
		}

		if (val >= data.length)
			return -1;

		/** there's a bit flipped somewhere in rel */
		byte rel = (byte) (data[val] << bitOffset);

		for (int i = bitOffset; i < 8; ++i)
		{
			/** check top byte */
			if ((rel & 0x80) != 0)
				return val * 8 + i;

			rel = (byte) (rel << 1);
		}
		return -1;
	}
}
