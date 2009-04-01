package edu.cs319.server;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;

import edu.cs319.client.IClient;
import edu.cs319.dataobjects.CoLabRoom;
import edu.cs319.dataobjects.CoLabRoomMember;
import edu.cs319.server.events.CoLabEvent;
import edu.cs319.util.Util;

public class Server implements IServer {
	/**
	 * Maps open colab rooms to their u-id
	 */
	private final Map<String, CoLabRoom> colabrooms;
	/**
	 * Maps all active clients to their u-id
	 */
	private final Map<String, IClient> regularClients;
	/**
	 * The client that is the db
	 */
	private IClient dbConnector;
	/**
	 * Queue that processes events in the order they were received
	 */
	private final CoLabEventQueue events;

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
		ServerLog.log.log(Level.INFO, "Server starting up");
		events.start();
		if (Util.DEBUG) {
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
			if (roomNames.contains(roomName)) {
				if (Util.DEBUG) {
					System.out.println("Tried to add a colabroom whos name already exists");
				}
				ServerLog.log
				.log(Level.WARNING, "Faild adding colabroom because of insitinct name");
				return false;
			}
		}
		IClient roomOwner = regularClients.get(username);
		if (roomOwner == null) {
			ServerLog.log.log(Level.WARNING,
			"Username that didn't exist tried to add new CoLabRoom");
			if (Util.DEBUG)
				System.out.println("Failed to add colab room");
			return false;
		}
		CoLabRoom roomToAdd = new CoLabRoom(roomName, new CoLabRoomMember(username, roomOwner),
				password);
		colabrooms.put(roomName, roomToAdd);

		if (Util.DEBUG) {
			System.out.println("Server: CoLab Room added successfully");
		}
		return true;
	}

	@Override
	public boolean changeUserPrivledge(String username, String roomname, CoLabPrivilegeLevel newPriv) {
		CoLabRoom room = colabrooms.get(roomname);
		if (room == null) {
			if (Util.DEBUG) {
				System.out.println("Failed to change user privledge, room didn't exist");
			}
			return false;
		}
		CoLabRoomMember member = room.getMemberByName(roomname);
		if (member == null) {
			if (Util.DEBUG) {
				System.out.println("Failed to change user privledge, user didn't exist in room");
			}
			return false;
		}
		boolean privSetSuccess = member.setPrivLevel(newPriv);
		if (privSetSuccess) {
			for (IClient client : room.getAllClients()) {
				client.changeUserPrivilege(username, newPriv);
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
		if (regularClients.containsKey(username)) {
			if (Util.DEBUG) {
				System.out.println("Failed to add new client due to non unigque username");
			}
			return false;
		}
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

	private class CoLabEventQueue extends Thread {

		private final long SLEEP_TIME = 10;

		private final Queue<CoLabEvent> events;

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
						if (Util.DEBUG)
							e.printStackTrace();
					}
				} else {
					// TODO something with the return type, like alert a user of a failed
					// action
					if (Util.DEBUG)
						System.out.println("Processing new event: " + evt.toString());
					evt.processEvent();
				}
			}
		}
	}
}
