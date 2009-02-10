package server;

import infoExperts.CoLabRoom;
import infoExperts.CoLabRoomMember;

import java.util.ArrayList;
import java.util.List;

public class Server implements IServer {

	private List<CoLabRoom> rooms;

	public Server() {
		rooms = new ArrayList<CoLabRoom>();
	}

	@Override
	public boolean connectToColabRoom(CoLabRoomMember newMem, String name, byte[] password) {
		CoLabRoom roomToJoin = this.getRoomByName(name);
		if (roomToJoin == null)
			return false;
		return roomToJoin.joinRoom(newMem, password);
	}

	@Override
	public boolean createCoLabRoom(CoLabRoomMember admin, String name, byte[] password) {
		return createCoLabRoom(admin, name, password, -1);
	}

	@Override
	public boolean createCoLabRoom(CoLabRoomMember admin, String name, byte[] password,
			int capacity) {
		if (containsRoomWithName(name))
			return false;
		CoLabRoom toAdd = new CoLabRoom(admin, name, password, capacity);
		rooms.add(toAdd);
		return true;
	}

	@Override
	public List<CoLabRoom> getAllRooms() {
		return rooms;
	}

	public CoLabRoom getRoomByName(String roomName) {
		for (CoLabRoom room : rooms) {
			if (room.getName().equals(roomName))
				return room;
		}
		return null;
	}

	private boolean containsRoomWithName(String roomName) {
		for (CoLabRoom room : rooms) {
			if (room.getName().equals(roomName))
				return true;
		}
		return false;
	}
}
