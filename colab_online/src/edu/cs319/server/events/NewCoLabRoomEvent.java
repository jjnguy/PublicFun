package edu.cs319.server.events;

import java.util.Collection;

import edu.cs319.server.IServer;

public class NewCoLabRoomEvent implements CoLabEvent {

	private IServer theServer;
	private String newName;
	private String foundingMemberName;
	private byte[] password;

	public NewCoLabRoomEvent(IServer server, String colabRoomName, String foundingMember,
			byte[] password) {
		this.theServer = server;
		this.newName = colabRoomName;
		this.foundingMemberName = foundingMember;
		this.password = password;
	}

	@Override
	public Collection<String> clientsToBeUpdated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean processEvent() {
		boolean result = theServer.addNewCoLabRoom(foundingMemberName, newName, password);
		return result;
	}
}
