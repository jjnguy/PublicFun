package edu.cs319.dataobjects.impl;

import edu.cs319.dataobjects.DocumentSubSection;

public class DocumentSubSectionImpl extends DocumentSubSection {
    
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
		if (locked && username.equals(lockHolder)) {
			this.text = (text == null) ? "" : text;
			return true;
		}
		return false;
	}
	
	/**
	 * Attempts to insert text into this subsection at the given start index.
	 * In order to insert text, the username must match the lockholder,
	 * and the subsection must be locked
	 * 
	 * @param username The user attempting to set this subsection's text
	 * @param start The index at which to insert text (inclusive)
	 * @param text The text to place in this subsection
	 * 
	 * @return Whether the text was inserted successfully
	 **/
	public boolean insertText(String username, int start, String text) {
		if(locked && username.equals(lockHolder)) {
			this.text = this.text.substring(0,start) + text + this.text.substring(start,this.text.length());
			return true;
		}
		return false;
	}
	
	/**
	 * Attempts to remove text from this subsection.
	 * In order to remove the text, the username must match the lockholder,
	 * and the subsection must be locked
	 * 
	 * @param username The user attempting to set this subsection's text
	 * @param start The index at which to start removing text (inclusive)
	 * @param end The index at which to stop removing text (exclusive)
	 * 
	 * @return Whether the text was removed successfully
	 **/
	public boolean removeText(String username, int start, int end) {
		if(locked && username.equals(lockHolder)) {
			this.text = this.text.substring(0,start) + this.text.substring(end,this.text.length());
			return true;
		}
		return false;
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
		return getName() + " : " + (locked ? lockHolder : "Not Locked");
	}

}
