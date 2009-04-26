package edu.cs319.client;

import java.util.Collection;
import java.util.List;

import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.dataobjects.SectionizedDocument;
import edu.cs319.server.CoLabPrivilegeLevel;

public interface IClient {

	public boolean coLabRoomMemberArrived(String username, CoLabPrivilegeLevel priv);

	public boolean coLabRoomMemberLeft(String username);

	public boolean updateAllSubsections(String documentId, List<DocumentSubSection> allSections);

	public boolean updateSubsectionAll(String usernameSender, String documentname, String sectionId, DocumentSubSection section);
			
	public boolean updateSubsectionInsert(String usernameSender, String documentname, String sectionId, int start, String update);

	public boolean updateSubsectionRemove(String usernameSender, String documentname, String sectionId, int start, int end);

	public boolean subsectionLocked(String usernameSender, String documentName, String sectionID);

	public boolean subsectionUnLocked(String usernameSender, String documentName, String sectionID);

	public boolean subsectionFlopped(String usernameSender, String documentName,
			String sectionIDMoveUp, String sectionIDMoveDown);

	public boolean newSubSection(String username, String documentName, String sectionId,
			DocumentSubSection section, int idx);

	public boolean subSectionRemoved(String username, String documentName, String sectionId);

	public boolean subSectionSplit(String username, String documentName, String oldSecName,
			String newName1, String newName2, int index);
			
	public boolean subSectionCombined(String username, String documentName, String sectionA,
			String sectionB, String newSection);

	public boolean newDocument(String username, String documentName);

	public boolean removeDocument(String username, String documentName);

	public boolean newChatMessage(String usernameSender, String message);

	public boolean newChatMessage(String usernameSender, String message, String recipiant);

	public boolean changeUserPrivilege(String username, CoLabPrivilegeLevel newPriv);

	public boolean allUsersInRoom(List<String> usernames, List<CoLabPrivilegeLevel> privs);

	public boolean allCoLabRooms(Collection<String> roomNames);
	
	public boolean listOfPersistedRooms(Collection<String> romnames);
	
	public boolean persistedCoLabRoom(List<SectionizedDocument> documentsInRoom);
	
	public boolean userAuthenticated(String username, boolean success);

	/**
	 * Returns the userName used on this client.
	 * @return the userName used on this client.
	 */
	public String getUserName();
	
	/**
	 * Sets the userName variable to the input username.
	 * @param username - String to change userName to
	 */
	public void setUserName(String username);
	
}
