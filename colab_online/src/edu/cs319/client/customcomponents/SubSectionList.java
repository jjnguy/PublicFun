package edu.cs319.client.customcomponents;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.dataobjects.SectionizedDocument;

public class SubSectionList extends JList implements SectionizedDocument {

	private SectionizedDocListModel model;

	public SubSectionList(SectionizedDocument doc) {
		model = new SectionizedDocListModel(doc);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setCellRenderer(new SectionCellRenderer());
		this.setModel(model);
	}

	public void subSectionUpdated(DocumentSubSection sec) {
		int idx = model.getSubSectionIndex(sec.getName());
		if (idx == -1) {
			return;
		}
		DocumentSubSection oldSec = (DocumentSubSection) model.getElementAt(idx);
		oldSec.setText(oldSec.lockedByUser(), sec.getText());
		model.refreshView();
		DocumentSubSection selected = (DocumentSubSection) getSelectedValue();
		if (selected == null)
			return;
		if (sec.getName().equals(((DocumentSubSection) getSelectedValue()).getName())) {
			setSelectedIndex(getSelectedIndex());
		}
	}

	public void fullyRefreshList(List<DocumentSubSection> newSet) {
		model.removeAllSubSections();
		model.addAllSubSections(newSet);
	}

	@Override
	public void addAllSubSections(List<DocumentSubSection> ss) {
		model.addAllSubSections(ss);
	}

	@Override
	public boolean addSubSection(DocumentSubSection ds, int index) {
		return model.addSubSection(ds, index);
	}

	@Override
	public boolean combineSubSections(String partA, String partB, String combinedName) {
		return model.combineSubSections(partA, partB, combinedName);
	}

	@Override
	public boolean flopSubSections(int idx1, int idx2) {
		return model.flopSubSections(idx1, idx2);
	}

	@Override
	public List<DocumentSubSection> getAllSubSections() {
		return model.getAllSubSections();
	}

	@Override
	public String getFullText() {
		return model.getFullText();
	}

	@Override
	public DocumentSubSection getSection(String id) {
		return model.getSection(id);
	}

	@Override
	public DocumentSubSection getSectionAt(int idx) {
		return model.getSectionAt(idx);
	}

	@Override
	public int getSubSectionCount() {
		return model.getSubSectionCount();
	}

	@Override
	public int getSubSectionIndex(String sectionID) {
		return model.getSubSectionIndex(sectionID);
	}

	@Override
	public void removeAllSubSections() {
		model.removeAllSubSections();
	}

	@Override
	public boolean removeSubSection(String name) {
		return model.removeSubSection(name);
	}

	@Override
	public boolean splitSubSection(String name, String partA, String partB, int splitIndex,
			String userName) {
		return model.splitSubSection(name, partA, partB, splitIndex, userName);
	}

	@Override
	public String getName() {
		return model.getName();
	}

	private class SectionCellRenderer extends JLabel implements ListCellRenderer {
		public SectionCellRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			DocumentSubSection sec = (DocumentSubSection) value;
			setText(sec.toString());
			if (sec.isLocked()) {
				setForeground(Color.LIGHT_GRAY);
				if (isSelected) {
					setBackground(new Color(98, 102, 145));
				} else {
					setBackground(Color.WHITE);
				}
			} else {
				setForeground(Color.BLACK);
				if (isSelected) {
					setBackground(new Color(185, 196, 225));
				} else {
					setBackground(Color.WHITE);
				}
			}
			return this;
		}
	}
}
