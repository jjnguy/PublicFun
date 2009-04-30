package edu.cs319.client;

import java.util.Collection;
import java.util.List;

import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.dataobjects.SectionizedDocument;
import edu.cs319.server.CoLabPrivilegeLevel;

/**
 * The IClient interface allows for interaction with a CoLab server.
 * 
 * When a client is registered with a CoLab server, they should send the server updates via the IServer interface.
 * They will then receive updates from the server via the IClient interface.
 * 
 * All of the methods in this interface return boolean types. In practice these values are meaningless.
 **/
public interface IClient {

	/**
	 * Indicates that a member has arrived to the current CoLab room.
	 * 
	 * @param username The name of the user who has just arrived.
	 * @param priv The priviledge level of the new user.
	 **/
	public boolean coLabRoomMemberArrived(String username, CoLabPrivilegeLevel priv);

	/**
	 * Indicates that a member has left the current CoLab room.
	 * 
	 * @param username The user who has left this room.
	 **/
	public boolean coLabRoomMemberLeft(String username);

	/**
	 * Indicates that all of the SubSections within the given document should be updated to match those given.
	 * 
	 * @param documentId The Document to update.
	 * @param allSections The new contents of that Document.
	 **/
	public boolean updateAllSubsections(String documentId, List<DocumentSubSection> allSections);
	
	/**
	 * Indicates that an individual SubSection should be completely updated with the given content.
	 * 
	 * @param usernameSender The sender who originally made the updates. This user must hold the lock on that SubSection.
	 * @param documentname The name of the document the SubSection is in.
	 * @param sectionId The name of the SubSection.
	 * @param section The new contents of the SubSection.
	 **/ 
	public boolean updateSubsectionAll(String usernameSender, String documentname, String sectionId, DocumentSubSection section);
	
	/**
	 * Indicates that an individual SubSection should have the given update inserted at the given position within it.
	 * 
	 * @param usernameSender The name of the user who originally made the update. This user must hold the lock on that SubSection.
	 * @param documentname The name of the document the SubSection is in.
	 * @param sectionId The name of the SubSection to be updated.
	 * @param start The character position within the SubSection to insert the update
	 * @param update The text to insert
	 **/ 		
	public boolean updateSubsectionInsert(String usernameSender, String documentname, String sectionId, int start, String update);

	/**
	 * Indicates that an individual SubSection should have the text between the given positions removed from it.
	 * 
	 * @param usernamesender The name of the user who originally made the update. This user must hold the lock on that SubSection.
	 * @param documentname The name of the document the SubSection is in.
	 * @param sectionId The name of the SubSection to be updated.
	 * @param start The character position within the SubSection to begin the remove (Inclusive)
	 * @param end The character position within the SubSection to end the remove (Exclusive)
	 **/
	public boolean updateSubsectionRemove(String usernameSender, String documentname, String sectionId, int start, int end);

	/**
	 * Indicates that an individual SubSection has been locked by the given user
	 * 
	 * @param usernameSender The user who has aquired the lock on the SubSection.
	 * @param documentName The name of the Document the the section is in.
	 * @param sectionID The name of the SubSection which has been locked
	 **/
	public boolean subsectionLocked(String usernameSender, String documentName, String sectionID);

	/**
	 * Indicates that an individual SubSection has been unlocked by the given user
	 * 
	 * @param usernameSender The user who has released the lock on the SubSection.
	 * @param documentName The name of the Document the the section is in.
	 * @param sectionID The name of the SubSection which has been unlocked
	 **/
	public boolean subsectionUnLocked(String usernameSender, String documentName, String sectionID);

	/**
	 * Indicates that two SubSections have had their positions within the Document switched.
	 * 
	 * @param usernameSender The user who performed the operation.
	 * @param documentName The document which has had its SubSections reordered.
	 * @param sectionIDMoveUp The name of the SubSection to move up in the SubSection list
	 * @param sectionIDMoveDown The name of the SubSection to move down in the SubSection list
	 **/
	public boolean subsectionFlopped(String usernameSender, String documentName,
			String sectionIDMoveUp, String sectionIDMoveDown);

	/**
	 * Indicates that a new SubSection has been added to the given document. 
	 * 
	 * @param username The user who created the SubSection.
	 * @param documentName The Document which the SubSection was added to.
	 * @param sectionId The name of the new SubSection
	 * @param section The contents of the new SubSection
	 * @param idx The position of the new SubSection within the Document's section list.
	 **/
	public boolean newSubSection(String username, String documentName, String sectionId,
			DocumentSubSection section, int idx);

