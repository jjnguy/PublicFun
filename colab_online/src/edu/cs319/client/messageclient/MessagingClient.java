package edu.cs319.client.messageclient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.cs319.server.IServer;

public class MessagingClient extends JPanel {

	private JTextArea topText;
	private JTextField bottomText;

	private String clientID;
	private String roomName;

	// TODO set the server if connected and in a room
	private IServer server;

	public MessagingClient() {
		super(new BorderLayout());
		Dimension pref = new Dimension(200, 200);
		topText = new JTextArea();
		topText.setEditable(false);
		topText.setPreferredSize(pref);
		JScrollPane topScroll = new JScrollPane(topText);
		bottomText = new JTextField();
		bottomText.addKeyListener(enterpressedL);
		JPanel splitter = new JPanel(new BorderLayout());
		splitter.add(topScroll, BorderLayout.CENTER);
		splitter.add(bottomText, BorderLayout.SOUTH);
		this.add(splitter, BorderLayout.CENTER);
	}

	public void setServer(IServer serverP) {
		server = serverP;
	}

	public boolean newChatMessage(String usernameSender, String message) {
		String fullTExt = usernameSender + ": " + message + "\n";
		topText.append(fullTExt);
		return true;
	}

	public boolean newChatMessage(String usernameSender, String message, String recipiant) {
		if (!clientID.equals(recipiant) || !clientID.equals(usernameSender))
			return true;
		String fullTExt = usernameSender + " :<private>: " + message + "\n";
		topText.append(fullTExt);
		return true;
	}

	private KeyListener enterpressedL = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (bottomText.getText().startsWith("@")) {
					int indexOfSpace = bottomText.getText().indexOf(" ");
					if (indexOfSpace == -1)
						return;
					String recippiant = bottomText.getText().substring(1, indexOfSpace);
					String message = bottomText.getText().substring(indexOfSpace + 1);
					server.newChatMessage(clientID, roomName, message, recippiant);
				} else {
					server.newChatMessage(clientID, roomName, bottomText.getText());
				}
				bottomText.setText("");
			}
		}
	};

	public void setRoomName(String text) {
		roomName = text;
	}

}
