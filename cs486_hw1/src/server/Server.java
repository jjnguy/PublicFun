package server;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Specialized server for sending text files to special clients
 * 
 * @author Justin Nelson
 * 
 */
public class Server implements Runnable {

	// The port to connect to
	private int port;

	/**
	 * Creates a default server listening on port 4860
	 */
	public Server() {
		this(4860);
	}

	/**
	 * Creates a server listening on a specified port
	 * 
	 * @param port
	 *            the port to listen on
	 */
	public Server(int port) {
		this.port = port;
	}

	/**
	 * Starts th eserver listening for new connections.
	 * 
	 * Can only deal with one at a time.
	 */
	@Override
	public void run() {
		ServerSocket serverSock = null;
		try {
			// Begin listening on the port
			serverSock = new ServerSocket(port);
		} catch (IOException e1) {
			System.out.println("Could not listen on port " + port);
			System.out.println("Exiting...");
			return;
		}
		System.out.println("Server is now listening on port " + port);
		// forever loop
		int failCount = 0;
		while (true) {
			Socket connection = null;
			try {
				// wait for new connection
				connection = serverSock.accept();
			} catch (IOException e) {
				// try a few times
				failCount++;
				if (failCount > 10) {
					System.out.println("Failed " + failCount + " times.  Exiting...");
					break;
				}
				System.out.println("Something went wrong...Trying again...");
				continue;
			}
			// once we have a connection, talk with the client
			try {
				handleConnection(connection);
			} catch (IOException e) {
				System.out.println("There was an error while trying to close the connection.");
				System.out.println("Continuing");
			}
		}
	}

	// handle requests from the client
	private void handleConnection(Socket s) throws IOException {
		Scanner clientReader = null;
		try {
			clientReader = new Scanner(s.getInputStream());
		} catch (IOException e) {
			System.out.println("Could not open stream to read messages from the client.");
			System.out.println("Exiting...");
			return;
		}
		PrintStream clientWriter = null;
		try {
			clientWriter = new PrintStream(s.getOutputStream());
		} catch (IOException e) {
			System.out.println("Could not open stream to send messages to the client.");
			System.out.println("Exiting...");
			return;
		}

		// get the directory request
		String requestedDir = clientReader.nextLine();
		System.out.println("Requested directory: " + requestedDir);
		File dir = new File(requestedDir);
		if (!dir.isDirectory())
			throw new IllegalArgumentException("The requested directory needs to be a directory.");
		
		// send the list of directories to the client
		String listOfDirs = combineArr(dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return !pathname.isDirectory();
			}
		}), ' ');
		clientWriter.println(listOfDirs);
		clientWriter.flush();

		// get the file that the client wants
		String getReq = clientReader.nextLine();
		System.err.println("File requested: " + getReq);
		String[] twoHalves = getReq.split(" ");
		if (twoHalves.length != 2)
			throw new IllegalArgumentException("The command needs to be in the form: GET FileName");
		if (!twoHalves[0].equalsIgnoreCase("GET"))
			throw new IllegalArgumentException("The command needs to be in the form: GET FileName");
		String requestedFile = requestedDir + twoHalves[1];
		
		// send the file to the client
		String entireFile = null;
		try {
			entireFile = new Scanner(new File(requestedFile)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			System.out.println("Could not open the specified file...Exiting...");
			return;
		}
		clientWriter.println(entireFile);
		clientWriter.flush();

		// wait until the client wants to be done
		while (true) {
			String quitReq = clientReader.nextLine();
			if (quitReq.equalsIgnoreCase("QUIT")) {
				s.close();
				break;
			} else
				continue;
		}
	}

	// simple method that creates a space delimited string of the file names
	private static String combineArr(File[] arr, char separator) {
		StringBuilder ret = new StringBuilder();
		for (File s : arr) {
			if (s.getName().contains(" "))
				ret.append("\"" + s.getName() + "\"").append(separator);
			else
				ret.append(s.getName()).append(separator);
		}
		return ret.substring(0, ret.length() - 1);
	}

	/**
	 * Main entry point for the server
	 * @param args not used
	 */
	public static void main(String[] args) {
		Server s = new Server();
		s.run();
	}
}
