package edu.cs319.dataobjects;

import edu.cs319.client.IClient;
import edu.cs319.server.CoLabPrivilegeLevel;
import edu.cs319.util.Util;

/**
 * Class for storing data about a member/perspective member of a CoLab room.
 * 
 * @author The Squirrels
 * 
 */
public class CoLabRoomMember {
	private String username;
	private IClient client;
	private CoLabPrivilegeLevel priv;

	/**
	 * Creates a new {@link CoLabRoomMember} with the supplied name and a Privilege level of
	 * Observer.
	 * 
	 * @param name
	 *            the name of the new CoLabRoomMember
	 */
	public CoLabRoomMember(String name, IClient client) {
		username = name;
		this.client = client;
		if (Util.isSuperAdmin(username)) {
			priv = CoLabPrivilegeLevel.SUPER_ADMIN;
		} else {
			priv = CoLabPrivilegeLevel.OBSERVER;
		}
	}

	/**
	 * Getter for the username
	 * 
	 * @return the name of the user
	 */
	public String name() {
		return username;
	}

	/**
	 * Getter for the privilege level of the user
	 * 
	 * @return the privilege level of the user
	 */
	public CoLabPrivilegeLevel privledges() {
		return priv;
	}

	/**
	 * Sets the privilege level of this CoLabRoomMember
	 * 
	 * @param lvl
	 *            the new privilege level
	 * @return whether or not the privilege was set
	 */
	public boolean setPrivLevel(CoLabPrivilegeLevel lvl) {
		if (lvl == CoLabPrivilegeLevel.SUPER_ADMIN) {
			if (!Util.isSuperAdmin(username))
				return false;
		}
		priv = lvl;
		return true;
	}

	public IClient getClient() {
		return client;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof String) {
			return obj.equals(username);
		}
		if (!(obj instanceof CoLabRoomMember)) {
			return false;
		}
		CoLabRoomMember other = (CoLabRoomMember) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
