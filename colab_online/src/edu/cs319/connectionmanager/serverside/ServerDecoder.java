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
public class ServerDecoder implements Runnable {
	
	private IServer actualServer;
	private Socket socket;
	private MessageInputStream in;
	private OutputStream out;

	public ServerDecoder(IServer actualServer, Socket socket) {
		this.actualServer = actualServer;
		this.socket = socket;
		try {
			this.in = new MessageInputStream(socket.getInputStream());
			this.out = socket.getOutputStream();
		} catch(IOException e) {
			if(Util.DEBUG) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				decodeMessage(in.readMessage());
			} catch (IOException e) {
				if (Util.DEBUG) {
					e.printStackTrace();
				}
			}
		}
	}

	public void decodeMessage(Message message) throws IOException {
		switch (message.getMessageType()) {
			case NEW_CLIENT:
				if (Util.DEBUG) {
					System.out.println("Socket port: " + s.getPort());
				}
				IClient toAdd = new ClientEncoder(message.getSentByClientName(), s);
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
