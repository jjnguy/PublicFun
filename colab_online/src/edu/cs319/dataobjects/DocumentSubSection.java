package edu.cs319.dataobjects;

import java.util.List;

import edu.cs319.connectionmanager.NotYetImplementedException;

public interface DocumentSubSection {

	public String getName();

	public String getText();

	public boolean setText(String text);

	public boolean isLocked();

	public void setLocked(boolean lock, String username);

	public String lockedByUser();

	public List<DocumentSubSection> split(int charIndex);

	
}
