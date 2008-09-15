package edu.iastate.cs309.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.iastate.cs309.communication.PasswordHash;

/**
 * A Util file containing simple random helpful methods and fields.
 * 
 * @author Justin
 */
public class Util
{
	/**
	 * For use during the project building phase
	 */
	public static final boolean DEBUG = true;

	/**
	 * Because we wish we had preprocessor statements and torrent transfers use
	 * 4 different threads
	 */
	public static final boolean TDEBUG = false;

	/**
	 * The current hashing algorithm to be used, default for torrent file
	 * creation
	 */
	public static final String hashingAlgorithmUsed = "SHA1";

	/**
	 * A convenience method for getting an Umlaut
	 * 
	 * @return the proper Übertorrent
	 */
	public static String getUber()
	{
		return '\u00dc' + "bertorrent";
	}

	/**
	 * Simple wrapper for validation urls
	 * 
	 * @param url
	 *            the supposed url string to validate
	 * @return true if the url is valid
	 */
	@Deprecated
	public static boolean validateUrl(String url)
	{
		try
		{
			URL right = new URL(url);
			if (Util.DEBUG)
			{
				System.out.println("The URL is: " + right.toString());
			}
		}
		catch (MalformedURLException e)
		{
			if (Util.DEBUG)
			{
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	/**
	 * @return the current version that Uebertorrent is
	 */
	public static double getVersionInfo()
	{
		return 1.05;
	}

	/**
	 * Returns the hash value of the given chars
	 * 
	 * Uses the default hash algorithm described above
	 * 
	 * @param in
	 *            the char[] to hash
	 * @return a byte[] of hashed values
	 */
	public static byte[] getHashedBytes(char[] in)
	{
		return getHashedBytes(new String(in).getBytes());
	}

	/**
	 * Returns the hash value of the given chars
	 * 
	 * Uses the default hash algorithm described above
	 * 
	 * @param in
	 *            the byte[] to hash
	 * @return a byte[] of hashed values
	 */
	public static byte[] getHashedBytes(byte[] in)
	{
		MessageDigest msg;
		try
		{
			msg = MessageDigest.getInstance(hashingAlgorithmUsed);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new AssertionError("Someone chose to use a hashing algorithm that doesn't exist.  Epic fail, go change it in the Util file.  SHA(1) or MD5");
		}
		msg.update(in);
		return msg.digest();
	}

	/**
	 * @param in
	 *            a byte[] to be hashed
	 * @return and encapsulated password hash object
	 */
	public static PasswordHash getPwordHashObject(byte[] in)
	{
		return new PasswordHash(getHashedBytes(in));
	}

	/**
	 * @param in
	 *            a char[] to be hashed
	 * @return and encapsulated password hash object
	 */
	public static PasswordHash getPwordHashObject(char[] in)
	{
		return getPwordHashObject(new String(in).getBytes());
	}

	//not central time
	/**
	 * Simple way to turn miliseconds into the current date
	 * 
	 * @param secondsP
	 *            Miliseconds since Jan 1 1970, or something like that
	 * @return a string representing the current date
	 */
	public static String getTimeStringFromMiliseconds(long secondsP)
	{
		int seconds = (int) (secondsP / 1000);
		double year = seconds / 60.0 / 60 / 24.0 / 365;
		double day = (year - Math.floor(year)) * 365;
		double hour = (day - Math.floor(day)) * 24;
		double min = (hour - Math.floor(hour)) * 60;
		double sec = (min - Math.floor(min)) * 60;
		return (hour < 10 ? "0" : "") + (int) hour + ":" + (min < 10 ? "0" : "") + (int) min + ":" + (Math.round(sec) < 10 ? "0" : "") + Math.round(sec);
	}

	/**
	 * Disallows instantiation
	 */
	/* Super */private Util()
	{
		while (true == true && false == false)
			System.out.println("Happy Happy Joy Joy, print fun stuff yayayayaya!!!!  Don't fscking try to instantiate me.");
	}

	/**
	 * Copies a file from a source to a destination using Channels
	 * 
	 * @param source
	 *            the source folder or file
	 * @param destination
	 *            destination folder
	 * @throws IOException
	 * 
	 * @return true if the filecopy was successful
	 */
	public static boolean fileCopy(File source, File destination) throws IOException
	{
		boolean retVal = false;
		FileChannel src = null;
		FileChannel dest = null;

		try
		{
			src = new FileInputStream(source).getChannel();
			dest = new FileOutputStream(destination).getChannel();

			dest.transferFrom(src, 0, src.size());
			retVal = true;
		}
		catch (FileNotFoundException e)
		{
			retVal = false;
			if (Util.DEBUG)
				e.printStackTrace();
		}
		finally
		{
			if (src != null)
				src.close();

			if (dest != null)
				dest.close();
		}
		return retVal;
	}

	/**
	 * Finds the total size of a directory or file.
	 * 
	 * @param file
	 *            the file or directory to find the total size of
	 * @return the total size of all of the files in the directory
	 */
	public static long getTotalLength(File file)
	{
		int total = 0;
		File[] files = file.listFiles();
		if (files == null)
			return file.length();
		for (File file2 : files)
		{
			if (!file2.isDirectory())
				total += file2.length();
			else
				total += getTotalLength(file2);
		}
		return total;
	}
}
