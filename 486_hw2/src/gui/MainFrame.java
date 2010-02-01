package gui;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
	}
	
	private void createComponents(){
		currentDirLabelLabel = new JLabel("Current Directory:");
		currentDirLabel = new JLabel("No Directory selected");
		changeDirButton = new JButton("Change Directory");
		downloadFileButton = new JButton("Download File");
		listFilesButton = new JButton("List Files");
		listFilesButton.setEnabled(false);
	}
	
	private void layoutComponents(){
		
	}

	public void connect(String host, int port) {
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
		client.disconnect();
	}

	public void getDirectoryListing() {
		client.changeDirectory(client.getDirectory());
	}

	public void changeDirectory(String newDirectory) {
		client.changeDirectory(newDirectory);
	}

	public void downloadFile(String fileName, File location) {
		client.downloadFile(fileName, location);
	}
}
