package edu.iastate.cs309.clientserverprotocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import edu.iastate.cs309.util.Util;

/**
 * Some utilities for network byte things.
 * 
 * @author singularity !!! Checking should be done by the calling program !!!
 *         These do NOT check anything and as a result may insult your mother,
 *         give you nightmares, erode your tenuous grip on reality, or do a
 *         myriad of other bad things. !!! These methods DO NOT CHECK ANYTHING
 *         !!! (except connectAndAuth does)
 */
public class NetUtils
{

	/** MAGIC NUMBERS * */

	/** chuck size for transfer() */
	private static final int blockSize = 1 << 18;

	/**
	 * convert 4 bytes to big endian (network byte order) integer value
	 * (Incidentally, the reason network byte order is big endian was for phone
	 * networks so routing could be done while the number was being entered.)
	 * 
	 * @param group
	 * @param offset
	 *            in byte array (should be zero in most cases)
	 * @return integer value
	 */
	public static int bytesToInt(byte[] group, int offset)
	{
		int retVal = 0;
		retVal += (0xff & group[offset]) << 24;
		retVal += (0xff & group[offset + 1]) << 16;
		retVal += (0xff & group[offset + 2]) << 8;
		retVal += (0xff & group[offset + 3]);
		return retVal;
	}

	/**
	 * convert integer to 4 bytes in big endian format
	 * 
	 * @param i
	 *            integer to convert
	 * @param message
	 *            byte array to write to
	 * @param offset
	 *            in message to begin writing
	 */
	public static void intToBytes(int i, byte[] message, int offset)
	{
		message[offset + 0] = (byte) (i >> 24);
		message[offset + 1] = (byte) (i >> 16);
		message[offset + 2] = (byte) (i >> 8);
		message[offset + 3] = (byte) (i);
	}

	/**
	 * convert 2 bytes to big endian (network byte order) short value
	 * 
	 * @param group
	 * @param offset
	 *            in byte array (should be zero in most cases)
	 * @return value as integer
	 */
	public static int bytesToShort(byte[] group, int offset)
	{
		return ((0xff & group[offset]) << 8) + (0xff & group[offset + 1]);
	}

	/**
	 * convert a short to big endian 2 byte representation
	 * 
	 * TODO david Why is this not a short?
	 * 
	 * @param i
	 * @param message
	 * @param offset
	 */
	public static void shortToBytes(int i, byte[] message, int offset)
	{
		if (i > 65535 || i < 0)
			System.err.println("shortToBytes() called with too large a value. Truncating!");
		message[offset + 0] = (byte) (i >> 8);
		message[offset + 1] = (byte) i;
		return;
	}

	/**
	 * Standard call to open ssl connections (used for initial login)
	 * 
	 * @param user
	 * @return ssl socket connected to things defined in passed UserToken
	 * @throws IOException
	 */
	static SSLSocket connect(final UserToken user) throws IOException
	{
		return connectOnPort(user, user.getPort());
	}

	/**
	 * "real" call to open ssl connections on a given port
	 * 
	 * @param port
	 * @param user
	 * @return SSL connection
	 * @throws IOException
	 */
	public static SSLSocket connectOnPort(final UserToken user, final int port) throws IOException
	{
		SSLSocket s;
		OutputStream out;

		SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();

		s = (SSLSocket) ssf.createSocket(user.getHost(), port);
		String[] ciphers = { "SSL_DH_anon_WITH_RC4_128_MD5" };
		s.setEnabledCipherSuites(ciphers);

		out = s.getOutputStream();

		/* authenticate */
		out.write(user.getPwHash().getBytes());

		return s;
	}

	/**
	 * completely read in an array
	 * 
	 * @param in
	 * @param array
	 * @param startIndex
	 * @param length
	 * @return something or other
	 * @throws IOException
	 */
	public static boolean readFully(InputStream in, byte[] array, int startIndex, int length) throws IOException
	{
		int totalRead = 0;
		int read = 0;

		while (totalRead < length)
		{
			read = in.read(array, startIndex + totalRead, length - totalRead);
			if (read < 0)
				/** we hit the EOF */
				return false;

			totalRead += read;
		}
		return true;
	}

