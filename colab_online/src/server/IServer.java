package server;

public interface IServer {

	public boolean addNewCoLabRoom(String username, String roomName, byte[] password,
			String host, int port);

	public boolean joinCoLabRoom(String username, String roomName, byte[] password,
			String host, int port);

	public boolean leaveCoLabRoom(String username, String rommname, String host, int port);

	public boolean textInserted(String username, String roomname, String host, int port,
			int pos, String text);

	public boolean textRemoved(String username, String roomname, String host, int port,
			int posStart, int posEnd);

	public boolean textChanged(String username, String roomname, String host, int port,
			int posStart, int posEnd, String text);

	public boolean textHighlighted(String username, String roomname, String host, int port,
			int posStart, int posEnd);

	public boolean textUnHighlighted(String username, String roomname, String host, int port,
			int posStart, int posEnd);

	public boolean newChatMessage(String username, String roomname, String host, int port,
			String message);

	public boolean newChatMessage(String username, String roomname, String host, int port,
			String message, String recipiant);

}
