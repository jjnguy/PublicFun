package edu.cs319.dataobjects.impl;

import java.util.ArrayList;
import java.util.List;

import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.dataobjects.SectionizedDocument;

public class SectionizedDocumentImpl {

	private List<DocumentSubSection> subSections;
	private String name;

	public SectionizedDocumentImpl(String name) {
		this.name = name;
		this.subSections = new ArrayList<DocumentSubSection>();
	}

	public String getName() {
		return name;
	}

	public List<DocumentSubSection> getAllSubSections() {
		return new ArrayList<DocumentSubSection>(subSections);
	}

	public int getSubsectionCount() {
		return subSections.size();
	}

	public DocumentSubSection getSectionAt(int index) {
		return subSections.get(index);
	}

	public DocumentSubSection getSection(String id) {
		return subSections.get(getSubSectionIndex(id));
	}

	public int getSubSectionIndex(String sectionID) {
		for (int i = 0; i < subSections.size(); i++) {
			if (subSections.get(i).getName().equals(sectionID)) {
				return i;
			}
		}
		return -1;
	}

	public String getFullText() {
		StringBuilder out = new StringBuilder();
		for (DocumentSubSection ds : subSections) {
			out.append(ds.getText());
			out.append("\n");
		}
		return out.toString();
	}

	public boolean addSubSection(DocumentSubSection ds, int index) {
		boolean success = false;
		if(!subSections.contains(ds)) {
			subSections.add(index, ds);
			success = true;
		}
		return success;
	}

	public void removeSubSection(String name) {
		subSections.remove(getSubSectionIndex(name));
	}

	public void splitSubSection(String name, String partA, String partB, int splitIndex) {
		int index = getSubSectionIndex(name);
		DocumentSubSection ds = subSections.get(index);
		subSections.remove(ds);
		DocumentSubSection first = new DocumentSubSectionImpl(partA);
		DocumentSubSection second = new DocumentSubSectionImpl(partB);
		String text = ds.getText();
		first.setLocked(true,"admin");
		first.setText(text.substring(0, splitIndex),"admin");
		second.setText(text.substring(splitIndex, text.length()),"admin");
		first.setLocked(false,"admin");
		second.setLocked(false,"admin");
		subSections.add(index, first);
		subSections.add(index + 1, second);
	}

	public void combineSubSections(String partA, String partB, String combinedName) {
		int index = getSubSectionIndex(partA);
		DocumentSubSection first = subSections.get(index);
		DocumentSubSection second = subSections.get(getSubSectionIndex(partB));
		subSections.remove(first);
		subSections.remove(second);
		DocumentSubSection combined = new DocumentSubSectionImpl(combinedName);
		combined.setLocked(true,"admin");
		combined.setText(first.getText() + "\n" + second.getText(),"admin");
		combined.setLocked(false,"admin");
		subSections.add(index, combined);
	}

	public boolean equals(Object o) {
		if(o instanceof String) {
			String s = (String) o;
			return s.equals(getName());
		} else if(o instanceof SectionizedDocument) {
			SectionizedDocument sd = (SectionizedDocument) o;
			return sd.getName().equals(getName());
		}
		return false;
	}

	public int hashCode() {
		return getName().hashCode();
	}
}
