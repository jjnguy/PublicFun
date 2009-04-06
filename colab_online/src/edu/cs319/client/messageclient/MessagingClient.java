package edu.cs319.client.messageclient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.cs319.client.IClient;
import edu.cs319.client.customcomponents.JRoomMemberList;
import edu.cs319.connectionmanager.NotYetImplementedException;
import edu.cs319.connectionmanager.clientside.ConnectionFactory;
import edu.cs319.connectionmanager.clientside.Proxy;
import edu.cs319.connectionmanager.serverside.ServerDecoder;
import edu.cs319.server.CoLabPrivilegeLevel;
import edu.cs319.util.Util;

public class MessagingClient extends JFrame implements IClient {

	private JTextArea topText;
	private JTextField bottomText;
	private JRoomMemberList membersInRoom;

	private String clientID;

	private Proxy proxy;

	public MessagingClient() {
		super("CoLabMessaging");
		membersInRoom = new JRoomMemberList();
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
		JMenuItem logInToServer = new JMenuItem("Connect to Server");
		logInToServer.addActionListener(connectTOServerAction);
		JMenuItem logInAsClient = new JMenuItem("Log in as Client");
		logInAsClient.addActionListener(logOnAsClient);
		JMenuItem showCoLabRooms = new JMenuItem("Show CoLabRooms");
		file.add(logInToServer);
		file.add(logInAsClient);
		file.add(showCoLabRooms);
		ret.add(file);
		return ret;
	}

	// TODO fix
	public boolean connectToServer(String host) {
		try {
			proxy = ConnectionFactory.connect(host, ServerDecoder.DEFAULT_PORT, this);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			if (Util.DEBUG)
				e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public boolean logInAsClient(String clientID) {
		if (connection == null) {
			return false;
		}
		this.clientID = clientID;
		return connection.addNewClient(null, clientID);
	}

	@Override
	public boolean coLabRoomMemberArrived(String username) {
		return membersInRoom.getModel().addNewMember(username);
	}

	@Override
	public boolean coLabRoomMemberLeft(String username) {
		return membersInRoom.getModel().removeMember(username);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean newChatMessage(String usernameSender, String message) {
		String fullTExt = usernameSender + ": " + message + "\n";
		topText.append(fullTExt);
		return true;
	}

	private ActionListener logOnAsClient = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = JOptionPane.showInputDialog(MessagingClient.this,
					"please enter the host to connect to.");
			if (username == null)
				return;
			System.out.println(MessagingClient.this.logInAsClient(username));
		}
	};

	private ActionListener connectTOServerAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String host = JOptionPane.showInputDialog(MessagingClient.this,
					"please enter the host to connect to.");
			if (host == null)
				return;
			System.out.println(MessagingClient.this.connectToServer(host));
		}

	};

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
