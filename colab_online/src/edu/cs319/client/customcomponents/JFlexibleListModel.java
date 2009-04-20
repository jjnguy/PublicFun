package edu.cs319.client.customcomponents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;

public class JFlexibleListModel<E> extends AbstractListModel {

	private List<E> members;

	public JFlexibleListModel() {
		members = new ArrayList<E>();
	}

	public boolean addNewMember(E member) {
		if (members.contains(member))
			return false;
		members.add(member);
		fireIntervalAdded(this, 0, getSize());
		return true;
	}

	public boolean remove(E obj){
		int idx = members.indexOf(obj);
		if (idx == -1)return false;;
		members.remove(idx);
		fireIntervalRemoved(this, 0, getSize());
		return true;
	}
	
	public int indexOf(Object o){
		return members.indexOf(o);
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
	
	public void update(){
		fireContentsChanged(this, 0, getSize());
	}

}
