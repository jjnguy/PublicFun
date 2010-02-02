package client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client {

	private int port;
	private String host;

	private Socket connection;

	private OutputStream serverOut;
	private InputStream serverIn;

	private String currentDirectory;

	private String[] listOfFiles;

	public void connect(String host, int port) throws UnknownHostException, IOException {
		connection = new Socket(host, port);
		serverOut = connection.getOutputStream();
		serverIn = connection.getInputStream();
		this.port = port;
		this.host = host;
	}

	public void changeDirectory(String newDirectory) throws IOException {
		if (newDirectory == null)
			return;
		serverOut.write(newDirectory.getBytes());
		serverOut.flush();
		currentDirectory = newDirectory.endsWith(File.separator) ? newDirectory : newDirectory + File.separator;
		byte[] buffer = new byte[1024];
		int bytesRead;
		String dirList = "";
		while ((bytesRead = serverIn.read(buffer)) > 0) {
			dirList += new String(buffer, 0, bytesRead);
		}
		listOfFiles = dirList.split("\\s+");
	}

	public File[] listFiles() {
		if (listOfFiles == null)
			throw new IllegalArgumentException("You must first choose a directory");
		List<File> ret = new ArrayList<File>(listOfFiles.length);
		for (String s : listOfFiles) {
			ret.add(new File(currentDirectory + s));
		}
		return ret.toArray(new File[ret.size()]);
	}

	public void downloadFile(String fileName, File location) throws IOException {
		serverOut.write((currentDirectory + fileName).getBytes());
		serverOut.flush();
	}

	public void disconnect() throws IOException {
		connection.close();
		connection = null;
	}

	public String getDirectory() {
		return currentDirectory;
	}
}
