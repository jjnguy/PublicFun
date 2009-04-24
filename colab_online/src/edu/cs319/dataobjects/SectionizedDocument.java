package edu.cs319.dataobjects;

import java.util.List;

/**
 * A SectionizedDocument is composed of multiple DocumentSubSections. Each DocumentSubSection
 * Is independent from the others. This allows a SectionizedDocument to be concurrently modified 
 * amongst the differnt DocumentSubSections.
 * 
 * @author Wayne Rowcliffe
 * @author Justin Nelson
 **/
public interface SectionizedDocument {

	/**
	 * The name of this SectionizedDocument
	 * 
	 * @return The name of this SectionizedDocument
	 **/
	public String getName();

	/**
	 * All DocumentSubSections in this document, in the order they appear.
	 * 
	 * @return All DocumentSubSections in this document.
	 **/
	public List<DocumentSubSection> getAllSubSections();
	
	/**
	 * Removes all DocumentSubSections from this document.
	 **/
	public void removeAllSubSections();

	/**
	 * Adds all DocumentSubSections in the given list to this document
	 * 
	 * @param ss The DocumentSubSections to add to this document
	 **/
	public void addAllSubSections(List<DocumentSubSection> ss);
	
	/**
	 * The number of DocumentSubSections in this document
	 * 
	 * @return The number of sub sections in this document.
	 **/
	public int getSubSectionCount();
	
	/**
	 * Returns the DocumentSubSection at the given index
	 * 
	 * @param idx The index in the document to return
	 * 
	 * @return The DocumentSubSection at idx.
	 **/
	public DocumentSubSection getSectionAt(int idx);
	
	/**
	 * Returns the DocumentSubSection with the given id
	 * 
	 * @param id The name of the sub section to return
	 * 
	 * @return The DocumentSubSection with the given name.
	 **/
	public DocumentSubSection getSection(String id);
	
	/**
	 * Returns the index in the document of the subsection with the given id
	 * 
	 * @param sectionID The id of the desired DocumentSubSection
	 * 
	 * @return The index at which the sub section with the given id appears.
	 **/
	public int getSubSectionIndex(String sectionID);
	
	/**
	 * The entire text of the document, obtained by combining all sub sections.
	 * 
	 * @return The entire text of the document.
	 **/
	public String getFullText();
	
	/**
	 * Adds the given subsection at the given index
	 * 
	 * @param ds The DocumentSubSection to add to this document
	 * @param index The index at which to add the given subsection
	 * 
	 * @return Whether the subsection was added successfully
	 **/
	public boolean addSubSection(DocumentSubSection ds, int index);

	/**
	 * Removes the subsection with the given name from this document
	 * 
	 * @param name The name of the subsection to remove
	 * 
	 * @return Whether the subsection was successfully removed
	 **/
	public boolean removeSubSection(String name);

	/**
	 * Splits the given subsection into two new subsections.
	 * This operation requires that the caller have a lock on the 
	 * subsection to split. This lock will be transfered to partB after the split,
	 * with partA releasing its lock.
	 * 
	 * @param name The name of the subsection to split
	 * @param partA The name to give to the first half of the split subsection
	 * @param partB The name to give to the second half of the split subsection
	 * @param splitIndex The index within the subsection to perform the split
	 * @param userName The name of the user holding the lock on the subsection
	 * 
	 * @return Whether the split was completed successfully
	 **/
	public boolean splitSubSection(String name, String partA, String partB, int splitIndex, String userName);

	/**
	 * Combines two subsections into a new subsection.
	 * Both subsections to combine must be unlocked. 
	 * The resulting subsection will also be unlocked.
	 * 
	 * @param partA The name of the first subsection to be combined
	 * @param partB The name of the second subsection to be combined
	 * @param combinedName The name to give the newly combined subsection.
	 * 
	 * @return Whether the combination was completed successfully
	 **/
	public boolean combineSubSections(String partA, String partB, String combinedName);
	
	/**
	 * Switches the order of the subsections at the given indices.
	 * 
	 * @param idx1 The first index in the document to switch
	 * @param idx2 The second index in the document to switch
	 * 
	 * @return Whether the order of the two subsections was swapped successfully
	 **/
	public boolean flopSubSections(int idx1, int idx2);
}
