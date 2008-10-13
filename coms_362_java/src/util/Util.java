package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Util {

	public static final boolean DEBUG = true;

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
		int[] b = new int[in.available()];
		int count = 0;
		while(in.available() > 0){
			b[count++] = in.read();
		}
		return b;
	}
	
}
