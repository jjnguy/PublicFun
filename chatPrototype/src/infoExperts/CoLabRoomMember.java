package infoExperts;

public class CoLabRoomMember {

	private String displayName;
	private PrivledgeLevel priv;

	public CoLabRoomMember(String dispName) {
		this.priv = PrivledgeLevel.OBSERVER;
		displayName = dispName;
	}

	public CoLabRoomMember(String dispName, PrivledgeLevel priv) {
		this.priv = priv;
		displayName = dispName;
	}
	
	public String getName(){
		return toString();
	}
	
	public void setPrivlege(PrivledgeLevel p){
		priv = p;
	}
	
	public PrivledgeLevel getPrivlege(){
		return priv;
	}
	
	@Override
	public String toString() {
		return displayName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoLabRoomMember other = (CoLabRoomMember) obj;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		return true;
	}
}
