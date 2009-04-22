package edu.cs319.dataobjects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cs319.client.IClient;
import edu.cs319.dataobjects.impl.SectionizedDocumentImpl;
import edu.cs319.server.CoLabPrivilegeLevel;

/**
 * Class defining a room for people to meet.
 * 
 * @author Justin Nelson
 * 
 */
public class CoLabRoom {

	private List<SectionizedDocument> documents;

	private String roomname;
	private Map<String, CoLabRoomMember> members;
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
		theAdmin.setPrivLevel(CoLabPrivilegeLevel.ADMIN);
		members = Collections.synchronizedMap(new HashMap<String, CoLabRoomMember>());
		documents = Collections.synchronizedList(new ArrayList<SectionizedDocument>());
		this.password = password;
	}

	/**
	 * Adds a new member to the CoLabRoom
	 * 
	 * @param member
	 *            the member-to-be-added's name
	 * @return whether or not the member already exists in a room.
	 */
	public boolean addMember(String username, CoLabRoomMember member) {
		members.put(username, member);
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
		return members.keySet().contains(name);
	}

	public CoLabRoomMember getMemberByName(String name) {
		return members.get(name);
	}

	public List<IClient> getAllClients() {
		List<IClient> ret = new ArrayList<IClient>();
		for (CoLabRoomMember member : members.values()) {
			ret.add(member.getClient());
		}
		return ret;
	}
	
	public Collection<CoLabRoomMember> getAllMembers(){
		return members.values();
	}

	public List<String> getAllClientNamesInRoom() {
		List<String> ret = new ArrayList<String>();
		ret.addAll(members.keySet());
		return ret;
	}

	public List<CoLabPrivilegeLevel> getAllPrivLevels(){
		List<CoLabPrivilegeLevel> ret = new ArrayList<CoLabPrivilegeLevel>();
		for (CoLabRoomMember member : members.values()) {
			ret.add(member.privledges());
		}
		return ret;
	}
	
	public boolean removeMember(String name) {
		if (!members.containsKey(name))
			return false;
		members.remove(name);
		return true;
	}

	public List<SectionizedDocument> getAllDocuments() {
		return documents;
	}

	public boolean newDocument(String docName) {
		SectionizedDocument doc = new SectionizedDocumentImpl(docName);
		if (documents.contains(doc))
			return false;
		documents.add(doc);
		return true;
	}

	public boolean addDocument(SectionizedDocument doc) {
		for (SectionizedDocument doc2 : documents) {
			if (doc2.getName().equals(doc.getName()))
				return false;
		}
		documents.add(doc);
		return true;
	}

	public SectionizedDocument getDocument(String name) {
		for (SectionizedDocument doc2 : documents) {
			if (doc2.getName().equals(name))
				return doc2;
		}
		return null;
	}

	public SectionizedDocument getDocument(int idx) {
		return documents.get(idx);
	}

	public boolean removeDocument(String docName) {
		int idx = documents.indexOf(new SectionizedDocumentImpl(docName));
		if (idx == -1)
			return false;
		documents.remove(idx);
		return true;
	}

	public byte[] password() {
		return password;
	}

	public String roomName() {
		return roomname;
	}

	@Override
	public String toString() {
		return roomName();
	}
}
