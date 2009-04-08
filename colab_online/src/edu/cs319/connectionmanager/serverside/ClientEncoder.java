package edu.cs319.connectionmanager.serverside;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.messaging.Message;
import edu.cs319.connectionmanager.messaging.MessageOutputStream;
import edu.cs319.connectionmanager.messaging.MessageType;
import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.server.CoLabPrivilegeLevel;
import edu.cs319.util.NotYetImplementedException;
import edu.cs319.util.Util;

/**
 * This class turns methods called on it by the Server into serialized byte strings and then sends
 * them down the network to the client.
 * 
 * @author Justin Nelson
 * 
 */
public class ClientEncoder implements IClient {
	private String username;
	private Socket connection;

	/**
	 * @param username
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public ClientEncoder(String username, Socket connection) throws UnknownHostException,
			IOException {
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
		// TODO throw new NotYetImplementedException();
		Message tosend = new Message(MessageType.MEMBER_JOIN_ROOM, username,
				new ArrayList<String>());
		MessageOutputStream out;
		try {
			out = new MessageOutputStream(connection.getOutputStream());
			out.printMessage(tosend);
		} catch (IOException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
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
	public String toString() {
		return username;
	}

	@Override
	public String getName() {
		return username;
	}

	@Override
	public boolean allCoLabRooms(Collection<String> roomNames) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean allUsersInRoom(Collection<String> usernames) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean newSubSection(String username, String sectionID, String documentName,
			DocumentSubSection section, int idx) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean subsectionLocked(String usernameSender, String documentName, String sectionID) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean subsectionUnLocked(String usernameSender, String documentName, String sectionID) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean updateAllSubsections(String documentId, List<DocumentSubSection> allSections) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean updateSubsection(String usernameSender, String documentname,
			DocumentSubSection section, String sectionID) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean subSectionRemoved(String username, String sectionID, String documentName) {
		throw new NotYetImplementedException();
	}

}
