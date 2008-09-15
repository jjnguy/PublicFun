package edu.iastate.cs309.test.davidsTests.unitTests;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import edu.iastate.cs309.clientserverprotocol.NetUtils;
import edu.iastate.cs309.communication.TorrentInfo;
import edu.iastate.cs309.torrentparser.ParseException;

/**
 * Ensure home-brew serialization works
 * 
 * @author sralmai
 * 
 */
public class TorrentInfoTest
{
	TorrentInfo t1, t2;

	@Test
	public void testEquals()
	{
		t1 = new TorrentInfo();
		t2 = new TorrentInfo();

		assertTrue(t1.equals(t2));

		t1.setBytesDownloaded(3342);
		t2.setBytesDownloaded(3232);

		assertTrue(!t1.equals(t2));
	}

	@Test
	public void testToBytes()
	{
		t1 = new TorrentInfo();
		byte[] raw = t1.toBytes();

		int magicOffset = NetUtils.bytesToInt(raw, 20);

		int lengthOfLastChunk = NetUtils.bytesToInt(raw, magicOffset);

		assertTrue("magicOffset: " + magicOffset + "last chunk: " + lengthOfLastChunk + "raw length: " + raw.length, (lengthOfLastChunk + 4 + magicOffset) == raw.length);
	}

	@Test
	public void testFromBytes()
	{
		t1 = new TorrentInfo();

		t1.setNumPieces(1);

		System.out.println("t1.toBytes");
		byte[] raw = t1.toBytes();

		t2 = new TorrentInfo();
		try
		{
			System.out.println("t2.fromBytes(raw)");
			t2.fromBytes(raw);
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		assertTrue(Arrays.equals(t1.toBytes(), t2.toBytes()));
		assertTrue("" + t2.getNumPieces(), t1.equals(t2));
	}
}
