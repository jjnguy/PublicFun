package edu.cs319.connectionmanager.clientside;

import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.NotYetImplementedException;
import edu.cs319.connectionmanager.messaging.Message;
import edu.cs319.connectionmanager.messaging.MessageOutputStream;
import edu.cs319.connectionmanager.messaging.MessageType;
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

	// TODO make constructor take Socket instead of host, this way we can keep it more symetrical
	// TODO with the server side
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

	// TODO deal with later, need to change the interface
	@Override
	public Collection<String> getAllCoLabRoomNames(String username) {
		Message toSend = new Message(MessageType.GET_ROOM_LIST, username, new ArrayList<String>());
		MessageOutputStream mout = new MessageOutputStream(host);
		try {
			mout.printMessage(toSend);
		} catch (IOException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}
			// return false;
		}
		// return true;
		return null;
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
	public boolean textChanged(String username, String roomname, int posStart, int posEnd,
			String text) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean textHighlighted(String username, String roomname, int posStart, int posEnd) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean textInserted(String username, String roomname, int pos, String text) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean textRemoved(String username, String roomname, int posStart, int posEnd) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean textUnHighlighted(String username, String roomname, int posStart, int posEnd) {
		throw new NotYetImplementedException();
	}

}
