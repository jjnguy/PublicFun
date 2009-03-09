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
	private byte[] password;

	/**
	 * Constructs a new CoLabRoom with the supplied name.
	 * 
	 * @param roomname
	 */
	public CoLabRoom(String roomname, CoLabRoomMember theAdmin) {
		this(roomname, theAdmin, null);
	}
	
	/**
	 * Constructs a new CoLabRoom with the supplied name and password.
	 * 
	 * @param roomname
	 */
	public CoLabRoom(String roomname, CoLabRoomMember theAdmin, byte[] password) {
		this.roomname = roomname;
		theAdmin.setPrivLevel(CoLabPrivledgeLevel.ADMIN);
		members = new ArrayList<CoLabRoomMember>();
		this.password = password;
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

	public byte[] password(){
		return password;
	}
	
	public String roomName(){
		return roomname;
	}
	
	@Override
	public String toString() {
		return roomName();
	}
}
