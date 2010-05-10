package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import client.Client;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private Client client;
	private boolean connected;

	private JLabel currentDirLabelLabel;
	private JLabel currentDirLabel;
	private JButton changeDirButton;
	private JButton downloadFileButton;
	private JButton refreshFilesButton;
	private JLabel listOfFilesLabel;
	private JComboBox listOfFiles;
	private JMenuItem connect;
	private JMenuItem disconnect;

	public MainFrame() {
		super("Download Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createComponents();
		createMenuBar();
		layoutComponents();
		setUpActions();
		pack();
		setVisible(true);
		connected = false;
	}

	private void createComponents() {
		currentDirLabelLabel = new JLabel("Current Directory:");
		currentDirLabel = new JLabel("No Directory selected");
		changeDirButton = new JButton("Change Directory");
		changeDirButton.setEnabled(false);
		downloadFileButton = new JButton("Download File");
		downloadFileButton.setEnabled(false);
		refreshFilesButton = new JButton("Refresh Files");
		refreshFilesButton.setEnabled(false);
		listOfFilesLabel = new JLabel("List of Files in Current Directory:");
		listOfFilesLabel.setVisible(false);
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
		JPanel centerPanel = new JPanel();
		centerPanel.add(listOfFilesLabel);
		centerPanel.add(listOfFiles);
		p.add(centerPanel, BorderLayout.CENTER);
		add(p);
		setResizable(false);
	}

	private void setUpActions() {
		changeDirButton.addActionListener(chageDirClicked);
		refreshFilesButton.addActionListener(refreshFilesClicked);
		downloadFileButton.addActionListener(downloadFileClicked);
		this.addWindowListener(windowClosingListener);
	}

	private void createMenuBar() {
		JMenuBar bar = new JMenuBar();
		JMenu file = new JMenu("File");
		connect = new JMenuItem("Connect");
		connect.addActionListener(connectClicked);
		disconnect = new JMenuItem("Disconnect");
		disconnect.setEnabled(false);
		disconnect.addActionListener(disconnectClicked);
		file.add(connect);
		file.add(disconnect);
		bar.add(file);
		setJMenuBar(bar);
	}

	public void connect(String host, int port) {
		client = new Client();
		try {
			client.connect(host, port);
			connected = true;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					MainFrame.this.changeDirButton.setEnabled(true);
					MainFrame.this.connect.setEnabled(false);
					MainFrame.this.disconnect.setEnabled(true);
				}
			});
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(this, "Something went wrong while connecting to the server.",
					"Error While Connecting", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Something went wrong while connecting to the server.",
					"Error While Connecting", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void disconnect() {
		if (!connected)
			return;
		try {
			client.disconnect();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Something went wrong while disconnecting from the server.\nConnection dropped.",
					"Error While Disconnecting", JOptionPane.ERROR_MESSAGE);
		}
		connected = false;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame.this.currentDirLabel.setText("No Directory selected");
				MainFrame.this.changeDirButton.setEnabled(false);
				MainFrame.this.refreshFilesButton.setEnabled(false);
				MainFrame.this.downloadFileButton.setEnabled(false);
				MainFrame.this.connect.setEnabled(true);
				MainFrame.this.disconnect.setEnabled(false);
				MainFrame.this.listOfFiles.setVisible(false);
				MainFrame.this.listOfFilesLabel.setVisible(false);
				MainFrame.this.pack();
			}
		});
	}

	public void getDirectoryListing() {
		try {
			client.changeDirectory(client.getDirectory());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Something went wrong while retrieving the directory listing.",
					"Error While Retrieving Directory Listing", JOptionPane.ERROR_MESSAGE);
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
					MainFrame.this.downloadFileButton.setEnabled(true);
					DefaultComboBoxModel model = new DefaultComboBoxModel(client.listFiles());
					MainFrame.this.listOfFiles.setModel(model);
					MainFrame.this.listOfFiles.setVisible(true);
					MainFrame.this.listOfFilesLabel.setVisible(true);
					MainFrame.this.pack();
				}
			});
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Something went wrong while changing directories.",
					"Error While Changing Directories", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void downloadFile(String fileName, File location) {
		try {
			client.downloadFile(fileName, location);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Something went wrong while downloading your file.",
					"Error While Downloading File", JOptionPane.ERROR_MESSAGE);
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

	private ActionListener refreshFilesClicked = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			MainFrame.this.changeDirectory(client.getDirectory());
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

	private ActionListener connectClicked = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (connected)
				return;
			String host = JOptionPane.showInputDialog("Please enter the host you would like to connect to.");
			if (host == null)
				return;
			connect(host, 4860);
		}
	};

	private ActionListener disconnectClicked = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (connected)
				MainFrame.this.disconnect();
		}
	};

	private WindowListener windowClosingListener = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			MainFrame.this.disconnect();
		}
	};

	public static void main(String[] args) {
		new MainFrame();
	}
}
