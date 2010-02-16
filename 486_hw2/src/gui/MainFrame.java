package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
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
	private JButton refreshFilesButton;
	private JComboBox listOfFiles;

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
		refreshFilesButton = new JButton("Refresh Files");
		refreshFilesButton.setEnabled(false);
		listOfFiles = new JComboBox();
		listOfFiles.setVisible(false);
	}

	private void layoutComponents() {
		JPanel p = new JPanel(new BorderLayout());
		JPanel northPanel = new JPanel();
		northPanel.add(currentDirLabelLabel);
		northPanel.add(currentDirLabel);
		northPanel.add(changeDirButton);
		northPanel.add(refreshFilesButton);
		northPanel.add(downloadFileButton);
		p.add(northPanel, BorderLayout.NORTH);
		p.add(listOfFiles, BorderLayout.CENTER);
		add(p);
		setResizable(false);
	}

	private void setUpActions() {
		changeDirButton.addActionListener(chageDirClicked);
		downloadFileButton.addActionListener(downloadFileClicked);
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
					MainFrame.this.refreshFilesButton.setEnabled(true);
					DefaultComboBoxModel model = new DefaultComboBoxModel(client.listFiles());
					MainFrame.this.listOfFiles.setModel(model);
					MainFrame.this.listOfFiles.setVisible(true);
					MainFrame.this.pack();
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

	private ActionListener downloadFileClicked = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			int choice = chooser.showSaveDialog(MainFrame.this);
			if (choice != JFileChooser.APPROVE_OPTION)
				return;
			String fileToDownload = ((File) listOfFiles.getSelectedItem()).getAbsolutePath();
			MainFrame.this.downloadFile(fileToDownload, chooser.getSelectedFile());
		}
	};

	class ListOfFilesComboBoxModel extends DefaultComboBoxModel {

	}

	public static void main(String[] args) {
		new MainFrame();
	}
}
