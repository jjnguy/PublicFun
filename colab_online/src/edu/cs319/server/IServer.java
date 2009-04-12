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

	public boolean newSubSection(String username, String roomname, String documentName,
			String sectionID, int idx);

	public boolean subSectionRemoved(String username, String roomname, String documentName,
			String sectionID);

	public boolean subSectionUpdated(String username, String roomname, String sectionID,
			String documentName, DocumentSubSection update);

	public boolean newChatMessage(String senderName, String roomname, String message);

	public boolean newChatMessage(String senderName, String roomname, String message,
			String recipiant);

	public boolean changeUserPrivledge(String username, String roomname, CoLabPrivilegeLevel newPriv);

}
