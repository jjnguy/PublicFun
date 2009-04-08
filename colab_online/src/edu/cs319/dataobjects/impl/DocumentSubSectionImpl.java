package edu.cs319.dataobjects.impl;

import java.util.List;

import edu.cs319.dataobjects.DocumentSubSection;

public class DocumentSubSectionImpl implements DocumentSubSection {

	private String name;
	private String text;

	private boolean locked;
	private String lockHolder;

	public DocumentSubsectionImpl(String name) {
		this.name = name;
		this.locked = false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getText() {
		return text;
	}

	public boolean setText(String text, String username) {
		boolean success = false;
		if (locked && username.equals(lockHolder)) {
			this.text = text;
			success = true;
		}
		return success;
	}

	@Override
	public boolean isLocked() {
		return locked;
	}

	@Override
	public void setLocked(boolean lock, String username) {
		if (!locked && lock) {
			this.locked = true;
			this.lockHolder = username;
		}
		if (locked && !lock) {
			if (username.equals(lockHolder)) {
				locked = false;
			}
		}
	}

	@Override
	public String lockedByUser() {
		String lockedBy = null;
		if (locked) {
			lockedBy = lockHolder;
		}
		return lockedBy;
	}

}
