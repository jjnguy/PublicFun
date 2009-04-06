package edu.cs319.client.customcomponents;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

public class JRoomMemberListModel extends AbstractListModel {

	private List<String> userIDs;

	public JRoomMemberListModel() {
		userIDs = new ArrayList<String>();
	}

	public boolean addNewMember(String userID) {
		if (userIDs.contains(userID))
			return false;
		userIDs.add(userID);
		return true;
	}

	public boolean removeMember(String userID) {
		int index = userIDs.indexOf(userID);
		if (index == -1)
			return false;
		userIDs.remove(0);
		return true;
	}

	@Override
	public Object getElementAt(int arg0) {
		if (userIDs.size() <= arg0)
			return null;
		return userIDs.get(arg0);
	}

	@Override
	public int getSize() {
		return userIDs.size();
	}

}
