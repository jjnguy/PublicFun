package edu.cs319.server;

import edu.cs319.client.IClient;
import edu.cs319.dataobjects.DocumentSubSection;

public interface IServer {

	public boolean addNewCoLabRoom(String username, String roomName, byte[] password);

	public boolean joinCoLabRoom(String username, String roomName, byte[] password);

	public boolean getAllCoLabRoomNames(String username);

	public boolean getClientsCurrentlyInRoom(String username, String roomName);

	public boolean addNewClient(IClient newClient, String username);

	public boolean leaveCoLabRoom(String username, String roomname);

	public boolean newDocument(String username, String roomname, String documentName);

	public boolean documentRemoved(String username, String roomname, String documentName);

	public boolean newSubSection(String username, String roomname, String documentName,
			String sectionID, int idx);

	public boolean subSectionRemoved(String username, String roomname, String documentName,
			String sectionID);

	public boolean subSectionUpdated(String username, String roomname, String documentName,
			String sectionID, DocumentSubSection update);

	public boolean subSectionLocked(String username, String roomname, String documentName,
			String sectionId);

	public boolean subSectionUnLocked(String username, String roomname, String documentName,
			String sectionId);

	public boolean subSectionFlopped(String username, String roomname, String documentName,
			String sectionIdMoveUp, String sectionIdMoveDown);

	public boolean subSectionSplit(String username, String roomname, String documentName,
			String oldSection, String newName1, String newName2, int index);

	public boolean subSectionCombined(String username, String roomname, String documentname,
			String sectionA, String sectionB, String newName);

	public boolean newChatMessage(String senderName, String roomname, String message);

	public boolean newChatMessage(String senderName, String roomname, String message,
			String recipiant);

	public boolean changeUserPrivledge(String username, String roomname, CoLabPrivilegeLevel newPriv);

	public boolean logOut(String username);

}
