package edu.cs319.client;

import edu.cs319.server.CoLabPrivilegeLevel;

public interface IClient {

	public boolean coLabRoomMemberArrived(String username);

	public boolean coLabRoomMemberLeft(String username);

	public boolean textInserted(int pos, String text);

	public boolean textRemoved(int posStart, int posEnd);

	public boolean textChanged(int posStart, int posEnd, String text);

	public boolean textHighlighted(int posStart, int posEnd);

	public boolean textUnHighlighted(int posStart, int posEnd);

	public boolean newChatMessage(String username, String message);

	public boolean newChatMessage(String username, String message, String recipiant);

	public boolean changeUserPrivilege(String username, CoLabPrivilegeLevel newPriv);

}
