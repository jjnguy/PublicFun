package client;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.gui.FullChatPanel;
import client.gui.WaitingForConnectionFrame;
import connectionmanager.IServerConnection;

public class ClientBackend implements LiveEditInterface {
	
	private static final int DEFAULT_PORT = 5001;
	private List<String> outgoingMessageBuffer;
	private boolean connected;

	private IServerConnection connection;
	
	private FullChatPanel gui;
	
	public ClientBackend(FullChatPanel gui){
		this.gui = gui;
		outgoingMessageBuffer = new ArrayList<String>();
	}
	
	@Override
	public boolean connectToCoLabServer(String host, int port) {
		if (connected())
			return true;
		connection.connect(host, port);
		connected = true;
		return this.connected();
	}
	
	@Override
	public void hostCoLabRoom() throws IOException {
		if (connected())
			return;

		WaitingForConnectionFrame f = new WaitingForConnectionFrame(ClientBackend.DEFAULT_PORT);
		connection = f.showConnectionDialog();
		if (connection == null)
			return;
		//sockIn = connection.getInputStream();
		//socOut = connection.getOutputStream();
		ClientChangeThread th = new ClientChangeThread(this);
		th.start();
		connected = true;
	}
	
	@Override
	public boolean connected() {
		return this.connected;
	}

	@Override
	public boolean newText(String text, String username) {
		return false;
	}

	@Override
	public IServerConnection getServerConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean highlightedText(int startPos, int endPos) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean movedMouse(int newX, int newY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean newText(int position, String text) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean textDeleted(int startPos, int endPos) {
		// TODO Auto-generated method stub
		return false;
	}

}
