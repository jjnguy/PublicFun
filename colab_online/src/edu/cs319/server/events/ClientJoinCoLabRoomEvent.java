package edu.cs319.server.events;

import edu.cs319.server.IServer;

public class ClientJoinCoLabRoomEvent implements CoLabEvent {
	private IServer theServer;
	private String username, roomname;
	private byte[] password;

	public ClientJoinCoLabRoomEvent(IServer server, String username, String roomName, byte[] password) {
		this.theServer = server;
		this.username = username;
		this.roomname = roomName;
		this.password = password;
	}

	@Override
	public boolean processEvent() {
		boolean result = theServer.joinCoLabRoom(username, roomname, password);
		return result;
	}
}
