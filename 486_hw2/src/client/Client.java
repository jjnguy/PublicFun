package client;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private int port;
	private String host;

	private Socket connection;
	
	private String currentDirectory;

	public void connect(String host, int port) throws UnknownHostException, IOException {
		connection = new Socket(host, port);
		this.port = port;
		this.host = host;
	}

	public void changeDirectory(String newDirectory) {
		currentDirectory = newDirectory;
	}

	public void downloadFile(String fileName, File location) {

	}
	
	public void disconnect(){
		
	}

	public String getDirectory() {
		return currentDirectory;
	}
}
