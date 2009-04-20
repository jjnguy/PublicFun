package edu.cs319.client.customcomponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.cs319.server.CoLabPrivilegeLevel;

public class JRoomListPanel extends JPanel {

	private JRoomMemberList roomList;
	JScrollPane listScroll;

	public JRoomListPanel() {
		roomList = new JRoomMemberList();
		setUpAppearance();
		setUpListeners();
		setPreferredSize(new Dimension(150, 425));
	}

	private void setUpAppearance() {
		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		listScroll = new JScrollPane(roomList);
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(listScroll, BorderLayout.CENTER);
	}

	private void setUpListeners() {
		roomList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public boolean setUserPrivledge(String id, CoLabPrivilegeLevel newPriv) {
		return this.roomList.setMemberPriv(id, newPriv);
	}

	public boolean addUser(String id) {
		return roomList.getModel().add(
				new RoomMemberLite(id, CoLabPrivilegeLevel.OBSERVER));
	}

	public boolean removeUser(String id) {
		return roomList.removeMember(id);
	}

	public void updateList(List<String> userNames, List<CoLabPrivilegeLevel> privs) {
		roomList.getModel().clearList();
		for (int i = 0; i < userNames.size(); i++) {
			roomList.getModel().add(new RoomMemberLite(userNames.get(i), privs.get(i)));
		}
	}

}
