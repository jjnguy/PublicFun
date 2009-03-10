package edu.cs319.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import edu.cs319.client.IClient;
import edu.cs319.dataobjects.CoLabRoom;
import edu.cs319.dataobjects.CoLabRoomMember;
import edu.cs319.util.Util;

public class Server implements IServer {

	private Map<String, CoLabRoom> colabrooms;
	private Map<String, IClient> regularClients;
	private IClient dbConnector;

	/**
	 * Creates a new server
	 */
	public Server() {
		// Lets be thread safe about this
		// Its best to always use protection
		colabrooms = Collections.synchronizedMap(new HashMap<String, CoLabRoom>());
		regularClients = Collections.synchronizedMap(new HashMap<String, IClient>());
	}

	public void startup() throws IOException {

	}

	@Override
	public boolean addNewCoLabRoom(String username, String roomName, byte[] password) {
		if (Util.DEBUG) {
			System.out.println("Server: Adding new CoLab Room");
		}

		Set<String> roomNames = colabrooms.keySet();
		synchronized (colabrooms) {
			if (roomNames.contains(roomName)) return false;
		}

		CoLabRoom roomToAdd = new CoLabRoom(roomName, new CoLabRoomMember(username), password);
		colabrooms.put(roomName, roomToAdd);

		if (Util.DEBUG) {
			System.out.println("Server: CoLab Room added successfully");
		}
		return true;
	}

	@Override
	public boolean changeUserPrivledge(String username, String roomname, CoLabPrivledgeLevel newPriv) {
		CoLabRoom room = colabrooms.get(roomname);
		CoLabRoomMember member = room.getMemberByName(roomname);
		boolean privSetSuccess = member.setPrivLevel(newPriv);
		if (privSetSuccess) {
			Collection<IClient> clients = regularClients.values();
			for (IClient client : clients) {
				client.changeUserPrivledge(username, newPriv);
			}
		}
		return privSetSuccess;
	}

	@Override
	public Collection<String> getAllCoLabRoomNames(String username) {
		return colabrooms.keySet();
	}

	@Override
	public boolean addNewClient(IClient newClient, String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean joinCoLabRoom(String username, String roomName, byte[] password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean leaveCoLabRoom(String username, String rommname) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean newChatMessage(String username, String roomname, String message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean newChatMessage(String username, String roomname, String message, String recipiant) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean textChanged(String username, String roomname, int posStart, int posEnd, String text) {
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
