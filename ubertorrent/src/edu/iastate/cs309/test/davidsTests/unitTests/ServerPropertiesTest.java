/**
 * 
 */
package edu.iastate.cs309.test.davidsTests.unitTests;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import edu.iastate.cs309.communication.ServerProperties;
import edu.iastate.cs309.torrentparser.ParseException;

/**
 * @author sralmai
 *
 */
public class ServerPropertiesTest
{
	/**
	 * sanity check for equality
	 */
	@Test
	public void testEquals()
	{
		ServerProperties s = new ServerProperties();
		ServerProperties p = new ServerProperties();
		
		assertTrue( s.equals(p) );
	}

	/**
	 * Test method for {@link edu.iastate.cs309.communication.ServerProperties#toBytes()}.
	 */
//	@Test
//	public void testToBytes()
//	{
//		fail("Not implemented");
//	}

	/**
	 * Test method for {@link edu.iastate.cs309.communication.ServerProperties#fromBytes(byte[])}.
	 */
//	@Test
//	public void testFromBytes()
//	{
//		fail("Not yet implemented");
//	}
//	
	/**
	 * ensure unbox box works
	 */
	@Test
	public void testUnboxBox()
	{
		ServerProperties s = new ServerProperties();
		ServerProperties p = new ServerProperties();
		
		/** make them not equal, so we can see changes from readBytes() */
		p.setPort((short)32);
		
		/** check box/unbox with defaults */
		try
		{
			p.fromBytes(s.toBytes());
		}
		catch (UnsupportedEncodingException e)
		{
			fail(e.getMessage());
		}
		catch (ParseException e)
		{
			fail(e.getMessage());
		}
		assertTrue( s.equals(p));
	}

}
