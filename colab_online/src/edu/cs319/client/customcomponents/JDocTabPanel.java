package edu.cs319.client.customcomponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;

import edu.cs319.dataobjects.SectionizedDocument;
import edu.cs319.dataobjects.impl.SectionizedDocumentImpl;

/**
 * 
 * @author Amelia Gee
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
	
	private SectionizedDocument doc;

	public JDocTabPanel(String name) {
		doc = new SectionizedDocumentImpl(name);
		
		sectionList = new JList();
		Font docFont = new Font("Courier New", Font.PLAIN, 11);
		documentPane = new JEditorPane();
		documentPane.setEditable(false);
		documentPane.setFont(docFont);
		PlainDocument doc = (PlainDocument)documentPane.getDocument();
		doc.putProperty(PlainDocument.tabSizeAttribute, 4);
		workPane = new JEditorPane();
		workPane.setFont(docFont);
		doc = (PlainDocument)workPane.getDocument();
		doc.putProperty(PlainDocument.tabSizeAttribute, 4);
		sectionUpButton = new JButton("^");
		sectionDownButton = new JButton("V");
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
		
		documentPane.setMinimumSize(new Dimension(0, 0));
		workPane.setMinimumSize(new Dimension(0, 0));
		
		workspace = new JSplitPane(JSplitPane.VERTICAL_SPLIT, documentPane, workPane);
		wholePane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sectionPanel, workspace);
		
		workspace.setDividerLocation(250);
		workspace.setOneTouchExpandable(true);
		wholePane.setDividerLocation(150);
		wholePane.setOneTouchExpandable(true);
		
		add(wholePane, BorderLayout.CENTER);
	}
	
	private void setUpListeners() {
		sectionUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		sectionDownButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public JList getList() {
		return sectionList;
	}
	
	public void updateDocPane() {
		documentPane.setText(doc.getFullText());
		sectionList.setListData(doc.getAllSubSections().toArray());
	}
	
	/*public JEditorPane getWorkPane() {
		return workPane;
	}
	*/

	public SectionizedDocument getSectionizedDocument() {
		return doc;
	}
}
