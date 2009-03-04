package server;

import java.util.List;

public interface IServer {

	public boolean addNewCoLabRoom(String username, String roomName, byte[] password);

	public boolean joinCoLabRoom(String username, String roomName, byte[] password);

	public List<String> getAllCoLabRoomNames(String username);

	public boolean leaveCoLabRoom(String username, String rommname);

	public boolean textInserted(String username, String roomname, int pos, String text);

	public boolean textRemoved(String username, String roomname, int posStart, int posEnd);

	public boolean textChanged(String username, String roomname, int posStart, int posEnd,
			String text);

	public boolean textHighlighted(String username, String roomname, int posStart, int posEnd);

	public boolean textUnHighlighted(String username, String roomname, int posStart, int posEnd);

	public boolean newChatMessage(String username, String roomname, String message);

	public boolean newChatMessage(String username, String roomname, String message,
			String recipiant);

	public boolean changeUserPrivledge(String username, String roomname,
			CoLabPrivledgeLevel newPriv);

}
