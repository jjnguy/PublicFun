package edu.cs319.client.customcomponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;

import edu.cs319.dataobjects.DocumentInfo;
import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.dataobjects.SectionizedDocument;
import edu.cs319.dataobjects.impl.DocumentSubSectionImpl;
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
	private JButton addSubSection;
	private JButton unlockSubSection;
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
		try {
			sectionUpButton = new JButton(new ImageIcon(ImageIO.read(new File(
					"images/green_up_arrow_small.png"))));
			sectionDownButton = new JButton(new ImageIcon(ImageIO.read(new File(
					"images/green_down_arrow_small.png"))));
		} catch (IOException e) {
			e.printStackTrace();
			sectionUpButton = new JButton("^");
			sectionDownButton = new JButton("V");
		}
		aquireLock = new JButton("Aquire Lock");
		updateSection = new JButton("Update");
		addSubSection = new JButton("New SubSection");
		unlockSubSection = new JButton("Unlock");
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
		norht.add(unlockSubSection);
		norht.add(addSubSection);
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

	private void newSubSection(String name) {
		info.getServer().newSubSection(info.getUserName(), info.getRoomName(), doc.getName(), name,
				doc.getSubsectionCount());
	}

	private void setUpListeners() {
		sectionUpButton.addActionListener(new UpButtonListener());
		sectionDownButton.addActionListener(new DownButtonListener());
		aquireLock.addActionListener(new AquireLockListener());
		updateSection.addActionListener(new UpdateSubSectionListener());
		sectionSelector.addActionListener(new SelectedSubSectionListener());
		addSubSection.addActionListener(new NewSubSectionListener());
		unlockSubSection.addActionListener(new ReleaseLockListener());
	}

	public JList getList() {
		return sectionList;
	}

	public void updateDocPane() {
		StringBuilder docText = new StringBuilder();
		for(int i = 0; i < doc.getSubsectionCount(); i++) {
			docText.append("----------START SECTION <" + doc.getSectionAt(i).getName() + ">----------\n");
			docText.append(doc.getSectionAt(i).getText());
			docText.append("\n----------END SECTION <" + doc.getSectionAt(i).getName() + ">------------\n");
		}
		documentPane.setText(docText.toString());
//		documentPane.setText(doc.getFullText());
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

	private void updateSubSection(DocumentSubSection ds, String newText) {
		DocumentSubSection temp = new DocumentSubSectionImpl(ds.getName());
		temp.setLocked(ds.isLocked(), ds.lockedByUser());
		temp.setText(info.getUserName(), newText);
		info.getServer().subSectionUpdated(info.getUserName(), info.getRoomName(),
				info.getDocumentName(), ds.getName(), temp);
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
			updateSubSection((DocumentSubSection) sectionSelector.getSelectedItem(), workPane
					.getText());
		}
	}

	private class SelectedSubSectionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			DocumentSubSection ds = getCurrentSubSection();
			if (ds != null) {
				workPane.setEditable(info.getUserName().equals(ds.lockedByUser()));
				workPane.setText(ds.getText());
			} else {
				workPane.setText("");
				workPane.setEditable(false);
			}
		}
	}

	private class NewSubSectionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String name = JOptionPane.showInputDialog(JDocTabPanel.this,
					"Name the subsection bitch!!");
			if (name == null)
				return;
			newSubSection(name);
		}
	}

	private class ReleaseLockListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			info.getServer().subSectionUnLocked(info.getUserName(), info.getRoomName(),
					info.getDocumentName(), getCurrentSubSection().getName());
		}
	}

	private class RightClickListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() != MouseEvent.BUTTON2)
				return;
			JMenu menu = new SectionRightClickMenu((DocumentSubSection)sectionList.getSelectedValue());
			menu.setVisible(true);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	public class SectionRightClickMenu extends JMenu {

		private JMenuItem aquireLockItem;
		private JMenuItem releaseLockItem;
		private JMenuItem newSubSectionItem;

		private DocumentSubSection sec;
		
		public SectionRightClickMenu(DocumentSubSection section) {
			super();
			sec = section;
			aquireLockItem = new JMenuItem("Aquire Lock");
			aquireLockItem.addActionListener(new AquireLockListener());
			add(aquireLockItem);
			releaseLockItem = new JMenuItem("Release Lock");
			releaseLockItem.addActionListener(new ReleaseLockListener());
			add(releaseLockItem);
			newSubSectionItem = new JMenuItem("Add New SubSection");
			newSubSectionItem.addActionListener(new NewSubSectionListener());
			add(newSubSectionItem);
		}
	}
}
