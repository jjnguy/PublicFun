package server;

import java.util.List;

import dataobjects.CoLabRoom;

public class Server implements IServer {
	
	private List<CoLabRoom> colabrooms;
	
	@Override
	public boolean addNewCoLabRoom(String username, String roomName, byte[] password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean changeUserPrivledge(String username, String roomname, CoLabPrivledgeLevel newPriv) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getAllCoLabRoomNames(String username) {
		// TODO Auto-generated method stub
		return null;
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
