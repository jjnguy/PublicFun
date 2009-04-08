package edu.cs319.server;

/**
 * Simple enum describing the privilege levels in a CoLab room.
 * 
 * @author The Squirrels
 * 
 */
public enum CoLabPrivilegeLevel {
	/**
	 * Regular Administrator in a CoLabRoom
	 */
	ADMIN("Admin"),
	/**
	 * Super Administrator, Moderator to all CoLab rooms.
	 */
	SUPER_ADMIN("Super Admin"),
	/**
	 * Observer in a CoLab Room. Can only watch changes being made and chat in
	 * the chat pane.
	 */
	OBSERVER("Observer"),
	/**
	 * Regular participant in a CoLab Room. Can make edits as well as view
	 * changes.
	 */
	PARTICIPANT("Participant");

	private CoLabPrivilegeLevel(String word) {
		name = word;
	}

	private String name;

	public static CoLabPrivilegeLevel getPrivilegeLevelFromString(String s) {
		CoLabPrivilegeLevel cpl = null;
		if(s.equals("Admin")) {
			cpl = ADMIN;
		} else if(s.equals("Super Admin")) {
			cpl = SUPER_ADMIN;
		} else if(s.equals("Observer")) {
			cpl = OBSERVER;
		} else if(s.equals("Participant")) {
			cpl = PARTICIPANT;
		}
		return cpl;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
