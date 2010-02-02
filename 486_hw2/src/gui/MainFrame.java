package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import client.Client;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private Client client;

	private JLabel currentDirLabelLabel;
	private JLabel currentDirLabel;
	private JButton changeDirButton;
	private JButton downloadFileButton;
	private JButton listFilesButton;

	public MainFrame() {
		super("Download Client");
		connect("localhost", 4860);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createComponents();
		layoutComponents();
		setUpActions();
		pack();
		setVisible(true);
	}

	private void createComponents() {
		currentDirLabelLabel = new JLabel("Current Directory:");
		currentDirLabel = new JLabel("No Directory selected");
		changeDirButton = new JButton("Change Directory");
		downloadFileButton = new JButton("Download File");
		listFilesButton = new JButton("List Files");
		listFilesButton.setEnabled(false);
	}

	private void layoutComponents() {
		JPanel p = new JPanel();
		p.add(currentDirLabelLabel);
		p.add(currentDirLabel);
		p.add(changeDirButton);
		p.add(listFilesButton);
		p.add(downloadFileButton);
		add(p);
	}

	private void setUpActions() {
		changeDirButton.addActionListener(chageDirClicked);
	}

	public void connect(String host, int port) {
		client = new Client();
		try {
			client.connect(host, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			client.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getDirectoryListing() {
		try {
			client.changeDirectory(client.getDirectory());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void changeDirectory(final String newDirectory) {
		try {
			client.changeDirectory(newDirectory);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					MainFrame.this.currentDirLabel.setText(newDirectory);
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void downloadFile(String fileName, File location) {
		try {
			client.downloadFile(fileName, location);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ActionListener chageDirClicked = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			final String choice = JOptionPane.showInputDialog("Please enter the new directory:");
			if (choice == null || choice.equals(""))
				return;
			MainFrame.this.changeDirectory(choice);
		}
	};

	public static void main(String[] args) {
		new MainFrame();
	}
}
