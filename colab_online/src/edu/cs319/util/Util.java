package edu.cs319.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for static helper methods
 * 
 * @author The Squirrels
 * 
 */
public class Util {

	/**
	 * boolean used to determine the status of the project It is used to allow debug print
	 * statements and logging and the sort.
	 */
	public static final boolean DEBUG = false;

	private static final String hashingAlgorithmUsed = "SHA1";

	private static List<String> privledgedUsers;
	static {
		privledgedUsers = new ArrayList<String>();
		privledgedUsers.add("jjnguy");
		privledgedUsers.add("agee");
		privledgedUsers.add("idallas");
		privledgedUsers.add("wrowcliffe");
	}

	/**
	 * Determines if a given username is a Super Admin
	 * 
	 * @param username
	 *            the name to check for user status
	 * @return whether or not a user is a Super Admin
	 */
	public static boolean isSuperAdmin(String username) {
		return privledgedUsers.contains(username);
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
	
	public static void main(String[] args) {
		System.out.println(Arrays.toString(getHashedBytes("kkk".getBytes())));
	}

}
