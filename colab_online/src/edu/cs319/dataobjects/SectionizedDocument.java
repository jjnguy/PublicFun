package edu.cs319.dataobjects;

import java.util.List;

public interface SectionizedDocument {

	public String getName();

	public List<DocumentSubSection> getAllSubSections();
	
	public int getSubsectionCount();
	
	public DocumentSubSection getSectionAt(int idx);
	
	public DocumentSubSection getSection(String id);
	
	public int getSubSectionIndex(String sectionID);
	
	public String getFullText();
	
	public void addSubSection(DocumentSubSection ds, int index);

	public void removeSubSection(String name);

	public void splitSubSection(String name, String partA, String partB, int splitIndex);

	public void combineSubSections(String partA, String partB, String combinedName);
}
