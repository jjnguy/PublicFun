package edu.cs319.dataobjects;

import java.util.List;

import edu.cs319.connectionmanager.NotYetImplementedException;

public abstract class DocumentSubSection {

	public abstract String getName();

	public abstract String getText();

	public abstract int getIndex();

	public abstract boolean isLocked();

	public abstract void setLocked(boolean lock, String username);

	public abstract String lockedByUser();

	public abstract List<DocumentSubSection> split(int charIndex);

	public static DocumentSubSection combine(List<DocumentSubSection> allSections) {
		throw new NotYetImplementedException();
	}

	public void setText(String substring) {
		// TODO Auto-generated method stub
		
	}
}
