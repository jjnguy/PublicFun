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
import edu.cs319.util.NotYetImplementedException;
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
		MessageOutputStream mOut = new MessageOutputStream(host);
		try {
			mOut.printMessage(toSend);
		} catch (IOException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean addNewCoLabRoom(String username, String roomName, byte[] password) {
		List<String> args = new ArrayList<String>();
		args.add(roomName);
		args.add(new String(new byte[] {}));
		Message toSen = new Message(MessageType.NEW_COLAB_ROOM, username, args);
		MessageOutputStream mOut = new MessageOutputStream(host);
		try {
			mOut.printMessage(toSen);
		} catch (IOException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean changeUserPrivledge(String username, String roomname, CoLabPrivilegeLevel newPriv) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean joinCoLabRoom(String username, String roomName, byte[] password) {
		List<String> args = new ArrayList<String>();
		args.add(roomName);
		args.add(new String(new byte[] {}));
		Message m = new Message(MessageType.MEMBER_JOIN_ROOM, username, args);
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
	public boolean leaveCoLabRoom(String username, String rommname) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean newChatMessage(String senderName, String roomname, String message) {
		// TODO Auto-generated method stub
		List<String> args = new ArrayList<String>();
		args.add(roomname);
		args.add(message);
		Message m = new Message(MessageType.NEW_MESSAGE, senderName, args);
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
	public boolean newChatMessage(String senderName, String roomname, String message,
			String recipiant) {
		// TODO Auto-generated method stub
		List<String> args = new ArrayList<String>();
		args.add(roomname);
		args.add(message);
		args.add(recipiant);
		Message m = new Message(MessageType.NEW_PRIVATE_MESSAGE, senderName, args);
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
	public boolean getAllCoLabRoomNames(String usename) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean getClientsCurrentlyInRoom(String username, String roomName) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean newSubSection(String username, String roomname, String documentName,
			String sectionID, int idx) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean subSectionRemoved(String username, String roomname, String documentName,
			String sectionID) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean subSectionUpdated(String username, String roomname, String sectionID,
			String documentName, DocumentSubSection update) {
		throw new NotYetImplementedException();
	}
}
