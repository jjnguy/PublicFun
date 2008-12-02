package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A Utility file for dealing with menial tasks.
 * 
 * @author Justin Nelson
 */
public class Util {

	/**
	 * Debug mode...or not
	 */
	public static final boolean DEBUG = true;

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
}