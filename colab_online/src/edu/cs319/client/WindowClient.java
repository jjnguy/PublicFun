package edu.cs319.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.cs319.client.customcomponents.JChatPanel;
import edu.cs319.client.customcomponents.JDocTabPanel;
import edu.cs319.client.customcomponents.JRoomListPanel;
import edu.cs319.connectionmanager.clientside.Proxy;
import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.dataobjects.SectionizedDocument;
import edu.cs319.dataobjects.impl.DocumentInfoImpl;
import edu.cs319.dataobjects.impl.DocumentSubSectionImpl;
import edu.cs319.server.CoLabPrivilegeLevel;
import edu.cs319.util.Util;

/**
 * 
 * @author Amelia Gee
 * @author Justin Nelson
 * 
 */
public class WindowClient extends JFrame implements IClient {

	private Proxy proxy;

	private WindowJoinCoLab colabRoomFrame;

	private String userName;
	private String roomName;

	private JTabbedPane documentPane;
	private Map<String, JDocTabPanel> documents;

	private JRoomListPanel roomMemberListPanel;
	private JChatPanel chatPanel;

	private JMenuItem openDocument;
	private JMenuItem newDocument;
	private JMenuItem logIn;
	private JMenuItem joinCoLabRoom;
	private JMenuItem disconnect;
	private JMenuItem exitCoLab;
	private final JCheckBox showRoomMembers = new JCheckBox("Display Room Members Window");
	private final JCheckBox showChat = new JCheckBox("Display Chat Window");
	private JMenuItem about;

	public WindowClient() {
		// setLookAndFeel();
		setTitle("CoLab");
		setSize(new Dimension(900, 500));
		setJMenuBar(createMenuBar());
		setListeners();

		roomMemberListPanel = new JRoomListPanel();
		documentPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		documents = new HashMap<String, JDocTabPanel>();
		chatPanel = new JChatPanel();

		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.add(roomMemberListPanel, BorderLayout.WEST);
		panel.add(documentPane, BorderLayout.CENTER);
		panel.add(chatPanel, BorderLayout.EAST);
		add(panel);
	}

	private JMenuBar createMenuBar() {
		JMenuBar mainMenu = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu connect = new JMenu("Connect");
		JMenu view = new JMenu("View");
		JMenu help = new JMenu("Help");
		openDocument = new JMenuItem("Add Document From File");
		newDocument = new JMenuItem("Add New Blank Document");
		logIn = new JMenuItem("Log In");
		joinCoLabRoom = new JMenuItem("Join CoLab Room");
		disconnect = new JMenuItem("Disconnect");
		exitCoLab = new JMenuItem("Exit CoLab");
		about = new JMenuItem("About CoLab");

		file.setMnemonic(KeyEvent.VK_F);
		connect.setMnemonic(KeyEvent.VK_C);
		view.setMnemonic(KeyEvent.VK_V);
		help.setMnemonic(KeyEvent.VK_H);
		openDocument.setMnemonic(KeyEvent.VK_D);
		openDocument.setDisplayedMnemonicIndex(4);
		newDocument.setMnemonic(KeyEvent.VK_N);
		logIn.setMnemonic(KeyEvent.VK_L);
		joinCoLabRoom.setMnemonic(KeyEvent.VK_J);
		disconnect.setMnemonic(KeyEvent.VK_D);
		exitCoLab.setMnemonic(KeyEvent.VK_X);
		showRoomMembers.setMnemonic(KeyEvent.VK_R);
		showChat.setMnemonic(KeyEvent.VK_C);
		about.setMnemonic(KeyEvent.VK_A);

		file.add(openDocument);
		file.add(newDocument);
		file.add(exitCoLab);
		connect.add(logIn);
		connect.add(joinCoLabRoom);
		connect.add(disconnect);
		view.add(showChat);
		view.add(showRoomMembers);
		help.add(about);

		setDisconnected();
		showChat.setSelected(true);
		showRoomMembers.setSelected(true);

		mainMenu.add(file);
		mainMenu.add(connect);
		mainMenu.add(view);
		mainMenu.add(help);
		return mainMenu;
	}

