package edu.cs319.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.cs319.client.IClient;
import edu.cs319.database.DocumentDatabaseUtil;
import edu.cs319.dataobjects.CoLabRoom;
import edu.cs319.dataobjects.CoLabRoomMember;
import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.dataobjects.SectionizedDocument;
import edu.cs319.dataobjects.impl.DocumentSubSectionImpl;
import edu.cs319.dataobjects.impl.SectionizedDocumentImpl;
import edu.cs319.util.Util;

// TODO listen to boolean return types of client code
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
				return joinCoLabRoom(username, roomName, null);
			}
		}
		IClient roomOwner = regularClients.get(username);
		if (roomOwner == null) {
			if (Util.DEBUG) {
				System.out.println("Failed to add colab room");
			}
			return false;
		}
		CoLabRoom roomToAdd = new CoLabRoom(roomName, new CoLabRoomMember(username, roomOwner),
				password);
		colabrooms.put(roomName, roomToAdd);

		if (Util.DEBUG) {
			System.out.println("Server: CoLab Room added successfully");
			System.out.println("Adding the creator to the room and setting his/her priv level");
		}
		joinCoLabRoom(username, roomName, password);
		changeUserPrivledge(username, roomName, CoLabPrivilegeLevel.ADMIN);
		return true;
	}

	@Override
	public boolean changeUserPrivledge(String username, String roomname, CoLabPrivilegeLevel newPriv) {
		if (Util.DEBUG) {
			System.out.println("Attempting to change user privledge.  Sent by " + username);
		}
		CoLabRoom room = colabrooms.get(roomname);
		synchronized (room) {
			if (room == null) {
				if (Util.DEBUG) {
					System.out.println("Failed to change user privledge, room didn't exist");
				}
				return false;
			}
			// Super admins stay superadmins
			if (Util.isSuperAdmin(username)) {
				return true;
			}
			CoLabRoomMember member = room.getMemberByName(username);
			if (member == null) {
				if (Util.DEBUG) {
					System.out
							.println("Failed to change user privledge, user didn't exist in room");
				}
				return false;
			}
			boolean privSetSuccess = member.setPrivLevel(newPriv);
			if (!privSetSuccess) {
				if (Util.DEBUG) {
					System.out.println("Failed to change user (" + username + ") priv in room "
							+ roomname);
				}
				return false;
			}
			// observers will lose all locks
			if (newPriv == CoLabPrivilegeLevel.OBSERVER) {
				releaseAllLocksOnAllDocs(username, roomname, room);
			}
			for (IClient client : room.getAllClients()) {
				client.changeUserPrivilege(username, newPriv);
			}
		}
		return true;
	}

	@Override
	public boolean addNewClient(IClient newClient, String username) {
		if (regularClients.containsKey(username)) {
			if (Util.DEBUG) {
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
		if (room == null)
			return false;
		synchronized (room) {
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
			boolean addtoroomsuccess = room.addMember(username, new CoLabRoomMember(username,
					client));
			if (!addtoroomsuccess) {
				if (Util.DEBUG) {
					System.out.println("Failed to add " + username + " to colabroom " + roomName);
				}
				return false;
			}
			// if the room was empty, the new member should assume admin role
			if (room.getAllClients().size() == 1) {
				changeUserPrivledge(username, roomName, CoLabPrivilegeLevel.ADMIN);
			}
			if (Util.isSuperAdmin(username)) {
				changeUserPrivledge(username, roomName, CoLabPrivilegeLevel.SUPER_ADMIN);
			}
			CoLabRoomMember member = room.getMemberByName(username);
			for (IClient client2 : room.getAllClients()) {
				client2.coLabRoomMemberArrived(username, member.privledges());
			}
			// TODO test
			client.allUsersInRoom(room.getAllClientNamesInRoom(), room.getAllPrivLevels());
			for (SectionizedDocument sd : room.getAllDocuments()) {
				client.newDocument(username, sd.getName());
				client.updateAllSubsections(sd.getName(), sd.getAllSubSections());
			}
		}
		return true;
	}

	@Override
	public boolean leaveCoLabRoom(String username, String rommname) {
		if (Util.DEBUG) {
			System.out.println("Leaving a colab room: " + username + " in room: " + rommname);
		}
		CoLabRoom room = colabrooms.get(rommname);
		if (room == null) {
			if (Util.DEBUG) {
				System.out.println("Failed to leave colabroom, room id doesn't exist");
			}
			return false;
		}
		synchronized (room) {
			CoLabRoomMember c = room.getMemberByName(username);
			if (c == null)
				return false;
			// If an admin leaves, someone else should be promoted to admin
			if (c.privledges() == CoLabPrivilegeLevel.ADMIN) {
				for (CoLabRoomMember member : room.getAllMembers()) {
					if (member.privledges() != CoLabPrivilegeLevel.ADMIN) {
						changeUserPrivledge(member.name(), rommname, CoLabPrivilegeLevel.ADMIN);
						break;
					}
				}
			}

			for (IClient client : room.getAllClients()) {
				client.coLabRoomMemberLeft(username);
			}

			releaseAllLocksOnAllDocs(username, rommname, room);
			if (!room.removeMember(username)) {
				if (Util.DEBUG) {
					System.out.println("could not remove roommember from room");
				}
				return false;
			}
			return true;
		}
	}

	private void releaseAllLocksOnAllDocs(String username, String roomname, CoLabRoom room) {
		for (SectionizedDocument doc : room.getAllDocuments()) {
			releaseLocks(doc, username, roomname);
		}
	}

	private void releaseLocks(SectionizedDocument doc, String username, String roomname) {
		for (DocumentSubSection sec : doc.getAllSubSections()) {
			if (username.equals(sec.lockedByUser())) {
				subSectionUnLocked(username, roomname, doc.getName(), sec.getName());
			}
		}
	}

	@Override
	public boolean newChatMessage(String usernameSender, String roomname, String message) {
		CoLabRoom room = colabrooms.get(roomname);
		synchronized (room) {
			if (room == null) {
				if (Util.DEBUG) {
					System.out.println("Failed send chat message, room id doesn't exist");
				}
				return false;
			}
			List<IClient> clientsInRoom = room.getAllClients();
			for (IClient client : clientsInRoom) {
				if (Util.DEBUG) {
					System.out.println("Sending chat message to: " + client.toString());
				}
				client.newChatMessage(usernameSender, message);
			}
			// if the user is all alone...have some fun
			if (clientsInRoom.size() == 1
					&& clientsInRoom.get(0).getUserName().equals(usernameSender)) {
				clientsInRoom.get(0).newChatMessage("The Darkness",
						"You are all alone.  Watch your back!");
			}
		}
		return true;
	}

	@Override
	public boolean newChatMessage(String usernameSender, String roomname, String message,
			String recipiant) {
		CoLabRoom room = colabrooms.get(roomname);
		synchronized (room) {
			if (room == null) {
				if (Util.DEBUG) {
					System.out.println("Failed send chat message, room id doesn't exist");
				}
				return false;
			}
			for (IClient client : room.getAllClients()) {
				client.newChatMessage(usernameSender, message, recipiant);
			}
		}
		return true;
	}

	// TODO error checking and reporting from here down

	@Override
	public boolean getAllCoLabRoomNames(String usename) {
		IClient client = regularClients.get(usename);
		if (client == null) {
			if (Util.DEBUG) {
				System.out.println("Server:  Client was null...what?");
			}
			return false;
		}
		if (colabrooms == null) {
			if (Util.DEBUG) {
				System.out.println("Server:  Colab Rooms were null...what?");
			}
			return false;
		}
		return client.allCoLabRooms(colabrooms.keySet());
	}

	@Override
	public boolean getClientsCurrentlyInRoom(String username, String roomname) {
		CoLabRoom room = colabrooms.get(roomname);
		if (room == null) {
			if (Util.DEBUG) {
				System.out.println("...................66666666666666.....Room was null, roomname: " + roomname + " username: " + username);
			}
			return false;
		}
		synchronized (room) {
			IClient toSendTo = regularClients.get(username);
			return toSendTo.allUsersInRoom(room.getAllClientNamesInRoom(), room.getAllPrivLevels());
		}
	}

	@Override
	public boolean newSubSection(String username, String roomname, String documentName,
			String sectionID, int idx) {
		if (Util.DEBUG) {
			System.out.println("Server.newSubSection: docname: " + documentName + " sectID: "
					+ sectionID);
		}
		CoLabRoom room = colabrooms.get(roomname);
		synchronized (room) {
			CoLabRoomMember m = room.getMemberByName(username);
			if (m.privledges() == CoLabPrivilegeLevel.OBSERVER) {
				if (Util.DEBUG) {
					System.out.println("Restricting an observer from adding a new subsection");
				}
				return true;
			}
			SectionizedDocument doc = room.getDocument(documentName);
			DocumentSubSection toAdd = new DocumentSubSectionImpl(sectionID);
			releaseLocks(doc, username, roomname);
			if (Util.DEBUG) {
				System.out.println("Server.newSubSection:  Document created to add, locked = "
						+ toAdd.isLocked() + " the guy who is locking it: " + toAdd.lockedByUser());
			}
			toAdd.setLocked(true, username);
			if (Util.DEBUG) {
				System.out
						.println("Server.newSubSection:  created doc, set locked by the user, locked = "
								+ toAdd.isLocked()
								+ " the guy who is locking it: "
								+ toAdd.lockedByUser());
			}
			boolean addResult = doc.addSubSection(toAdd, idx);
			if (!addResult)
				return false;
			for (IClient client : room.getAllClients()) {
				client.newSubSection(username, documentName, sectionID, toAdd, idx);
			}
		}
		return true;
	}

	@Override
	public boolean subSectionRemoved(String username, String roomname, String documentName,
			String sectionID) {
		if (Util.DEBUG) {
			System.out.println("Server.subSectionRemoved: docname: " + documentName + " sectID: "
					+ sectionID);
		}
		CoLabRoom room = colabrooms.get(roomname);
		synchronized (room) {
			CoLabRoomMember m = room.getMemberByName(username);
			if (m.privledges() == CoLabPrivilegeLevel.OBSERVER) {
				if (Util.DEBUG) {
					System.out.println("Restricting an observer from removing subsection");
				}
				return true;
			}
			SectionizedDocument doc = room.getDocument(documentName);
			doc.removeSubSection(sectionID);
			for (IClient client : room.getAllClients()) {
				client.subSectionRemoved(username, sectionID, documentName);
			}
		}
		return true;
	}

	@Override
	public boolean subSectionUpdatedAll(String username, String roomname, String documentName,
			String sectionID, DocumentSubSection update) {
		CoLabRoom room = colabrooms.get(roomname);
		synchronized (room) {
			CoLabRoomMember m = room.getMemberByName(username);
			if (m.privledges() == CoLabPrivilegeLevel.OBSERVER) {
				if (Util.DEBUG) {
					System.out.println("Restricting an observer from updating subsection");
				}
				return true;
			}
			SectionizedDocument doc = room.getDocument(documentName);
			DocumentSubSection sec = doc.getSection(sectionID);
			// Don't send updates that don't change anything
			try {
				if (sec.getName().equals(sectionID) && sec.isLocked() == update.isLocked()
						&& sec.lockedByUser().equals(update.lockedByUser())
						&& sec.getText().equals(update.getText())) {
					if (Util.DEBUG) {
						System.out.println("Ignoring no change update");
					}
					return false;
				}
			} catch (NullPointerException e) {
				return false;
			}
			sec.setText(username, update.getText());
			System.out.println("Updating SubSection->  Name: " + sec.getName() + " LockHolder: "
					+ sec.lockedByUser() + " Updated By: " + username);
			for (IClient client : room.getAllClients()) {
				client.updateSubsectionAll(username, documentName, sectionID, update);
			}
		}
		return true;
	}

	@Override
	public boolean documentRemoved(String username, String roomname, String documentName) {
		if (Util.DEBUG) {
			System.out.println("Server.documentRemoved: docname: " + documentName);
		}
		CoLabRoom room = colabrooms.get(roomname);
		synchronized (room) {
			CoLabRoomMember m = room.getMemberByName(username);
			if (m.privledges() == CoLabPrivilegeLevel.OBSERVER) {
				if (Util.DEBUG) {
					System.out.println("Restricting an observer from removing document");
				}
				return true;
			}
			if (room.removeDocument(documentName)) {
				System.out.println("Removed From Server");
				for (IClient client : room.getAllClients()) {
					client.removeDocument(username, documentName);
				}
				return true;
			}
			return false;
		}
	}

	@Override
	public boolean newDocument(String username, String roomname, String documentName) {
		if (Util.DEBUG) {
			System.out.println("Server.newDocument: docname: " + documentName);
		}
		CoLabRoom room = colabrooms.get(roomname);
		synchronized (room) {
			CoLabRoomMember m = room.getMemberByName(username);
			if (m.privledges() == CoLabPrivilegeLevel.OBSERVER) {
				if (Util.DEBUG) {
					System.out.println("Restricting an observer from adding document");
				}
				return true;
			}
			if (room.newDocument(documentName)) {
				for (IClient client : room.getAllClients()) {
					client.newDocument(username, documentName);
				}
				return true;
			}
			return false;
		}
	}

	@Override
	public boolean logOut(String username) {
		for (CoLabRoom room : colabrooms.values()) {
			if (room.containsMemberByName(username)) {
				if (Util.DEBUG) {
					System.out.println("Leave colabroom based on user die");
				}
				leaveCoLabRoom(username, room.roomName());
			}
		}
		return null != regularClients.remove(username);
	}

	@Override
	public boolean subSectionLocked(String username, String roomname, String documentName,
			String sectionID) {
		if (Util.DEBUG) {
			System.out.println("Server.subSectionLocked: docname: " + documentName + " sectID: "
					+ sectionID + " LockHolder: " + username);
		}
		CoLabRoom room = colabrooms.get(roomname);
		synchronized (room) {
			CoLabRoomMember m = room.getMemberByName(username);
			if (m == null)
				return false;
			if (m.privledges() == CoLabPrivilegeLevel.OBSERVER) {
				if (Util.DEBUG) {
					System.out.println("Restricting an observer from locking subsection");
				}
				return true;
			}
			SectionizedDocument doc = room.getDocument(documentName);
			if (doc.getSection(sectionID).isLocked()) {
				if (Util.DEBUG) {
					System.out.println("Ignoring already locked lock request");
				}
				return true;
			}
			releaseLocks(doc, username, roomname);
			doc.getSection(sectionID).setLocked(true, username);
			for (IClient c : room.getAllClients()) {
				c.subsectionLocked(username, documentName, sectionID);
			}
		}
		return true;
	}

	@Override
	public boolean subSectionUnLocked(String username, String roomname, String documentName,
			String sectionID) {
		CoLabRoom room = colabrooms.get(roomname);
		synchronized (room) {
			DocumentSubSection ds = room.getDocument(documentName).getSection(sectionID);
			if (!ds.isLocked()) {
				if (Util.DEBUG) {
					System.out.println("Ignoring already unlocked unlock request");
				}
				return true;
			}
			ds.setLocked(false, username);
			System.out.println("SubSection Status->  Name: " + ds.getName() + " LockHolder: "
					+ ds.lockedByUser());
			for (IClient c : room.getAllClients()) {
				c.subsectionUnLocked(username, documentName, sectionID);
			}
		}
		return true;
	}

	@Override
	public boolean subSectionFlopped(String username, String roomname, String documentName,
			String sectionIdMoveUp, String sectionIdMoveDown) {
		CoLabRoom room = colabrooms.get(roomname);
		synchronized (room) {
			CoLabRoomMember m = room.getMemberByName(username);
			if (m.privledges() == CoLabPrivilegeLevel.OBSERVER) {
				if (Util.DEBUG) {
					System.out.println("Restricting an observer from flopping subsection");
				}
				return true;
			}
			SectionizedDocument doc = room.getDocument(documentName);
			int idx1 = doc.getSubSectionIndex(sectionIdMoveDown);
			int idx2 = doc.getSubSectionIndex(sectionIdMoveUp);
			doc.flopSubSections(idx1, idx2);
			for (IClient c : room.getAllClients()) {
				c.subsectionFlopped(username, documentName, sectionIdMoveUp, sectionIdMoveDown);
			}
		}
		return true;
	}

	@Override
	public boolean subSectionSplit(String username, String roomname, String documentName,
			String oldSection, String newName1, String newName2, int index) {
		CoLabRoom room = colabrooms.get(roomname);
		if (room == null) {
			return false;
		}
		synchronized (room) {
			CoLabRoomMember m = room.getMemberByName(username);
			if (m.privledges() == CoLabPrivilegeLevel.OBSERVER) {
				if (Util.DEBUG) {
					System.out.println("Restricting an observer from splitting subsection");
				}
				return true;
			}
			SectionizedDocument doc = room.getDocument(documentName);
			if (doc.getSubSectionIndex(newName2) != -1 && doc.getSubSectionIndex(newName1) != -1) {
				if (Util.DEBUG) {
					System.out.println("Ignoring nonunique section name split.");
				}
				return false;
			}
			doc.splitSubSection(oldSection, newName1, newName2, index, username);
			for (IClient c : room.getAllClients()) {
				c.subSectionSplit(username, documentName, oldSection, newName1, newName2, index);
			}
			return true;
		}
	}

	public boolean subSectionCombined(String username, String roomname, String documentname,
			String sectionA, String sectionB, String newName) {

		CoLabRoom room = colabrooms.get(roomname);
		synchronized (room) {
			CoLabRoomMember m = room.getMemberByName(username);
			if (m.privledges() == CoLabPrivilegeLevel.OBSERVER) {
				if (Util.DEBUG) {
					System.out.println("Restricting an observer from merging subsection");
				}
				return true;
			}
			SectionizedDocument doc = room.getDocument(documentname);
			if (!newName.equals(sectionB) && !newName.equals(sectionA)) {
				if (doc.getSubSectionIndex(newName) != -1) {
					if (Util.DEBUG) {
						System.out.println("Ignoring nonunique section name merge.");
					}
					return false;
				}
			}
			doc.combineSubSections(sectionA, sectionB, newName);
			for (IClient c : room.getAllClients()) {
				c.subSectionCombined(username, documentname, sectionA, sectionB, newName);
			}
			return true;
		}
	}

	@Override
	public boolean subSectionUpdatedInsert(String username, String roomname, String documentName,
			String sectionId, int start, String text) {
		CoLabRoom room = colabrooms.get(roomname);
		synchronized (room) {
			CoLabRoomMember m = room.getMemberByName(username);
			if (m.privledges() == CoLabPrivilegeLevel.OBSERVER) {
				if (Util.DEBUG) {
					System.out.println("Restricting an observer from updating subsection");
				}
				return true;
			}
			SectionizedDocument doc = room.getDocument(documentName);
			DocumentSubSection update = doc.getSection(sectionId);
			update.insertText(username, start, text);
			for (IClient client : room.getAllClients()) {
				client.updateSubsectionInsert(username, documentName, sectionId, start, text);
			}
		}
		return true;
	}

	@Override
	public boolean subSectionUpdatedRemove(String username, String roomname, String documentName,
			String sectionId, int start, int end) {
		CoLabRoom room = colabrooms.get(roomname);
		synchronized (room) {
			CoLabRoomMember m = room.getMemberByName(username);
			if (m.privledges() == CoLabPrivilegeLevel.OBSERVER) {
				if (Util.DEBUG) {
					System.out.println("Restricting an observer from updating subsection");
				}
				return true;
			}
			SectionizedDocument doc = room.getDocument(documentName);
			DocumentSubSection update = doc.getSection(sectionId);
			update.removeText(username, start, end);
			for (IClient client : room.getAllClients()) {
				client.updateSubsectionRemove(username, documentName, sectionId, start, end);
			}
		}
		return true;
	}

	@Override
	public boolean getAllRoomsPersisted(String username) {
		// TODO Auto-generated method stub
		List<String> roomnames = DocumentDatabaseUtil.getRoomNames(username);
		IClient client = regularClients.get(username);
		return client.listOfPersistedRooms(roomnames);
	}

	@Override
	public boolean openPersistedRoom(String username, String roomname) {
		// TODO Auto-generated method stub
		CoLabRoom fakeRoom = DocumentDatabaseUtil.getCoLabRoom(roomname);
		CoLabRoom actualRoom = new CoLabRoom(fakeRoom.roomName(), new CoLabRoomMember(username,
				regularClients.get(username)));
		for (SectionizedDocument doc : fakeRoom.getAllDocuments()) {
			SectionizedDocument docToAdd = new SectionizedDocumentImpl(doc.getName());
			for (DocumentSubSection sec: doc.getAllSubSections()){
				DocumentSubSectionImpl secToAdd = new DocumentSubSectionImpl(sec.getName());
				secToAdd.setLocked(true, "admin");
				secToAdd.setText("admin", sec.getText());
				secToAdd.setLocked(false, "admin");
				System.out.println("Section is Locked: Name: " + secToAdd.getName() + " Locked: " + ((secToAdd.lockedByUser() == null) ? "nl" : secToAdd.lockedByUser()) + " " + secToAdd.isLocked());
				docToAdd.addSubSection(secToAdd, docToAdd.getSubSectionCount());
			}
			actualRoom.addDocument(docToAdd);
		}
		colabrooms.put(roomname, actualRoom);
		return joinCoLabRoom(username, roomname, null);
	}

	@Override
	public boolean saveCoLabRoom(String username, String roomname) {
		// TODO Auto-generated method stub
		if (Util.DEBUG) {
			System.out.println("-------------------Saving colab room: " + roomname);
		}
		CoLabRoom room = colabrooms.get(roomname);
		DocumentDatabaseUtil.saveCoLab(username, room);
		for (IClient c : room.getAllClients()) {
			c.coLabRoomMemberLeft(c.getUserName());
		}
		colabrooms.remove(roomname);
		return true;
	}

	@Override
	public boolean authenticateUser(String username, byte[] password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createUser(String username, byte[] password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUser(String username) {
		// TODO Auto-generated method stub
		return false;
	}
}
