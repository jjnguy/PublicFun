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

import client.gui.FullChatPanel;
import client.gui.WaitingForConnectionFrame;

public class ClientBackend implements LiveEditInterface {
	
	private static final int DEFAULT_PORT = 5001;
	private List<String> outgoingMessageBuffer;
	private boolean connected;

	private Socket connection;
	private InputStream sockIn;
	private OutputStream socOut;
	
	private FullChatPanel gui;
	
	public ClientBackend(FullChatPanel gui){
		this.gui = gui;
		outgoingMessageBuffer = new ArrayList<String>();
	}
	
	@Override
	public boolean sendMessage() {
		while (!outgoingMessageBuffer.isEmpty()) {
			String outgoingMessage = outgoingMessageBuffer.remove(0);
			try {
				socOut.write(outgoingMessage.getBytes());
			} catch (IOException e) {
				return false;
			}
		}
		return true;
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
	public boolean connectToChatServer(String host, int port) {
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
		InputListenerThread th = new InputListenerThread(this);
		th.start();
		connected = true;
		return this.connected();
	}
	
	@Override
	public void hostConversation() throws IOException {
		if (connected())
			return;

		WaitingForConnectionFrame f = new WaitingForConnectionFrame(ClientBackend.DEFAULT_PORT);
		connection = f.showConnectionDialog();
		if (connection == null)
			return;
		sockIn = connection.getInputStream();
		socOut = connection.getOutputStream();
		InputListenerThread th = new InputListenerThread(this);
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
	public void newMessage(String text, String username) {
	}

}
