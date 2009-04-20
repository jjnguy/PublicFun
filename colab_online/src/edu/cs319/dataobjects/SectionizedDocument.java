package edu.cs319.dataobjects;

import java.util.List;

public interface SectionizedDocument {

	public String getName();

	public List<DocumentSubSection> getAllSubSections();
	
	public void removeAllSubSections();

	public void addAllSubSections(List<DocumentSubSection> ss);
	
	public int getSubSectionCount();
	
	public DocumentSubSection getSectionAt(int idx);
	
	public DocumentSubSection getSection(String id);
	
	public int getSubSectionIndex(String sectionID);
	
	public String getFullText();
	
	public boolean addSubSection(DocumentSubSection ds, int index);

	public boolean removeSubSection(String name);

	public boolean splitSubSection(String name, String partA, String partB, int splitIndex, String userName);

	public boolean combineSubSections(String partA, String partB, String combinedName);
	
	public boolean flopSubSections(int idx1, int idx2);
}
