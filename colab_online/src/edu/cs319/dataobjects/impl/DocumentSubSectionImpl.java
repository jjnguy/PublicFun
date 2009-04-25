package edu.cs319.dataobjects.impl;

import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.util.Util;

public class DocumentSubSectionImpl extends DocumentSubSection {
       
	private Long id;

	private String name;

	private String text;

	private boolean locked;
	
	private String lockHolder;

	public DocumentSubSectionImpl(){
		name = "";
		locked = false;
		text = "";
	}
	
	/**
	 * @return the lockHolder
	 */
	public String getLockHolder() {
		return lockHolder;
	}

	/**
	 * @param lockHolder the lockHolder to set
	 */
	public void setLockHolder(String lockHolder) {
		this.lockHolder = lockHolder;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public DocumentSubSectionImpl(String name) {
		this.name = name;
		this.locked = false;
		this.text = "";
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public boolean setText(String username, String text) {
		System.out.println("Text: " + text + " Username: " + username);
		boolean success = false;
		if (locked && username.equals(lockHolder)) {
			this.text = (text == null) ? "" : text;
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
				this.locked = false;
				this.lockHolder = "";
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

	@Override
	public boolean equals(Object o) {
		if (o instanceof String) {
			String s = (String) o;
			return s.equals(getName());
		} else if (o instanceof DocumentSubSection) {
			DocumentSubSection ds = (DocumentSubSection) o;
			return ds.getName().equals(this.getName());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public String toDelimmitedString() {
		return name + (char) 31 + lockHolder + (char) 31 + text;
	}

	@Override
	public String toString() {
		if (Util.DEBUG)
			return getName() + " : " + (locked ? lockHolder : "Not Locked");
		return getName() + " : " + (locked ? lockHolder : "");
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
