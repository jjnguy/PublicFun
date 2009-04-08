package edu.cs319.client.customcomponents;

import java.awt.BorderLayout;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JRoomList extends JPanel {
	
	private JRoomMemberList roomList;
	
	public JRoomList() {
		//TODO change this to take in a list parameter of all people
		// in the room already
		roomList = new JRoomMemberList();
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setUpAppearance();
		setUpListeners();
	}
	
	private void setUpAppearance() {
		setLayout(new BorderLayout(10, 10));
		add(roomList, BorderLayout.CENTER);
	}
	
	private void setUpListeners() {
		roomList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void updateList(List<String> userNames) {
		Iterator<String> iter = userNames.iterator();
		while(iter.hasNext()) {
			String cur = iter.next();
			if(!roomList.getModel().contains(cur)) {
				roomList.getModel().addNewMember(cur);
			}
		}
	}
	
}
