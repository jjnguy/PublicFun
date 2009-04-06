package edu.cs319.connectionmanager.clientside;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.NotYetImplementedException;
import edu.cs319.connectionmanager.messaging.Message;
import edu.cs319.connectionmanager.messaging.MessageInputStream;
import edu.cs319.util.Util;

/**
 * This class needs to be instantiated with some implementation of IClient. It listens on port 3333
 * for messages from the server.
 * 
 * It calls the proper methods on the Client passed into the constructor which will properly
 * display the changes in some way to the user.
 * 
 * @author Justin Nelson
 * @author Wayne Rowcliffe
 * 
 */
public class ClientDecoder implements Runnable {
	public static final int DEFAULT_PORT = 3333;

	private IClient actualClient;
	private MessageInputStream mIn;

	public ClientDecoder(IClient actualClient, InputStream in) {
		this.actualClient = actualClient;
		this.mIn = new MessageInputStream(in);
	}

	@Override
	public void run() {
		while(true) {
			try {
				decodeMessage(mIn.readMessage());
			} catch (IOException e) {
				if (Util.DEBUG) {
					e.printStackTrace();
				}
			}
		}
	}

	private void decodeMessage(Message message) throws IOException {
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
