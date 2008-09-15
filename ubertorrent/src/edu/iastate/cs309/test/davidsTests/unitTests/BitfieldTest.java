package edu.iastate.cs309.test.davidsTests.unitTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.iastate.cs309.torrentManager.containers.Bitfield;
import edu.iastate.cs309.torrentManager.exceptions.MalformedBitfieldException;

/**
 * sanity checks for Bitfield
 * 
 * @author sralmai
 * 
 */
public class BitfieldTest
{
	/** the bitfield to play with */
	Bitfield b = null;

	/**
	 * ensure Bitfield(int) throws up on bad values
	 * 
	 */
	@Test
	public void testBitfieldInt()
	{
		boolean try1 = false;
		try
		{
			/** too low */
			b = new Bitfield(0);
		}
		catch (IllegalArgumentException e)
		{
			try1 = true;
		}

		try
		{
			/** decent value */
			b = new Bitfield(4);
		}
		catch (IllegalArgumentException e)
		{
			fail("A good bitfield died!");
		}

		assertTrue(try1);
	}

	/**
	 * ensure new bitfields from arrays are good
	 * 
	 * 
	 */
	@Test
	public void testBitfieldByteArrayInt()
	{
		boolean try1 = false;
		boolean try2 = false;
		boolean try3 = false;

		byte[] small = new byte[8];

		try
		{
			/** too low */
			b = new Bitfield(small, 0);
		}
		catch (MalformedBitfieldException e)
		{
			try1 = true;
		}

		try
		{
			/** too high */
			b = new Bitfield(small, 65);
		}
		catch (MalformedBitfieldException e)
		{
			try2 = true;
		}

		try
		{
			/** decent value */
			b = new Bitfield(small, 33);
		}
		catch (MalformedBitfieldException e)
		{
			fail("A good bitfield died!");
		}

		try
		{
			/** dirty up the high bits */
			small[5] = 0x21;
			b = new Bitfield(small, 33);
		}
		catch (MalformedBitfieldException e)
		{
			try3 = true;
		}

		assertTrue(try1);
		assertTrue(try2);
		assertTrue(try3);
	}

	/** TODO: finish this */
	@Test
	public void testSet()
	{
		/** bitfield is single byte length */
		b = new Bitfield(4);

		b.set(2);

		byte[] raw = b.getBytes();

		assertTrue(raw.length == 1);
		assertTrue(raw[0] == 0x20);
	}

	@Test
	public void testUnset()
	{
		b = new Bitfield(234);
		b.set(0);
		b.set(1);
		b.set(2);

		b.unset(1);

		byte[] raw = b.getBytes();

		assertTrue((0xff & raw[0]) == 0xA0);

	}

	@Test
	public void testIsSet()
	{
		b = new Bitfield(2);
		b.set(0);
		b.unset(1);

		assertFalse(b.isSet(1));
		assertTrue(b.isSet(0));
	}

	@Test
	public void testGetSize()
	{
		/** from int constructor */
		try
		{
			b = new Bitfield(5);
		}
		catch (IllegalArgumentException e)
		{
			fail("couldnt make a bitfield by int");
		}

		assertTrue(b.getSize() == 5);

		/** from byte[] int constructor */
		try
		{
			b = new Bitfield(new byte[1], 5);
		}
		catch (MalformedBitfieldException e)
		{
			fail("couldnt make a bitfield by byte array");
		}

		assertTrue(b.getSize() == 5);

	}

	@Test
	public void testTotalSet()
	{
		byte[] array = new byte[10];

		array[0] = (byte) 0xff; // 8 bits
		array[1] = (byte) 0xfe; // 7 bits
		array[9] = (byte) 0x0a; // 2 bits

		try
		{
			b = new Bitfield(array, 80);
		}
		catch (MalformedBitfieldException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}

		assertTrue(b.totalSet() == 17);
	}

	@Test
	public void testFirstSet()
	{
		b = new Bitfield(3);
		assertTrue(b.firstSet(0) == -1);
		assertTrue(b.firstSet(1) == -1);
		assertTrue(b.firstSet(2) == -1);

		byte[] array = new byte[10];

		array[0] = (byte) 0xff; // 8 bits 11111111
		array[1] = (byte) 0xfe; // 7 bits 11111110
		array[9] = (byte) 0x0a; // 2 bits 00001010

		try
		{
			b = new Bitfield(array, 80);
		}
		catch (MalformedBitfieldException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}

		assertTrue(b.firstSet(0) == 0);
		assertTrue(b.firstSet(1) == 1);
		assertTrue(b.firstSet(8) == 8);
		assertTrue(b.firstSet(14) == 14);
		assertTrue(b.firstSet(15) == 76);
		assertTrue(b.firstSet(69) == 76);
		assertTrue(b.firstSet(75) == 76);
		assertTrue(b.firstSet(76) == 76);
		assertTrue(b.firstSet(77) == 78);

	}
}
