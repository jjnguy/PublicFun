package edu.cs319.dataobjects;

public interface DocumentSubSection {

	public String getName();
	public String getText();
	public boolean isLocked();
	public void setLocked(boolean lock, String username);
	public String lockedByUser();
	
}
