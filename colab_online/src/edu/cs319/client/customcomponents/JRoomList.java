package edu.cs319.client.customcomponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

public class JRoomList extends JPanel {
	
	private JRoomMemberList roomList;
	
	public JRoomList() {
		roomList = new JRoomMemberList();
		setUpAppearance();
		setPreferredSize(new Dimension(150, 500));
	}
	
	private void setUpAppearance() {
		setLayout(new BorderLayout(10, 10));
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		roomList.setVisibleRowCount(10);
		add(roomList, BorderLayout.CENTER);
	}
	
	public void updateList(Collection<String> userNames) {
		for(int i = 0; i < roomList.getModel().getSize(); i++) {
			String cur = (String) roomList.getModel().getElementAt(i);
			if(!userNames.contains(cur)) {
				roomList.getModel().removeMember(cur);
			}
		}
		Iterator<String> iter = userNames.iterator();
		while(iter.hasNext()) {
			String cur = iter.next();
			if(!roomList.getModel().contains(cur)) {
				roomList.getModel().addNewMember(cur);
			}
		}
	}
	
}
