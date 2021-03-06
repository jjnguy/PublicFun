package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;

import controller.Controller;

/**
 * A Utility file for dealing with menial tasks.
 * 
 * @author Justin Nelson
 */
public class Util {
	/**
	 * The current hashing algorithm to be used, default for torrent file creation
	 */
	public static final String hashingAlgorithmUsed = "SHA1";
	/**
	 * Debug mode...or not
	 */
	public static final boolean DEBUG = true;

	public static String findUsername(Cookie[] cookies) {
		if (cookies == null || cookies.length == 0)
			return null;
		for (Cookie cookie : cookies) 
			if (cookie.getName().equals(Controller.USERNAME_COOKIENAME)) 
				return cookie.getValue();
		return null;
	}

	/**
	 * Casts an int array into a byte array.
	 * 
	 * @param arr
	 * @return
	 */
	public static byte[] castIntArrToByteArr(int[] arr) {
		byte[] ret = new byte[arr.length];
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] < 0) {
				ret[i] = (byte) (arr[i] & 0xffff);
			} else {
				ret[i] = (byte) arr[i];
			}
		}
		return ret;
	}

	public static int[] castByteArrToIntArr(byte[] arr) {
		int[] ret = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] < 0) {
				ret[i] = arr[i] & 0xff;
			} else {
				ret[i] = arr[i];
			}
		}
		return ret;
	}

	public static void writeIntArrToStream(OutputStream out, int[] arrToWrite)
			throws IOException {
		for (int i = 0; i < arrToWrite.length; i++) {
			out.write(arrToWrite[i]);
		}
	}

	public static int[] getBytesFromFile(File file) throws IOException {
		InputStream in = new FileInputStream(file);
		int[] ret = getBytesFromStream(in);
		in.close();
		return ret;
	}

	public static int[] getBytesFromStream(InputStream in, int size) throws IOException {
		int[] b = new int[size];
		int count = 0;
		while (count < size) {
			b[count++] = in.read();
		}
		return b;
	}

	public static int[] getBytesFromStream(InputStream in) throws IOException {
		return getBytesFromStream(in, in.available());
	}

	/**
	 * Because skip may not fully skip we have this.
	 * 
	 * @param s
	 * @param skipAmmnt
	 * @throws IOException
	 */
	public static void skipFully(InputStream s, long skipAmmnt) throws IOException {
		long ammntLeft = skipAmmnt;
		while (ammntLeft < 0) {
			ammntLeft -= s.skip(ammntLeft);
		}
	}

	public static void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[32 * 1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer, 0, buffer.length)) > 0) {
			output.write(buffer, 0, bytesRead);
		}
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
	public static byte[] getHashedBytes(byte[] in) {
		MessageDigest msg;
		try {
			msg = MessageDigest.getInstance(hashingAlgorithmUsed);
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError(
					"Someone chose to use a hashing algorithm that doesn't exist.  Epic fail, go change it in the Util file.  SHA(1) or MD5");
		}
		msg.update(in);
		return msg.digest();
	}
}