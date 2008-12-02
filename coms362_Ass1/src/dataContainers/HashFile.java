package dataContainers;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class HashFile {
	public static final int ID_SIZE = 0;
	public static final int ENTRY_SIZE = 0;

	private static int numFiles = 0;
	private static final String HASH_FILE_LOC = "hash/file";

	private long fileLength;
	private RandomAccessFile contents;

	/**
	 * Creates a hash file that is 1mb in size
	 * 
	 * @throws IOException
	 */
	public HashFile() throws IOException {
		this(31);
	}

	public HashFile(int initialSize) throws IOException {
		contents = new RandomAccessFile(HASH_FILE_LOC + numFiles, "rw");
		fileLength = ENTRY_SIZE * getNextPrime(initialSize);
		contents.seek(fileLength);
		contents.writeByte(0);
	}

	public void store(String courseID, StudentData sData) throws IOException {
		long hash = courseID.hashCode() % fileLength;
		while (true) {
			contents.seek(hash);
			if (contents.read() == 0) {
				contents.seek(hash);
				contents.write(courseID.getBytes());
				contents.write(sData.getBytes());
				return;
			}
			// TODO check for running past the filesize
			hash += ENTRY_SIZE;
		}
	}

	public List<String> getStudentsInClass(String clssID) throws IOException {
		List<String> ret = new ArrayList<String>();
		long hash = clssID.hashCode() % fileLength;
		int pos = 0;
		contents.seek(pos);
		byte[] entry = new byte[ENTRY_SIZE];
		while(pos < fileLength){
			contents.read(entry);
			
		}
		
		return ret;
	}

	private static int getNextPrime(int numCloseToPrime) {
		if (numCloseToPrime < 3)
			return 2;
		int cur = --numCloseToPrime;
		while (true) {
			cur++;
			boolean isPrime = true;
			if (cur % 2 == 0)
				continue;
			int lim = (int) Math.sqrt(cur) + 1;
			for (int i = 3; i < lim; i += 2) {
				if (cur % i == 0) {
					isPrime = false;
					break;
				}
			}
			if (isPrime) {
				return cur;
			}
		}
	}
}
