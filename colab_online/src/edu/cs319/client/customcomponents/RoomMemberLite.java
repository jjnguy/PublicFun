package edu.cs319.client.customcomponents;

import edu.cs319.server.CoLabPrivilegeLevel;

public class RoomMemberLite {

	private String name;
	private CoLabPrivilegeLevel priv;

	public RoomMemberLite(String name, CoLabPrivilegeLevel priv) {
		this.name = name;
		this.priv = priv;
	}

	public void setPriv(CoLabPrivilegeLevel newPriv) {
		priv = newPriv;
	}

	public CoLabPrivilegeLevel getPriv(){
		return priv;
	}
	
	@Override
	public String toString() {
		return name + ": " + priv.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof String) {
			return obj.equals(name);
		}
		if (!(obj instanceof RoomMemberLite)) {
			return false;
		}
		RoomMemberLite other = (RoomMemberLite) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
