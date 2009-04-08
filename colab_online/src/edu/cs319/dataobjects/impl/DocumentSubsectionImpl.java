package edu.cs319.dataobjects;

import java.util.List;

import edu.cs319.connectionmanager.NotYetImplementedException;

public class DocumentSubSectionImpl {

	private String name;
	private int index;

	private String text;
	
	private boolean locked;
	private String lockHolder;

	public DocumentSubSectionImpl(String name, int index) {
		this.name = name;
		this.index = index;
		this.locked = false;
	}

	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}

	public boolean setText(String text,String username) {
		boolean success = false;
		if(locked && username.equals(lockHolder)) {
			this.text = text;
			success = true;
		}
		return success;
	}


	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean lock, String username) {
		if(!locked && lock) {
			this.locked = true;
			this.lockHolder = username;
		}
		if(locked && !lock) {
			if(username.equals(lockHolder)) {
				locked = false;
			}
		}
	}

	public String lockedByUser() {
		String lockedBy = "";
		if(locked) {
			lockedBy = lockHolder;
		}
	}
}
