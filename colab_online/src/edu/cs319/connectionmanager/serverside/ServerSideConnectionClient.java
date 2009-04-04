package edu.cs319.connectionmanager.serverside;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.messaging.Message;
import edu.cs319.connectionmanager.messaging.MessageOutputStream;
import edu.cs319.connectionmanager.messaging.MessageType;
import edu.cs319.server.CoLabPrivilegeLevel;
import edu.cs319.util.Util;

public class ServerSideConnectionClient implements IClient {
	private String username;
	private Socket connection;

	/**
	 * @param username
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public ServerSideConnectionClient(String username, Socket connection)
			throws UnknownHostException, IOException {
		if (Util.DEBUG) {
			System.out.println("Creating server side client connection");
		}
		this.username = username;
		this.connection = connection;
	}

	// TODO encode a message and send it down the pipe

	public String getUername() {
		return username;
	}

	@Override
	public boolean changeUserPrivilege(String username, CoLabPrivilegeLevel newPriv) {
		return false;
	}

	@Override
	public boolean coLabRoomMemberArrived(String username) {
		return false;
	}

	@Override
	public boolean coLabRoomMemberLeft(String username) {
		return false;
	}

	@Override
	public boolean newChatMessage(String usernameSender, String message) {
		if (Util.DEBUG) {
			System.out.println("Sending new chat message down the pipes.  Reciever: " + username);
		}
		List<String> args = new ArrayList<String>();
		args.add(message);
		Message m = new Message(MessageType.NEW_MESSAGE, usernameSender, args);
		MessageOutputStream out;
		try {
			out = new MessageOutputStream(connection.getOutputStream());
			out.printMessage(m);
		} catch (IOException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean newChatMessage(String usernameSender, String message, String recipiant) {
		if (Util.DEBUG) {
			System.out.println("Sending new chat message down the pipes.  Reciever: " + username);
		}
		List<String> args = new ArrayList<String>();
		args.add(message);
		args.add(recipiant);
		Message m = new Message(MessageType.NEW_PRIVATE_MESSAGE, usernameSender, args);
		MessageOutputStream out;
		try {
			out = new MessageOutputStream(connection.getOutputStream());
			out.printMessage(m);
		} catch (IOException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean textChanged(int posStart, int posEnd, String text) {
		return false;
	}

	@Override
	public boolean textHighlighted(int posStart, int posEnd) {
		return false;
	}

	@Override
	public boolean textInserted(int pos, String text) {
		return false;
	}

	@Override
	public boolean textRemoved(int posStart, int posEnd) {
		return false;
	}

	@Override
	public boolean textUnHighlighted(int posStart, int posEnd) {
		return false;
	}

	@Override
	public String toString() {
		return username;
	}

	@Override
	public String getName() {
		return username;
	}

}
