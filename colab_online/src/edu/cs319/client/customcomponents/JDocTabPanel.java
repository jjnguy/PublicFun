package edu.cs319.client.customcomponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 * 
 * @author Amelia
 *
 */
public class JDocTabPanel extends JPanel {
	
	private JList sectionList;
	//TODO turn docPane/workPane into JSplitPane
	private JSplitPane workspace;
	private JEditorPane documentPane;
	private JEditorPane workPane;
	private JButton sectionUpButton;
	private JButton sectionDownButton;
	
	public JDocTabPanel() {
		sectionList = new JList();
		documentPane = new JEditorPane();
		workPane = new JEditorPane();
		workspace = new JSplitPane(JSplitPane.VERTICAL_SPLIT, documentPane, workPane);
		sectionUpButton = new JButton("Move Up");
		sectionDownButton = new JButton("Move Down");
		setUpAppearance();
		setUpListeners();
	}
	
	private void setUpAppearance() {
		setLayout(new BorderLayout(10, 10));
		sectionList.setPreferredSize(new Dimension(150, 100));
		workPane.setPreferredSize(new Dimension(50, 75));
		workspace.setDividerLocation(250);
		
		JPanel buttonPanel = new JPanel(new BorderLayout(5, 5));
		buttonPanel.add(sectionUpButton, BorderLayout.NORTH);
		buttonPanel.add(sectionDownButton, BorderLayout.SOUTH);
		JPanel westPanel = new JPanel(new BorderLayout(10, 10));
		westPanel.add(sectionList, BorderLayout.CENTER);
		westPanel.add(buttonPanel, BorderLayout.SOUTH);
		add(westPanel, BorderLayout.WEST);
		add(workspace, BorderLayout.CENTER);
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
	
	public JEditorPane getDocPane() {
		return documentPane;
	}
	
	public JEditorPane getWorkPane() {
		return workPane;
	}
	
}
