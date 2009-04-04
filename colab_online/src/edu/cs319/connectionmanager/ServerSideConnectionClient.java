package edu.cs319.connectionmanager;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.messaging.MessageOutputStream;
import edu.cs319.server.CoLabPrivilegeLevel;
import edu.cs319.util.Util;

public class ServerSideConnectionClient implements IClient {
	private String username;
	private Socket connection;

	/**
	 * @param username
	 * @param ip
	 *            the ip of the actual client on the other side
	 * @param port
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public ServerSideConnectionClient(String username, Socket connection)
			throws UnknownHostException, IOException {
		if (Util.DEBUG){
			System.out.println("Creating server side client connection");
		}
		this.username = username;
		this.connection = connection;
	}

	public String getUername() {
		return username;
	}

	// TODO do we want to keep the connections open, or do we want to re connect every time
	private MessageOutputStream getMessageOutputStream() {
		try {
			MessageOutputStream out = new MessageOutputStream(connection.getOutputStream());
			return out;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
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
		return false;
	}

	@Override
	public boolean newChatMessage(String usernameSender, String message, String recipiant) {
		return false;
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

}
