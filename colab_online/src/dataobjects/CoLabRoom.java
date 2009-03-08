package dataobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Class defining a room for people to meet.
 * @author Justin
 *
 */
public class CoLabRoom {

	private String roomname;
	private List<CoLabRoomMember> members;
	
	/**
	 * Constructs a new CoLabRoom with the supplied name.
	 * @param roomname
	 */
	public CoLabRoom(String roomname) {
		this.roomname = roomname;
		members = new ArrayList<CoLabRoomMember>();
	}
}
