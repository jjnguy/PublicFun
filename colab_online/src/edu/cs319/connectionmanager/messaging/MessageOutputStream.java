package edu.cs319.connectionmanager.messaging;

import java.io.IOException;
import java.io.OutputStream;

public class MessageOutputStream {
	OutputStream out;

	public MessageOutputStream(OutputStream s) {
		out = s;
	}

	public void printMessage(Message message) throws IOException {
		out.write(message.encode());
	}
}
