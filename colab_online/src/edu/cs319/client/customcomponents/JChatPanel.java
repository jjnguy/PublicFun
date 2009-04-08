package edu.cs319.client.customcomponents;

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

/**
 * 
 * @author Amelia
 * 
 */
public class JChatPanel extends JPanel {

	private JTextArea topText;
	private JTextField bottomText;

	private String clientID;
	private String roomName;

	// TODO set the server if connected and in a room
	private IServer server;

	public JChatPanel() {
		super(new BorderLayout(10, 10));
		Dimension pref = new Dimension(250, 200);
		topText = new JTextArea();
		topText.setEditable(false);
		topText.setPreferredSize(pref);
		JScrollPane topScroll = new JScrollPane(topText);
		bottomText = new JTextField();
		bottomText.addKeyListener(enterpressedL);
		add(topScroll, BorderLayout.CENTER);
		add(bottomText, BorderLayout.SOUTH);
	}

	public void connect(IServer serverP, String clientID, String roomName) {
		server = serverP;
	}

	public void newChatMessage(String usernameSender, String message) {
		String fullTExt = usernameSender + ": " + message + "\n";
		topText.append(fullTExt);
	}

	public void newChatMessage(String usernameSender, String message, String recipiant) {
		if (!clientID.equals(recipiant) && !clientID.equals(usernameSender))
			return;
		String fullTExt = usernameSender + " :<private>: " + message + "\n";
		topText.append(fullTExt);
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

}
