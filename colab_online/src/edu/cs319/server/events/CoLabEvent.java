package edu.cs319.server.events;

import java.util.Collection;

import edu.cs319.client.IClient;

public interface CoLabEvent {

	public boolean processEvent();
	public Collection<String> clientsToBeUpdated();
}
