package edu.cs319.connectionmanager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.messaging.Message;
import edu.cs319.connectionmanager.messaging.MessageInputStream;
import edu.cs319.server.Server;
import edu.cs319.util.Util;

public class ServerSideConnectionServer implements Runnable {
	private Server actualServer;
	public static final int DEFAULT_PORT = 4444;

	public ServerSideConnectionServer(Server actualServer) {
		this.actualServer = actualServer;
	}

	@Override
	public void run() {
		while (true) {
			ServerSocket serverSOck = null;
			try {
				serverSOck = new ServerSocket(DEFAULT_PORT);
				Socket s = serverSOck.accept();
				// TODO implement a queue of these things, need to decode them in order
				(new DecodeMessage(s)).start();
			} catch (IOException e1) {
				if (Util.DEBUG) {
					e1.printStackTrace();
				}
			}
		}
	}

	private class DecodeMessage extends Thread {
		private Socket s;

		public DecodeMessage(Socket s) {
			this.s = s;
		}

		// TODO deal with return values
		public void decodeMessage() throws IOException {
			MessageInputStream mIn = new MessageInputStream(s.getInputStream());
			Message message = mIn.readMessage();
			switch (message.getMessageType()) {
			case NEW_CLIENT:
				IClient toAdd = new ServerSideConnectionClient(message.getSentByClientName(), s
						.getInetAddress().getHostName(), s.getPort());
				actualServer.addNewClient(toAdd, message.getSentByClientName());
				break;
			case MEMBER_JOIN_ROOM:
				actualServer.joinCoLabRoom(message.getSentByClientName(), message.getArgumentList()
						.get(0), message.getArgumentList().get(1).getBytes());
			case NEW_MESSAGE:
				actualServer.newChatMessage(message.getSentByClientName(), message
						.getArgumentList().get(0), message.getArgumentList().get(1));
			default:
				throw new NotYetImplementedException();
			}
		}

		@Override
		public void run() {
			try {
				decodeMessage();
			} catch (IOException e) {
				if (Util.DEBUG) {
					e.printStackTrace();
				}
			}
		}
	}
}
