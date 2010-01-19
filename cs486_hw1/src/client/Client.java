package client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements Runnable {

	// Host to connect to
	private String host;
	// Port to connect to
	private int port;

	/**
	 * Default constructor. Connects to port 4860 on 'localhost'
	 */
	public Client() {
		this("localhost", 4860);
	}

	/**
	 * Created a basic Client
	 * 
	 * @param host
	 *            the desired host to connect to
	 * @param port
	 *            the desired port to connect to
	 */
	public Client(String host, int port) {
		this.port = port;
		this.host = host;
	}

	/**
	 * Starts the Client.
	 */
	@Override
	public void run() {
		Socket server = null;
		try {
			// Try to connect to the server
			server = new Socket(host, port);
		} catch (UnknownHostException e) {
			System.out.println("Could not connect to the specified Host.");
			System.out.println("Exiting...");
			return;
		} catch (IOException e) {
			System.out.println("Something bad happened.");
			System.out.println("Exiting...");
			return;
		}
		// Once you have a connection, try to communicate with the server
		handleConnection(server);
	}

	// Handles one connection to a server
	private void handleConnection(Socket s) {
		PrintStream serverWriter = null;
		try {
			serverWriter = new PrintStream(s.getOutputStream());
		} catch (IOException e) {
			System.out.println("Could not open stream to send messages to the server.");
			System.out.println("Exiting...");
			return;
		}
		Scanner serverReader = null;
		try {
			serverReader = new Scanner(s.getInputStream());
		} catch (IOException e) {
			System.out.println("Could not open stream to read messages from the server.");
			System.out.println("Exiting...");
			return;
		}

		// Send the directory message
		System.out.println("Enter the directory that you would like to search:");
		Scanner stdin = new Scanner(System.in);
		String directory = stdin.nextLine();
		serverWriter.println(directory);
		serverWriter.flush();

		// read directory listing
		System.out.println(serverReader.nextLine());
		System.out.println();

		// Send the get request
		System.out.println("Select file to download:");
		String requestedFile = stdin.nextLine();
		serverWriter.println("GET " + requestedFile);
		serverWriter.flush();
		System.err.println("Sent file request: GET " + requestedFile);

		// Read the contents of the file
		System.out.println("Contents of the requested file:");
		System.out.println(serverReader.nextLine());

		// don't quit until the user says yes...
		while (true) {
			System.out.print("Do you want to quit?(Y/N): ");
			String response = stdin.nextLine();
			if (response.trim().equalsIgnoreCase("y")) {
				serverWriter.println("QUIT\n");
				break;
			}
		}
	}

	/**
	 * Main entry point for the Client
	 * @param args Not used
	 */
	public static void main(String[] args) {
		Client c = new Client();
		c.run();
	}
}
