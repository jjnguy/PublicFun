package edu.cs319.dataobjects.impl;

import java.util.ArrayList;
import java.util.List;

import edu.cs319.dataobjects.DocumentSubSection;

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

	public void addSubSection(DocumentSubSection ds, int index) {
		subSections.add(index, ds);
	}

	public void removeSubSection(String name) {
		subSections.remove(getSubSectionIndex(name));
	}

	public void splitSubSection(String name, String partA, String partB, int splitIndex) {
		int index = getSubSectionIndex(name);
		DocumentSubSection ds = subSections.get(index);
		subSections.remove(ds);
		DocumentSubSection first = new DocumentSubsectionImpl(partA);
		DocumentSubSection second = new DocumentSubsectionImpl(partB);
		String text = ds.getText();
		first.setText(text.substring(0, splitIndex));
		second.setText(text.substring(splitIndex, text.length()));
		subSections.add(index, first);
		subSections.add(index + 1, second);
	}

	public void combineSubSections(String partA, String partB, String combinedName) {
		int index = getSubSectionIndex(partA);
		DocumentSubSection first = subSections.get(index);
		DocumentSubSection second = subSections.get(getSubSectionIndex(partB));
		subSections.remove(first);
		subSections.remove(second);
		DocumentSubSection combined = new DocumentSubsectionImpl(combinedName);
		combined.setText(first.getText() + "\n" + second.getText());
		subSections.add(index, combined);
	}
}
