package edu.cs319.server;

import java.util.Collection;

import edu.cs319.client.IClient;

public interface IServer {

	public boolean addNewCoLabRoom(String username, String roomName, byte[] password);

	public boolean joinCoLabRoom(String username, String roomName, byte[] password);

	/**
	 * 
	 * @param username
	 * @return all of the names of the currently available rooms note - when
	 *         iterating through this Collection you must do it within a
	 *         synchronized block
	 */
	public Collection<String> getAllCoLabRoomNames(String username);

	public boolean addNewClient(IClient newClient);

	public boolean leaveCoLabRoom(String username, String rommname);

	public boolean textInserted(String username, String roomname, int pos, String text);

	public boolean textRemoved(String username, String roomname, int posStart, int posEnd);

	public boolean textChanged(String username, String roomname, int posStart, int posEnd, String text);

	public boolean textHighlighted(String username, String roomname, int posStart, int posEnd);

	public boolean textUnHighlighted(String username, String roomname, int posStart, int posEnd);

	public boolean newChatMessage(String username, String roomname, String message);

	public boolean newChatMessage(String username, String roomname, String message, String recipiant);

	public boolean changeUserPrivledge(String username, String roomname, CoLabPrivledgeLevel newPriv);

}
