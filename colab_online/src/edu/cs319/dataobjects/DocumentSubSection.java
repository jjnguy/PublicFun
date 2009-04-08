package edu.cs319.dataobjects;


public interface DocumentSubSection {

	public String getName();

	public String getText();

	public boolean setText(String text, String username);

	public boolean isLocked();

	public void setLocked(boolean lock, String username);

	public String lockedByUser();

	public String toDelimmitedString();
}
