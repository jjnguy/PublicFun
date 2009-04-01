package edu.cs319.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class WindowJoinCoLab extends JFrame {
	
	private JList roomList;
	private DefaultListModel listModel;
	private JButton joinButton = new JButton("Join");
	private JButton createButton = new JButton("Create");
	
	public WindowJoinCoLab() {
		this.setTitle("Join a CoLab Room");
		this.setSize(500, 400);
		setUpAppearance();
		setUpListeners();
	}
	
	private void setUpAppearance() {
		listModel = new DefaultListModel();
		roomList = new JList(listModel);
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		roomList.setSelectedIndex(0);
		roomList.setVisibleRowCount(8);
		JScrollPane listScrollPane = new JScrollPane(roomList);
		
		
		JLabel topLabel = new JLabel("Please choose an " +
				"existing CoLab to join or create a new CoLab.");
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(listScrollPane, BorderLayout.CENTER);
		
	}
	
	private void setUpListeners() {
		roomList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO list selection event
				
			}
		});
		
		joinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO join coLab room
				
			}
		});
		
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO check for existing coLab room with same name; create new coLab room
				
			}
		});
	}
	
}
