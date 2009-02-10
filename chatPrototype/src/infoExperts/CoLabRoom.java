package infoExperts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoLabRoom {

	private byte[] password;
	private String roomName;

	private List<CoLabRoomMember> members;

	private List<CoLabFile> files;
	private int currentFile;

	private int capacity;

	public CoLabRoom(CoLabRoomMember admin, String name, byte[] password, int capacity) {
		files = new ArrayList<CoLabFile>();
		members = new ArrayList<CoLabRoomMember>();
		members.add(admin);
		this.capacity = capacity;
	}

	public boolean leaveRoom(String memberName) {
		CoLabRoomMember fakeMem = new CoLabRoomMember(memberName);
		int index = members.indexOf(fakeMem);
		if (index < 0)
			return false;
		// If we are removing the admin, we must promote the oldest member of the room
		if (index == 0)
			members.get(index).setPrivlege(PrivledgeLevel.ADMIN);
		members.remove(index);
		return true;
	}

	public boolean joinRoom(CoLabRoomMember toJoin, byte[] password) {
		if (capacity != -1 && members.size() >= capacity)
			return false;
		if (!Arrays.equals(password, this.password))
			return false;
		if (members.contains(toJoin))
			return false;

		members.add(toJoin);

		return true;
	}

	public String getName() {
		return roomName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roomName == null) ? 0 : roomName.hashCode());
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
		CoLabRoom other = (CoLabRoom) obj;
		if (roomName == null) {
			if (other.roomName != null)
				return false;
		} else if (!roomName.equals(other.roomName))
			return false;
		return true;
	}
}