	private void setListeners() {
		// FILE menu items
		openDocument.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser choose = new JFileChooser();
				int choice = choose.showOpenDialog(WindowClient.this);
				if (choice != JFileChooser.APPROVE_OPTION)
					return;
				String docName = JOptionPane.showInputDialog(WindowClient.this,
						"Enter the name of the document:");
				String secName = JOptionPane.showInputDialog(WindowClient.this,
						"Enter the name of the subsection:");
				File choiceF = choose.getSelectedFile();
				proxy.getServer().newDocument(userName, roomName, docName);
				DocumentSubSection section = new DocumentSubSectionImpl(secName);
				section.setLocked(true, userName);
				proxy.getServer().newSubSection(userName, roomName, docName, secName, 0);
				proxy.getServer().subSectionLocked(userName, roomName, docName, secName);
				try {
					section.setText(userName, new Scanner(choiceF).useDelimiter("//Z").next());
				} catch (FileNotFoundException e1) {
					if (Util.DEBUG)
						e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Shit, file could not be opened!!!!");
					return;
				}
				System.out.println("WindowClient Upload Document: Username: " + userName
						+ " DocumentName: " + docName + " SectionName: " + secName
						+ " LockHolder: " + section.lockedByUser());
				proxy.getServer().subSectionUpdated(userName, roomName, docName, secName, section);
			}
		});
		newDocument.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String docName = JOptionPane.showInputDialog(WindowClient.this,
						"Enter the name of the document:");
				String secName = JOptionPane.showInputDialog(WindowClient.this,
						"Enter the name of the subsection:");
				proxy.getServer().newDocument(userName, roomName, docName);
				DocumentSubSection section = new DocumentSubSectionImpl(secName);
				section.setLocked(true, userName);
				proxy.getServer().newSubSection(userName, roomName, docName, secName, 0);
				proxy.getServer().subSectionLocked(userName, roomName, docName, secName);
				System.out.println("WindowClient New Blank Document: Username: " + userName
						+ " DocumentName: " + docName + " SectionName: " + secName
						+ " LockHolder: " + section.lockedByUser());
				proxy.getServer().subSectionUpdated(userName, roomName, docName, secName, section);
			}
		});
		exitCoLab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WindowClient.this.processWindowEvent(new WindowEvent(WindowClient.this,
						WindowEvent.WINDOW_CLOSING));
			}
		});

		// CONNECT menu items
		logIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				proxy = WindowLogIn.showLoginWindow(WindowClient.this, WindowClient.this);
				if (proxy != null) {
					colabRoomFrame = new WindowJoinCoLab(WindowClient.this, proxy.getServer());
					setLogIn();
				}
			}
		});
		joinCoLabRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = colabRoomFrame.showRoomDialogue();
				if (result == WindowJoinCoLab.ROOM_JOINED) {
					setJoinedRoom();
				}
			}
		});
		disconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				proxy.getServer().leaveCoLabRoom(userName, roomName);
				try {
					proxy.close();
				} catch (IOException io) {
					if (Util.DEBUG) {
						io.printStackTrace();
					}
				}
			}
		});
		
		// VIEW menu items
		showRoomMembers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				roomMemberListPanel.setVisible(showRoomMembers.isSelected());
			}
		});
		showChat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chatPanel.setVisible(showChat.isSelected());
			}
		});

		// HELP menu items
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO make info window

			}
		});

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (disconnect.isEnabled()) {
					proxy.getServer().leaveCoLabRoom(userName, roomName);
					proxy.getServer().logOut(userName);
				}
				chatPanel.shutdownTray();
			}
		});
	}

	/**
	 * Set menu items enabled/disabled for when user is logged in.
	 */
	private void setLogIn() {
		openDocument.setEnabled(false);
		newDocument.setEnabled(false);
		logIn.setEnabled(false);
		joinCoLabRoom.setEnabled(true);
		disconnect.setEnabled(true);
		String title = getTitle();
		setTitle(title + " - " + getUserName());
	}

	/**
	 * Set menu items enabled/disabled for when user has joined a CoLab Room.
	 */
	private void setJoinedRoom() {
		openDocument.setEnabled(true);
		newDocument.setEnabled(true);
		logIn.setEnabled(false);
		joinCoLabRoom.setEnabled(false);
		disconnect.setEnabled(true);
		String title = getTitle();
		setTitle(title + " - " + getRoomName());
	}

	/**
	 * Set menu items enabled/disabled for when user is disconnected.
	 */
	private void setDisconnected() {
		openDocument.setEnabled(false);
		newDocument.setEnabled(false);
		logIn.setEnabled(true);
		joinCoLabRoom.setEnabled(false);
		disconnect.setEnabled(false);
	}

	@Override
	public boolean allCoLabRooms(Collection<String> roomNames) {
		if (colabRoomFrame == null) {
			if (Util.DEBUG) {
				System.out.println("Client was sent list of all rooms before frame was created");
			}
			return false;
		}
		colabRoomFrame.roomsUpdated(roomNames);
		return true;
	}

	@Override
	public boolean allUsersInRoom(List<String> usernames, List<CoLabPrivilegeLevel> privs) {
		roomMemberListPanel.updateList(usernames, privs);
		return true;
	}

	@Override
	public boolean changeUserPrivilege(String username, CoLabPrivilegeLevel newPriv) {
		return this.roomMemberListPanel.setUserPrivledge(username, newPriv);
	}

	@Override
	public boolean coLabRoomMemberArrived(String username) {
		chatPanel.newChatMessage("Server", "<New Chat Member '" + username + "'>");
		return roomMemberListPanel.addUser(username);
	}

	@Override
	public boolean coLabRoomMemberLeft(String username) {
		chatPanel.newChatMessage("Server", "<Chat Member Left '" + username + "'>");
		return roomMemberListPanel.removeUser(username);
	}

	@Override
	public boolean newChatMessage(String usernameSender, String message) {
		chatPanel.newChatMessage(usernameSender, message);
		return true;
	}

	@Override
	public boolean newChatMessage(String usernameSender, String message, String recipiant) {
		chatPanel.newChatMessage(usernameSender, message, recipiant);
		return true;
	}

	@Override
	public boolean newSubSection(String username, String documentName, String sectionID,
			DocumentSubSection section, int idx) {
		System.out.println("WindowClient New SubSection: Username: " + username + " Document: "
				+ documentName + " SectionID: " + sectionID + " LockHolder: "
				+ section.lockedByUser());

		SectionizedDocument doc = documents.get(documentName).getSectionizedDocument();
		doc.addSubSection(section, idx);
		documents.get(documentName).updateDocPane();
		return true;
	}

	@Override
	public boolean newDocument(String username, String documentName) {
		JDocTabPanel doc = documents.get(documentName);
		if (doc != null) {
			throw new IllegalStateException("Two documents cannot have the same name");
		}
		doc = new JDocTabPanel(new DocumentInfoImpl(proxy.getServer(),roomName,documentName,userName));
		documents.put(documentName, doc);
		documentPane.add(documentName, doc);
		System.out.println("WindowClient New Document: Username: " + username + " DocumentName: "
				+ documentName);
		documents.get(documentName).updateDocPane();
		return true;

	}

	@Override
	public boolean removeDocument(String username, String documentName) {
		JDocTabPanel doc = documents.get(documentName);
		if (doc == null) {
			throw new IllegalStateException("This document does not exist");
		}
		documents.remove(documentName);
		documentPane.remove(doc);
		documents.get(documentName).updateDocPane();
		return true;
	}

	@Override
	public boolean subsectionLocked(String usernameSender, String documentName, String sectionId) {
		SectionizedDocument doc = documents.get(documentName).getSectionizedDocument();
		DocumentSubSection hi = doc.getSection(sectionId);
		hi.setLocked(true, usernameSender);
		System.out.println("WindowClient SubSection Locked: Username: " + usernameSender
				+ " Document: " + documentName + " SectionName: " + sectionId + " LockHolder: "
				+ hi.lockedByUser());
		documents.get(documentName).updateDocPane();
		return true;
	}

	@Override
	public boolean subsectionUnLocked(String usernameSender, String documentName, String sectionId) {
		SectionizedDocument doc = documents.get(documentName).getSectionizedDocument();
		doc.getSection(sectionId).setLocked(false, usernameSender);
		documents.get(documentName).updateDocPane();
		return true;
	}

	@Override
	public boolean subsectionFlopped(String usernameSender, String documentName,
			String sectionIDMoveUp, String sectionIDMoveDown) {
		SectionizedDocument doc = documents.get(documentName).getSectionizedDocument();
		int idx1 = doc.getSubSectionIndex(sectionIDMoveUp);
		int idx2 = doc.getSubSectionIndex(sectionIDMoveDown);
		doc.flopSubSections(idx1, idx2);
		documents.get(documentName).updateDocPane();
		return false;
	}
	
	@Override
	public boolean subSectionRemoved(String username, String sectionId, String documentName) {
		SectionizedDocument doc = documents.get(documentName).getSectionizedDocument();
		doc.removeSubSection(sectionId);
		documents.get(documentName).updateDocPane();
		return true;
	}
	
	@Override
	public boolean updateAllSubsections(String documentId, List<DocumentSubSection> allSections) {
		SectionizedDocument doc = documents.get(documentId).getSectionizedDocument();
		doc.removeAllSubSections();
		doc.addAllSubSections(allSections);
		documents.get(documentId).updateDocPane();
		return true;
	}

	@Override
	public boolean updateSubsection(String usernameSender, String documentname,
			DocumentSubSection section, String sectionID) {
		SectionizedDocument doc = documents.get(documentname).getSectionizedDocument();
		doc.getSection(sectionID).setText(usernameSender, section.getText());
		System.out.println("WindowClient Updating SubSection: Username: " + usernameSender
				+ " Document: " + documentname + " SectionName: " + sectionID + " LockHolder: "
				+ section.lockedByUser());
		documents.get(documentname).updateDocPane();
		return true;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	public void setUserName(String un) {
		userName = un;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String rn) {
		roomName = rn;
	}

	public void chatLogin() {
		try {
			chatPanel.connect(proxy.getServer(), userName, roomName);
		} catch (IOException e) {
			if (Util.DEBUG)
				e.printStackTrace();
		}
	}

	/**
	 * Sets the look and feel of an application to that of the system it is running on. (Java's
	 * default looks bad)
	 */
	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

}
