package edu.cs319.connectionmanager.clientside;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.NotYetImplementedException;
import edu.cs319.connectionmanager.messaging.Message;
import edu.cs319.connectionmanager.messaging.MessageInputStream;
import edu.cs319.util.Util;

public class ClientSideConnectionClient implements Runnable {
	public static final int DEFAULT_PORT = 3333;

	private IClient actualClient;

	public ClientSideConnectionClient(IClient actualClient) {
		this.actualClient = actualClient;
	}

	@Override
	public void run() {
		ServerSocket servSock = null;
		try {
			servSock = new ServerSocket(DEFAULT_PORT);
		} catch (IOException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}
			return;
		}
		while (true) {
			try {
				Socket s = servSock.accept();
				decodeMessage(s);
			} catch (IOException e) {
				if (Util.DEBUG) {
					e.printStackTrace();
				}
			}
		}
	}

	private void decodeMessage(Socket s) throws IOException {
		// TODO implement all messages
		MessageInputStream mIn = new MessageInputStream(s.getInputStream());
		Message message = mIn.readMessage();
		switch (message.getMessageType()) {
		case NEW_MESSAGE:
			actualClient.newChatMessage(message.getSentByClientName(), message.getArgumentList()
					.get(0));
			break;
		case NEW_PRIVATE_MESSAGE:
			actualClient.newChatMessage(message.getSentByClientName(), message.getArgumentList()
					.get(0), message.getArgumentList().get(1));
			break;
		default:
			throw new NotYetImplementedException();
		}
	}

}
