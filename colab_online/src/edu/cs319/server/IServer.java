package edu.cs319.server;

import edu.cs319.client.IClient;
import edu.cs319.dataobjects.DocumentSubSection;

public interface IServer {

	public boolean addNewCoLabRoom(String username, String roomName, byte[] password);

	public boolean joinCoLabRoom(String username, String roomName, byte[] password);

	/**
	 * 
	 * @param username
	 * @return all of the names of the currently available rooms note - when iterating through this
	 *         Collection you must do it within a synchronized block
	 */
	public boolean getAllCoLabRoomNames(String usename);

	public boolean getClientsCurrentlyInRoom(String username);

	public boolean addNewClient(IClient newClient, String username);

	public boolean leaveCoLabRoom(String username, String rommname);

	public boolean newSubSection(String username, String roomname, String sectionID);

	public boolean subSectionRemoved(String username, String roomname, String sectionID);

	public boolean subSectionUpdated(String username, String roomname, String sectionID, DocumentSubSection update);

	public boolean newChatMessage(String senderName, String roomname, String message);

	public boolean newChatMessage(String senderName, String roomname, String message,
			String recipiant);

	public boolean changeUserPrivledge(String username, String roomname, CoLabPrivilegeLevel newPriv);

}
