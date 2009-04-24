package edu.cs319.dataobjects;

import edu.cs319.dataobjects.impl.DocumentSubSectionImpl;

/**
 * A DocumentSubSection is an individual, lockable subsection of a SectionizedDocument.
 * 
 * @author Wayne Rowcliffe
 * @author Justin Nelson
 **/
public abstract class DocumentSubSection {

	/**
	 * The name of this DocumentSubSection
	 * 
	 * @return The name of this subsection
	 **/
	public abstract String getName();

	/**
	 * The text contained in this DocumentSubSection
	 * 
	 * @return The text contained in this subsection
	 **/
	public abstract String getText();

	/**
	 * Attempts to set the text in this subsection.
	 * In order to set the text, the username must match the lockholder,
	 * and the subsection must be locked
	 * 
	 * @param username The user attempting to set this subsection's text
	 * @param text The text to place in this subsection
	 * 
	 * @return Whether the text was set successfully
	 **/
	public abstract boolean setText(String username, String text);

	/**
	 * Whether this subsection is locked
	 * 
	 * @return Whether this subsection is locked
	 **/
	public abstract boolean isLocked();

	/**
	 * Attempts to change the lock status of this subsection.
	 * If unlocked, a lock can be set by any user
	 * If locked, the subsection can only be unlocked by the user holding the lock
	 * 
	 * @param lock Whether to lock or unlock the document
	 * @param username The user requesting the change in lock status
	 **/
	public abstract void setLocked(boolean lock, String username);

	/**
	 * The user holding the lock on this subsection
	 * 
	 * @return The name of the user holding the lock on this subsection, null if unlocked
	 **/
	public abstract String lockedByUser();

	/**
	 * Converts this DocumentSubSection into a String which can be sent over a network
	 * 
	 * @return The delimmited form of this subsection
	 **/
	public abstract String toDelimmitedString();

	/**
	 * Parses a DocumentSubSection from a delimmited String.
	 * 
	 * @param string The delimmited String representing the subsection
	 * 
	 * @return A DocumentSubSection containing the same data as the original
	 **/
	public static DocumentSubSection getFromDelimmitedString(String string) {
		// TODO probably broken, look here first
		int idxFirstDelim = string.indexOf((char) 31);
		int idxSecondDelim = string.indexOf((char) 31, idxFirstDelim + 1);
		String name = string.substring(0, idxFirstDelim);
		String lockHolder = string.substring(idxFirstDelim + 1, idxSecondDelim);
		String text = string.substring(idxSecondDelim + 1);
		DocumentSubSection ret = new DocumentSubSectionImpl(name);
		ret.setLocked(true,"admin");
		ret.setText("admin",text);
		ret.setLocked(false,"admin");
		if (lockHolder.length() > 0) {
			ret.setLocked(true, lockHolder);
		}
		return ret;
	}
}
