package edu.cs319.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;

import edu.cs319.client.IClient;
import edu.cs319.dataobjects.CoLabRoom;
import edu.cs319.dataobjects.CoLabRoomMember;
import edu.cs319.server.events.CoLabEvent;
import edu.cs319.util.JustinsQueue;
import edu.cs319.util.Util;

public class Server implements IServer {

	private Map<String, CoLabRoom> colabrooms;
	private Map<String, IClient> regularClients;
	private Map<CoLabRoom, List<String>> usersInTheRooms;
	private IClient dbConnector;
	private CoLabEventQueue events;

	/**
	 * Creates a new server
	 */
	public Server() {
		// Lets be thread safe about this
		// Its best to always use protection
		colabrooms = Collections.synchronizedMap(new HashMap<String, CoLabRoom>());
		regularClients = Collections.synchronizedMap(new HashMap<String, IClient>());
		events = new CoLabEventQueue();
	}

	public void startup() throws IOException {
		events.start();
		if (Util.DEBUG){
			System.out.println("Server Starting up...");
		}
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

	/**
	 * A client is tied to the user that controls it
	 */
	@Override
	public boolean addNewClient(IClient newClient, String username) {
		regularClients.put(username, newClient);
		return true;
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

	private class CoLabEventQueue extends Thread {

		private long SLEEP_TIME = 100;

		private Queue<CoLabEvent> events;

		public CoLabEventQueue() {
			events = new LinkedList<CoLabEvent>();
		}

		public synchronized void addEvent(CoLabEvent evnt) {
			events.add(evnt);
		}

		@Override
		public void run() {
			while (true) {
				CoLabEvent evt = events.poll();
				if (evt == null) {
					try {
						Thread.sleep(SLEEP_TIME);
					} catch (InterruptedException e) {
						if (Util.DEBUG) e.printStackTrace();
					}
				} else {
					evt.processEvent();
				}
			}
		}
	}
}
