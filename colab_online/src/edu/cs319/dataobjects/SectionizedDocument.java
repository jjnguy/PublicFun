package edu.cs319.dataobjects;

import java.util.List;

public interface SectionizedDocument {

	public List<DocumentSubSection> getAllSubSections();
	public int getSubsectionCount();
	public DocumentSubSection getSectionAt(int idx);
	public int getSubSectionIndex(String sectionID);
	public String getFullText();
	
}
