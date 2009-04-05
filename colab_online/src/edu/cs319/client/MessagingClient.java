package edu.cs319.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.cs319.connectionmanager.NotYetImplementedException;
import edu.cs319.connectionmanager.clientside.ClientSideConnectionServer;
import edu.cs319.server.CoLabPrivilegeLevel;

public class MessagingClient extends JFrame implements IClient {

	private JTextArea topText;
	private JTextField bottomText;
	private JList membersInRoom;

	private ClientSideConnectionServer connection;

	public MessagingClient() {
		super("CoLabMessaging");
		membersInRoom = new JList();
		membersInRoom.setPreferredSize(new Dimension(100, 210));
		Dimension pref = new Dimension(200, 200);
		topText = new JTextArea();
		topText.setEditable(false);
		topText.setPreferredSize(pref);
		JScrollPane topScroll = new JScrollPane(topText);
		bottomText = new JTextField();
		JPanel splitter = new JPanel(new BorderLayout());
		splitter.add(topScroll, BorderLayout.CENTER);
		splitter.add(bottomText, BorderLayout.SOUTH);
		this.add(membersInRoom, BorderLayout.WEST);
		this.add(splitter, BorderLayout.CENTER);
		setJMenuBar(createMenuBar());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private JMenuBar createMenuBar() {
		JMenuBar ret = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem logInToServer = new JMenuItem("Log in to Server");
		JMenuItem showCoLabRooms = new JMenuItem("Show CoLabRooms");
		file.add(logInToServer);
		file.add(showCoLabRooms);
		ret.add(file);
		return ret;
	}

	public boolean connectToServer(String host) {
		connection = new ClientSideConnectionServer(host);
		return true;
	}

	public boolean logInAsClient(String clientID) {
		if (connection == null) {
			return false;
		}
		return connection.addNewClient(null, clientID);
	}

	@Override
	public boolean coLabRoomMemberArrived(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean coLabRoomMemberLeft(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean newChatMessage(String usernameSender, String message) {
		// TODO Auto-generated method stub
		return false;
	}

	private KeyListener enterpressedL = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				// TODO send a message
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public boolean newChatMessage(String usernameSender, String message, String recipiant) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean changeUserPrivilege(String username, CoLabPrivilegeLevel newPriv) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean textChanged(int posStart, int posEnd, String text) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean textHighlighted(int posStart, int posEnd) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean textInserted(int pos, String text) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean textRemoved(int posStart, int posEnd) {
		throw new NotYetImplementedException();
	}

	@Override
	public boolean textUnHighlighted(int posStart, int posEnd) {
		throw new NotYetImplementedException();
	}

	public static void main(String[] args) {
		new MessagingClient();
	}
}

class CoLabRoomsPane extends JDialog {

	private ClientSideConnectionServer con;
	private JList listOfRooms;
	private JButton joinSelectedRoom;
	private JTextField newRoomName;
	private JButton createNewRoom;

	public CoLabRoomsPane(JFrame parent, ClientSideConnectionServer connection, String username) {
		super(parent, "Available CoLab Rooms");
		con = connection;
		Collection<String> allRooms = connection.getAllCoLabRoomNames(username);
		listOfRooms = new JList(allRooms.toArray());
		listOfRooms.setPreferredSize(new Dimension(200, 200));
		joinSelectedRoom = new JButton("Join Selected Room");
		newRoomName = new JTextField(15);
		createNewRoom = new JButton("Create Room");
		this.add(listOfRooms, BorderLayout.CENTER);
	}

}
