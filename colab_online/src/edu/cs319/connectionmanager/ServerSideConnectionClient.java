package edu.cs319.connectionmanager;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.cs319.client.IClient;
import edu.cs319.server.CoLabPrivilegeLevel;

public class ServerSideConnectionClient implements IClient {
	private String username, ip;
	private int port;
	private Socket connection;

	public ServerSideConnectionClient(String username, String ip, int port)
			throws UnknownHostException, IOException {
		this.ip = ip;
		this.username = username;
		this.port = port;
		connection = new Socket(ip, port);
	}

	public String getUername() {
		return username;
	}

	// TODO do we want to keep the connections open, or do we want to re connect every time
	private PrintStream getMessageStream() {
		try {
			PrintStream out = new PrintStream(connection.getOutputStream());
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
		PrintStream out = getMessageStream();
		out.close();
		return false;
	}

	@Override
	public boolean coLabRoomMemberArrived(String username) {
		PrintStream out = getMessageStream();
		return false;
	}

	@Override
	public boolean coLabRoomMemberLeft(String username) {
		PrintStream out = getMessageStream();
		return false;
	}

	@Override
	public boolean newChatMessage(String usernameSender, String message) {
		PrintStream out = getMessageStream();
		return false;
	}

	@Override
	public boolean newChatMessage(String usernameSender, String message, String recipiant) {
		PrintStream out = getMessageStream();
		return false;
	}

	@Override
	public boolean textChanged(int posStart, int posEnd, String text) {
		PrintStream out = getMessageStream();
		return false;
	}

	@Override
	public boolean textHighlighted(int posStart, int posEnd) {
		PrintStream out = getMessageStream();
		return false;
	}

	@Override
	public boolean textInserted(int pos, String text) {
		PrintStream out = getMessageStream();
		return false;
	}

	@Override
	public boolean textRemoved(int posStart, int posEnd) {
		PrintStream out = getMessageStream();
		return false;
	}

	@Override
	public boolean textUnHighlighted(int posStart, int posEnd) {
		PrintStream out = getMessageStream();
		return false;
	}

}
