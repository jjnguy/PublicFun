package dataobjects;

import java.util.ArrayList;
import java.util.List;

import server.CoLabPrivledgeLevel;

/**
 * Class defining a room for people to meet.
 * @author The Squirrels
 *
 */
public class CoLabRoom {

	private String roomname;
	private List<CoLabRoomMember> members;
	
	/**
	 * Constructs a new CoLabRoom with the supplied name.
	 * @param roomname
	 */
	public CoLabRoom(String roomname, CoLabRoomMember theAdmin) {
		this.roomname = roomname;
		theAdmin.setPrivLevel(CoLabPrivledgeLevel.ADMIN);
		members = new ArrayList<CoLabRoomMember>();
	}
}
