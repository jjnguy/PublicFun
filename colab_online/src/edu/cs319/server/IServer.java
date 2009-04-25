package edu.cs319.server;

import edu.cs319.client.IClient;
import edu.cs319.dataobjects.DocumentSubSection;

public interface IServer {

	/**
	 * Adds a new CoLabRoom the the server
	 * 
	 * @param username
	 *            the user who wants to add the room
	 * @param roomName
	 *            the name of the new room
	 * @param password
	 *            the password of the room, can be null
	 * @return whether or not the add was successful
	 */
	public boolean addNewCoLabRoom(String username, String roomName, byte[] password);

	/**
	 * Adds user to a CoLabRoom
	 * 
	 * @param username
	 *            the member that wants to join
	 * @param roomName
	 *            the name of the room the user wants to join
	 * @param password
	 *            the password of the room
	 * @return whether or not the join was successful
	 */
	public boolean joinCoLabRoom(String username, String roomName, byte[] password);

	/**
	 * Alerts the server that a client would like the names of all of the CoLabRooms on the server.
	 * 
	 * @param username
	 *            the user who wants the list
	 * @return nothing important
	 */
	public boolean getAllCoLabRoomNames(String username);

	/**
	 * Alerts the Server that a client would like to know who is all in a CoLabRoom
	 * 
	 * @param username
	 *            the name of the user who wants the info
	 * @param roomName
	 *            the name of the room
	 * @return nothing important
	 */
	public boolean getClientsCurrentlyInRoom(String username, String roomName);

	/**
	 * Adds a new client to the server
	 * 
	 * @param newClient
	 *            an instance of IClient
	 * @param username
	 *            the name of the new client
	 * @return nothing important
	 */
	public boolean addNewClient(IClient newClient, String username);

	/**
	 * Alerts the server that a member would like to leave a CoLabRoom
	 * 
	 * @param username
	 *            the name of the user who would like to leave
	 * @param roomname
	 *            the name of the room to leave
	 * @return nothing important
	 */
	public boolean leaveCoLabRoom(String username, String roomname);

	/**
	 * Adds a new document to a CoLabRoom
	 * 
	 * @param username
	 *            the name of the user who added the document
	 * @param roomname
	 *            the name of the room to add the document to
	 * @param documentName
	 *            the name of the new document
	 * @return nada
	 */
	public boolean newDocument(String username, String roomname, String documentName);

	/**
	 * Alerts the Server that a document is removed from a Room
	 * 
	 * @param username
	 *            the name of the user who removed the doc
	 * @param roomname
	 *            the name of the room from which the document was removed
	 * @param documentName
	 *            the name of the document that was removed
	 * @return nada
	 */
	public boolean documentRemoved(String username, String roomname, String documentName);

	/**
	 * Alerts the server that a new subsection is to be created in a document
	 * 
	 * @param username
	 *            the name of the user that added the subsection
	 * @param roomname
	 *            the name of the room where the document should be added
	 * @param documentName
	 *            the name of the document where the section should be added
	 * @param sectionID
	 *            the name of the new section
	 * @param idx
	 *            the index where the section should be added
	 * @return nada
	 */
	public boolean newSubSection(String username, String roomname, String documentName,
			String sectionID, int idx);

	/**
	 * Alerts the server that a user wants to remove a subsection
	 * 
	 * @param username
	 *            the name of the user who wants to remove the subsection
	 * @param roomname
	 *            the name of the room from which to remove the subsection
	 * @param documentName
	 *            the name of the document from which to remove the subsection
	 * @param sectionID
	 *            the name of the subsection to remove
	 * @return nada
	 */
	public boolean subSectionRemoved(String username, String roomname, String documentName,
			String sectionID);

	public boolean subSectionUpdatedAll(String username, String roomname, String documentName, String sectionID, DocumentSubSection update);

	public boolean subSectionUpdatedInsert(String username, String roomname, String documentName, String sectionId, int start, String update);
	
	public boolean subSectionUpdatedRemove(String username, String roomname, String documentName, String sectionId, int start, int end);

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
	
	public boolean getAllRoomsPersisted(String username);
	
	public boolean openPersistedRoom(String username, String roomname);
	
	public boolean saveCoLabRoom(String username, String roomname);

}
