package edu.cs319.connectionmanager.clientside;

import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.messaging.Message;
import edu.cs319.connectionmanager.messaging.MessageOutputStream;
import edu.cs319.connectionmanager.messaging.MessageType;
import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.server.CoLabPrivilegeLevel;
import edu.cs319.server.IServer;
import edu.cs319.util.Util;

/**
 * When a client calls methods on this class it then transforms those messages to serialized byte
 * strings, and then passes them down to the Server over the network.
 * 
 * 
 * @author Justin Nelson
 * 
 */
public class ServerEncoder implements IServer {
	private OutputStream host;

	public ServerEncoder(OutputStream outputStream) {
		this.host = outputStream;
	}

	@Override
	public boolean addNewClient(IClient newClient, String username) {
		Message toSend = new Message(MessageType.NEW_CLIENT, username, new ArrayList<String>());
		return printMessageToStream(toSend);
	}

	@Override
	public boolean addNewCoLabRoom(String username, String roomName, byte[] password) {
		List<String> args = new ArrayList<String>();
		args.add(roomName);
		args.add(new String(new byte[] {}));
		Message toSen = new Message(MessageType.NEW_COLAB_ROOM, username, args);
		return printMessageToStream(toSen);
	}

	@Override
	public boolean changeUserPrivledge(String username, String roomname, CoLabPrivilegeLevel newPriv) {
		List<String> args = new ArrayList<String>();
		args.add(roomname);
		args.add(newPriv.toString());
		Message m = new Message(MessageType.CHANGE_USER_PRIV, username, args);
		return printMessageToStream(m);
	}

	@Override
	public boolean joinCoLabRoom(String username, String roomName, byte[] password) {
		List<String> args = new ArrayList<String>();
		args.add(roomName);
		args.add(new String(new byte[] {}));
		Message m = new Message(MessageType.MEMBER_JOIN_ROOM, username, args);
		return printMessageToStream(m);
	}

	@Override
	public boolean leaveCoLabRoom(String username, String roomname) {
		List<String> args = new ArrayList<String>();
		args.add(roomname);
		Message m = new Message(MessageType.MEMBER_LEAVE_ROOM, username, args);
		return printMessageToStream(m);
	}

	@Override
	public boolean newChatMessage(String senderName, String roomname, String message) {
		List<String> args = new ArrayList<String>();
		args.add(roomname);
		args.add(message);
		Message m = new Message(MessageType.NEW_MESSAGE, senderName, args);
		return printMessageToStream(m);
	}

	@Override
	public boolean newChatMessage(String senderName, String roomname, String message,
			String recipiant) {
		List<String> args = new ArrayList<String>();
		args.add(roomname);
		args.add(message);
		args.add(recipiant);
		Message m = new Message(MessageType.NEW_PRIVATE_MESSAGE, senderName, args);
		return printMessageToStream(m);
	}

	private boolean printMessageToStream(Message m) {
		try {
			MessageOutputStream out = new MessageOutputStream(host);
			out.printMessage(m);
		} catch (UnknownHostException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}
			return false;
		} catch (IOException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean getAllCoLabRoomNames(String username) {
		List<String> args = new ArrayList<String>();
		Message m = new Message(MessageType.GET_ROOM_LIST, username, args);
		return printMessageToStream(m);
	}

	@Override
	public boolean getClientsCurrentlyInRoom(String username, String roomName) {
		List<String> args = new ArrayList<String>();
		args.add(roomName);
		Message m = new Message(MessageType.MEMBERS_IN_ROOM, username, args);
		return printMessageToStream(m);
	}

	@Override
	public boolean newSubSection(String username, String roomname, String documentName,
			String sectionID, int idx) {
		List<String> args = new ArrayList<String>();
		args.add(roomname);
		args.add(documentName);
		args.add(sectionID);
		args.add(Integer.toString(idx));
		Message m = new Message(MessageType.NEW_SUBSECTION, username, args);
		return printMessageToStream(m);
	}

	@Override
	public boolean subSectionRemoved(String username, String roomname, String documentName,
			String sectionID) {
		List<String> args = new ArrayList<String>();
		args.add(roomname);
		args.add(documentName);
		args.add(sectionID);
		Message m = new Message(MessageType.REMOVE_SUBSECTION, username, args);
		return printMessageToStream(m);
	}

	@Override
	public boolean subSectionUpdated(String username, String roomname, String sectionID,
			String documentName, DocumentSubSection update) {
		List<String> args = new ArrayList<String>();
		args.add(roomname);
		args.add(sectionID);
		args.add(documentName);
		args.add(update.toDelimmitedString());
		Message m = new Message(MessageType.UPDATE_SUBSECTION, username, args);
		return printMessageToStream(m);
	}

	@Override
	public boolean documentRemoved(String username, String roomname, String documentName) {
		List<String> args = new ArrayList<String>();
		args.add(roomname);
		args.add(documentName);
		Message m = new Message(MessageType.REMOVE_DOCUMENT, username, args);
		return printMessageToStream(m);
	}

	@Override
	public boolean newDocument(String username, String roomname, String documentName) {
		List<String> args = new ArrayList<String>();
		args.add(roomname);
		args.add(documentName);
		Message m = new Message(MessageType.NEW_DOCUMENT, username, args);
		return printMessageToStream(m);
	}

	@Override
	public boolean logOut(String username) {
		Message m = new Message(MessageType.LOG_OUT, username, new ArrayList<String>());
		return printMessageToStream(m);
	}

}
