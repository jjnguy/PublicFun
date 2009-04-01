package edu.cs319.connectionmanager.messaging;

import java.util.List;

public interface Message {
	public String getSentByClientName();

	public String getMessageType();

	public List<String> getArgumentList();
}
