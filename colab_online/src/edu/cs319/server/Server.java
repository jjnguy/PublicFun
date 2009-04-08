package edu.cs319.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import edu.cs319.client.IClient;
import edu.cs319.dataobjects.CoLabRoom;
import edu.cs319.dataobjects.CoLabRoomMember;
import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.dataobjects.SectionizedDocument;
import edu.cs319.dataobjects.impl.DocumentSubSectionImpl;
import edu.cs319.util.Util;

// TODO listen to boolean return types of client code
// TODO the text change like methods don't hold a user accountable for the changes
public class Server implements IServer {
	private static Server instance = null;

	public static Server getInstance() {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

	/**
	 * Maps open colab rooms to their u-id
	 */
	private final Map<String, CoLabRoom> colabrooms;
	/**
	 * Maps all active clients to their u-id
	 */
	private final Map<String, IClient> regularClients;
	/**
	 * The client that is the db
	 */
	private IClient dbConnector;

	/**
	 * Creates a new server
	 */
	private Server() {
		// Lets be thread safe about this
		// Its best to always use protection
		colabrooms = Collections.synchronizedMap(new HashMap<String, CoLabRoom>());
		regularClients = Collections.synchronizedMap(new HashMap<String, IClient>());
	}

	@Override
	public boolean addNewCoLabRoom(String username, String roomName, byte[] password) {
		if (Util.DEBUG) {
			System.out.println("Server: Adding new CoLab Room");
		}
		Set<String> roomNames = colabrooms.keySet();
		synchronized (colabrooms) {
			if (roomNames.contains(roomName)) {
				if (Util.DEBUG) {
					System.out.println("Tried to add a colabroom whos name already exists");
				}
				ServerLog.log
						.log(Level.WARNING, "Faild adding colabroom because of insitinct name");
				return false;
			}
		}
		IClient roomOwner = regularClients.get(username);
		if (roomOwner == null) {
			ServerLog.log.log(Level.WARNING,
					"Username that didn't exist tried to add new CoLabRoom");
			if (Util.DEBUG)
				System.out.println("Failed to add colab room");
			return false;
		}
		CoLabRoom roomToAdd = new CoLabRoom(roomName, new CoLabRoomMember(username, roomOwner),
				password);
		colabrooms.put(roomName, roomToAdd);

		if (Util.DEBUG) {
			System.out.println("Server: CoLab Room added successfully");
		}
		return true;
	}

	@Override
	public boolean changeUserPrivledge(String username, String roomname, CoLabPrivilegeLevel newPriv) {
		CoLabRoom room = colabrooms.get(roomname);
		if (room == null) {
			if (Util.DEBUG) {
				System.out.println("Failed to change user privledge, room didn't exist");
			}
			return false;
		}
		CoLabRoomMember member = room.getMemberByName(roomname);
		if (member == null) {
			if (Util.DEBUG) {
				System.out.println("Failed to change user privledge, user didn't exist in room");
			}
			return false;
		}
		boolean privSetSuccess = member.setPrivLevel(newPriv);
		if (privSetSuccess) {
			for (IClient client : room.getAllClients()) {
				client.changeUserPrivilege(username, newPriv);
			}
		}
		return privSetSuccess;
	}

	@Override
	public boolean addNewClient(IClient newClient, String username) {
		ServerLog.log.log(Level.FINE, "Adding client: " + username);
		if (regularClients.containsKey(username)) {
			if (Util.DEBUG) {
				ServerLog.log.log(Level.WARNING, "Client add failed du t ononunique username");
				System.out.println("Failed to add new client due to non unigque username");
			}
			return false;
		}
		regularClients.put(username, newClient);
		return true;
	}

	@Override
	public boolean joinCoLabRoom(String username, String roomName, byte[] password) {
		// TODO password protection support
		CoLabRoom room = colabrooms.get(roomName);
		if (room == null) {
			if (Util.DEBUG) {
				System.out.println("Failed to join colabroom, room id doesn't exist");
			}
			return false;
		}
		IClient client = regularClients.get(username);
		if (client == null) {
			if (Util.DEBUG) {
				System.out.println("Failed to join colabroom, client id doesn't exist");
			}
			return false;
		}
		boolean addtoroomsuccess = room.addMember(username, new CoLabRoomMember(username, client));
		if (addtoroomsuccess) {
			for (IClient client2 : room.getAllClients()) {
				client2.coLabRoomMemberArrived(username);
			}
		}
		return addtoroomsuccess;
	}

	@Override
	public boolean leaveCoLabRoom(String username, String rommname) {
		CoLabRoom room = colabrooms.get(rommname);
		if (room == null) {
			if (Util.DEBUG) {
				System.out.println("Failed to leave colabroom, room id doesn't exist");
			}
			return false;
		}
		boolean removesuccess = room.removeMember(username);
		if (removesuccess) {
			for (IClient client : room.getAllClients()) {
				client.coLabRoomMemberLeft(username);
			}
		}
		return removesuccess;
	}

	@Override
	public boolean newChatMessage(String usernameSender, String roomname, String message) {
		CoLabRoom room = colabrooms.get(roomname);
		if (room == null) {
			if (Util.DEBUG) {
				System.out.println("Failed send chat message, room id doesn't exist");
			}
			return false;
		}
		for (IClient client : room.getAllClients()) {
			if (Util.DEBUG) {
				System.out.println("Sending chat message to: " + client.toString());
			}
			client.newChatMessage(usernameSender, message);
		}
		return true;
	}

	@Override
	public boolean newChatMessage(String usernameSender, String roomname, String message,
			String recipiant) {
		CoLabRoom room = colabrooms.get(roomname);
		if (room == null) {
			if (Util.DEBUG) {
				System.out.println("Failed send chat message, room id doesn't exist");
			}
			return false;
		}
		for (IClient client : room.getAllClients()) {
			client.newChatMessage(usernameSender, message, recipiant);
		}
		return true;
	}

	// TODO error checking and reporting from here down

	@Override
	public boolean getAllCoLabRoomNames(String usename) {
		IClient client = regularClients.get(usename);
		return client.allCoLabRooms(colabrooms.keySet());
	}

	@Override
	public boolean getClientsCurrentlyInRoom(String username, String roomname) {
		CoLabRoom room = colabrooms.get(roomname);
		IClient toSendTo = regularClients.get(username);
		return toSendTo.allUsersInRoom(room.getAllClientNamesInRoom());
	}

	@Override
	public boolean newSubSection(String username, String roomname, String documentName,
			String sectionID, int idx) {
		CoLabRoom room = colabrooms.get(roomname);
		SectionizedDocument doc = room.getDocument(documentName);
		DocumentSubSection toAdd = new DocumentSubSectionImpl(sectionID);
		boolean addResult = doc.addSubSection(toAdd, idx);
		if (!addResult)
			return false;
		for (IClient client : room.getAllClients()) {
			client.newSubSection(username, sectionID, documentName, toAdd, idx);
		}
		return true;
	}

	@Override
	public boolean subSectionRemoved(String username, String roomname, String documentName,
			String sectionID) {
		CoLabRoom room = colabrooms.get(roomname);
		SectionizedDocument doc = room.getDocument(documentName);
		doc.removeSubSection(sectionID);
		for (IClient client : room.getAllClients()) {
			client.subSectionRemoved(username, sectionID, documentName);
		}
		return true;
	}

	@Override
	public boolean subSectionUpdated(String username, String roomname, String sectionID,
			String documentName, DocumentSubSection update) {
		CoLabRoom room = colabrooms.get(roomname);
		SectionizedDocument doc = room.getDocument(documentName);
		int docIndex = doc.getSubSectionIndex(sectionID);
		doc.removeSubSection(sectionID);
		doc.addSubSection(update, docIndex);
		for (IClient client : room.getAllClients()) {
			client.updateSubsection(username, documentName, update, sectionID);
		}
		return true;
	}
}