	/**
	 * Indicates that a SubSection has been removed from the given document. 
	 * 
	 * @param username The user who removed the SubSection. This user must hold the lock on that SubSection.
	 * @param documentName The Document which the SubSection was removed from.
	 * @param sectionId The name of the removed SubSection
	 **/
	public boolean subSectionRemoved(String username, String documentName, String sectionId);

	/**
	 * Indicates that a SubSection has been split into two new SubSections.
	 * 
	 * @param username The user who split the SubSection. This user must hold the lock on that SubSection.
	 * @param documentName The Document which the split SubSection is in.
	 * @param oldSecName The name of the SubSection that was split
	 * @param newName1 The name of the top half of the split SubSection
	 * @param newName2 The name of the bottom half of the split SubSection
	 * @param index The character position within the SubSection the split occured at.
	 **/
	public boolean subSectionSplit(String username, String documentName, String oldSecName,
			String newName1, String newName2, int index);
		
	/**
	 * Indicates that two SubSections have been merged into one new SubSection.
	 * 
	 * @param username The user who merged the SubSections. 
	 * @param documentName The name of the Document the SubSections are contained in.
	 * @param sectionA The name of the first SubSection to merge. This must be unlocked.
	 * @param sectionB The name of the second SubSection to merge. This must be unlocked.
	 * @param newSection The name to give the new SubSection.
	 **/	
	public boolean subSectionCombined(String username, String documentName, String sectionA,
			String sectionB, String newSection);

	/**
	 * Indicates that a new Document has been created in the current CoLab room.
	 * 
	 * @param username The user who created the new Document.
	 * @param documentName The name of the new Document.
	 **/
	public boolean newDocument(String username, String documentName);
	
	/**
	 * Indicates that a Document has been removed from the current CoLab room.
	 * 
	 * @param username The user who removed the Document.
	 * @param documentName The name of the removed Document.
	 **/
	public boolean removeDocument(String username, String documentName);

	/**
	 * Indicates that a new Chat Message has been received.
	 * 
	 * @param usernameSender The user who sent the message.
	 * @param message The contents of the message.
	 **/
	public boolean newChatMessage(String usernameSender, String message);

	/**
	 * Indicates that a new Chat Message has been sent to a user.
	 * 
	 * @param usernameSender The user who sent the message.
	 * @param message The contents of the message
	 * @param recipiant The user who the message was sent to. (Presumably you)
	 **/
	public boolean newChatMessage(String usernameSender, String message, String recipiant);

	/**
	 * Indicates that the given user's priviledge has been changed.
	 * 
	 * @param username The user whose priviledge has been changed.
	 * @param newPriv The new priviledge level of the user.
	 **/
	public boolean changeUserPrivilege(String username, CoLabPrivilegeLevel newPriv);

	/**
	 * Lists all users in the current CoLab room.
	 * 
	 * @param usernames The list of users in the room.
	 * @param privs The priviledge levels of the users, where user[i] has priviledge privs[i]
	 **/
	public boolean allUsersInRoom(List<String> usernames, List<CoLabPrivilegeLevel> privs);

	/**
	 * Lists all CoLab rooms currently open on the server.
	 * 
	 * @param roomNames The names of the rooms which are currently open on the server
	 **/
	public boolean allCoLabRooms(Collection<String> roomNames);
	
	/**
	 * Lists the CoLab rooms which this user has saved on the server.
	 * 
	 * @param roomnames The names of the rooms which are currently saved for the user on the server.
	 **/
	public boolean listOfPersistedRooms(Collection<String> roomnames);
	
	/**
	 * Never Used / UnNeeded
	 **/
	@Deprecated
	public boolean persistedCoLabRoom(List<SectionizedDocument> documentsInRoom);
	
	/**
	 * Indicates whether the given user has been logged into the server successfully
	 * 
	 * @param username The user who is being authenticated (Presumably you)
	 * @param success Whether the user was authentiated and logged in by the server.
	 **/
	public boolean userAuthenticatedResult(String username, boolean success);
	
	/**
	 * Indicates whether the given user has been created on the server.
	 * 
	 * @param username The user attemptin to create a new account (Presumably you)
	 * @param success Whether the user has succeeded in creating a new account.
	 **/
	public boolean userCreateResult(String username, boolean success);
	
	/**
	 * Returns the userName used on this client.
	 * 
	 * @return the userName used on this client.
	 */
	public String getUserName();
	
	/**
	 * Sets the userName variable to the input username.
	 * 
	 * @param username - String to change userName to
	 */
	public void setUserName(String username);
	
}
