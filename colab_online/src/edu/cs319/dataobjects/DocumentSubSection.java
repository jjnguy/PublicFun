package edu.cs319.dataobjects;

import edu.cs319.dataobjects.impl.DocumentSubSectionImpl;

public abstract class DocumentSubSection {

	public abstract String getName();

	public abstract String getText();

	public abstract boolean setText(String username, String text);

	public abstract boolean isLocked();

	public abstract void setLocked(boolean lock, String username);

	public abstract String lockedByUser();

	public abstract String toDelimmitedString();

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
