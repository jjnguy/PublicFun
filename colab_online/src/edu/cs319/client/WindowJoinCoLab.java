package edu.cs319.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import edu.cs319.client.customcomponents.JRoomMemberList;
import edu.cs319.server.IServer;
import edu.cs319.util.Util;

/**
 * 
 * @author Amelia Gee
 * @author Justin Nelson
 * 
 */
public class WindowJoinCoLab extends JDialog {

	private JRoomMemberList roomList;
	private JTextField createField = new JTextField();
	private JButton joinButton = new JButton("Join");
	private JButton createButton = new JButton("Create");
	private JButton cancelButton = new JButton("Cancel");
	private JButton refreshButton = new JButton("Refresh");
	private JLabel refreshTimeStamp = new JLabel("No List Yet");

	private WindowClient parent;

	private IServer server;

	public WindowJoinCoLab(WindowClient parent, IServer server) {
		super(parent, "Join a CoLab Room");
		if (server == null) {
			if (Util.DEBUG) {
				System.out.println("Null server passed into a join colab room frame");
			}
			throw new IllegalArgumentException("The server must not be null");
		}
		this.parent = parent;
		this.server = server;
		this.setSize(500, 400);
		this.setMinimumSize(new Dimension(500, 400));
		setUpAppearance();
		setUpListeners();
		this.repaint();
	}

	private void setUpAppearance() {
		Insets borderInsets = new Insets(0, 0, 20, 0);
		roomList = new JRoomMemberList();
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		roomList.setSelectedIndex(0);
		roomList.setVisibleRowCount(8);
		JScrollPane listScrollPane = new JScrollPane(roomList);

		JLabel listLabel = new JLabel("Existing CoLab Rooms:");
		JLabel createLabel = new JLabel("Create a New CoLab:");
		JLabel cancelLabel = new JLabel("Join a CoLab Room at another time:");
		createField.setPreferredSize(new Dimension(200, 25));

		JPanel westPanel = new JPanel(new BorderLayout(10, 10));
		westPanel.add(listLabel, BorderLayout.NORTH);
		westPanel.add(createLabel, BorderLayout.SOUTH);
		JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
		centerPanel.add(listScrollPane, BorderLayout.CENTER);
		centerPanel.add(createField, BorderLayout.SOUTH);
		JPanel eastPanel = new JPanel(new BorderLayout(10, 10));
		eastPanel.add(joinButton, BorderLayout.NORTH);
		eastPanel.add(createButton, BorderLayout.SOUTH);
		JPanel southPanel = new JPanel(new BorderLayout(10, 10));
		southPanel.add(cancelLabel, BorderLayout.WEST);
		southPanel.add(cancelButton, BorderLayout.EAST);

		JLabel topLabel = new JLabel("Please choose an "
				+ "existing CoLab to join or create a new CoLab.");
		topLabel.setBorder(new EmptyBorder(borderInsets));
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.add(topLabel, BorderLayout.NORTH);
		mainPanel.add(westPanel, BorderLayout.WEST);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(eastPanel, BorderLayout.EAST);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		this.add(mainPanel);
		fillCoLabRoomList();
	}

	public void roomsUpdated(Collection<String> roomNames) {
		roomList.getModel().clearList();
		roomList.getModel().addAll(roomNames);
		Calendar c = GregorianCalendar.getInstance();
		refreshTimeStamp.setText(c.getTime().toString());
	}

	private void fillCoLabRoomList() {
		// ask for all room members
		server.getAllCoLabRoomNames(parent.getUserName());
	}

	private void setUpListeners() {
		joinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String joinName = (String) roomList.getSelectedValue();
				server.joinCoLabRoom(parent.getUserName(), joinName, null);
				dispose();
			}
		});

		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (createField.getText().length() == 0)
					return;
				if (server.addNewCoLabRoom(parent.getUserName(), createField.getText(), null)) {
					server.joinCoLabRoom(parent.getUserName(), createField.getText(), null);
					dispose();
				}
			}
		});

		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fillCoLabRoomList();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
	}
}
