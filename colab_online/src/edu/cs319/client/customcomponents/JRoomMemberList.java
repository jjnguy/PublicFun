package edu.cs319.client.customcomponents;

import javax.swing.JList;

public class JRoomMemberList extends JList {

	private JRoomMemberListModel model;

	public JRoomMemberList() {
		model = new JRoomMemberListModel();
		setModel(model);
	}

	@Override
	public JRoomMemberListModel getModel() {
		return model;
	}

}
