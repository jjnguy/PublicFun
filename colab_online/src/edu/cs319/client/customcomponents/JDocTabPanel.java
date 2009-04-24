package edu.cs319.client.customcomponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.PlainDocument;

import edu.cs319.client.WindowClient;
import edu.cs319.dataobjects.DocumentInfo;
import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.dataobjects.SectionizedDocument;
import edu.cs319.dataobjects.impl.DocumentSubSectionImpl;
import edu.cs319.dataobjects.impl.SectionizedDocumentImpl;
import edu.cs319.server.CoLabPrivilegeLevel;
import edu.cs319.util.Util;

/**
 * 
 * @author Amelia Gee
 * @author Wayne Wrowweeclifffe
 * @author Justin Nelson
 * 
 */
@SuppressWarnings("serial")
public class JDocTabPanel extends JPanel {

	// Number of milliseconds between automatic updates
	private final static int UPDATE_NUM_MS = 500;

	private JPanel sectionPanel;
	private JSplitPane wholePane;
	private JSplitPane workspace;
	private DocumentDisplayPane topFullDocumentPane;
	private JEditorPane currentWorkingPane;
	private JButton sectionUpButton;
	private JButton sectionDownButton;
	private JButton aquireLock;
	private JButton updateSection;
	private JButton addSubSection;
	private JButton unlockSubSection;

	private SubSectionList listOfSubSections;

	final private DocumentInfo info;

	private WindowClient client;

	public JDocTabPanel(DocumentInfo info, WindowClient client) {
		this.info = info;
		this.client = client;
		setName(info.getDocumentName());
		listOfSubSections = new SubSectionList(new SectionizedDocumentImpl(info.getDocumentName()));
		Font docFont = new Font("Courier New", Font.PLAIN, 11);
		topFullDocumentPane = new DocumentDisplayPane();
		topFullDocumentPane.setEditable(false);
		topFullDocumentPane.setFont(docFont);
		topFullDocumentPane.setLineWrap(false);
		topFullDocumentPane.setTabSize(4);

		currentWorkingPane = new WorkingPane();
		currentWorkingPane.setFont(docFont);
		currentWorkingPane.addMouseListener(new RightClickListener());
		PlainDocument doc2 = (PlainDocument) currentWorkingPane.getDocument();
		doc2.putProperty(PlainDocument.tabSizeAttribute, 4);
		setUpAppearance();
		setUpListeners();

		Timer timer = new Timer(UPDATE_NUM_MS, new AutoUpdateTask());
		timer.start();
	}

	private void setUpAppearance() {
		try {
			sectionUpButton = new JButton(new ImageIcon(ImageIO.read(new File(
					"images/green_up_arrow_small.png"))));
			sectionDownButton = new JButton(new ImageIcon(ImageIO.read(new File(
					"images/green_down_arrow_small.png"))));
		} catch (IOException e) {
			if (Util.DEBUG) {
				e.printStackTrace();
			}
			sectionUpButton = new JButton("^");
			sectionDownButton = new JButton("v");
		}
		sectionUpButton.setToolTipText("Move Section Up");
		sectionDownButton.setToolTipText("Move Section Down");
		aquireLock = new JButton("Aquire Lock");
		updateSection = new JButton("Update");
		addSubSection = new JButton("New SubSection");
		unlockSubSection = new JButton("Unlock");
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel buttonPanel = new JPanel(new BorderLayout(5, 5));
		buttonPanel.add(sectionUpButton, BorderLayout.NORTH);
		buttonPanel.add(addSubSection, BorderLayout.CENTER);
		buttonPanel.add(sectionDownButton, BorderLayout.SOUTH);
		sectionPanel = new JPanel(new BorderLayout(10, 10));
		sectionPanel.add(listOfSubSections, BorderLayout.CENTER);
		sectionPanel.add(buttonPanel, BorderLayout.SOUTH);

		JPanel bottomPane = new JPanel(new BorderLayout());
		//JPanel north = new JPanel();
		//north.add(aquireLock);
		//north.add(updateSection);
		//north.add(unlockSubSection);
		//north.add(addSubSection);
		//bottomPane.add(north, BorderLayout.NORTH);

		topFullDocumentPane.setMinimumSize(new Dimension(0, 0));
		currentWorkingPane.setMinimumSize(new Dimension(0, 0));
		JScrollPane workScroll = new JScrollPane(currentWorkingPane);
		JScrollPane docScroll = new JScrollPane(topFullDocumentPane);
		bottomPane.add(workScroll, BorderLayout.CENTER);
		workspace = new JSplitPane(JSplitPane.VERTICAL_SPLIT, docScroll, bottomPane);
		wholePane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sectionPanel, workspace);
		wholePane.setContinuousLayout(true);

