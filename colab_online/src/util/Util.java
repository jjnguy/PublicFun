package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for static helper methods
 * 
 * @author The Squirrels
 * 
 */
public class Util {
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
}
