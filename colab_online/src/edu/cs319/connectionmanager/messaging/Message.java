package edu.cs319.connectionmanager.messaging;

import java.util.List;

public class Message {
	private MessageType messageType;
	private String clientName;
	private List<String> arguments;

	public Message(MessageType m, String clientName, List<String> args) {
		this.messageType = m;
		this.arguments = args;
		this.clientName = clientName;
	}

	public String getSentByClientName() {
		return clientName;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public List<String> getArgumentList() {
		return arguments;
	}

	public byte[] encode() {
		return null;
	}

	public static Message decode(byte[] info) {
		return null;
	}
}
