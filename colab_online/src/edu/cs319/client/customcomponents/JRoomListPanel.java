package edu.cs319.client.customcomponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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

	public boolean addUser(String id) {
		return roomList.getModel().addNewMember(id);
	}

	public boolean removeUser(String id) {
		return roomList.getModel().removeMember(id);
	}

	public void updateList(Collection<String> userNames) {
		roomList.getModel().clearList();
		roomList.getModel().addAll(userNames);
	}

}
