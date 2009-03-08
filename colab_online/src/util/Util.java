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

	public static boolean isSuperAdmin(String username) {
		return privledgedUsers.contains(username);
	}
	
	
}
