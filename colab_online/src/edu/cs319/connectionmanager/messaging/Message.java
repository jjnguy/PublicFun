package edu.cs319.connectionmanager.messaging;

import java.util.List;

public interface Message {
	public String getSentByClientName();

	public MessageType getMessageType();

	public List<String> getArgumentList();
}
