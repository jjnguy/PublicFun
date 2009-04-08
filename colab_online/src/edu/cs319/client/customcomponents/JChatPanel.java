package edu.cs319.client.customcomponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

/**
 * 
 * @author Amelia
 *
 */
public class JChatPanel extends JPanel {
	
	private JEditorPane archive;
	private JEditorPane currentMsg;
	private JButton sendButton;
	
	public JChatPanel() {
		archive = new JEditorPane();
		archive.setEditable(false);
		currentMsg = new JEditorPane();
		sendButton = new JButton("Send");
		setUpAppearance();
		setUpListeners();
		setPreferredSize(new Dimension(200, 50));
	}
	
	private void setUpAppearance() {
		currentMsg.setPreferredSize(new Dimension(50, 75));
		setLayout(new BorderLayout(10, 10));
		add(archive, BorderLayout.CENTER);
		JPanel newMsgPanel = new JPanel(new BorderLayout(10, 10));
		newMsgPanel.add(currentMsg, BorderLayout.CENTER);
		newMsgPanel.add(sendButton, BorderLayout.EAST);
		add(newMsgPanel, BorderLayout.SOUTH);
	}
	
	private void setUpListeners() {
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}
