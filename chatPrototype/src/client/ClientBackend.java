package client;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import connectionmanager.IServerConnection;

import client.gui.FullChatPanel;
import client.gui.WaitingForConnectionFrame;

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
		connection.connect();
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
