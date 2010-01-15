package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server implements Runnable {

	private int port;

	public Server() {
		this(4860);
	}

	public Server(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		System.out.println("Server is now listening on port " + port);
		while (true) {
			Socket connection = null;
			try {
				ServerSocket serverSock = new ServerSocket(port);
				connection = serverSock.accept();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			try {
				handleConnection(connection);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void handleConnection(Socket s) throws IOException {
		Scanner clientReader = new Scanner(s.getInputStream());
		BufferedOutputStream clientWriter = new BufferedOutputStream(s.getOutputStream());

		String requestedDir = clientReader.nextLine();
		System.out.println("Requested directory: " + requestedDir);
		File dir = new File(requestedDir);
		if (!dir.isDirectory())
			throw new IllegalArgumentException("The requested directory needs to be a directory.");
		String listOfDirs = combineArr(dir.list(), ' ');
		clientWriter.write(listOfDirs.getBytes());
		clientWriter.flush();
		
		String getReq = clientReader.nextLine();
		System.out.println("File request: " + getReq);
		String[] twoHalves = getReq.split(" ");
		if (twoHalves.length != 2)
			throw new IllegalArgumentException("The command needs to be in the form: GET FileName");
		if (!twoHalves[0].equalsIgnoreCase("GET"))
			throw new IllegalArgumentException("The command needs to be in the form: GET FileName");
		String requestedFile = requestedDir + twoHalves[1];
		File fileToWrite = new File(requestedFile);
		BufferedInputStream fileReader = new BufferedInputStream(new FileInputStream(fileToWrite));
		byte[] buffer = new byte[64];
		while (fileReader.read(buffer) > 0)
			clientWriter.write(buffer);
		clientWriter.flush();
		
		String quitReq = clientReader.nextLine();
		if (quitReq.equalsIgnoreCase("QUIT"))
			s.close();
	}

	private static String combineArr(String[] arr, char separator) {
		StringBuilder ret = new StringBuilder();
		for (String s : arr) {
			if (s.contains(" "))
				ret.append("\"" + s + "\"").append(separator);
			else
				ret.append(s).append(separator);
		}
		return ret.substring(0, ret.length() - 1);
	}
	
	public static void main(String[] args) {
		Server s = new Server();
		s.run();
	}
}