	/**
	 * readInt
	 * 
	 * read a big endian integer from the input stream
	 * 
	 * @param in
	 *            InputStream to read from
	 * @return integer read from stream
	 * @throws IOException
	 */
	public static int readInt(InputStream in) throws IOException
	{
		byte[] msg = new byte[4];

		/** read byte by byte to block until everything is done */
		for (int i = 0; i < 4; ++i)
		{
			int tmp = in.read();
			if (tmp < 0)
				throw new IOException("hit end of stream on readInt()");
			msg[i] = (byte) tmp;
		}
		return bytesToInt(msg, 0);
	}

	/**
	 * 
	 * @param timeout
	 * @param retVal
	 * @param i
	 */
	public static void longToBytes(long timeout, byte[] retVal, int i)
	{
		int lowInt = (int) timeout & 0xFFFFFFFF;
		int highInt = (int) (timeout >> 32);

		intToBytes(highInt, retVal, i);
		intToBytes(lowInt, retVal, i + 4);

	}

	/**
	 * @param msg
	 * @param index
	 * @return long
	 */
	public static long bytesToLong(byte[] msg, int index)
	{
		long retval = 0;
		retval += ((long) bytesToInt(msg, index)) << 32;
		retval += bytesToInt(msg, index + 4);

		return retval;
	}

	/**
	 * encode string
	 * 
	 * @param s
	 *            string to encode
	 * @return string encoded for transport
	 */
	public static byte[] encodeForTrans(String s)
	{
		/** return 0 four bytes for null */
		if (s == null)
			return new byte[4];
		byte[] raw = null;
		try
		{
			raw = s.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			die(e);
		}
		byte[] ret = new byte[raw.length + 4];

		NetUtils.intToBytes(raw.length, ret, 0);
		System.arraycopy(raw, 0, ret, 4, raw.length);

		return ret;
	}

	/**
	 * decode a string from a byte array
	 * 
	 * @param msg
	 *            byte array to decode from
	 * @param offset
	 *            offset to begin decoding
	 * @return decoded String (which may be null)
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(byte[] msg, int offset) throws UnsupportedEncodingException
	{
		int len = bytesToInt(msg, offset);

		/** encoded null string */
		if (len == 0)
			return null;

		byte[] rawBytes = new byte[len];

		System.arraycopy(msg, offset + 4, rawBytes, 0, len);
		return new String(rawBytes, "UTF-8");
	}

	/**
	 * Push an InputStream through to an OutputStream. Used by file transfer
	 * threads.
	 * 
	 * @param in
	 *            InputStream to read from
	 * @param out
	 *            OutputStream to send to
	 * @throws IOException
	 */
	public static void transfer(InputStream in, OutputStream out) throws IOException
	{
		int len = 0;

		byte[] buff = new byte[blockSize];

		len = in.read(buff);

		while (len > -1)
		{
			if (Util.DEBUG)
			{
				System.out.println("dumping " + len + " bytes through a connection");
			}
			out.write(buff, 0, len);
			len = in.read(buff);
		}
	}

	/**
	 * hard die
	 * 
	 * @param e
	 */
	public static void die(Exception e)
	{
		System.err.println("Oh god, the world is ending:\n" + e.getMessage());
		System.exit(1);
	}

	/**
	 * print byte array as a hex string
	 * 
	 * @param array
	 */
	public static String printAsHex(byte[] array)
	{
		byte ch = 0x00;

		int i = 0;
		String lazy[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

		StringBuffer out = new StringBuffer(array.length * 2);

		while (i < array.length)
		{

			ch = (byte) (array[i] & 0xF0); // Strip off high nibble

			ch = (byte) (ch >>> 4); // shift the bits down

			ch = (byte) (ch & 0x0F); // must do this if high order bit is on!

			out.append(lazy[ch]); // convert the nibble to a String Character

			ch = (byte) (array[i] & 0x0F); // Strip off low nibble 

			out.append(lazy[ch]); // convert the	 nibble to a String Character

			i++;

		}

		return new String(out);

	}
}
