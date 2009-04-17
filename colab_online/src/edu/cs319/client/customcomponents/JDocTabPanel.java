package edu.cs319.client.customcomponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;

import edu.cs319.dataobjects.DocumentInfo;
import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.dataobjects.SectionizedDocument;
import edu.cs319.dataobjects.impl.SectionizedDocumentImpl;

/**
 * 
 * @author Amelia Gee
 * @author Wayne
 * @author Justin Nelson
 * 
 */
public class JDocTabPanel extends JPanel {

	private JList sectionList;
	private JPanel sectionPanel;
	private JSplitPane wholePane;
	private JSplitPane workspace;
	private JEditorPane documentPane;
	private JEditorPane workPane;
	private JButton sectionUpButton;
	private JButton sectionDownButton;
	private JButton aquireLock;
	private JButton updateSection;
	private JComboBox sectionSelector;

	private SectionizedDocument doc;
	private DocumentInfo info;

	public JDocTabPanel(DocumentInfo info) {
		this.info = info;
		doc = new SectionizedDocumentImpl(info.getDocumentName());

		sectionList = new JList();
		Font docFont = new Font("Courier New", Font.PLAIN, 11);
		documentPane = new JEditorPane();
		documentPane.setEditable(false);
		documentPane.setFont(docFont);
		PlainDocument doc2 = (PlainDocument) documentPane.getDocument();
		doc2.putProperty(PlainDocument.tabSizeAttribute, 4);
		doc2.putProperty(PlainDocument.lineLimitAttribute, Integer.MAX_VALUE);
		workPane = new JEditorPane();
		workPane.setFont(docFont);
		doc2 = (PlainDocument) workPane.getDocument();
		doc2.putProperty(PlainDocument.tabSizeAttribute, 4);
		sectionUpButton = new JButton("^");
		sectionDownButton = new JButton("V");
		aquireLock = new JButton("Aquire Lock");
		updateSection = new JButton("Update");
		sectionSelector = new JComboBox(doc.getAllSubSections().toArray());
		setUpAppearance();
		setUpListeners();
	}

	private void setUpAppearance() {
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel buttonPanel = new JPanel(new BorderLayout(5, 5));
		buttonPanel.add(sectionUpButton, BorderLayout.NORTH);
		buttonPanel.add(sectionDownButton, BorderLayout.SOUTH);
		sectionPanel = new JPanel(new BorderLayout(10, 10));
		sectionPanel.add(sectionList, BorderLayout.CENTER);
		sectionPanel.add(buttonPanel, BorderLayout.SOUTH);

		JPanel bottomPane = new JPanel(new BorderLayout());
		JPanel norht = new JPanel();
		norht.add(sectionSelector);
		norht.add(aquireLock);
		norht.add(updateSection);
		bottomPane.add(norht, BorderLayout.NORTH);

		documentPane.setMinimumSize(new Dimension(0, 0));
		workPane.setMinimumSize(new Dimension(0, 0));
		JScrollPane workScroll = new JScrollPane(workPane);
		JScrollPane docScroll = new JScrollPane(documentPane);
		bottomPane.add(workScroll, BorderLayout.CENTER);
		workspace = new JSplitPane(JSplitPane.VERTICAL_SPLIT, docScroll, bottomPane);
		wholePane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sectionPanel, workspace);

		workspace.setDividerLocation(250);
		workspace.setOneTouchExpandable(true);
		wholePane.setDividerLocation(150);
		wholePane.setOneTouchExpandable(true);

		add(wholePane, BorderLayout.CENTER);
	}

	private void setUpListeners() {
		sectionUpButton.addActionListener(new UpButtonListener());
		sectionDownButton.addActionListener(new DownButtonListener());
		aquireLock.addActionListener(new AquireLockListener());
		updateSection.addActionListener(new UpdateSubSectionListener());
		sectionSelector.addActionListener(new SelectedSubSectionListener());
	}

	public JList getList() {
		return sectionList;
	}

	public void updateDocPane() {
		documentPane.setText(doc.getFullText());
		sectionList.setListData(doc.getAllSubSections().toArray());
		Object selected = sectionSelector.getSelectedItem();
		sectionSelector.removeAllItems();
		for (DocumentSubSection ds : doc.getAllSubSections()) {
			sectionSelector.addItem(ds);
		}
		sectionSelector.setSelectedItem(selected);
	}

	public SectionizedDocument getSectionizedDocument() {
		return doc;
	}

	private void updateSubSection(DocumentSubSection ds) {
		info.getServer().subSectionUpdated(info.getUserName(), info.getRoomName(),
				info.getDocumentName(), ds.getName(), ds);
	}

	private DocumentSubSection getCurrentSubSection() {
		return (DocumentSubSection) sectionSelector.getSelectedItem();
	}

	private class UpButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

		}
	}

	private class DownButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

		}
	}

	private class AquireLockListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			info.getServer().subSectionLocked(info.getUserName(), info.getRoomName(),
					info.getDocumentName(), getCurrentSubSection().getName());
		}
	}

	private class UpdateSubSectionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			updateSubSection((DocumentSubSection) sectionSelector.getSelectedItem());
		}
	}

	private class SelectedSubSectionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			DocumentSubSection ds = getCurrentSubSection();
			if (ds == null)
				return;
			workPane.setEditable(info.getUserName().equals(ds.lockedByUser()));
			workPane.setText(ds.getText());
		}
	}

}
