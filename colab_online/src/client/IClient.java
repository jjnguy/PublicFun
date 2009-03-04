package client;

import server.CoLabPrivledgeLevel;

public interface IClient {

	public boolean newCoLabRoomMember(String username);

	public boolean coLabRoomMemberLeft(String username);

	public boolean textInserted(int pos, String text);

	public boolean textRemoved(int posStart, int posEnd);

	public boolean textChanged(int posStart, int posEnd, String text);

	public boolean textHighlighted(int posStart, int posEnd);

	public boolean textUnHighlighted(int posStart, int posEnd);

	public boolean newChatMessage(String username, String message);

	public boolean newChatMessage(String username, String message, String recipiant);

	public boolean changeUserPrivledge(String username, CoLabPrivledgeLevel newPriv);

}
