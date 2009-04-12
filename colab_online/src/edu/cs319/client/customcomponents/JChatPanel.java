package edu.cs319.client.customcomponents;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.cs319.server.IServer;

/**
 * 
 * @author Amelia Gee
 * @author Justin Nelson
 * 
 */
public class JChatPanel extends JPanel {

	private JTextArea topText;
	private JTextField bottomText;

	private String clientID;
	private String roomName;

	private TrayIcon trayI;

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

	public void connect(IServer serverP, String clientID, String roomName) throws IOException {
		server = serverP;
		this.clientID = clientID;
		this.roomName = roomName;
		// TODO get icons
		this.trayI = new TrayIcon(ImageIO.read(new File("images/tempIcon.bmp")));
		SystemTray tray = SystemTray.getSystemTray();
		try {
			tray.add(trayI);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void newChatMessage(String usernameSender, String message) {
		String fullTExt = usernameSender + ": " + message + "\n";
		topText.append(fullTExt);
		if (!isVisible())
			displayBottomPopup(usernameSender, message);
	}

	public void newChatMessage(String usernameSender, String message, String recipiant) {
		if (!clientID.equals(recipiant) && !clientID.equals(usernameSender))
			return;
		String fullTExt = usernameSender + " :<private>: " + message + "\n";
		topText.append(fullTExt);
		if (!isVisible())
			displayBottomPopup(usernameSender, message);
	}

	private void displayBottomPopup(String usernameSender, String message) {
		trayI.displayMessage("New Message From " + usernameSender, message, MessageType.INFO);
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
