package client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements Runnable {

	private String host;
	private int port;

	public Client() {
		this("localhost", 4860);
	}

	public Client(String host, int port) {
		this.port = port;
		this.host = host;
	}

	@Override
	public void run() {
		Socket server = null;
		try {
			server = new Socket(host, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			handleConnection(server);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleConnection(Socket s) throws IOException {
		PrintStream serverWriter = new PrintStream(s.getOutputStream());
		Scanner serverReader = new Scanner(s.getInputStream());

		System.out.println("Enter the directory that you would like to search:");
		Scanner stdin = new Scanner(System.in);
		String directory = stdin.nextLine();
		serverWriter.println(directory);
		serverWriter.flush();

		System.out.println(serverReader.nextLine());
		System.out.println();

		System.out.println("Select file to download:");
		String requestedFile = stdin.nextLine();
		serverWriter.println("GET " + requestedFile);
		serverWriter.flush();
		System.err.println("Sent file request: GET " + requestedFile);

		System.out.println("Contents of the requested file:");
		System.out.println(serverReader.nextLine());

		while (true) {
			System.out.print("Do you want to quit?(Y/N): ");
			String response = stdin.nextLine();
			if (response.trim().equalsIgnoreCase("y")) {
				serverWriter.println("QUIT\n");
				break;
			}
		}
	}

	public static void main(String[] args) {
		Client c = new Client();
		c.run();
	}
}
