package edu.cs319.server.events;

import edu.cs319.server.IServer;

public class NewCoLabRoomEvent implements CoLabEvent {

	private final IServer theServer;
	private final String newName;
	private final String foundingMemberName;
	private final byte[] password;

	public NewCoLabRoomEvent(IServer server, String colabRoomName, String foundingMember,
			byte[] password) {
		this.theServer = server;
		this.newName = colabRoomName;
		this.foundingMemberName = foundingMember;
		this.password = password;
	}

	@Override
	public boolean processEvent() {
		boolean result = theServer.addNewCoLabRoom(foundingMemberName, newName, password);
		return result;
	}

	@Override
	public String toString() {
		return "NewCoLabRoomEvent: RoomName: " + this.foundingMemberName + ", MemberName: "
		+ this.foundingMemberName;
	}
}
