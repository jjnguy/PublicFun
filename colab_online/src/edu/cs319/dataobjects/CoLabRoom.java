package edu.cs319.dataobjects;

import java.util.ArrayList;
import java.util.List;

import edu.cs319.server.CoLabPrivledgeLevel;


/**
 * Class defining a room for people to meet.
 * 
 * @author The Squirrels
 * 
 */
public class CoLabRoom {

	private String roomname;
	private List<CoLabRoomMember> members;

	/**
	 * Constructs a new CoLabRoom with the supplied name.
	 * 
	 * @param roomname
	 */
	public CoLabRoom(String roomname, CoLabRoomMember theAdmin) {
		this.roomname = roomname;
		theAdmin.setPrivLevel(CoLabPrivledgeLevel.ADMIN);
		members = new ArrayList<CoLabRoomMember>();
	}

	/**
	 * Adds a new member to the CoLabRoom
	 * 
	 * @param member
	 *            the member-to-be-added's name
	 * @return whether or not the member already exists in a room.
	 */
	public boolean addMember(String member) {
		if (!containsMemberByName(member)) members.add(new CoLabRoomMember(member));
		return true;
	}

	/**
	 * checks if a member is in a colab room
	 * 
	 * @param name
	 *            the name to check
	 * @return whether or not the member is in a room
	 */
	public boolean containsMemberByName(String name) {
		for (CoLabRoomMember member : members) {
			if (member.name().equals(name)) return true;
		}
		return false;
	}
}
