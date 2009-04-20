package edu.cs319.client.customcomponents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;

import edu.cs319.server.CoLabPrivilegeLevel;

public class JRoomMemberListModel<E> extends AbstractListModel {

	private List<E> members;

	public JRoomMemberListModel() {
		members = new ArrayList<E>();
	}

	public boolean addNewMember(E member) {
		if (members.contains(member))
			return false;
		members.add(member);
		fireIntervalAdded(this, 0, getSize());
		return true;
	}

	public boolean removeMember(String userID) {
		RoomMemberLite dummy = new RoomMemberLite(userID, null)
		;int index = members.indexOf(dummy);
		if (index == -1)
			return false;
		members.remove(index);
		fireIntervalRemoved(this, 0, getSize());
		return true;
	}
	
	public boolean setMemberPriv(String id, CoLabPrivilegeLevel priv){
		RoomMemberLite dummy = new RoomMemberLite(id, priv);
		int idx = members.indexOf(dummy);
		E mem = members.get(idx);
		// TODO fix
		//mem.setPriv(priv);
		fireContentsChanged(this, 0, getSize());
		return true;
	}

	public void clearList() {
		members.clear();
		fireIntervalRemoved(this, 0, getSize());
	}

	public void addAll(Collection<E> newVals) {
		members.addAll(newVals);
		fireIntervalAdded(this, 0, getSize());
	}

	@Override
	public Object getElementAt(int arg0) {
		if (members.size() <= arg0)
			return null;
		return members.get(arg0);
	}

	@Override
	public int getSize() {
		return members.size();
	}

	public boolean contains(String userID) {
		return members.contains(userID);
	}

}
