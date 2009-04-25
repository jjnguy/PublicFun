package edu.cs319.client.customcomponents;

import java.util.List;

import javax.swing.AbstractListModel;

import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.dataobjects.SectionizedDocument;
import edu.cs319.util.Util;

public class SectionizedDocListModel extends AbstractListModel implements SectionizedDocument {
	
	private SectionizedDocument doc;

	public SectionizedDocListModel(SectionizedDocument doc) {
		this.doc = doc;
	}

	@Override
	public Object getElementAt(int arg0) {
		return doc.getSectionAt(arg0);
	}

	@Override
	public int getSize() {
		return doc.getSubSectionCount();
	}

	@Override
	public void addAllSubSections(List<? extends DocumentSubSection> ss) {
		doc.addAllSubSections(ss);
		fireIntervalAdded(this, 0, getSize());
	}

	@Override
	public boolean addSubSection(DocumentSubSection ds, int index) {
		if (doc.addSubSection(ds, index)) {
			fireIntervalAdded(this, index, index);
			return true;
		}else {
			if (Util.DEBUG) {
				System.out.println("Adding subsection t oa doc failed");
			}
		}
		return false;
	}

	@Override
	public boolean combineSubSections(String partA, String partB, String combinedName) {
		if (doc.combineSubSections(partA, partB, combinedName)) {
			fireContentsChanged(this, 0, getSize());
			return true;
		}
		return false;
	}

	@Override
	public boolean flopSubSections(int idx1, int idx2) {
		if (doc.flopSubSections(idx1, idx2)) {
			fireContentsChanged(this, idx1, idx2);
			return true;
		}
		return false;
	}

	@Override
	public List<DocumentSubSection> getAllSubSections() {
		return doc.getAllSubSections();
	}

	@Override
	public String getFullText() {
		return doc.getFullText();
	}

	@Override
	public String getName() {
		return doc.getName();
	}

	@Override
	public DocumentSubSection getSection(String id) {
		return doc.getSection(id);
	}

	@Override
	public DocumentSubSection getSectionAt(int idx) {
		return doc.getSectionAt(idx);
	}

	@Override
	public int getSubSectionCount() {
		return doc.getSubSectionCount();
	}

	@Override
	public int getSubSectionIndex(String sectionID) {
		return doc.getSubSectionIndex(sectionID);
	}

	@Override
	public void removeAllSubSections() {
		doc.removeAllSubSections();
		fireIntervalRemoved(this, 0, getSize());
	}

	@Override
	public boolean removeSubSection(String name) {
		int idx = getSubSectionIndex(name);
		if (doc.removeSubSection(name)) {
			fireIntervalRemoved(this, idx, idx);
			return true;
		}
		return false;
	}

	@Override
	public boolean splitSubSection(String name, String partA, String partB, int splitIndex,
			String userName) {
		if (doc.splitSubSection(name, partA, partB, splitIndex, userName)) {
			fireContentsChanged(this, 0, getSize());
			return true;
		}
		return false;
	}

	public void refreshView() {
		fireContentsChanged(this, 0, getSize());
	}

}
