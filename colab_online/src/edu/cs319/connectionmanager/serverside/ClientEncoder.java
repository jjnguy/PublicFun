package edu.cs319.connectionmanager.serverside;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.NotYetImplementedException;
import edu.cs319.connectionmanager.messaging.Message;
import edu.cs319.connectionmanager.messaging.MessageOutputStream;
import edu.cs319.connectionmanager.messaging.MessageType;
import edu.cs319.server.CoLabPrivilegeLevel;
import edu.cs319.util.Util;

/**
 * This class turns methods called on it by the Server into serialized byte strings and then sends
 * them down the network to the client.
 * 
 * @author Justin Nelson
 * 
 */
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
		throw new NotYetImplementedException();
	}

	@Override
	public boolean coLabRoomMemberArrived(String username) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean coLabRoomMemberLeft(String username) {
		throw new NotYetImplementedException();
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
		throw new NotYetImplementedException();
	}

	@Override
	public boolean textHighlighted(int posStart, int posEnd) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean textInserted(int pos, String text) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean textRemoved(int posStart, int posEnd) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean textUnHighlighted(int posStart, int posEnd) {
		throw new NotYetImplementedException();
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
