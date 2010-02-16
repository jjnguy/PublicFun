package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client {

	public static final byte EOF = 4;

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
		String dirList = readDirectoryListingFromServer(newDirectory);
		listOfFiles = dirList.split("\\s+");
	}

	private String readDirectoryListingFromServer(String newDirectory) throws IOException {
		serverOut.write(newDirectory.getBytes());
		serverOut.flush();
		currentDirectory = newDirectory.endsWith(File.separator) ? newDirectory : newDirectory + File.separator;
		byte[] buffer = new byte[1024];
		int bytesRead;
		System.out.print("Reading directory listing from server...");
		String dirList = "";
		while ((bytesRead = serverIn.read(buffer)) > 0) {
			dirList += new String(buffer, 0, bytesRead);
			System.out.println(dirList);
			if (dirList.charAt(dirList.length() - 1) == EOF)
				break;
		}
		System.out.println("Done!");
		return dirList.substring(0, dirList.length() - 1);
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

	public void downloadFile(String fullPath, File location) throws IOException {
		File reguestedFile = new File(fullPath);
		String getReq = "GET " + reguestedFile.getName() + ((char) EOF);
		serverOut.write(getReq.getBytes());
		serverOut.flush();
		FileOutputStream fout = new FileOutputStream(location);
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = serverIn.read(buffer)) > 0) {
			if (buffer[bytesRead - 1] == EOF) {
				fout.write(buffer, 0, bytesRead - 1);
				break;
			} else {
				fout.write(buffer, 0, bytesRead);
			}
		}
		fout.close();
	}

	public void disconnect() throws IOException {
		connection.close();
		connection = null;
	}

	public String getDirectory() {
		return currentDirectory;
	}
}