		workspace.setDividerLocation(250);
		workspace.setContinuousLayout(true);
		workspace.setOneTouchExpandable(true);
		wholePane.setDividerLocation(150);
		wholePane.setOneTouchExpandable(true);

		add(wholePane, BorderLayout.CENTER);
	}

	public static boolean hasPermission(WindowClient client) {
		if (client.getPrivLevel() == CoLabPrivilegeLevel.OBSERVER) {
			JOptionPane
					.showMessageDialog(
							null,
							"You do not have permission to do this action.  Ask your Admin for a promotion",
							"Insufficient Permissions", JOptionPane.INFORMATION_MESSAGE, null);
			return false;
		}
		return true;
	}

	public void newSubSection(String name) {
		if (!hasPermission(client)) {
			return;
		}
		System.out.println("Creating New SubSection: " + info);
		info.getServer().newSubSection(info.getUserName(), info.getRoomName(),
				listOfSubSections.getName(), name, listOfSubSections.getSubSectionCount());
	}

	private void setUpListeners() {
		listOfSubSections.addMouseListener(new RightClickListener());
		listOfSubSections.addMouseListener(new DoubleClickListener());
		sectionUpButton.addActionListener(new UpButtonListener());
		sectionDownButton.addActionListener(new DownButtonListener());
		aquireLock.addActionListener(new AquireLockListener());
		updateSection.addActionListener(new UpdateSubSectionListener());
		listOfSubSections.addListSelectionListener(new SelectedSubSectionListener());
		addSubSection.addActionListener(new NewSubSectionListener());
		unlockSubSection.addActionListener(new ReleaseLockListener());
	}

	public JList getJListOfSubSections() {
		return listOfSubSections;
	}

	public void updateTopDocumentPane() {
		topFullDocumentPane.updateDocument(listOfSubSections);
	}

	public SectionizedDocument getSectionizedDocument() {
		return listOfSubSections;
	}
	
	public void subSectionCreated(DocumentSubSection ds, int index) {
		System.out.println("Adding new SubSection: " + info + " SectionName: " + ds.getName() + " Currently Selected: " + getCurrentlySelectedSubSection());
		getSectionizedDocument().addSubSection(ds, index);
		if(ds.lockedByUser().equals(info.getUserName()) || listOfSubSections.getSelectedIndex() == -1) {
			listOfSubSections.setSelectedIndex(index);
		}
	}
	
	public void subSectionFlopped(String up, String down) {
		System.out.println("Flopping SubSection: " + info + " Section Up: " + up + " Section Down: " + down + " Currently Selected: " + getCurrentlySelectedSubSection());
		int idx1 = getSectionizedDocument().getSubSectionIndex(up);
		int idx2 = getSectionizedDocument().getSubSectionIndex(down);
		getSectionizedDocument().flopSubSections(idx1, idx2);
		if(getCurrentlySelectedSubSection().getName().equals(down)) {
			listOfSubSections.setSelectedIndex(idx2);
		} else if(getCurrentlySelectedSubSection().getName().equals(up)) {
			listOfSubSections.setSelectedIndex(idx1);
		}
		
		updateTopDocumentPane();
	}
	
	public void subSectionLocked(String section, String user) {
		System.out.println("Locking SubSection: " + info + " Locking: " + section + " Currently Selected: " + getCurrentlySelectedSubSection());
		String current = getCurrentlySelectedSubSection().getName();
		getSectionizedDocument().getSection(section).setLocked(true, user);
		listOfSubSections.subSectionUpdated(getSectionizedDocument().getSection(section));
		if(section.equals(current)) {
			listOfSubSections.setSelectedIndex(getSectionizedDocument().getSubSectionIndex(current));
			updateWorkPane(section);
		}
	}
	
	public void subSectionMerged(String one, String two, String sec) {
		System.out.println("Merging Two SubSections: One: " + one + " Two: " + two + " New: " + sec + " " + info + " Currently Selected: " + getCurrentlySelectedSubSection());	
		String current = getCurrentlySelectedSubSection().getName();
		getSectionizedDocument().combineSubSections(one, two, sec);
		updateTopDocumentPane();
		if(one.equals(current) || two.equals(current)) {
			listOfSubSections.setSelectedIndex(getSectionizedDocument().getSubSectionIndex(sec));
		}
	}
	
	public void subSectionSplit(String old, String one, String two, int index, String user) {
		System.out.println("Splitting SubSection: Old: " + old + " One: " + one + " Two: " + two + " " + info + " Currently Selected: " + getCurrentlySelectedSubSection());
		String current = getCurrentlySelectedSubSection().getName();
		getSectionizedDocument().splitSubSection(old, one, two, index, user);
		updateTopDocumentPane();
		if(old.equals(current)) {
			listOfSubSections.setSelectedIndex(getSectionizedDocument().getSubSectionIndex(two));
		}
	}
	
	public void subSectionsRefreshed(List<DocumentSubSection> all) {
		System.out.println("Updating All SubSections: " + info + " Currently Selected: " + getCurrentlySelectedSubSection());
		getSectionizedDocument().removeAllSubSections();
		getSectionizedDocument().addAllSubSections(all);
		updateTopDocumentPane();
		if(listOfSubSections.getSelectedIndex() == -1 && all.size() > 0) {
			listOfSubSections.setSelectedIndex(0);
		}
	}
	
	public void subSectionRemoved(String section) {
		System.out.println("Removing SubSection: " + info + " Removing: " + section + " Currently Selected: " + getCurrentlySelectedSubSection());
		String current = getCurrentlySelectedSubSection().getName();
		getSectionizedDocument().removeSubSection(section);
		updateTopDocumentPane();
		if(section.equals(current) && getSectionizedDocument().getSubSectionCount() > 0) {
			listOfSubSections.setSelectedIndex(0);
		}
	}
	
	public void subSectionUnlocked(String section, String user) {
		System.out.println("Unlocking SubSection: " + info + " Unlocking: " + section + " Currently Selected: " + getCurrentlySelectedSubSection());
		String current = getCurrentlySelectedSubSection().getName();
		getSectionizedDocument().getSection(section).setLocked(false, user);
		listOfSubSections.subSectionUpdated(getSectionizedDocument().getSection(section));
		if(section.equals(current)) {
			listOfSubSections.setSelectedIndex(getSectionizedDocument().getSubSectionIndex(current));
			updateWorkPane(section);
		}
	}
	
	public void subSectionUpdated(String secId, String user, DocumentSubSection sec) {
		System.out.println("Updating SubSection: " + info + " Updating: " + secId + " Currently Selected: " + getCurrentlySelectedSubSection());
		String current = getCurrentlySelectedSubSection().getName();
		getSectionizedDocument().getSection(secId).setText(user, sec.getText());
		updateTopDocumentPane();
		if(secId.equals(current)) {
			listOfSubSections.setSelectedIndex(getSectionizedDocument().getSubSectionIndex(current));
			if(!info.getUserName().equals(getSectionizedDocument().getSection(current).lockedByUser())) {
				updateWorkPane(current);
			}
		}
	}

	private void updateSubSection(DocumentSubSection ds, String newText) {
		if (!hasPermission(client))
			return;
		if (ds == null) {
			if (Util.DEBUG) {
				System.out.println("JDocTabedPanel.updateSubSection  the section was null...wtf");
			}
			return;
		}
		System.out.println("");
		System.out.println("AutoUpdating!!!");
		DocumentSubSection temp = new DocumentSubSectionImpl(ds.getName());
		temp.setLocked(ds.isLocked(), ds.lockedByUser());
		temp.setText(info.getUserName(), newText);
		info.getServer().subSectionUpdated(info.getUserName(), info.getRoomName(),
				info.getDocumentName(), ds.getName(), temp);
	}

	private DocumentSubSection getCurrentlySelectedSubSection() {
		DocumentSubSection sel = (DocumentSubSection) listOfSubSections.getSelectedValue();
		if (sel == null) {
			if (listOfSubSections.getModel().getSize() == 0) {
				return null;
			}
			return (DocumentSubSection) listOfSubSections.getModel().getElementAt(0);
		}
		return sel;
	}

	private class AutoUpdateTask implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// if the current subsection is not locked by this user, don't send the updates
			if (!info.getUserName().equals(getCurrentlySelectedSubSection().lockedByUser())) {
				return;
			}
			if (currentWorkingPane.getText().trim().equals("")) {
				return;
			}
			if (getCurrentlySelectedSubSection().getText().equals(currentWorkingPane.getText())) {
				return;
			}
			System.out.println("AutoUpdating SubSection: " + info + " Currently Selected: " + getCurrentlySelectedSubSection());
			updateSubSection(getCurrentlySelectedSubSection(), currentWorkingPane.getText());
		}
	}

	private class UpButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!hasPermission(client)) {
				return;
			}
			System.out.println("Moving SubSection Up: " + info + " Currently Selected: " + getCurrentlySelectedSubSection());
			if (listOfSubSections.getSelectedIndex() > 0) {
				DocumentSubSection moveUp = (DocumentSubSection) listOfSubSections
						.getSelectedValue();
				DocumentSubSection moveDown = (DocumentSubSection) listOfSubSections.getModel()
						.getElementAt(listOfSubSections.getSelectedIndex() - 1);
				info.getServer().subSectionFlopped(info.getUserName(), info.getRoomName(),
						info.getDocumentName(), moveUp.getName(), moveDown.getName());
				listOfSubSections.setSelectedValue(moveUp, true);
			}
		}
	}

	private class DownButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!hasPermission(client)) {
				return;
			}
			System.out.println("Moving SubSection Down: " + info + " Currently Selected: " + getCurrentlySelectedSubSection());
			if (listOfSubSections.getSelectedIndex() != -1
					&& listOfSubSections.getSelectedIndex() < listOfSubSections.getModel()
							.getSize() - 1) {
				DocumentSubSection moveDown = (DocumentSubSection) listOfSubSections
						.getSelectedValue();
				DocumentSubSection moveUp = (DocumentSubSection) listOfSubSections.getModel()
						.getElementAt(listOfSubSections.getSelectedIndex() + 1);
				info.getServer().subSectionFlopped(info.getUserName(), info.getRoomName(),
						info.getDocumentName(), moveUp.getName(), moveDown.getName());
				listOfSubSections.setSelectedValue(moveDown, true);
			}
		}
	}



	private class UpdateSubSectionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!hasPermission(client)) {
				return;
			}
			System.out.println("Updating SubSection: " + info + " Currently Selected: " + getCurrentlySelectedSubSection());
			updateSubSection(getCurrentlySelectedSubSection(), currentWorkingPane.getText());
		}
	}

	private class SelectedSubSectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting() == false) {
				DocumentSubSection ds = getCurrentlySelectedSubSection();
				updateWorkPane(ds);
			}
		}
	}

	public void updateWorkPane(String secName) {
		System.out.println("Updating Working Pane: " + info + " Currently Selected: " + getCurrentlySelectedSubSection() + " Updating with Section: " + secName);
		updateWorkPane(listOfSubSections.getSection(secName));
	}

	public void updateWorkPane(DocumentSubSection ds) {
		synchronized (currentWorkingPane) {
			// TODO this hopefully works!!
			if (ds.equals(getCurrentlySelectedSubSection())) {
				int carrotPos = currentWorkingPane.getCaretPosition();
				if (ds == null) {
					currentWorkingPane.setText("");
					currentWorkingPane.setEditable(false);
					return;
				}
				currentWorkingPane.setEditable(info.getUserName().equals(ds.lockedByUser()));
				currentWorkingPane.setText(ds.getText());
				int length = currentWorkingPane.getText().length();
				currentWorkingPane.setCaretPosition(carrotPos <= length ? carrotPos : length);
			}
		}
	}

	private class NewSubSectionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!hasPermission(client))
				return;
			String name = JOptionPane.showInputDialog(JDocTabPanel.this,
					"Enter a name for the new SubSection");
			if (name == null)
				return;
			newSubSection(name);
		}
	}
	
	private class AquireLockListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			aquireLockRequest();
		}
	}
	
	private void aquireLockRequest() {
		if (!hasPermission(client)) {
			return;
		}
		System.out.println("Aquiring Lock: " + info + " Currently Selected: "  + getCurrentlySelectedSubSection());
		info.getServer().subSectionLocked(info.getUserName(), info.getRoomName(),
				info.getDocumentName(), getCurrentlySelectedSubSection().getName());
	}

	private class ReleaseLockListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			releaseLockRequest();
		}
	}
	
	private void releaseLockRequest() {
		System.out.println("Releasing Lock: " + info + " Currently Selected: " + getCurrentlySelectedSubSection());
		info.getServer().subSectionUpdated(info.getUserName(), info.getRoomName(),
				info.getDocumentName(), getCurrentlySelectedSubSection().getName(),
				getCurrentlySelectedSubSection());
		info.getServer().subSectionUnLocked(info.getUserName(), info.getRoomName(),
				info.getDocumentName(), getCurrentlySelectedSubSection().getName());
	}

	private class DeleteSubSectionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Deleting SubSection: " + info + " Currently Selected: "  + getCurrentlySelectedSubSection());
			deleteSubSection();
		}
	}
	
	public void deleteSubSection() {
		/*
		 * if(!hasPermission(client)) return;
		 */
		DocumentSubSection sec = getCurrentlySelectedSubSection();
		if (info.getUserName().equals(sec.lockedByUser())) {
			int newSelection = listOfSubSections.getSelectedIndex();
			info.getServer().subSectionRemoved(info.getUserName(), info.getRoomName(),
					info.getDocumentName(), sec.getName());
			listOfSubSections.setSelectedIndex(newSelection);
		}
	}

	private class SplitSubSectionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Splitting SubSection: " + info + " Currently Selected: " + getCurrentlySelectedSubSection());
			splitSubSection();
		}
	}
	
	public void splitSubSection() {
		/*
		 * if (!hasPermission(client)) return;
		 */
		String name1 = JOptionPane.showInputDialog(JDocTabPanel.this, "Name of the first part:");
		if (name1 == null)
			return;
		String name2 = JOptionPane.showInputDialog(JDocTabPanel.this, "Name of the second part:");
		if (name2 == null)
			return;
		DocumentSubSection sec = getCurrentlySelectedSubSection();
		int idx = SplitChooser.showSplitChooserDialog(sec);
		if (idx == -1)
			return;
		info.getServer().subSectionSplit(info.getUserName(), info.getRoomName(),
				listOfSubSections.getName(), sec.getName(), name1, name2, idx);
	}

	private class MergeSubSectionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Merging SubSections: " + info + " Currently Selected: " + getCurrentlySelectedSubSection());
			mergeSubSection();
		}
	}
	
	public void mergeSubSection() {
		/*
		 * if (!hasPermission(client)) return;
		 */
		int count = listOfSubSections.getSubSectionCount();
		if (count < 2)
			return;
		DocumentSubSection top = null, bottom = null;
		if (listOfSubSections.getSelectedIndex() == 0) {
			top = listOfSubSections.getSectionAt(0);
			bottom = listOfSubSections.getSectionAt(1);
		} else if (listOfSubSections.getSelectedIndex() == count - 1) {
			top = listOfSubSections.getSectionAt(count - 2);
			bottom = listOfSubSections.getSectionAt(count - 1);
		} else {
			String[] values = { "Above", "Below" };
			String aboveOrBelow = (String) JOptionPane
					.showInputDialog(
							JDocTabPanel.this,
							"Would you like to merge the selected section \nwith the section above or below?",
							"Merge SubSections", JOptionPane.QUESTION_MESSAGE, null, values,
							values[0]);
			if (aboveOrBelow == null)
				return;
			if (aboveOrBelow.equals("Above")) {
				top = listOfSubSections.getSectionAt(listOfSubSections.getSelectedIndex() - 1);
				bottom = listOfSubSections.getSectionAt(listOfSubSections.getSelectedIndex());
			} else if (aboveOrBelow.equals("Below")) {
				top = listOfSubSections.getSectionAt(listOfSubSections.getSelectedIndex());
				bottom = listOfSubSections.getSectionAt(listOfSubSections.getSelectedIndex() + 1);
			}
		}
		String name = JOptionPane.showInputDialog(JDocTabPanel.this, "Name of merged section:");
		if (name == null) {
			return;
		}
		info.getServer().subSectionCombined(info.getUserName(), info.getRoomName(),
				listOfSubSections.getName(), top.getName(), bottom.getName(), name);
	}

	private static class SplitChooser extends JDialog {
		private JTextArea theText;
		private JButton ok;
		private JLabel instr;
		private int retIndex = -1;

		private SplitChooser(DocumentSubSection sec) {
			super();
			setModal(true);
			instr = new JLabel("Place your cursor where you would like to split the subsection");
			theText = new JTextArea();
			theText.setText(sec.getText());
			// theText.setEditable(false);
			Font f = new Font("Courier New", Font.PLAIN, 11);
			theText.setFont(f);
			KeyListener[] list = theText.getKeyListeners();
			for (KeyListener k : list) {
				theText.removeKeyListener(k);
			}
			theText.setTabSize(4);
			ok = new JButton("Ok");
			JScrollPane scroll = new JScrollPane(theText);
			add(scroll, BorderLayout.CENTER);
			add(instr, BorderLayout.NORTH);
			add(ok, BorderLayout.SOUTH);
			ok.addActionListener(okAction);
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			pack();
		}

		public int getChosenIndex() {
			return retIndex;
		}

		public static int showSplitChooserDialog(DocumentSubSection sec) {
			SplitChooser ch = new SplitChooser(sec);
			ch.setVisible(true);
			if (Util.DEBUG) {
				System.out.println("Index chosen: " + ch.getChosenIndex());
			}
			return ch.getChosenIndex();
		}

		private ActionListener okAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				retIndex = theText.getCaretPosition();
				SplitChooser.this.dispose();
			}
		};
	}

	private class RightClickListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			doPop(e);
		}

		private void doPop(MouseEvent e) {
			if (e.getButton() != MouseEvent.BUTTON3)
				return;
			if (Util.DEBUG) {
				System.out.println("click event");
			}
			if (e.isPopupTrigger()) {
				JPopupMenu menu;
				if (e.getSource() == currentWorkingPane) {
					menu = new WorkingViewRightClickMenu();
				} else {
					menu = new SectionRightClickMenu(getCurrentlySelectedSubSection());
				}
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			doPop(e);
		}
	}
	
	private class DoubleClickListener extends MouseAdapter {
		
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				if(info.getUserName().equals(getCurrentlySelectedSubSection().lockedByUser())) {
					releaseLockRequest();
				} else if(getCurrentlySelectedSubSection().lockedByUser() == null) {
					aquireLockRequest();
				}
			}
		}

	}

	public class WorkingViewRightClickMenu extends JPopupMenu {

		private JMenuItem splitSubSectionItem;
		private long bornondate;

		public WorkingViewRightClickMenu() {
			bornondate = System.currentTimeMillis();
			splitSubSectionItem = new JMenuItem("Split SubSection At Carrot");
			splitSubSectionItem.addActionListener(new SplitAtCarrotListener());
			add(splitSubSectionItem);
		}

	}

	private class SplitAtCarrotListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int i = currentWorkingPane.getCaretPosition();
			if (i == -1) {
				return;
			}
			String name1 = JOptionPane
					.showInputDialog(JDocTabPanel.this, "Name of the first part:");
			if (name1 == null) {
				return;
			}
			String name2 = JOptionPane.showInputDialog(JDocTabPanel.this,
					"Name of the second part:");
			if (name2 == null) {
				return;
			}
			DocumentSubSection sec = getCurrentlySelectedSubSection();
			info.getServer().subSectionSplit(info.getUserName(), info.getRoomName(),
					listOfSubSections.getName(), sec.getName(), name1, name2, i);
		}

	}

	public class SectionRightClickMenu extends JPopupMenu {

		private JMenuItem aquireLockItem;
		private JMenuItem releaseLockItem;
		private JMenuItem splitSectionItem;
		private JMenuItem mergeSectionItem;
		private JMenuItem newSubSectionItem;
		private JMenuItem deleteSubSectionItem;

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
			splitSectionItem = new JMenuItem("Split SubSection");
			splitSectionItem.addActionListener(new SplitSubSectionListener());
			add(splitSectionItem);
			mergeSectionItem = new JMenuItem("Merge SubSection");
			mergeSectionItem.addActionListener(new MergeSubSectionListener());
			add(mergeSectionItem);
			newSubSectionItem = new JMenuItem("Add New SubSection");
			newSubSectionItem.addActionListener(new NewSubSectionListener());
			add(newSubSectionItem);
			deleteSubSectionItem = new JMenuItem("Delete SubSection");
			deleteSubSectionItem.addActionListener(new DeleteSubSectionListener());
			add(deleteSubSectionItem);
		}

	}


}
