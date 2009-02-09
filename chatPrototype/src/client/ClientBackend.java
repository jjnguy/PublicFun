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
	public boolean hasMesageToSend() {
		return !outgoingMessageBuffer.isEmpty();
	}

	@Override
	public void saveConversation() {
		JFileChooser choose = new JFileChooser();
		int choice = choose.showSaveDialog(gui);
		if (choice == JFileChooser.CANCEL_OPTION)
			return;

		File f = choose.getSelectedFile();

		PrintStream out = null;
		try {
			out = new PrintStream(f);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(gui, "Failed to save conversation.", "Save Fail",
					JOptionPane.ERROR_MESSAGE);
		}
		out.close();
	}

	@Override
	public boolean connectToCoLabServer(String host, int port) {
		if (connected())
			return true;
		try {
			connection = new Socket(host, port);
			sockIn = connection.getInputStream();
			socOut = connection.getOutputStream();
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(gui, "Could not resolve host.", "Not Connected!",
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(gui, "Failed to send message.",
					"Message Send Fail", JOptionPane.ERROR_MESSAGE);
		}
		ClientChangeThread th = new ClientChangeThread(this);
		th.start();
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
		sockIn = connection.getInputStream();
		socOut = connection.getOutputStream();
		ClientChangeThread th = new ClientChangeThread(this);
		th.start();
		connected = true;
	}
	

	@Override
	public InputStream getIStream() {
		return sockIn;
	}

	@Override
	public boolean connected() {
		return this.connected;
	}

	@Override
	public void newText(String text, String username) {
	}

}
