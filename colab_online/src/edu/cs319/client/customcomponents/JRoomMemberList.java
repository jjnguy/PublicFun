package edu.cs319.client.customcomponents;

import javax.swing.JList;

public class JRoomMemberList extends JList {

	private JRoomMemberListModel<RoomMemberLite> model;

	public JRoomMemberList() {
		model = new JRoomMemberListModel<RoomMemberLite>();
		setModel(model);
	}

	@Override
	public JRoomMemberListModel<RoomMemberLite> getModel() {
		return model;
	}

}
