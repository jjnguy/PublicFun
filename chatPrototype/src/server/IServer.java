package server;

import java.util.List;

import infoExperts.CoLabRoom;
import infoExperts.CoLabRoomMember;

public interface IServer {

	public boolean createCoLabRoom(CoLabRoomMember admin, String name, byte[] password);
	public boolean createCoLabRoom(CoLabRoomMember admin, String name, byte[] password, int capacity);
	
	public boolean connectToColabRoom(CoLabRoomMember newMem, String name, byte[] password);
	
	public List<CoLabRoom> getAllRooms();
}
