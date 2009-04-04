package edu.cs319.connectionmanager.clientside;

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
import edu.cs319.connectionmanager.serverside.ServerSideConnectionServer;
import edu.cs319.server.CoLabPrivilegeLevel;
import edu.cs319.server.IServer;
import edu.cs319.util.Util;

public class ClientSideConnectionServer implements IServer {
	private String host;

	public ClientSideConnectionServer(String host) {
		this.host = host;
	}

	@Override
	public boolean addNewClient(IClient newClient, String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addNewCoLabRoom(String username, String roomName, byte[] password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean changeUserPrivledge(String username, String roomname, CoLabPrivilegeLevel newPriv) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<String> getAllCoLabRoomNames(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean joinCoLabRoom(String username, String roomName, byte[] password) {
		List<String> args = new ArrayList<String>();
		args.add(roomName);
		args.add(new String(new byte[] {}));
		Message m = new Message(MessageType.MEMBER_JOIN_ROOM, username, args);
		try {
			Socket c = new Socket(host, ServerSideConnectionServer.DEFAULT_PORT);
			MessageOutputStream out = new MessageOutputStream(c.getOutputStream());
			out.printMessage(m);
		} catch (UnknownHostException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}return false;
		} catch (IOException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}return false;
		}
		return true;
	}

	@Override
	public boolean leaveCoLabRoom(String username, String rommname) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean newChatMessage(String senderName, String roomname, String message) {
		// TODO Auto-generated method stub
		List<String> args = new ArrayList<String>();
		args.add(message);
		Message m = new Message(MessageType.MEMBER_JOIN_ROOM, senderName, args);
		try {
			Socket c = new Socket(host, ServerSideConnectionServer.DEFAULT_PORT);
			MessageOutputStream out = new MessageOutputStream(c.getOutputStream());
			out.printMessage(m);
		} catch (UnknownHostException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}return false;
		} catch (IOException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}return false;
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
		Message m = new Message(MessageType.MEMBER_JOIN_ROOM, senderName, args);
		try {
			Socket c = new Socket(host, ServerSideConnectionServer.DEFAULT_PORT);
			MessageOutputStream out = new MessageOutputStream(c.getOutputStream());
			out.printMessage(m);
		} catch (UnknownHostException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}return false;
		} catch (IOException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}return false;
		}
		return true;
	}

	@Override
	public boolean textChanged(String username, String roomname, int posStart, int posEnd,
			String text) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean textHighlighted(String username, String roomname, int posStart, int posEnd) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean textInserted(String username, String roomname, int pos, String text) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean textRemoved(String username, String roomname, int posStart, int posEnd) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean textUnHighlighted(String username, String roomname, int posStart, int posEnd) {
		// TODO Auto-generated method stub
		return false;
	}

}
