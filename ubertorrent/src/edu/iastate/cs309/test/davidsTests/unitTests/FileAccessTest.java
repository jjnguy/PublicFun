package edu.iastate.cs309.test.davidsTests.unitTests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.iastate.cs309.torrentManager.TorrentFile;
import edu.iastate.cs309.torrentManager.containers.FileAccess;
import edu.iastate.cs309.torrentManager.exceptions.IncompleteFileException;
import edu.iastate.cs309.torrentparser.ParseException;

/**
 * Test FileAccess against the test torrent in testTorrentFiles
 * 
 * @author sralmai
 * 
 */
public class FileAccessTest
{
	/** the object we are testing */
	private static FileAccess f = null;

	/** raw access to what FileAccess should be describing */
	private static TorrentFile tf = null;

	byte completeBitfield = (byte) 0x80;
	private static final String torrent = "testTorrentFiles/secret.torrent";
	private static final String baseDir = "testTorrentFiles";

	/**
	 * Set up the TorrentFile used by FileAccess and create a FileAccess object
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		tf = new TorrentFile(torrent);
		f = new FileAccess(baseDir, tf, null, true);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testConstructor()
	{
		try
		{
			TorrentFile torrentFile = new TorrentFile(torrent);
			FileAccess fileAccess = new FileAccess(baseDir, torrentFile, null, true);

			fileAccess.getRawBitfield();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testGetRawBitfield()
	{
		byte[] rawBits = f.getRawBitfield();

		int expectedSize = (int) Math.ceil(tf.getNumOfPieces() / 8.0);

		assertTrue(rawBits.length == expectedSize);
		assertTrue(rawBits[0] == completeBitfield);

	}

	@Test
	public void testWriteBytes()
	{
		/** input file bigger than the array */
		byte[] rawBits = new byte[100];

		byte[] raw2 = new byte[100];

		try
		{
			InputStream in = f.getFile(0);
			int read = in.read(rawBits);

			/** write the same file back */
			f.writeBytes(0, rawBits, read);

			in = f.getFile(0);
			int read2 = in.read(raw2);

			/** make sure everything is the same */
			assertTrue(read == read2);
			Arrays.equals(rawBits, raw2);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		catch (IncompleteFileException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testNumOfCompletePieces()
	{
		// only 1 piece 
		assertTrue(f.numOfCompletePieces() == 1);
	}

	@Test
	public void testComplete()
	{
		assertTrue(f.complete());
	}

	@Test
	public void testHavePiece()
	{
		assertTrue(f.havePiece(0));
	}

	@Test
	public void testGetFile()
	{
		byte[] msg = new byte[64];
		try
		{
			InputStream in = f.getFile(0);
			/*
			 * TODO (FindBugs)
			 * 
			 * [RR] Method ignores results of InputStream.read()
			 * [RR_NOT_CHECKED]
			 * 
			 * This method ignores the return value of one of the variants of
			 * java.io.InputStream.read() which can return multiple bytes. If
			 * the return value is not checked, the caller will not be able to
			 * correctly handle the case where fewer bytes were read than the
			 * caller requested. This is a particularly insidious kind of bug,
			 * because in many programs, reads from input streams usually do
			 * read the full amount of data requested, causing the program to
			 * fail only sporadically.
			 */
			in.read(msg);
			System.out.println(new String(msg, "UTF-8"));

			in = f.getFile(1);
			in.read(msg);
			System.out.println(new String(msg, "UTF-8"));

			in = f.getFile(2);
			in.read(msg);
			System.out.println(new String(msg, "UTF-8"));
		}
		catch (IncompleteFileException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
}
