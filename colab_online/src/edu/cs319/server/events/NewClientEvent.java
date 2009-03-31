package edu.cs319.server.events;

import edu.cs319.client.IClient;
import edu.cs319.server.IServer;

public class NewClientEvent implements CoLabEvent {

	private final IServer theServer;
	private final IClient theClient;
	private final String username;

	public NewClientEvent(IServer server, IClient client, String username) {
		this.theClient = client;
		this.theServer = server;
		this.username = username;
	}

	@Override
	public boolean processEvent() {
		boolean result = theServer.addNewClient(theClient, username);
		return result;
	}

}
