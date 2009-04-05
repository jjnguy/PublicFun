package edu.cs319.connectionmanager.serverside;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.NotYetImplementedException;
import edu.cs319.connectionmanager.messaging.Message;
import edu.cs319.connectionmanager.messaging.MessageInputStream;
import edu.cs319.server.IServer;
import edu.cs319.util.Util;

/**
 * Listens on the server side for changes from the connected clients. Passes events from the network
 * connection on the an implementation of the Server for keeping track of all of the clients.
 * 
 * @author Justin Nelson
 * 
 */
public class ServerSideConnectionServer implements Runnable {
	private IServer actualServer;
	public static final int DEFAULT_PORT = 4444;

	public ServerSideConnectionServer(IServer actualServer) {
		this.actualServer = actualServer;
	}

	@Override
	public void run() {
		ServerSocket serverSOck = null;
		try {
			serverSOck = new ServerSocket(DEFAULT_PORT);
		} catch (IOException e) {
			if (Util.DEBUG)
				e.printStackTrace();
			return;
		}
		while (true) {
			try {
				Socket s = serverSOck.accept();
				// TODO implement a queue of these things, need to decode them in order, maybe not
				decodeMessage(s);
			} catch (IOException e1) {
				if (Util.DEBUG) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void decodeMessage(Socket s) throws IOException {
		MessageInputStream mIn = new MessageInputStream(s.getInputStream());
		Message message = mIn.readMessage();
		switch (message.getMessageType()) {
		case NEW_CLIENT:
			if (Util.DEBUG) {
				System.out.println("Socket port: " + s.getPort());
			}
			IClient toAdd = new ServerSideConnectionClient(message.getSentByClientName(), s);
			actualServer.addNewClient(toAdd, message.getSentByClientName());
			break;
		case NEW_COLAB_ROOM:
			actualServer.addNewCoLabRoom(message.getSentByClientName(), message.getArgumentList()
					.get(0), message.getArgumentList().get(1).getBytes());
		case MEMBER_JOIN_ROOM:
			actualServer.joinCoLabRoom(message.getSentByClientName(), message.getArgumentList()
					.get(0), message.getArgumentList().get(1).getBytes());
			break;
		case NEW_MESSAGE:
			actualServer.newChatMessage(message.getSentByClientName(), message.getArgumentList()
					.get(0), message.getArgumentList().get(1));
			break;
		case NEW_PRIVATE_MESSAGE:
			actualServer.newChatMessage(message.getSentByClientName(), message.getArgumentList()
					.get(0), message.getArgumentList().get(1), message.getArgumentList().get(2));
			break;
		default:
			throw new NotYetImplementedException();
		}
	}
}
