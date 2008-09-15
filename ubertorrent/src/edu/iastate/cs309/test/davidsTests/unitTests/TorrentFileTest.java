package edu.iastate.cs309.test.davidsTests.unitTests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.iastate.cs309.torrentManager.TorrentFile;

/**
 * Run all methods on the test torrent in TorrentFiles
 * 
 * @author sralmai
 * 
 */
public class TorrentFileTest
{
	private static String torrent = "testTorrentFiles/secret.torrent";

	TorrentFile tf = null;

	/**
	 * Set up the TorrentFile
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		try
		{
			tf = new TorrentFile(torrent);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testGetAnnounce()
	{
		String correctValue = "http://open.tracker.thepiratebay.org:80/announce";

		assertTrue(tf.getAnnounce().equals(correctValue));
	}

	@Test
	public void testPieceLength()
	{
		int correct = 32768;
		assertTrue(tf.pieceLength() == correct);
	}

	@Test
	public void testOnlyPrivatePeers()
	{
		/** not set in the torrent */
		assertTrue(!tf.onlyPrivatePeers());
	}

	@Test
	public void testGetNumOfFiles()
	{
		/** 3 files */
		assertTrue(tf.getNumOfFiles() == 3);
	}

	@Test
	public void testIsSingleFile()
	{
		/** has 3 files */
		assertTrue(!tf.isSingleFile());
	}

	@Test
	public void testGetBaseName()
	{
		String correct = "secretFiles";
		assertTrue(tf.getBaseName().equals(correct));
	}

	@Test
	public void testFileLength()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testFileLengthInt()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testFilePath()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetNumOfPieces()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetHash()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetInfoHash()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testLastPieceLength()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetDotTorrentFile()
	{
		fail("Not yet implemented");
	}

}
