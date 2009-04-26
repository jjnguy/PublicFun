package edu.cs319.connectionmanager.serverside;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.List;

import edu.cs319.client.IClient;
import edu.cs319.connectionmanager.messaging.Message;
import edu.cs319.connectionmanager.messaging.MessageInputStream;
import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.server.CoLabPrivilegeLevel;
import edu.cs319.server.IServer;
import edu.cs319.util.NotYetImplementedException;
import edu.cs319.util.Util;

/**
 * Listens on the server side for changes from the connected clients. Passes events from the network
 * connection on the an implementation of the Server for keeping track of all of the clients.
 * 
 * @author Justin Nelson
 * @author Wayne Rowcliffe
 */
public class ServerDecoder implements Runnable {

	private IServer actualServer;
	private Socket socket;
	private MessageInputStream in;
	private String name;

	public ServerDecoder(IServer actualServer, Socket socket) {
		this.actualServer = actualServer;
		this.socket = socket;
		try {
			this.in = new MessageInputStream(socket.getInputStream());
		} catch (IOException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				decodeMessage(in.readMessage());
			} catch (IOException e) {
				System.out.println("Things are broke: Name: " + name);
				if (name != null) {
					actualServer.logOut(name);
				}
				return;
			}
		}
	}

	public void decodeMessage(Message message) throws IOException {
		String cln = message.getSentByClientName();
		if (name == null) {
			name = cln;
		}
		List<String> args = message.getArgumentList();
		switch (message.getMessageType()) {
		case NEW_CLIENT:
			IClient toAdd = new ClientEncoder(cln, socket);
			actualServer.addNewClient(toAdd, cln);
			break;
		case NEW_COLAB_ROOM:
			actualServer.addNewCoLabRoom(cln, args.get(0), args.get(1).getBytes());
			break;
		case CHANGE_USER_PRIV:
			actualServer.changeUserPrivledge(cln, args.get(0), CoLabPrivilegeLevel
					.getPrivilegeLevelFromString(args.get(1)));
			break;
		case MEMBER_JOIN_ROOM:
			actualServer.joinCoLabRoom(cln, args.get(0), args.get(1).getBytes());
			break;
		case MEMBER_LEAVE_ROOM:
			actualServer.leaveCoLabRoom(cln, args.get(0));
			break;
		case NEW_MESSAGE:
			actualServer.newChatMessage(cln, args.get(0), args.get(1));
			break;
		case NEW_PRIVATE_MESSAGE:
			actualServer.newChatMessage(cln, args.get(0), args.get(1), args.get(2));
			break;
		case GET_ROOM_LIST:
			actualServer.getAllCoLabRoomNames(cln);
			break;
		case MEMBERS_IN_ROOM:
			actualServer.getClientsCurrentlyInRoom(cln, args.get(0));
			break;
		case NEW_SUBSECTION:
			actualServer.newSubSection(cln, args.get(0), args.get(1), args.get(2), Integer
					.parseInt(args.get(3)));
			break;
		case REMOVE_SUBSECTION:
			actualServer.subSectionRemoved(cln, args.get(0), args.get(1), args.get(2));
			break;
		case UPDATE_SUBSECTION:
			actualServer.subSectionUpdatedAll(cln, args.get(0), args.get(1), args.get(2),
					DocumentSubSection.getFromDelimmitedString(args.get(3)));
			break;
		case SUBSECTION_LOCKED:
			actualServer.subSectionLocked(cln, args.get(0), args.get(1), args.get(2));
			break;
		case SUBSECTION_UNLOCKED:
			actualServer.subSectionUnLocked(cln, args.get(0), args.get(1), args.get(2));
			break;
		case REMOVE_DOCUMENT:
			actualServer.documentRemoved(cln, args.get(0), args.get(1));
			break;
		case NEW_DOCUMENT:
			actualServer.newDocument(cln, args.get(0), args.get(1));
			break;
		case LOG_OUT:
			actualServer.logOut(cln);
			try {
				socket.close();
			} catch (IOException e) {
				if (Util.DEBUG) {
					e.printStackTrace();
				}
			}
			break;
		case SUBSECTION_FLOPPED:
			actualServer.subSectionFlopped(cln, args.get(0), args.get(1), args.get(2), args.get(3));
			break;
		case SUBSECTION_SPLIT:
			actualServer.subSectionSplit(cln, args.get(0), args.get(1), args.get(2), args.get(3),
					args.get(4), Integer.parseInt(args.get(5)));
			break;
		case SUBSECTION_COMBINE:
			actualServer.subSectionCombined(cln, args.get(0), args.get(1), args.get(2),
					args.get(3), args.get(4));
			break;
		case UPDATE_SUBSECTION_INSERT:
			actualServer.subSectionUpdatedInsert(cln, args.get(0), args.get(1), args.get(2),
					Integer.parseInt(args.get(3)), args.get(4));
			break;
		case UPDATE_SUBSECTION_REMOVE:
			actualServer.subSectionUpdatedRemove(cln, args.get(0), args.get(1), args.get(2),
					Integer.parseInt(args.get(3)), Integer.parseInt(args.get(4)));
			break;
		case ALL_PERSISTED_ROOMS:
			actualServer.getAllRoomsPersisted(cln);
			break;
		case PERSISTED_ROOM:
			actualServer.openPersistedRoom(cln, args.get(0));
			break;
		case SAVE_ROOM:
			actualServer.saveCoLabRoom(cln, args.get(0));
			break;
		case USER_AUTHENTICATE:
			if (Util.DEBUG) {
				System.out.println(args);
			}
			actualServer.authenticateUser(new ClientEncoder(cln, socket), cln, URLDecoder.decode(args.get(0), "UTF-8").getBytes());
			break;
		case USER_CREATE:
			actualServer.createUser(new ClientEncoder(cln, socket), cln, URLDecoder.decode(args.get(0), "UTF-8").getBytes());
			break;
		case USER_DELETE:
			System.err.println("98q4hpoiu3qhf9824343434343434343-yth98f)432htg-	98432	 [23yt4325");
			actualServer.deleteUser(cln);
			break;
		default:
			throw new NotYetImplementedException();
		}
	}
}
