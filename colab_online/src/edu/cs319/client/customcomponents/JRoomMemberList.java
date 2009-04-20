package edu.cs319.client.customcomponents;

import javax.swing.JList;

import edu.cs319.server.CoLabPrivilegeLevel;

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

	public boolean removeMember(String userID) {
		RoomMemberLite dummy = new RoomMemberLite(userID, null);
		return model.remove(dummy);
	}

	public boolean setMemberPriv(String id, CoLabPrivilegeLevel priv) {
		RoomMemberLite dummy = new RoomMemberLite(id, priv);
		int idx = model.indexOf(dummy);
		if (idx == -1)return false;
		RoomMemberLite mem = (RoomMemberLite) model.getElementAt(idx);
		mem.setPriv(priv);
		model.update();
		return true;
	}

}
